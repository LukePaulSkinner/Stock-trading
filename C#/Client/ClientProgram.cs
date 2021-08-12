using System;

namespace BankClient
{
    class Program
    {
        static void Main(string[] args)
        {
            

            try
            {

                using (Client client = new Client())
                {
                    int CustomerId = client.getCustomerID();
                    Console.WriteLine("Logged in successfully.");

                    while (true)
                    {
                        Console.WriteLine($"Your ID:{CustomerId}");
                        int[] accountNumbers = client.GetAccountNumbers();
                        Console.WriteLine("Currently active accounts:");
                        foreach (int account in accountNumbers){
                            if(account==client.FindStock())
                                Console.WriteLine($"Account:{account} :Has stock TRUE");
                            else   
                                Console.WriteLine($"Account:{account} :Has stock FALSE");
                        }
                            

                        Console.WriteLine("If you would like to send the stock Press T. If you would like to leave the market press L. Press any other key to reprint.");
                        var choice = Console.ReadLine().Trim().ToUpper();
                        switch (choice)
                        {
                            case "T":
                                int toAccount;
                                Console.WriteLine("Pleases enter an account to transfer to.");
                                try{
                                    toAccount = int.Parse(Console.ReadLine());
                                    client.Transfer(toAccount);
                                }catch(FormatException){
                                    Console.WriteLine("String detected please enter a number");
                                }



                                break;

                            case "L":
                                Console.WriteLine("Leaving market...");
                                Environment.Exit(0);
                                break;

                            default:
                                Console.WriteLine($"Unknown command: {choice}");
                                break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }
    }
}
