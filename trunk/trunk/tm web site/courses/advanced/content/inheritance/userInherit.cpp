class MyString{
public:
	MyString(const char* p);	// Construct using a standard string
	MyString();			// "default" (no arguement) constructor
	MyString(const MyString& orig);		// Copy constructor
	~MyString();		// standard destructor to deal with heap

// Accessor functions - used to read object data without changing it
	int length() const;
	char getChar(int i) const;	// get char at location i
	void get(char* buff) const;	// Get the string & put it into user buff
	bool compare(const MyString& other) const;	// true if equal
	
// Mutator functions - used to change string objects
	void setChar(int i, char c); // Change char at i to c
	void changeTo(const char* newString);	// Change the whole string
	
	/* Notice the change to pass by ref to improve efficiency */
	void changeTo(const MyString& newString);	// Function overload

private:
	char* mPtr;		// pointer into the heap where the actual string will be
	int mLength;	// length of the string
};
/*#TA*/ // A class of users for network management
class User{
public:
	User(char* n, char* u, char* p);

// Accessors
	MyString name() const;	
	int allocation() const;	// Disk space allowed
	MyString password() const;
	MyString uname() const;
	bool confirm(const MyString& uName, const MyString& pass) const;
// Mutators
	bool setPassword(const MyString& p1,const MyString& p2);
	void setAllocation(int a);

protected:
	MyString mName;		// Notice attributes that are OBJECTS!
	MyString mUname;
	MyString mPassword;
	int mAllocation;	// Memory allocation, in MBytes
};/*#/TA*/

/*#TB*/class Student: public User{
public:
	Student(char* n, char* id, char* u, char* p);
// Accessors
	MyString id() const;

// Mutators
	void buy(long amount);
	bool approvePrint(int pages);

protected:
	MyString mId;
	long mPrintPennies;
};/*#/TB*/

/*#TC*/class Faculty: public User{
public:
	Faculty(char* n, char* u, char* p, char* today);

// accessor functions
	long pages() const;
	MyString lastCleared() const;

// mutator functions
	bool approvePrint(int pages);	// increases page count
	void clear(char* d);		// clears count & notes date
protected:
	long mPages;		// Pages used since
	MyString mCleared;		// last date cleared
};/*#/TC*/



#include <iostream>
using namespace std;

/*#TD*/int main(){
	Faculty mpbl("Michael Bruce-Lockhart", "mpbl", "bftplx", "11/11/2002");
	Student cullam("Cullam Bruce-Lockhart", "2001001762", "cullam", "oBiewan");
//	Faculty mpblClone(mpbl);
	mpbl.setAllocation(100);
	cullam.setAllocation(10);
	cullam.buy(1000);	// $10.00 printing credit

	return 0;
}
/*#/TD*/
/***************************************************************
			Implementation of User 
			(normally in a separate file)
******************************************************************/

// Constructors in the form
//	 <Name>(<argument list>) : <initialization list> {
//		special code
//	}

/*#TE*/ User::User(char* n, char* u, char* p) : mName(n),
	mUname(u),mPassword(p),mAllocation(0){
}
/*#/TE*/
// Accessors
MyString User::name() const {return mName;}
MyString User::uname() const {return mUname;}
MyString User::password() const {return mPassword;}
int User::allocation() const {return mAllocation;}	// Disk space allowed

// A little more complex accessor, used to confirm username, password pairs
bool User::confirm(const MyString& uName,const MyString& pass) const {
	return(mUname.compare(uName) && mPassword.compare(pass));
}

// Mutators
// Set password assumes two identical password strings
bool User::setPassword(const MyString& new1,const MyString& new2){
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
MyString Student::id() const {return mId;}

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
/*#TE*/ Student::Student(char* n, char* id, char* u, char* p) :
	User(n,u,p),mId(id){	// Initialization - data to User constructor
	mAllocation = 5;
	mPrintPennies = 0;
}/*#/TE*/		

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

// Accessors
long Faculty::pages() const {return mPages;}

MyString Faculty::lastCleared() const {return mCleared;}
		

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
long strLen(const char* pStr);
void strCpy(char* dest, const char* source);

MyString::MyString(){
	mLength = 0;
	if(mPtr = new char[1])
		*mPtr = '\0';
}

MyString::MyString(const char* p){
	mLength = strLen(p);
	if (mPtr = new char[mLength+1])
	strCpy(mPtr,p);
	else
		mLength = 0;
}

MyString::MyString(const MyString& original){
	if (mPtr = new char[original.mLength + 1]){ // Request heap space
		mLength = original.mLength;	// Success
		strCpy(mPtr, original.mPtr);	// Copy actual string
	}
	else mLength = 0;	// Consistent with constructor
}

MyString::~MyString(){
	delete []mPtr;
}

int MyString::length() const {return mLength;}

char MyString::getChar(int i) const {
	if (i < 0 || i >= mLength)
		return '\0';
	return *(mPtr + i);
}

bool MyString::compare(const MyString& other) const {
	if (mLength != other.mLength) return false;
	for (int i = 0; i < mLength; i++){
		if (*(mPtr+i) != *(other.mPtr+i))
			return false;	// exit as soon as a difference
	}
	return true; // everything the same if arrive here
}

void MyString::get(char* buff) const {strCpy(buff,mPtr);}

void MyString::setChar(int i, char c){
	if (i >= 0 && i < mLength)
		*(mPtr + i) = c;
}

void MyString::changeTo(const char* newString){
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

void MyString::changeTo(const MyString& newString){
	changeTo(newString.mPtr);  // Just convert to conventional call
}

void strCpy(char* dest,const char* source){
	while(*dest++ = *source++)
		;
}

long strLen(const char* pStr){
	long count = 0;
	while (*(pStr + count))
		count++;
	return count;
}
