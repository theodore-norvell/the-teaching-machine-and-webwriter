//! Run.
/*#HA*/ /*#HB*/ /*#HC*/ /*#HD*/ /*#HE*/ // There's an error in this file. Can you use the TM to spot it
// and figure out the fix?
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
/*#DA*/ // A class of users for network management
class User{
public:
	User(char* n, char* u, char* p);

// Accessors
	MyString& name();	// return reference
	int allocation();	// Disk space allowed
	MyString password();	// return value
	MyString uname() ;
	bool confirm(const MyString& uName, const MyString& pass);
// Mutators
	bool setPassword(const MyString& p1, const MyString& p2);
	void setAllocation(int a);
private:
	MyString mName;		// Notice attributes that are OBJECTS!
	MyString mUname;
	MyString mPassword;
	int mAllocation;	// Memory allocation, in MBytes
};/*#HA*/

#include <iostream>
using namespace std;

int main(){
/*#DC*/	User mpbl("Michael Bruce-Lockhart", "mpbl", "bftplx");
	User theo("Theo Norvell", "theo", "charl1eB");/*#HC*/
/*#DD*/	User mpblClone(mpbl);/*#HD*/
	mpbl.setAllocation(100);
	MyString pw1("unlucky") ;
	MyString pw2("unlucky") ;
/*#DE*/	mpblClone.setPassword( pw1, pw2 );/*#HE*/
	
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

/*#DB*/ User::User(char* n, char* u, char* p) : mName(n),
	mUname(u),mPassword(p){
	
}/*#HB*/

// Accessors
MyString& User::name() {return mName;}	// Note returning reference
MyString User::uname() {return mUname;} // returning value
MyString User::password(){return mPassword;}
int User::allocation() {return mAllocation;}	// Disk space allowed

// A little more complex accessor, used to confirm username, password pairs
bool User::confirm(const MyString& uName, const MyString& pass) {
	return(mUname.compare(uName) && mPassword.compare(pass));
}

// Mutators
// Set password assumes two identical password strings
bool User::setPassword(const MyString& new1, const MyString& new2){
	if (new1.length()<6 || !new1.compare(new2)) return false;
	mPassword.changeTo(new1);	// change password
	return true;
}

void User::setAllocation(int a){
	mAllocation = a;
}

/***************************************************************
			Implementation of MyString 
			(normally in a separate file)
******************************************************************/

// These would normally be declared by including <cstring>
long strLen(const char* pStr);
void strCpy(char* dest,  const char* source);

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
char MyString::getChar(int i) const{
	if (i < 0 || i >= mLength)
		return '\0';
	return *(mPtr + i);
}

bool MyString::compare(const MyString& other) const{
	if (mLength != other.mLength) return false;
	for (int i = 0; i < mLength; i++){
		if (*(mPtr+i) != *(other.mPtr+i))
			return false;	// exit as soon as a difference
	}
	return true; // everything the same if arrive here
}

void MyString::get(char* buff)const {strCpy(buff,mPtr);}

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
	}
	strCpy(mPtr, newString);	
}

// Overloaded version of changeTo to allow the string to
// be changed to equal another MyString object

void MyString::changeTo(const MyString& newString){
	changeTo(newString.mPtr);  // Just convert to conventional call
}

void strCpy(char* dest, const char* source){
	while(*dest++ = *source++)
		;
}

long strLen(const char* pStr){
	long count = 0;
	while (*(pStr + count))
		count++;
	return count;
}/*#/H*/
 