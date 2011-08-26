/*#TA*/ /* The interface for a class to model bank accounts */

class Account {
public:
// Constructors
	Account();
	Account(bool od);
	
// Accessor functions
	double getBalance();
	
// Mutator functions
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

int main(){
	Account mine(true), yours;

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

/*#TB*/Account::Account(){
	balance = 0;
	minBalance = 0;
	overDraftAllowed = false;
	itemCharge = .25;
	monthCharge = 2.00;
	interestRate = .0175;
}

Account::Account(bool od){
	balance = 0;
	minBalance = 0;
	overDraftAllowed = od;
	itemCharge = .25;
	monthCharge = 2.00;
	interestRate = .0175;
}/*#/TB*/

double Account::getBalance(){return balance;}

// This function must be called ONCE when an ccount object is
//	first created

	
// Assertion: amount is non-negative
void Account::deposit(double amount) {
	if (amount >= 0)    
		balance = balance + amount;
}

// Assertion: amount is non-negative
void Account::withdraw(double amount) {
	if (amount < 0) return;  // assertion failure
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
