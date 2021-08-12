package Assignment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable {
    final int port = 8888;

    private final Scanner reader;
    private final PrintWriter writer;
    private int customerID;

    public Client() throws Exception {
        // Connecting to the server and creating objects for communications
        Socket socket = new Socket("localhost", port);

        reader = new Scanner(socket.getInputStream());


        writer = new PrintWriter(socket.getOutputStream(), true);

        int custID = findID();

        this.customerID= custID;



    }

    private int findID(){
        writer.println("id");
        int ID = Integer.parseInt(reader.nextLine());
        return ID;
    }

    public int getCustomerID(){return customerID;}


    public int findStock(){
        writer.println("stock");
        String  line = reader.nextLine();
        return Integer.parseInt(line);
    }



    public void transfer(int toAccount) throws Exception {
        // Writing the command
        writer.println("send "+toAccount);

        // Reading the response
        String line = reader.nextLine();
        if (line.trim().compareToIgnoreCase("success") != 0)
            throw new Exception(line);
    }



    public int[] getAccountNumbers() {
        // Sending command
        writer.println("ACCOUNTS");

        // Reading the number of accounts
        String line = reader.nextLine();
        int numberOfAccounts = Integer.parseInt(line);

        // Reading the account numbers
        int[] accounts = new int[numberOfAccounts];
        for (int i = 0; i < numberOfAccounts; i++) {
            line = reader.nextLine();
            accounts[i] = Integer.parseInt(line);
        }

        return accounts;
    }



    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }
}
