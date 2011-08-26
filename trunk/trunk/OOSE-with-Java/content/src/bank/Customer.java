package bank;

public class Customer {
    private Name name ;
    private Account[] accounts ;
    
    public long getBalance() {
        long balance = 0 ;
        for( int i=0 ; i < accounts.length ; ++i ) {
            balance += accounts[i].getBalance() ; 
        }
        return balance ;
    }
}
