//! Run.
/*#HB*/class MyString{
public:
	MyString(const char* p);	// Construct using a standard string
	MyString();			// "default" (no arguement) constructor
	~MyString();		// standard destructor to deal with heap
	void setChar(const int i, const char c); // Change char at i to c

private:
	char* mPtr;		// pointer into the heap where the actual string will be
	int mLength;	// length of the string
};/*#HA*/

using namespace std;

int main(){
	MyString greetings("Hello world!");
	MyString empty;
	return 0;
}

// These would normally be declared by including <cstring>
long strLen(const char* pStr);
void strCpy(char* dest, const char* source);
/*#DB*/
MyString::MyString(const char* p){
	mLength = strLen(p);
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


void MyString::setChar(const int i, const char c){
	if (i >= 0 && i < mLength)
		*(mPtr + i) = c;
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
