import java.io.IOException;
import java.io.OutputStream;


/**
 * This is StringZipOutputStream works on LZW compression
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : StringZipOutputStream.java, 2015/10/11
 */
public class StringZipOutputStream {
    private int blockSize = 250;
    private int COMPRESSION_DICTIONARY_LIMIT = 8192;
    private LZWDictionary<String, Integer> dictionary =
            new LZWDictionary<String, Integer>(COMPRESSION_DICTIONARY_LIMIT);
    private StringBuffer buffer = new StringBuffer(1024 * blockSize);
    private int[] outputBuffer = new int[2 * (1024 * blockSize)];
    private int uniqueId = 256;
    private OutputStream outputStream;
    public int counter = 0;


    // Creates a new output stream with a default buffer size.
    public StringZipOutputStream(OutputStream out) {
        this(out, 250);
    }

    private StringZipOutputStream(OutputStream out, int blockSize) {
        this.outputStream = out;
        this.blockSize = blockSize;
        buildDictionary();
    }

    /**
     * Builds dictionary of ASCII characters
     */
    private void buildDictionary() {
        for (int i = 0; i < 256; i++) {
            dictionary.put((char) i + "", i);
        }
    }

    // Writes aStrign compressed output stream. This method will
    // block until all data is written.
    public void write(String string) throws IOException {
        string = string + System.lineSeparator();
        for (char c : string.toCharArray()) {
            if (buffer.capacity() - buffer.length() == 0) {
                internalWrite(buffer);
                internalFlush();
                resetStates();
                buffer.setLength(0);
            }
            buffer.append(c);
        }
    }

    /**
     * Compresses input puts it in output buffer.
     *
     * @param stringBuffer input buffer
     * @throws IOException
     */
    public void internalWrite(StringBuffer stringBuffer) throws IOException {
        String word = "";
        for (int i = 0; i < stringBuffer.length(); i++) {

            char currentCharacter = stringBuffer.charAt(i);

            String wordAndCharacter = word + currentCharacter;

            if (dictionary.get(wordAndCharacter) != null) {
                word = wordAndCharacter;

            } else {

                Integer lookUp = dictionary.get(word);

                if (lookUp == null) {

                    if (word.length() == 1) {
                        internalWriteByte((char) (int) word.charAt(0));
                    }
                } else {
                    internalWriteByte(lookUp);
                }

                dictionary.put(wordAndCharacter, uniqueId++);
                word = "" + currentCharacter;
            }
        }

        if (!word.equals("")) {
            internalWriteByte(dictionary.get(word));
        }

    }

    /**
     * Converts into two byte notation
     * writes into output buffer.
     *
     * @param value value
     * @throws IOException
     */
    private void internalWriteByte(int value) throws IOException {
        internalWrite(value / 255);
        internalWrite(value % 255);
    }


    /**
     * writes into output buffer
     *
     * @param value value
     */
    private void internalWrite(int value) {
        outputBuffer[counter++] = value;
    }

    /**
     * Resets states of compression
     * algorithm
     */
    private void resetStates() {
        dictionary.clear();
        uniqueId = 256;
        buildDictionary();
    }

    /**
     * Flush output buffer
     *
     * @throws IOException
     */
    private void internalFlush() throws IOException {
        int dig1 = counter >>> 24;
        int dig2 = counter >>> 16;
        int dig3 = counter >>> 8;
        int dig4 = counter;

        outputStream.write(dig1);
        outputStream.write(dig2);
        outputStream.write(dig3);
        outputStream.write(dig4);
        outputStream.flush();

        for (int i = 0; i < counter; i++) {
            outputStream.write(outputBuffer[i]);
        }
        outputBuffer = new int[2 * (1024 * blockSize)];
        counter = 0;
    }

    /**
     * flushes input and output buffer
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        internalWrite(buffer);
        internalFlush();
        buffer.setLength(0);
    }

    /**
     * flushes and closes stream
     *
     * @throws IOException
     */
    public void close() throws IOException {
        flush();
        outputStream.close();
    }
}

