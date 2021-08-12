package Assignment;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientProgram extends JFrame {

    private static String clientlog = "";
    private static int customerId;
    private static Client client;
    private static int Stock = 0;
    private static int[] accounts;

    public static void main(String[] args) throws IOException{
        //Setting up GUI

        JFrame f = new JFrame();
        f.setSize(800, 500);
        JButton b = new JButton("Give Stock");//creating instance of JButton
        b.setBounds(350, 420, 130, 30);//x axis, y axis, width, height
        JTextArea textArea = new JTextArea();
        textArea.setBounds(20, 420, 320, 30);
        TextArea log = new TextArea();
        log.setBounds(20, 20, 460, 400);
        log.setEditable(false);
        f.add(log);
        TextArea users = new TextArea();
        users.setBounds(500, 20, 300, 460);
        users.setEditable(false);
        f.add(users);

        f.add(textArea);
        f.add(b);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = textArea.getText();
                try {
                    int toAccount = Integer.parseInt(input);
                    client.transfer(toAccount);
                }

                catch(NumberFormatException er) {

                    clientlog += "You have entered a string .Please enter an integer.\n";
                    log.setText(clientlog);
                } catch (Exception e1) {
                    clientlog += e1.getMessage()+ "\n";
                    log.setText(clientlog);
                }
            }
        });


        while (true) {

            try {
                client = new Client();

                customerId = client.getCustomerID();
                //This is used when the market is rebuilt. This code assigns the stock to the previous owner before the server closed.
                if (client.findStock()==customerId){
                    if (Stock!=client.findStock()){
                        accounts = client.getAccountNumbers();
                        //waits for all clients to connect
                        while (!(contains(accounts,Stock))){
                            accounts = client.getAccountNumbers();
                            Thread.sleep(100);
                        }
                        //assigns to previous owner
                        client.transfer(Stock);
                    }

                }

                //Updating Jframe with login
                f.setTitle("Your ID: " + customerId);
                clientlog += "Logged in successfully.\n";
                log.setText(clientlog);


                //loops until an exception occurs
                while (true) {
                    //Updating the connected clients
                    accounts = client.getAccountNumbers();
                    String usertext = "";
                    usertext += "Currently active accounts: \n";
                    Stock = client.findStock();
                    for (Integer accountNumber : accounts) {
                        if (accountNumber == Stock) {
                            usertext += "Account: " + accountNumber + " : Has stock TRUE \n";
                        } else {
                            usertext += "Account: " + accountNumber + " : Has stock FALSE \n";
                        }
                    }
                    users.setText(usertext);
                    Thread.sleep(500);
                }
            //Will trigger when no server is found
            } catch (Exception e) {
                clientlog += "No server found, looking for new server\n";
                log.setText(clientlog);
                //If the client is the stockholder then they will rebuild the server
                if (customerId==Stock) {
                    final String dir = System.getProperty("user.dir")+"\\src\\Assignment\\Server.jar";

                    ProcessBuilder pb = new ProcessBuilder("java", "-jar", dir);
                    Process p = pb.start();

                    clientlog += "Rejoining server\n";
                    log.setText(clientlog);
                }
            }

            //CODE FOR REBUILDING MARKET
            int IDpos = findIndex(accounts,customerId);

            try {
                //the code below creates and closes clients on the new server such so that once all the clients have connected the new server will have the same clients with the same ids as the old.
                if (IDpos==0){

                    for (int i =0;i<accounts[IDpos];i++){
                        Client a = new Client();
                        a.close();
                    }
                }
                else {
                    //Waiting for other clients to rebuild their ids
                    Thread.sleep(IDpos*1000);
                    for (int i =0;i<(accounts[IDpos]-accounts[IDpos-1])-1;i++){
                        Client a = new Client();
                        a.close();
                    }
                }

            } catch (Exception e) {
            }
        }
    }


    //Finding index of item in array used for server creation task
    public static int findIndex(int arr[], int t)
    {


        if (arr == null) {
            return -1;
        }


        int len = arr.length;
        int i = 0;


        while (i < len) {


            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }

    //check if array contains used for server creation task
    public static boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }


}
