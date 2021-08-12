package Assignment;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProgram extends JFrame
{

    public static JTextArea logbox = new JTextArea();
    public static JTextArea accountbox = new JTextArea();
    private final static int port = 8888;

    private static final Market market = new Market();

    private static String logtext = "";



    public static void main(String[] args)
    {
        JFrame f=new JFrame();
        f.setSize(1000,500);
        //JTextArea logbox = new JTextArea();
        logbox.setBounds(10,10,780,440);
        logbox.setEditable(false);

        accountbox.setBounds(810,10,160,440);
        accountbox.setEditable(false);

        f.add(accountbox);
        f.add(logbox);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        RunServer();
    }



    private static void RunServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for incoming connections...");
            log("Waiting for incoming connections...");
            while (true) {
                //When a new client connects they are assigned a client handler and are connected to the market
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, market)).start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void log(String s){
        logtext += s + "\n";
        logbox.setText(logtext);
    }
    public static void accountlog(String s){
        accountbox.setText(s);

    }

}
