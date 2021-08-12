using System;
using System.IO;
using System.Net.Sockets;

namespace BankClient
{
    class Client : IDisposable
    {
        const int port = 8888;

        private int CustomerId;
        private readonly StreamReader reader;
        private readonly StreamWriter writer;

        public Client()
        {
            // Connecting to the server and creating objects for communications
            TcpClient tcpClient = new TcpClient("localhost", port);
            NetworkStream stream = tcpClient.GetStream();
            reader = new StreamReader(stream);
            writer = new StreamWriter(stream);

            this.CustomerId = FindID();
        }


        public int FindID(){
            writer.WriteLine("id");
            writer.Flush();
            int ID =int.Parse(reader.ReadLine());
            return ID;
        }

        public int getCustomerID(){return CustomerId;}

        public int FindStock(){
            writer.WriteLine("stock");
            writer.Flush();
            String line = reader.ReadLine();
            return int.Parse(line);
        }




        public void Transfer(int toAccount)
        {
            // Writing the command
            writer.WriteLine($"send {toAccount}");
            writer.Flush();

            // Reading the response
            string line = reader.ReadLine();
            if (line.Trim().ToLower() != "success")
                Console.WriteLine(line);
        }



        public int[] GetAccountNumbers()
        {
            // Sending command
            writer.WriteLine("ACCOUNTS");
            writer.Flush();

            // Reading the number of accounts
            string line = reader.ReadLine();
            int numberOfAccounts = int.Parse(line);

            // Reading the account numbers
            int[] accounts = new int[numberOfAccounts];
            for (int i = 0; i < numberOfAccounts; i++)
            {
                line = reader.ReadLine();
                accounts[i] = int.Parse(line);
            }

            return accounts;
        }







        public void Dispose()
        {
            reader.Close();
            writer.Close();
        }
    }
}
