import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {
    public boolean checkUser(int y) throws IOException;
    public String inbox(int user) throws IOException;
    public String read(int user, String email) throws IOException;
    public String sendEmail(int user, String subject, String body) throws IOException;
    public String OTP(String[] splited, String subject, String body, int recUser) throws IOException;

}