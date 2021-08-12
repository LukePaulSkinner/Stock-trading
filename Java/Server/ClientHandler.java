package Assignment;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private static Market market;


    public ClientHandler(Socket socket, Market market) {
        this.socket = socket;
        this.market = market;
    }

    @Override
    public void run() {
        //Need to get unique ID
        int customerId=0;
        synchronized (market.allaccounts){
            customerId= market.allaccounts.size();
        }


        try (
                Scanner scanner = new Scanner(socket.getInputStream());
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            try {

                market.createAccount(customerId);
                if (market.findStock2()==customerId){
                    ServerProgram.log("New connection; customer ID " + customerId+" There are no other users so this user has been given the stock");
                }else {ServerProgram.log("New connection; customer ID " + customerId);}






                connectedaccounts();





                while (true) {




                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");

                    switch (substrings[0].toLowerCase()) {
                        case "accounts":
                            List<Integer> listOfAccounts = market.getListOfAccounts();
                            writer.println(listOfAccounts.size());
                            for (Integer accountNumber : listOfAccounts)
                                writer.println(accountNumber);
                            break;

                        case "send":
                            int toAccount = Integer.parseInt(substrings[1]);
                            //Checking the user has the stock
                            if (customerId!=market.findStock2()){
                                writer.println("ERROR: You do not have the stock");

                            }
                            //Checking the user being sent the stock exists
                            else if (!(market.getListOfAccounts().contains(toAccount))){
                                writer.println("ERROR: That user does not exist");
                            }
                            //sending the stock
                            else {
                                market.transfer(customerId, toAccount);
                                writer.println("SUCCESS");
                                ServerProgram.log("Transferred stock from user:"+customerId+" To user:"+toAccount);
                            }

                            break;
                        case "stock":
                            writer.println(market.findStock2());
                            break;
                        case "id":
                            writer.println(customerId);
                            break;






                        default:
                            throw new Exception("Unknown command: " + substrings[0]);
                    }
                }
            } catch (Exception e) {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }
        } catch (Exception e) {
        } finally {
            if (customerId==market.findStock2()){
                market.leaveMarket(customerId);
                market.giveRandomStock();
                if (market.findStock2()==-1){
                    ServerProgram.log("User: "+customerId+ " has left the market. This user had the stock. There are no other users to give stock to. Stock will be given to the next user to join.");
                }
                else {ServerProgram.log("User: "+ customerId + " has left the market. This user had the stock. Transferring stock to user: "+market.findStock2());}

            }
            market.leaveMarket(customerId);
            ServerProgram.log("Customer " + customerId + " disconnected.");


            connectedaccounts();


        }
    }

    private void connectedaccounts(){
        String accountsString="";
        accountsString+="Currently connected accounts:\n";
        List<Integer> leaveaccounts = market.getListOfAccounts();
        for (Integer accountNumber : leaveaccounts)
            accountsString+="Account id: "+accountNumber+"\n";
        ServerProgram.accountlog(accountsString);
    }

}
