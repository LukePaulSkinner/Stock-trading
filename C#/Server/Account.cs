namespace Assignment_C_
{
    class Account
    {
        public Account(int accountNumber, bool stock)
        {
            Stock = stock;
            AccountNumber = accountNumber;
        }

        public bool Stock { get; set; }
        public int AccountNumber { get; }

    }
}
