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

    public String OTP(String[] splited, String subject, String body, int recUser) throws IOException{
        long n = 187; //public
        long e = 23; //public key
        long d = 7; //private key
        long x0 = (long)(Math.random()*100);
        long x1 = (long)(Math.random()*100);
        ArrayList<Long> messages = new ArrayList<Long>();
        ArrayList<Long> ranMessages = new ArrayList<Long>();
        ArrayList<Long> numbers = new ArrayList<Long>();
        ArrayList<Long> hiddens = new ArrayList<Long>();
        Random random = new Random();
        long v = 0;
        String t = "";

        for(int i = 0; i < splited.length; i++){
            for(int j = 0; j < splited[i].length(); j++){
                String s = splited[i];
                char ch = s.charAt(j);
                int num = (int)ch - (int)'a' + 1;
                t += String.valueOf(num);
                System.out.println(t);
            }
            t += " ";
        }

        String[] splitNums = t.split("\\s+");

        for(int z = 0; z < splitNums.length; z++){
            long message = Long.parseLong(splitNums[z]);
            messages.add(message);
        }


        //get random messages
        //choose random message 0 or 1 and generate key k and return to sender
        //compute 2 keys and send to recipient
        //recipient receives original messages and decrypts using the key k and can see the original message

        int choice = (int)Math.round( Math.random()*(splitNums.length - 1));
        System.out.println("choice: " + choice);

        System.out.println("original message: " + messages.get(choice));

        long key = (long)(Math.random()*100);
        System.out.println("key: " + key);

        for(int ab = 0; ab < splitNums.length; ab++){
            ranMessages.add((long)(Math.random()*100));
        }

        v = ranMessages.get(choice) + fun(key, e, n);
        if(v>n) v = v%n;
        System.out.println("v: " + v);

        for(int c = 0; c < ranMessages.size(); c++){
            numbers.add(v - ranMessages.get(c));
        }

        for(int f = 0; f < numbers.size(); f++){
            long k = fun(numbers.get(f), d, n);
            hiddens.add(messages.get(f) + k);
        }

        long decryptedMessage = hiddens.get(choice) - key;

        System.out.print(decryptedMessage);

        if (messages.get(choice) == decryptedMessage){
            sendEmail(recUser, subject, body);
            return "Email authenticated and sent.";
        }
        else{
            return "Connection could not be authenticated, email not sent";
        }

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