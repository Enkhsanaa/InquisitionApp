package mn.enkhsanaa;

import mn.enkhsanaa.model.Question;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Enkhsanaa Natsagdorj on 11/28/2017.
 * Client class
 */

public class Client {
    private Socket socket;
    private Scanner sc;
    private ObjectInputStream is;
    private DataOutputStream os;

    private String name;
    private static boolean showAnswers;
    private static Question currentQuestion;

    public Client(String name, boolean showAnswers) {
        this.name = name;
        this.showAnswers = showAnswers;

    }
    public void startTest() {
        try {
            initConnection();
            sendName();
            new Thread(new ReadResponse(os)).start();

            while (true) {
                if (!runTest()) break;
            }

            closeConnection();
        } catch (IOException e) {
            System.out.println("Client failed miserably.. Message: " + e.getMessage());
        }
    }
    private void sendName() throws IOException {
        os.writeInt(name.length());
        os.writeBytes(name);
        os.flush();
    }
    private boolean runTest() throws IOException {
        Object o = null;
        try {
            o = is.readObject();
            currentQuestion = (Question) o;
            currentQuestion.print(System.out);
        } catch (Exception e) {
            displayResult((String) o);
            return false;
        }
        return true;
    }

    private void displayResult(String result) {
        System.out.print(result);
        System.exit(0);
    }

    private class ReadResponse implements Runnable  {
        private DataOutputStream os;
        public ReadResponse(DataOutputStream os) {
            this.os = os;
        }
        public void run() {
            char response;
            String line;
            while (true) {
                line = sc.nextLine();
                if (line.length() > 0) {
                    response = line.charAt(0);
                    if ('A' <= response && response < 'A' + currentQuestion.getOptionCount()) {
                        try {
                            os.write((byte) response);
                            os.flush();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (showAnswers) {
                            if (currentQuestion.checkAnswer(String.valueOf(response))) {
                                System.out.println("Correct!");
                            } else {
                                System.out.println("Wrong! The correct answer is " + currentQuestion.getAnswer());
                            }
                        }
                        continue;
                    }
                }
                System.out.println("Invalid answer");
            }
        }
    }
    private void initConnection() throws  IOException {
        socket = new Socket(Main.ADDRESS, Main.PORT_NUM);
        sc = new Scanner(System.in);
        is = new ObjectInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }
    private void closeConnection() throws IOException {
        is.close();
        os.close();
        sc.close();
        socket.close();
    }
}
