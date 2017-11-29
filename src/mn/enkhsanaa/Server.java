
package mn.enkhsanaa;

import mn.enkhsanaa.model.Test;
import mn.enkhsanaa.thread.ServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Enkhsanaa Natsagdorj on 11/28/2017.
 * Server Class
 */

public class Server {
    private Test test;
    private ServerSocket serverSocket;

    public Server(Test test) {
        this.test = test;
        System.out.println("Server Listening......");
        try {
            serverSocket = new ServerSocket(Main.PORT_NUM);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
    }
    public void run() {
        System.out.println("Waiting for clients");
        while (true) {
            try {
                Socket s = serverSocket.accept();
                s.setSoTimeout(Main.TIMEOUT);
                Thread st = new Thread(new ServerThread(s, new Test(test)));
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
                break;
            }
        }
    }
}
