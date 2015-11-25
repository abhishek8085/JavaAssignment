

import java.io.*;
// import java.util.zip.StringZipInputStream;
// import java.util.zip.StringZipOutputStream;

public class
		MyCompressTest {

    final  int MAX = 1024;
    String inputFileName 	= "c:\\words\\compressed.txt";
    String outputFileName 	= "c:\\words\\compressed.txt";
    String uncompressed 	= "c:\\words\\output.txt";

    void compress()	{	
	try {
		String aWord;

		BufferedReader input = new BufferedReader(new FileReader(inputFileName));
		StringZipOutputStream aStringZipOutputStream = new StringZipOutputStream( new FileOutputStream(outputFileName));

		while (  ( aWord = input.readLine() )  != null ) {
				System.out.println("write:	" + aWord);
				aStringZipOutputStream.write(aWord);
		}
		aStringZipOutputStream.close();
		input.close();
		
	} catch ( Exception e )	{
		e.printStackTrace();
		System.exit(1);
	}
    }
    void unCompress()	{	
	try {
		String aWord;
		byte[] buffer = new byte[MAX];

		BufferedWriter uncompress = new BufferedWriter(new FileWriter(uncompressed));
		StringZipInputStream aStringZipInputStream = new StringZipInputStream( new FileInputStream(inputFileName));
		String theWord;

		while (  ( theWord = aStringZipInputStream.read() ) != null ) {
			Thread.sleep(20);
			//System.out.println(theWord);
			uncompress.write(theWord, 0, theWord.length());

		}
		aStringZipInputStream.close();
		uncompress.close();
		
	} catch ( Exception e )	{
		e.printStackTrace();
		System.exit(1);
	}
    }
    public static void main( String args[] ) {
	MyCompressTest aMyCompressTest = new MyCompressTest();
	//aMyCompressTest.compress();
		aMyCompressTest.unCompress();

    }
}
