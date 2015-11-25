import java.io.*;

/**
 * Created by Abhishek8085 on 11-10-2015.
 */
public class InputTest {
    public static void main(String[] args) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File("C:\\words\\compressed.txt")));
            StringZipInputStream stringZipInputStream = new StringZipInputStream(bufferedInputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("C:\\words\\output.txt")));

            String string;
            while ((string = stringZipInputStream.read()) != null) {
                bufferedWriter.write(string);
                bufferedWriter.flush();

            }
            stringZipInputStream.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
