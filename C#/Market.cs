using System;
using System.Collections.Generic;

namespace Assignment_C_
{
    class Market
    {
        public readonly Dictionary<int, Account> activeaccounts = new Dictionary<int, Account>();
        public readonly Dictionary<int, Account> allaccounts = new Dictionary<int, Account>();

        //Create account DONE
        public void CreateAccount(int accountNumber)
        {

            lock(activeaccounts){
                if (findStock()==-1){
                    Account account = new Account(accountNumber,true);
                    activeaccounts.Add(accountNumber, account);
                    allaccounts.Add(accountNumber, account);
                }else{
                    Account account = new Account(accountNumber,false);
                    activeaccounts.Add(accountNumber, account);
                    allaccounts.Add(accountNumber, account);
                }
            }
            

            
        }
        //Account list NEED FIX
        public int[] GetListOfAccounts()
        {
            lock(activeaccounts){

            List<int> result = new List<int>();
            foreach (Account account in activeaccounts.Values)
                result.Add(account.AccountNumber);

            return result.ToArray();

            }
        }






        //Find stock DONE
        public int findStock()
        {
            lock (activeaccounts) {
                foreach (Account account in activeaccounts.Values){
                    if (account.Stock){
                        return account.AccountNumber;
                    }

                }
                    
                return -1;
        }
    }



        //Leave market DONE
        public void LeaveMarket(int ID){
            lock(activeaccounts){
                activeaccounts.Remove(ID);
                
            }
        }



        //Give stock DONE
        public void GiveRandomStock(){
            lock(activeaccounts){
                if(activeaccounts.Count>0){
                    Random rand = new Random();
                    int randomint =  rand.Next(activeaccounts.Count);
                    int count = 0;
                    foreach (Account account in activeaccounts.Values){
                        if(count==randomint){
                            account.Stock=true;
                        }
                        count++;
                    }
                }
            }
        }



        //Transfer  DONE
        public void Transfer(int fromAccount, int toAccount)
        {
            lock (activeaccounts)
            {
                if (activeaccounts[fromAccount].Stock == false)
                    throw new Exception($"You do not have the stock");
                else if (!activeaccounts.ContainsKey(toAccount))
                    throw new Exception(
                        $"That account does not exist");
                else{
                    activeaccounts[fromAccount].Stock=false;
                    activeaccounts[toAccount].Stock=true;
                }
            }
        }


    }
}
