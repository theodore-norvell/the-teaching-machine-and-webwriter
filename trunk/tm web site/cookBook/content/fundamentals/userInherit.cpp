/*#H*/ class MyString{
/*#P="MyString" "An <b>object</b> of the MyString class"*/
public:
	MyString(char* p);	// Construct using a standard string
	MyString();			// "default" (no arguement) constructor
	MyString(MyString& orig);		// Copy constructor
	~MyString();		// standard destructor to deal with heap

// Accessor functions - used to read object data without changing it
	int length() ;
	char getChar(int i) ;	// get char at location i
	void get(char* buff) ;	// Get the string & put it into user buff
	bool compare(MyString& other);	// true if equal
	
// Mutator functions - used to change string objects
	void setChar(int i, char c); // Change char at i to c
	void changeTo(char* newString);	// Change the whole string
	
	/* Notice the change to pass by ref to improve efficiency */
	void changeTo(MyString& newString);	// Function overload

private:
	char* mPtr;		// pointer into the heap where the actual string will be
	int mLength;	// length of the string
};
/*#DA*/ // A class of users for network management
class User{
public:
	User(char* n, char* u, char* p);

// Accessors
	MyString& name();	// return reference
	int allocation();	// Disk space allowed
	MyString password();	// return value
	MyString uname() ;
	bool confirm(MyString& uName, MyString& pass);
// Mutators
	bool setPassword(MyString& p1, MyString& p2);
	void setAllocation(int a);

protected:
	MyString /*#p="MyString"*/mName/*#/p*/;		// Notice attributes that are OBJECTS!
	MyString mUname;
	MyString mPassword;
	int mAllocation;	// Memory allocation, in MBytes
};/*#HA*/

/*#DB*/class Student: public User{
public:
	Student(char* n, char* id, char* u, char* p);
	MyString id();
	void buy(long amount);
	bool approvePrint(int pages);
protected:
	MyString mId;
	long mPrintPennies;
};/*#HB*/

/*#DC*/class Faculty: public User{
public:
	Faculty(char* n, char* u, char* p, char* today);

// accessor functions
	long pages();
	MyString cleared();

// mutator functions
	bool approvePrint(int pages);	// increases page count
	void clear(char* d);		// clears count & notes date
protected:
	long mPages;		// Pages used since
	MyString mCleared;		// last date cleared
};/*#HC*/



#include <iostream>
using namespace std;

int main(){
/*#DD*/	Faculty mpbl("Michael Bruce-Lockhart", "mpbl", "bftplx", "11/11/2002");
	Student cullam("Cullam Bruce-Lockhart", "2001001762", "cullam", "oBiewan");/*#HD*/
/*#DE*/	// Faculty mpblClone(mpbl);
/*#HE*/	mpbl.setAllocation(100);
	cullam.setAllocation(10);
	cullam.buy(1000);	// $10.00 printing credit

	return 0;
}

/***************************************************************
			Implementation of User 
			(normally in a separate file)
******************************************************************/

// Constructors in the form
//	 <Name>(<argument list>) : <initialization list> {
//		special code
//	}

/*#DI*/ User::User(char* n, char* u, char* p) : mName(n),
	mUname(u),mPassword(p),mAllocation(0){
}
/*#HI*/
// Accessors
MyString& User::name() {return mName;}	// Note returning reference
MyString User::uname() {return mUname;} // returning value
MyString User::password(){return mPassword;}
int User::allocation() {return mAllocation;}	// Disk space allowed

// A little more complex accessor, used to confirm username, password pairs
bool User::confirm(MyString& uName, MyString& pass) {
	return(mUname.compare(uName) && mPassword.compare(pass));
}

// Mutators
// Set password assumes two identical password strings
bool User::setPassword(MyString& new1, MyString& new2){
	if (new1.length()<6 || !new1.compare(new2)) return false;
	mPassword.changeTo(new1);	// change password
	return true;
}

void User::setAllocation(int a){
	mAllocation = a;
}


const int PAGE_COST = 5;

/***************************************************************
			Implementation of Student (derived from User)
			(normally in a separate file)
******************************************************************/
// Accessors
MyString Student::id() {return mId;}

// Mutators
void Student::buy(long amount){
	if(amount > 0)
		mPrintPennies = mPrintPennies + amount;
}

bool Student::approvePrint(int pages){
	if (pages < 0 || pages * PAGE_COST > mPrintPennies) return false;
	mPrintPennies = mPrintPennies - pages * PAGE_COST;
	return true;
}

// Constructors
/*#DI*/ Student::Student(char* n, char* id, char* u, char* p) :
	User(n,u,p),mId(id){	// Initialization - data to User constructor
	mAllocation = 5;
	mPrintPennies = 0;
}/*#HI*/		

/***************************************************************
			Implementation of Faculty (derived from User)
			(normally in a separate file)
******************************************************************/
// Constructors
Faculty::Faculty(char* n, char* u, char* p, char* date) :
User(n,u,p),mCleared(date) {	// Initialization - data to User constructor
	mAllocation = 100;
	mPages = 0;
}		

bool Faculty::approvePrint(int pages){
	mPages = mPages + pages;	// Faculty given approval automatically
	return true;
}

void Faculty::clear(char* d){
	mPages = 0;
	mCleared.changeTo(d);
}


/***************************************************************
			Implementation of MyString 
			(normally in a separate file)
******************************************************************/

// These would normally be declared by including <cstring>
long strLen( char* pStr);
void strCpy(char* dest,  char* source);

MyString::MyString(){
	mLength = 0;
	if(mPtr = new char[1])
		*mPtr = '\0';
}

MyString::MyString( char* p){
	mLength = strLen(p);
	if (mPtr = new char[mLength+1])
	strCpy(mPtr,p);
	else
		mLength = 0;
}

MyString::MyString( MyString& original){
	if (mPtr = new char[original.mLength + 1]){ // Request heap space
		mLength = original.mLength;	// Success
		strCpy(mPtr, original.mPtr);	// Copy actual string
	}
	else mLength = 0;	// Consistent with constructor
}

MyString::~MyString(){
	delete []mPtr;
}

int MyString::length() {return mLength;}
char MyString::getChar(int i) {
	if (i < 0 || i >= mLength)
		return '\0';
	return *(mPtr + i);
}

bool MyString::compare(MyString& other){
	if (mLength != other.mLength) return false;
	for (int i = 0; i < mLength; i++){
		if (*(mPtr+i) != *(other.mPtr+i))
			return false;	// exit as soon as a difference
	}
	return true; // everything the same if arrive here
}

void MyString::get(char* buff){strCpy(buff,mPtr);}

void MyString::setChar(int i, char c){
	if (i >= 0 && i < mLength)
		*(mPtr + i) = c;
}

void MyString::changeTo(char* newString){
	long l = strLen(newString); 
	if (l != mLength){	// if diff, re-allocate memory
		char* pTemp = new char[l+1];
		if (!pTemp) return; //punt!!
		delete [] mPtr;	// Release old storage
		mPtr = pTemp;	// hook-up to new
		mLength =l;		// Forgot to do this in the original
	}
	strCpy(mPtr, newString);	
}

// Overloaded version of changeTo to allow the string to
// be changed to equal another MyString object

void MyString::changeTo(MyString& newString){
	changeTo(newString.mPtr);  // Just convert to conventional call
}

void strCpy(char* dest, char* source){
	while(*dest++ = *source++)
		;
}

long strLen( char* pStr){
	long count = 0;
	while (*(pStr + count))
		count++;
	return count;
}/*#/H*/
