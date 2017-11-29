package mn.enkhsanaa;

import mn.enkhsanaa.model.Test;
import java.io.*;

public class Main {
    public static String ADDRESS = null;
    public static final int PORT_NUM = 1113;
    public static final int TIMEOUT = 60000;

    private static final String binFilePath = "questions.bin";
    public static final String textFilePath = "questions.txt";

    public static void main(String[] args) {
        try {
            if (args.length == 0) args = new String[]{"--help"};
            switch (args[0]) {
                case "init":
                    if (!(args.length <= 2)) {
                        System.out.println("Invalid number of arguments");
                        break;
                    }
                    boolean fromFile = false;
                    if (args.length == 2 && args[1].equals("--fromFile")) fromFile = true;
                    Test newTest = new Test();
                    newTest.read(fromFile);

                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binFilePath));
                    oos.writeObject(newTest);
                    break;
                case "print":
                    try (ObjectInputStream ois=new ObjectInputStream(new FileInputStream(binFilePath))){
                        Test test = (Test) ois.readObject();
                        test.print(System.out);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "client":
                    if (args.length < 3) {
                        System.out.println("Invalid number of arguments");
                        break;
                    }
                    String name = args[1];
                    ADDRESS = args[2];
                    boolean showAnswers = false;
                    if (args.length == 4) {
                        if (args[3].equals("--showAnswers"))
                            showAnswers = true;
                        else {
                            System.out.println("Invalid Argument");
                            break;
                        }
                    }
                    Client client = new Client(name, showAnswers);
                    client.startTest();
                    break;
                case "server":
                    try (ObjectInputStream ois=new ObjectInputStream(new FileInputStream(binFilePath))){
                        Test test = (Test) ois.readObject();
                        Server server = new Server(test);
                        server.run();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("java -jar InquisitionApp.jar init [--fromFile]");
                    System.out.println("    Use to generate your own test, creates questions.bin");
                    System.out.println("    fromFile        - Reads from question.txt");
                    System.out.println("");
                    System.out.println("java -jar InquisitionApp.jar print");
                    System.out.println("    Print contents of the questions.bin");
                    System.out.println("    File questions.bin should exist in a same folder");
                    System.out.println("");
                    System.out.println("java -jar InquisitionApp.jar server");
                    System.out.println("    Runs server on port " + PORT_NUM);
                    System.out.println("");
                    System.out.println("java -jar InquisitionApp.jar client NAME 127.0.0.1 [--showAnswers]");
                    System.out.println("    Run client and connect to server");
                    System.out.println("    NAME            - Name to use");
                    System.out.println("    127.0.0.1       - Address of the server");
                    System.out.println("    showAnswers     - If the correct answer should be shown after each answer");
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}