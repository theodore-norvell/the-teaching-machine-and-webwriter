//! Run.
/*#HA*/ /*#HB*/ /*#HC*/ /*#HD*/ /*#HE*/ /*#HF*/ /* The interface for a class to model bank accounts */
/*#DA*/ // The base class - distills what is common to all accounts
class Account {
public:
// Constructors
	Account();
	
// Accessor functions
	double getBalance() const;
	
// Mutator functions
	void credit(double amount);
	void debit(double amount);

// The data that we must track for each account object
protected:
	double balance;
};/*#HA*/

/*#DC*/ class Checking: public Account{	
/* Checking is derived from Account
	Checking accounts are accounts that permit overdrafts and charge
	interest on negative minimum monthly balances */
public:
	Checking(bool od);

	double getMinBalance() const;

	void setODPolicy(bool allowed);
	void monthEnd();
	void debit(double amount);	// Over-ride default version in account


// Set rates for all checking accounts
	static void setRate(double iRate);
	static void setMonthly(double charge);
	static void setItem(double charge);

protected:
	bool odAllowed;
	double minBalance;

// All checking accounts will use the same rates
	static double interestRate;
	static double monthlyCharge;
	static double itemCharge;
};/*#HC*/

/*#DD*/ class Savings: public Account{
/* Savings is derived from Account
	Savings accounts are accounts that permit no overdrafts but pay
	interest on positive minimum monthly balances */
public:
	Savings();

	double getMinBalance() const;

	void debit(double amount); // generic functionoverride 

	void monthEnd();	
	static void setRate(double iRate);
	static void setMonthly(double charge);
	static void setItem(double charge);


protected:
	double minBalance;
	static double interestRate;
	static double monthlyCharge;
	static double itemCharge;
}; /*#HD*/

#include <iostream>
using namespace std;

// A conventional function with an account object argument
void transaction(Account& a, double amount); 

int main(){
/*#DF*/	Account mine, yours;	// "Generic" accounts

// With static functions we can set rates for all savings accounts
// without actually having declared any Savings Object yet
	Savings::setItem(.50);
	Savings::setMonthly(1.50);
	Savings::setRate(.002);
// Now we declare some Savings Accounts
	Savings mySavings, yourSavings;

	Checking myChecking(true), yourChecking(false);

	mine.credit(200);
	mine.debit(100);
	mine.credit(1500);
	cout << "my Balance is " << mine.getBalance() << endl;

	mySavings.credit(200);
	mySavings.debit(100);
	mySavings.credit(1500);
	mySavings.monthEnd();
	cout << "my Savings Balance is " << mySavings.getBalance() << endl;

// How come we didn't get any interest??
	mySavings.debit(100);
	mySavings.credit(2000);
	mySavings.monthEnd();
	cout << "my Savings Balance is " << mySavings.getBalance() << endl;

	myChecking.credit(100);
	myChecking.debit(200);
	myChecking.credit(1500);
	myChecking.monthEnd();
	cout << "my Checking Balance is " << myChecking.getBalance() << endl;/*#HD*/

	yours.credit(100);
	yours.debit(200);
	yours.credit(1500);
	cout << "your Balance is " << yours.getBalance() << endl;

// Let's try using the transaction function with different Accounts
	transaction(mine, 200.);
	transaction(mine, -100.);
	transaction(mySavings, 200.);
	transaction(mySavings, -100.);

	return 0;
}/*#HF*/

/*#DB*/Account::Account(){
	balance = 0;
}

double Account::getBalance() const {return balance;}

// Generic approaches to mutators. Not right for all accounts
	
// Assertion: amount is non-negative
void Account::credit(double amount) {
	if (amount >= 0)    
		balance = balance + amount;
}

// Assertion: amount is non-negative
void Account::debit(double amount) {
	if (amount > 0 && amount <= balance )
		balance = balance - amount;
}/*#HB*/


/*#DE*/// Checking implementations
double Checking::interestRate = .0175;	// overdrafts
double Checking::monthlyCharge = 2.00;
double Checking::itemCharge = 0.0;

void Checking::setRate(double iRate){interestRate = iRate;}
void Checking::setMonthly(double charge){monthlyCharge = charge;}
void Checking::setItem(double charge){itemCharge = charge;}

Checking::Checking(bool od){ // Account constructor called 1st
	odAllowed = od;
	minBalance = 0.;
}

double Checking::getMinBalance() const {return minBalance;}


void Checking::setODPolicy(bool od){
	odAllowed = od;
}

void Checking::debit(double amount){
	if (amount < 0) return;  // assertion failure
	if (!odAllowed && balance - amount - itemCharge < 0 )
		return;		// overdraft not allowed
	balance = balance - amount - itemCharge;
	if (balance < minBalance)
		minBalance = balance;
}

void Checking::monthEnd(){ // over-rides Account version
	debit(monthlyCharge-itemCharge);
	if (minBalance < 0) balance = balance + interestRate*minBalance;
	minBalance = balance;
}/*#HE*/

// Savings implementations

double Savings::interestRate = .0025;	// interest
double Savings::monthlyCharge = 1.00;
double Savings::itemCharge = 0.30;

void Savings::setRate(double iRate){interestRate = iRate;}
void Savings::setMonthly(double charge){monthlyCharge = charge;}
void Savings::setItem(double charge){itemCharge = charge;}


Savings::Savings(){ // Account constructor called 1st
	minBalance = 0.;
}

double Savings::getMinBalance() const {return minBalance;}

void Savings::debit(double amount){
	if (amount < 0 || balance - amount - itemCharge < 0 )
		return;		// overdraft not allowed
	balance = balance - amount - itemCharge;
	if (balance < minBalance)
		minBalance = balance;
}

void Savings::monthEnd(){ // over-rides Account version
	debit(monthlyCharge-itemCharge);
	if (minBalance > 0) balance = balance + interestRate*minBalance;
	minBalance = balance;
}

void transaction(Account& a, double amount){
	if (amount < 0)
		a.debit(-amount);
	else
		a.credit(amount);
}
