import java.io.Serializable;
import java.lang.String; /**
/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : HandshakeInfo.java, 2015/11/06
 */
public class HandshakeInfo implements Serializable
{
    private String sourceAddress;
    private String userName;
    private char gamePiece=0;

    private String lookupURL;


    public HandshakeInfo(String sourceAddress, char gamePiece) {
        this.sourceAddress = sourceAddress;
        this.gamePiece = gamePiece;
    }

    public HandshakeInfo(String sourceAddress, String userName,char  gamePiece ) {
        this.sourceAddress = sourceAddress;
        this.userName = userName;
        this.gamePiece = gamePiece;
    }

    public HandshakeInfo(String sourceAddress, String userName, char gamePiece, String lookupURL) {
        this.sourceAddress = sourceAddress;
        this.userName = userName;
        this.gamePiece = gamePiece;
        this.lookupURL = lookupURL;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getUserName() {
        return userName;
    }

    public char getGamePiece() {
        return gamePiece;
    }

    public String getLookupURL() {
        return lookupURL;
    }

}
