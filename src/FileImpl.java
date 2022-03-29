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

    public long OTP(long message0, long message1) throws IOException{
        long n = 187; //public
        long e = 23; //public key
        long d = 7; //private key
        long x0 = (long)(Math.random()*100);
        long x1 = (long)(Math.random()*100);
        Random random = new Random();
        long v = 0;


        //get random messages
        //choose random message 0 or 1 and generate key k and return to sender
        //compute 2 keys and send to recipient
        //recipient receives original messages and decrypts using the key k and can see the original message

        int choice = (int)Math.round( Math.random());
        System.out.println("choice: " + choice);

        long key = (long)(Math.random()*100);
        System.out.println("key: " + key);


        if(choice == 0){
            v = x0 + fun(key,e,n);
			if(v> n) v = v%n;
        }
        else{
            v = x1 + fun(key,e,n);
			if(v>n) v = v%n;
        }
        System.out.println("v: " + v);

        long num1 = v - x0;
        long num2 = v - x1;

        long k0 = fun(num1, d, n);
        long k1 = fun(num2, d, n);
        System.out.println("k0: " + k0 + "\nk1: " + k1);

        
        long hiddenMessage = message0 + k0;
        long hiddenMessage2 = message1 + k1;
        System.out.println("hidden messages: \n1. " + hiddenMessage + "\n2. " + hiddenMessage2);

        long[] hiddenMessages = {hiddenMessage, hiddenMessage2};

        long decryptedMessage = hiddenMessages[choice] - key;

        System.out.print(decryptedMessage);

        return decryptedMessage;
    }

    static long fun(long vx, long d, long n){
		long k = 1;
		for(long i=0;i<d;i++){
			k = k*vx;
			if(k>n) k = k%n;
		}
		return k;
	}

    static long getV(long k, long e, long n){
		long v = 1;
		for(long i=0;i<e;i++){
			v = v*k;
			if(v>n) v = v%n;
		}
		return v;
	}
}