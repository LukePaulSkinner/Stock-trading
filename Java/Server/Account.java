package Assignment;
public class Account {
    //Account class to help store information about users and stock
    private final int accountNumber;
    private boolean stock;

    public Account(int accountNumber, Boolean stock) {

        this.accountNumber = accountNumber;
        this.stock = stock;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public boolean getStock(){return stock;}

    public void setStock(boolean newstock){this.stock = newstock;}



}
