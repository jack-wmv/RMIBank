import java.io.*;
import java.rmi.*;
import java.util.*;
import java.rmi.server.UnicastRemoteObject;


public class FileImpl extends UnicastRemoteObject
        implements FileInterface {

    private String name;
    int[] array = new int[10];
    int i = 0;
    int id;
    String file;
    HashMap<Integer, String> keys = new HashMap<Integer, String>();
    File userList = new File("Users.txt");
    Writer output;
    Writer output1;
    String newAcc = "Balance: 0\n\nTransaction History\n---------------------";
    BufferedReader read;
    String accountInfo;
    String newLine = System.getProperty("line.separator");

    public FileImpl(String s) throws RemoteException{
        super();
        name = s;
    }

    public boolean checkUser(int y) throws IOException{
        String user = Integer.toString(y);

        boolean found = false;
        Scanner scanner = new Scanner(userList);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals(user)){
                found = true;
                break;
            }
        }

        if(!found){
            return false;
        }
        else{
            return true;

        }

    }


    public String inbox(int user) throws IOException {
        String currentUser = Integer.toString(user);
        String fileNames = "";
        File folder = new File("UserFiles/"+currentUser + "/inbox");
        File[] listOfFiles = folder.listFiles();
        
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            fileNames += "\n" + listOfFiles[i].getName();
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }

        return fileNames;
    }

    public String read(int user, String email) throws IOException {
        System.out.println(email);
        String currentUser = Integer.toString(user);
        String emailText = "";
        String fileName = "UserFiles/"+currentUser + "/inbox/" + email;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                emailText += "\n" + line;
            }
         }

        return emailText;
    }

    
    public String sendEmail(int recUser, String subject, String body) throws IOException {
        File myObj = new File("UserFiles/"+recUser + "/inbox/"+subject+".txt");
        myObj.createNewFile();
        FileWriter myWriter = new FileWriter("UserFiles/"+recUser + "/inbox/"+subject+".txt");
        myWriter.write(subject);
        myWriter.write("\n\n" + body);
        myWriter.close();

        return "email sent";
    }

    public int OTP(int message0, int message1) throws IOException{
        int n = 187; //public
        int e = 23; //public key
        int d = 7; //private key
        int[] ranMessages = {100, 200};
        Random random = new Random();


        //get random messages
        //choose random message 0 or 1 and generate key k and return to sender
        //compute 2 keys and send to recipient
        //recipient receives original messages and decrypts using the key k and can see the original message

        int choice = (int)Math.round( Math.random());
        System.out.println("choice: " + choice);

        int key = random.nextInt(10) + 1;
        System.out.println("key: " + key);


        int encryptedV = (int)((ranMessages[choice] + Math.pow(key, e)) % n);
        System.out.println("encyrypted V: " + encryptedV);

        int k0 = Math.abs((int)( Math.pow(encryptedV - ranMessages[0], d) % n));
        int k1 = Math.abs((int)( Math.pow(encryptedV - ranMessages[1], d) % n));
        System.out.println("k0: " + k0 + "\nk1: " + k1);

        int hiddenMessage = message0 + k0;
        int hiddenMessage2 = message1 + k1;
        System.out.println("hidden messages: \n1. " + hiddenMessage + "\n2. " + hiddenMessage2);

        int[] hiddenMessages = {hiddenMessage, hiddenMessage2};

        int decryptedMessage = hiddenMessages[choice] - key;

        System.out.print(decryptedMessage);

        return decryptedMessage;
    }
}