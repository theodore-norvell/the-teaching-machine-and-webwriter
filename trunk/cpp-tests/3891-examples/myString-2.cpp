//! Run.
/*#HB*/class MyString{
public:
	MyString(const char* p);	// Construct using a standard string
	void setChar(const int i, const char c); // Change char at i to c

private:
	int mLength;	// length of the string
};/*#HA*/

using namespace std;

int main(){
	MyString greetings("Hello world!");
	return 0;
}

MyString::MyString(const char* p){
}

void MyString::setChar(const int i, const char c){
}