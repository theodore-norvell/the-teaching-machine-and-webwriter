//! Run.
class MyString {
public:
	MyString();
	MyString(const char* pCString);
	long getLength() const;
	int  howMany() const;         // retrieve the no. created
	char get(int i) const; 		// get i'th character
	void conCat(MyString back);	// Return back concatenated to end of string
	~MyString();
private:
	static int count;
	long length;
	char* pString;
};
/* LanguageInterface.PError: Syntax Error: Encountered ";" in file ""source.cpp"" near or on line 14, column 2.

Was expecting one of:
    "(" ...
    "::" ...
    "~" ...
    "operator" ...
    <ID> ...
    "auto" ...
    "register" ...
    "static" ...
    "extern" ...
    "mutable" ...
    "char" ...
    "wchar_t" ...
    "bool" ...
    "short" ...
    "int" ...
    "long" ...
    "signed" ...
    "unsigned" ...
    "float" ...
    "double" ...
    "void" ...
    "class" ...
    "struct" ...
    "union" ...
    "enum" ...
    "typename" ...
    "const" ...
    "volatile" ...
    "inline" ...
    "virtual" ...
    "explicit" ...
    "friend" ...
    "typedef" ...
    "*" ...
    "&" ...
	at Cpp.CPlusPlusLang.parse(CPlusPlusLang.java:187)
	at Evaluator.Evaluator.<init>(Evaluator.java:139)
	at TMBigApplet.loadString(TMBigApplet.java:170)
	at TMMainFrame.loadString(TMMainFrame.java:313)
	at TMTinyApplet.loadString(TMTinyApplet.java:37)

*/ /*#H*/
// standard string functions
long strlen(const char* pStr);
void strcpy(char* pDest, const char* pSource);


int main(){
	MyString greetings("Hello ");
	MyString all("world!");
	greetings.howMany();
	greetings.conCat(all);
	greetings.howMany();
	return 0;
}
int MyString::count = 0;

MyString::MyString(){
	count++;
	length = 0;
	pString = new char[1];
	*pString = '\0';
}

MyString::MyString(const char* pCStr){
	count++;
	length = strlen(pCStr);
	pString = new char[length+1];
	if (pString) strcpy(pString,pCStr);
}

long MyString::getLength() const{
	return length;
}

int MyString::howMany() const{
	return count;
}

char MyString::get(int i) const{
	if (i < 0 || i > length-1)
		return '\0';	// should throw an error
	return *(pString + i);
}

void MyString::conCat(MyString back){
	char* pTemp = new char[length + back.length + 1];
	if (pTemp) {
		strcpy(pTemp,pString);
		strcpy(pTemp + length, back.pString);
		delete [] pString;
		pString = pTemp;
	}  // else should throw error
}

MyString::~MyString(){
	delete [] pString;
	count--;
}

long strlen( const char* pStr){
	long len = 0;
	while (*(pStr+len))
		len++;
	return len;
}

void strcpy(char* pDest, const char* pSource){
	while (*pDest++ = *pSource++)
		;
}
/*#/H*/

