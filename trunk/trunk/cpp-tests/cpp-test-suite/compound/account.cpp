//! Run.
/*#HA*/#include <iostream>
using namespace std;

/*#DA*/struct Account{
    int number;
    double balance;
    bool overdraftAllowed;
};
/*#HA*/
bool transaction(Account& anAccount, double amount);

void main(){
    Account johnsAccount;
    Account myAccount;
    johnsAccount.number = 2002;
    johnsAccount.balance = 0.;
    johnsAccount.overdraftAllowed = false;
    myAccount.number = 2037;
    myAccount.balance = 0.;
    myAccount.overdraftAllowed = true;
    transaction(johnsAccount,100.);
    transaction(myAccount,100.);
    if (transaction(johnsAccount,-200.))
        cout << "Yes! You withdrew $100.00 more than you put in!\n";
    else cout << "Sorry. Not enough money, John!\n\n";
    if (transaction(myAccount,-200.))
        cout << "Yes! You withdrew $100.00 more than you put in!\n";
    else cout << "Sorry. Not enough money!\n\n";
}
/*#DA*/
bool transaction(Account& anAccount, double amount){
    bool allowed;
    if (amount < 0) { // then a debit
        if (anAccount.balance + amount < 0
				&& anAccount.overdraftAllowed) {
            anAccount.balance = anAccount.balance + amount;
            allowed = true;
        }
        else allowed = false; // not enough money
    } else { // deposit
        anAccount.balance = anAccount.balance + amount;
        allowed = true;
    }
    return allowed;
}
