import java.io.IOException;
import java.io.InputStream;

/**
 *StringZipInputStream used to uncompress
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : StringZipInputStream.java, 2015/10/11
 */
public class StringZipInputStream {
    private int COMPRESSION_DICTIONARY_LIMIT = 65555;
    private int uniqueId = 256;
    private LZWDictionary<Integer, String> readDictionary = new LZWDictionary<Integer, String>(COMPRESSION_DICTIONARY_LIMIT);
    private InputStream inputStream;
    private byte[] byteBuffer;
    private int bytesCounter = 0;
    private int byteRemaining = 0;
    private boolean hasBeenReset = true;
    private String word;
    private String entry = "";
    private StringBuffer stringBuffer = new StringBuffer(10000);
    private Object bufferMutex = new Object();
    private Object bufferFillerMutex = new Object();
    private IOException ioException;
    private volatile boolean readyFile=false;
    private volatile boolean readComplete = false;

    // Creates a new input stream with a default buffer size.
    public StringZipInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        buildDictionary();
        Thread bufferFiller = new BufferFiller();
        bufferFiller.setPriority(5);
        bufferFiller.start();
        while (!readyFile);
    }

    /**
     * Builds dictionary of ASCII characters
     */
    private void buildDictionary() {
        for (int i = 0; i < 256; i++)
            readDictionary.put(i, "" + (char) i);
    }

    // Reads data into a string. the method will block until some input can be
    // read; otherwise, no bytes are read and null is returned.
    public String read() throws IOException, InterruptedException {

        if (ioException != null)
        {
            throw  ioException;
        }

        synchronized (bufferFillerMutex)
        {
            while(!readComplete&&stringBuffer.length()==0)
            {
                System.out.println("blocking");
                bufferFillerMutex.wait();
            }
        }

        synchronized (bufferMutex) {

            if (readComplete && stringBuffer.length()==0)
            {
                return null;
            }

            char returnChar;
            if ((returnChar = stringBuffer.charAt(0))!=0) {
                stringBuffer.deleteCharAt(0);
                bufferMutex.notifyAll();
                return returnChar+"";
            }
            return null;
        }

    }


    private class BufferFiller extends Thread
    {

        @Override
        public void run() {

            while (!readComplete) {
                synchronized (bufferMutex) {
                    while (stringBuffer.length() >= 10000) {
                        try {
                            readyFile = true;
                            bufferMutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    synchronized (bufferFillerMutex) {
                        String read;
                        if ((read = internalRead())!=null) {
                            stringBuffer.append(read);
                        }else
                        {
                            stringBuffer.append(read);
                            readComplete = true;
                        }
                        bufferFillerMutex.notify();
                    }

                } catch (IOException e) {
                    ioException = e;
                }
            }
        }
    }

    // Reads data into a string. the method will block until some input can be
    // read; otherwise, no bytes are read and null is returned.
    public String internalRead() throws IOException {

        if (byteBuffer == null) {
            fillBuffer();
        }

        if (bytesCounter + 1 >= byteRemaining) {
            if (inputStream.available() != 0) {
                fillBuffer();
                resetStates();
                bytesCounter = 0;
            } else {
                return null;
            }
        }

        int currentCharacter = mergeMSBLBB(byteBuffer[bytesCounter++] & 0xFF,
                byteBuffer[bytesCounter++] & 0xFF);
        if (hasBeenReset) {
            word = (char) currentCharacter + "";
            hasBeenReset = false;
            return word;
        }

        if (readDictionary.get(currentCharacter) != null) {
            entry = readDictionary.get(currentCharacter);
        } else if (currentCharacter == uniqueId) {
            entry = word + word.charAt(0);
        } else {
            throw new IllegalStateException("Not found in dictionary: " +
                    currentCharacter + "  " + uniqueId);
        }

        readDictionary.put(uniqueId++, word + entry.charAt(0));

        word = entry;

        return entry;
    }

    /**
     * Resets states of compression
     * algorithm
     */
    private void resetStates() {
        hasBeenReset = true;
        word = "";
        readDictionary.clear();
        buildDictionary();
        uniqueId = 256;
        entry = "";
    }


    /**
     * Merges msb and lsb to give 16-bit int value
     * @param msb msb
     * @param lsb lsb
     * @return integer value
     */
    private int mergeMSBLBB(int msb, int lsb) {
        return (msb * 255) + lsb;
    }

    /**
     * Fills in input buffer.
     * @throws IOException
     */
    private void fillBuffer() throws IOException {

        int dig1 = inputStream.read();
        int dig2 = inputStream.read();
        int dig3 = inputStream.read();
        int dig4 = inputStream.read();

        int value = (dig1 << 24) & 0xff000000 |
                (dig2 << 16) & 0x00ff0000 |
                (dig3 << 8) & 0x0000ff00 |
                (dig4 << 0) & 0x000000ff;
        byteBuffer = new byte[value];
        byteRemaining = inputStream.read(byteBuffer);
    }


    // Closes this input stream and releases any system resources associated
    // with the stream.
    public void close() throws IOException {
        inputStream.close();
    }


}

