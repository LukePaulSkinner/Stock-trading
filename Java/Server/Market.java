package Assignment;


import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Market {

    public static final Map<Integer, Account> activeaccounts = new TreeMap<>();
    public static final Map<Integer, Account> allaccounts = new TreeMap<>();

    //Creates an account and adds them to the account maps
    public static void createAccount(int accountNumber)
    {
        synchronized (activeaccounts) {
            if (Market.findStock() == -1) {
                Account account = new Account(accountNumber, true);
                activeaccounts.put(accountNumber, account);
                allaccounts.put(accountNumber, account);
            } else {
                Account account = new Account(accountNumber, false);
                activeaccounts.put(accountNumber, account);
                allaccounts.put(accountNumber, account);
            }
        }

    }
    //Removes an account from the active accounts map
    public void leaveMarket(int customerId){
        synchronized (activeaccounts) {
            activeaccounts.remove(customerId);
        }
    }

    //Give Stock to a random user
    public void giveRandomStock(){
        synchronized (activeaccounts) {
            if (activeaccounts.size()>0) {
                Random rand = new Random();
                int randomint = rand.nextInt(activeaccounts.size());
                int count = 0;
                for (Account account : activeaccounts.values()) {
                    if (count == randomint) {
                        account.setStock(true);
                    }
                    count++;
                }
            }
        }
    }

    public List<Integer> getListOfAccounts() {
        synchronized (activeaccounts) {
            List<Integer> result = new ArrayList<Integer>();

            for (Account account : activeaccounts.values())
                result.add(account.getAccountNumber());

            return result;
        }
    }
    public static int findStock(){
        synchronized (activeaccounts) {
            for (Account account : activeaccounts.values())
                if (account.getStock()) {
                    return account.getAccountNumber();
                }
            return -1;
        }
    }
    public  int findStock2(){
        synchronized (activeaccounts) {
            for (Account account : activeaccounts.values())
                if (account.getStock()) {
                    return account.getAccountNumber();
                }
            return -1;
        }
    }








    public void transfer(int fromAccount, int toAccount) throws Exception {
        synchronized (activeaccounts)
        {
            //Check account has the stock
            if (!activeaccounts.get(fromAccount).getStock()) {
                throw new Exception("You do not have the stock");
            }
            //Checking account transfering to exists
            else if (!activeaccounts.containsKey(toAccount)){
                throw new Exception("That account does not exist");
            }
            else {
                activeaccounts.get(fromAccount).setStock(false);
                activeaccounts.get(toAccount).setStock(true);
            }

        }
    }



}
