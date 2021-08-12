using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace Assignment_C_
{
    class ServerProgram
    {
        private const int port = 8888;

        private static readonly Market market = new Market();

        static void Main(string[] args)
        {


            RunServer();
        }

        private static void RunServer()
        {
            TcpListener listener = new TcpListener(IPAddress.Loopback, port);
            listener.Start();
            Console.WriteLine("Waiting for incoming connections...");
            while (true)
            {
                TcpClient tcpClient = listener.AcceptTcpClient();
                new Thread(HandleIncomingConnection).Start(tcpClient);
            }
        }

        private static void HandleIncomingConnection(object param)
        {
            TcpClient tcpClient = (TcpClient) param;
            using (Stream stream = tcpClient.GetStream())
            {
                StreamWriter writer = new StreamWriter(stream);
                StreamReader reader = new StreamReader(stream);
                int customerId = 0;

                lock(market.allaccounts){
                    customerId=market.allaccounts.Count;
                }


                try
                {
                    market.CreateAccount(customerId);
                    if(market.findStock()==customerId){
                        Console.WriteLine($"New connection; customer ID {customerId} There are no other users so they have been given the stock");
                    }
                    else{
                        Console.WriteLine($"New connection; customer ID {customerId}");
                    }
                    
                    Console.WriteLine("Currently connected accounts:");
                    int[] listOfAccounts = market.GetListOfAccounts();
                    foreach (int accountNumber in listOfAccounts)
                        Console.WriteLine($"Account ID: {accountNumber}");


                    while (true)
                    {
                        string line = reader.ReadLine();
                        string[] substrings = line.Split(' ');
                        switch (substrings[0].ToLower())
                        {
                            case "accounts":

                                int[] listOfAccounts1 = market.GetListOfAccounts();
                                writer.WriteLine(listOfAccounts1.Length);
                                foreach (int accountNumber in listOfAccounts1)
                                    writer.WriteLine(accountNumber);
                                writer.Flush();
                                    
                                break;

                            case "send":
                                
                                int toAccount = int.Parse(substrings[1]);




                                bool isaccount = false;
                                int[] checkvalues = market.GetListOfAccounts();
                                foreach (int accountNumber in checkvalues){
                                    if(toAccount==accountNumber){
                                        isaccount=true;
                                    }
                                }




                                
                                if(customerId!=market.findStock()){
                                    writer.WriteLine("ERROR: You do not have the stock");
                                    writer.Flush();
                                }
                                else if(isaccount==false){
                                    writer.WriteLine("ERROR: That is not a valid account");
                                    writer.Flush();
                                }
                                else{
                                    market.Transfer(customerId,toAccount);
                                    writer.WriteLine("SUCCESS");
                                    writer.Flush();
                                    Console.WriteLine($"Transferred stock from user:{customerId} To user:{toAccount}");
                                }
                                break;
                            case "stock":
                                writer.WriteLine(market.findStock());
                                writer.Flush();
                                break;    
                            case "id":
                                writer.WriteLine(customerId);
                                writer.Flush();
                                break;
                            default:
                                throw new Exception($"Unknown command: {substrings[0]}.");
                        }
                    }
                }
                catch (Exception e)
                {
                    try
                    {
                        writer.WriteLine("ERROR " + e.Message);
                        writer.Flush();
                        tcpClient.Close();
                    }
                    catch
                    {
                        Console.WriteLine("Failed to send error message. User may have left");
                    }
                }
                finally
                {
                    if(customerId==market.findStock()){
                        market.LeaveMarket(customerId);
                        market.GiveRandomStock();
                        if(market.findStock()==-1){
                            Console.WriteLine($"User:{customerId} has left the market. This user had the stock. There are no other users to give stock to. Stock will be given to the next user to join.");
                        }
                        else{Console.WriteLine($"User:{customerId} has left the market. This user had the stock.Transferring stock to user:{market.findStock()}");}
                    }
                    else{
                        market.LeaveMarket(customerId);
                        Console.WriteLine($"Customer {customerId} disconnected.");
                    }
                    Console.WriteLine("Currently connected accounts:");
                    int[] leaveaccount = market.GetListOfAccounts();
                    writer.WriteLine(leaveaccount.Length);
                    foreach (int accountNumber in leaveaccount)
                        Console.WriteLine($"Account ID: {accountNumber}");
                    
                }
            }
        }
    }
}

