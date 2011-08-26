/*#TA*/ /* The interface for a class to model bank accounts */

class Account {
public:
// Accessor functions
	double getBalance();
	
// Mutator functions
	void initialize();			// call once when account created
	void deposit(double amount);
	void withdraw(double amount);
	void changeOD(bool allowed);
	void setRate(double iRate);
	void setMonthly(double charge);
	void setItem(double charge);
	void monthEnd();
	
// The data that we must track for each account object
private:
	double balance;
	double minBalance;
	bool overDraftAllowed;
	double interestRate;
	double itemCharge;
	double monthCharge;
}; /*#/TA*/

#include <iostream>
using namespace std;
/*#TB*/
int main(){
	Account mine, yours;

	mine.initialize();
	mine.changeOD(true);
	mine.deposit(100);
	mine.withdraw(200);
	mine.deposit(1500);
	mine.monthEnd();
	cout << "my Balance is " << mine.getBalance() << endl;

	yours.deposit(100);
	yours.withdraw(200);
	yours.deposit(1500);
	yours.monthEnd();
	cout << "your Balance is " << yours.getBalance() << endl;

	return 0;
}
/*#/TB*/
double Account::getBalance(){return balance;}

// This function must be called ONCE when an ccount object is
//	first created

void Account::initialize(){
	balance = 0;
	minBalance = 0;
	overDraftAllowed = false;
	itemCharge = .25;
	monthCharge = 2.00;
	interestRate = .0175;
}
	
// assertion: amount is non-negative
void Account::deposit(double amount) {
	if (amount >= 0)
		balance = balance + amount;
}

// assertion: amount is non-negative
void Account::withdraw(double amount) {
	if (amount < 0) return; 	//Assertion failure
	if (!overDraftAllowed && balance - amount - itemCharge < 0)
		return;		// overdraft not allowed
	balance = balance - amount - itemCharge;
	if (balance < minBalance)
		minBalance = balance;
}

void Account::changeOD(bool allowed){
	overDraftAllowed = allowed;
}
void Account::setRate(double iRate){
	interestRate = iRate;
}
void Account::setMonthly(double charge){
	monthCharge = charge;
}

void Account::setItem(double charge){
	itemCharge = charge;
}

void Account::monthEnd(){
	withdraw(monthCharge);
	if (minBalance < 0)
		withdraw(interestRate*(-minBalance));
	minBalance = balance;
}

