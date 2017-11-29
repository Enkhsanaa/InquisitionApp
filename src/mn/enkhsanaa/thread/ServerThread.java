package mn.enkhsanaa.thread;

import mn.enkhsanaa.model.Question;
import mn.enkhsanaa.model.Test;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Enkhsana Natsagdorj on 11/28/2017.
 * Server thread
 */

public class ServerThread implements Runnable {
    private Test test = null;
    private DataInputStream is = null;
    private ObjectOutputStream os = null;
    private Socket socket;
    private String clientName;

    private int correct;
    private int currentQuestionIndex;

    public ServerThread(Socket socket, Test test) {
        this.socket = socket;
        this.test = test;
        this.correct = 0;
        this.clientName = "";
        this.currentQuestionIndex = 0;
    }

    private void publish(Question question) throws IOException {
        os.writeObject(question);
        os.flush();
    }
    private void publishResult() throws IOException {
        String response = "You have answered " + correct + "/" + currentQuestionIndex + " questions correctly";
        System.out.println(clientName + " response: " + response);
        os.writeObject(response);
        os.flush();
    }

    private void getName() throws IOException {
        int length = 0;
        do {
            length = is.readInt();
            if (length > 0) {
                byte[] data = new byte[length];
                is.readFully(data);
                clientName = new String(data);
                System.out.println("Client connected: " + clientName);
            }
        } while (length <= 0);

    }
    private void handleClientResponse() throws IOException {
        byte[] data = new byte[1];
        is.readFully(data);
        String answer = new String(data);
        if (test.checkAnswer(currentQuestionIndex, answer)) {
            this.correct++;
            System.out.println(clientName + " correctly respond to question " + currentQuestionIndex + ". Current correct answer count is " + this.correct);
        }
        else System.out.println(clientName + " gave wrong response to question " + currentQuestionIndex + ". Current correct answer count is " + this.correct);
    }

    public void run() {
        try {
            initConnection();
            while (clientName.length() == 0) getName();
            while (currentQuestionIndex < test.getQuestionCount()) {
                System.out.println("Sending question " + currentQuestionIndex + " to " + clientName);
                publish(test.getQuestion(currentQuestionIndex));
                try {
                    handleClientResponse();
                } catch (SocketTimeoutException e) {
                    System.out.println(clientName + " didn't respond to question " + currentQuestionIndex + ". Taking as wrong answer");
                }
                currentQuestionIndex++;
            }
            publishResult();
            closeConnection();
        } catch(IOException e){
            System.out.println(clientName + " closed connection. Result: " + correct + "/" + currentQuestionIndex + " was answered.");

        }
    }

    // Helpers
    private void initConnection() throws IOException {
        is = new DataInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
    }
    private void closeConnection() throws IOException {
        is.close();
        os.close();
        socket.close();
        System.out.println("Closing socket for " + clientName + ". Result: " + correct + "/" + currentQuestionIndex + " was answered.");
    }
}
