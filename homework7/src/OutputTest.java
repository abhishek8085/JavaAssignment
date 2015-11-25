import java.io.*;

/**
 * Created by Abhishek8085 on 11-10-2015.
 */
public class OutputTest {

    public static void main(String[] args) {

        try {


            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File("C:\\words\\compressed.txt")));
            StringZipOutputStream stringZipOutputStream = new StringZipOutputStream(bufferedOutputStream);

            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\words\\input.txt")));
            String readLine = "";

            while ((readLine = bufferedReader.readLine()) != null) {
                stringZipOutputStream.write(readLine);
            }


            stringZipOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
