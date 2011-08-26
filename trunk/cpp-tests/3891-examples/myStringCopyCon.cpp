//! Run.
/*#HB*/ /*#HC*/ /*#HD*/ /*#HE*/ /*#HF*/ /*#HG*/class MyString{
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
	void changeTo(MyString newString);	// Function overload

private:
	char* mPtr;		// pointer into the heap where the actual string will be
	int mLength;	// length of the string
};/*#HA*/

#include <iostream>
using namespace std;

int main(){
	MyString unlucky("unlucky");	
	MyString empty;				// empty strings are valid
	MyString echo(unlucky);		// Echo is a copy
	if (echo.compare(unlucky))
		cout << "They're the same" << endl;
/*#DG*/    MyString password("bfltpx");	// Hah! Get that one.
	password.changeTo("btfplk");	//Oops! I spelled it wrong
	password.changeTo(unlucky);		// Heck with it, I'll never remember it!
/*#HG*/	return 0;
}


/***************************************************************
			Implementation of MyString 
			(normally in a separate file)
******************************************************************/

// These would normally be declared by including <cstring>
long strLen( char* pStr);
void strCpy(char* dest,  char* source);

/*#DB*/MyString::MyString(){
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
}/*#HB*/

/*#DC*/MyString::~MyString(){
	delete []mPtr;
}/*#HC*/

int MyString::length() {return mLength;}
char MyString::getChar(int i) {
	if (i < 0 || i >= mLength)
		return '\0';
	return *(mPtr + i);
}

/*#DD*/bool MyString::compare(MyString& other){
	if (mLength != other.mLength) return false;
	for (int i = 0; i < mLength; i++){
		if (*(mPtr+i) != *(other.mPtr+i))
			return false;	// exit as soon as a difference
	}
	return true; // everything the same if arrive here
}/*#HD*/

void MyString::get(char* buff){strCpy(buff,mPtr);}

void MyString::setChar(int i, char c){
	if (i >= 0 && i < mLength)
		*(mPtr + i) = c;
}
/*#DE*/void MyString::changeTo(char* newString){
	long l = strLen(newString); 
	if (l != mLength){	// if diff, re-allocate memory
		char* pTemp = new char[l+1];
		if (!pTemp) return; //punt!!
		delete [] mPtr;	// Release old storage
		mPtr = pTemp;	// hook-up to new
	}
	strCpy(mPtr, newString);	
}/*#HE*/

/*#DF*/ // Overloaded version of changeTo to allow the string to
// be changed to equal another MyString object

void MyString::changeTo(MyString newString){
	changeTo(newString.mPtr);  // Just convert to conventional call
}/*#HF*/

void strCpy(char* dest, char* source){
	while(*dest++ = *source++)
		;
}

long strLen( char* pStr){
	long count = 0;
	while (*(pStr + count))
		count++;
	return count;
}
