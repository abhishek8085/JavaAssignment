import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class HandshakeManager {

    public boolean performHandshake( Socket socket )
    {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            outputStream.write(new HandShakeFrame(InetAddress.getLocalHost().toString(),
                    new HandshakeInfo(InetAddress.getLocalHost().toString(),
                            Config.SERVER_PORT_NUMBER,"",true)).serialise());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
