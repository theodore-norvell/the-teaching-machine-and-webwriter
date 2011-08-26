//! Run.
/*#HB*/class MyString{
public:
	MyString(const char* p);	// Construct using a standard string
	MyString();			// "default" (no arguement) constructor
	~MyString();		// standard destructor to deal with heap

// Accessor functions - used to read object data without changing it
	int length() const;
	char getChar(int i) const;	// get char at location i
	void get(char* buff) const;	// Get the string & put it into user buff
	
// Mutator functions - used to change string objects
	void setChar(const int i, const char c); // Change char at i to c

private:
	char* mPtr;		// pointer into the heap where the actual string will be
	int mLength;	// length of the string
};/*#HA*/

#include <iostream>
using namespace std;

void hufflepuff();

int main(){
	MyString greetings("Hello world!");
	MyString empty;
	hufflepuff();
	return 0;
}

void hufflepuff(){
	MyString yeah("Hurray for HufflePuff!"); // Create string
	char buffer[25];	// Create a buffer to put c-style string
	yeah.get(buffer);	// Load the string into the buffer
	cout << buffer << endl;	// and output it
}


// These would normally be declared by including <cstring>
long xxyyzz(const char* pStr);
void strCpy(char* dest, const char* source);
/*#DB*/
MyString::MyString(const char* p){
	mLength = xxyyzz(p);
	if (mPtr = new char[mLength+1])
	strCpy(mPtr,p);
	else
		mLength = 0;
}

MyString::MyString(){
	mLength = 0;
	if(mPtr = new char[1])
		*mPtr = '\0';
}

MyString::~MyString(){
	delete []mPtr;
}/*#HB*/

int MyString::length() const{return mLength;}
char MyString::getChar(int i) const{
	if (i < 0 || i >= mLength)
		return '\0';
	return *(mPtr + i);
}

void MyString::get(char* buff)const{strCpy(buff,mPtr);}

void MyString::setChar(const int i, const char c){
	if (i >= 0 && i < mLength)
		*(mPtr + i) = c;
}

void strCpy(char* dest,const char* source){
	while(*dest++ = *source++)
		;
}

long xxyyzz(const char* pStr){
	long count = 0;
	while (*(pStr + count))
		count++;
	return count;
}
