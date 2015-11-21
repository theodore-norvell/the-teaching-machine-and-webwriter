/*#TS*/
#include <new>
using namespace std;



class string{
public:
    //typedef unsigned size_type;
	static /*size_type*/unsigned npos;
	string();
	string(char* str);
	string(char single);
	string(const string& orig);
	~string();

	/*size_type*/unsigned length() const;
	char at(/*size_type*/unsigned i) const;

	/*size_type*/unsigned find(const string& subStr, /*size_type*/unsigned start) const;
	/*size_type*/unsigned find(char* pSubStr, /*size_type*/unsigned start) const;
	/*size_type*///unsigned find(const string& subStr)const;
	/*size_type*/unsigned find(char* pSubStr)const;

	string& insert(/*size_type*/unsigned p, const string& subStr);
	string& insert(/*size_type*/unsigned p, char* pSubStr);
	string& replace(/*size_type*/unsigned start, /*size_type*/unsigned count, const string& subStr);
	string& replace(/*size_type*/unsigned start, /*size_type*/unsigned count, char* pSubStr);
	string& erase(/*size_type*/unsigned start, /*size_type*/unsigned count);
	string& assign(const string& oldString, /*size_type*/unsigned start, /*size_type*/unsigned count);
	string& assign(char* pOldString, /*size_type*/unsigned start, /*size_type*/unsigned count);
	string& append(const string& aString);
	string& append(char* pString);
	string substr(unsigned start, unsigned count) const;
	void swap(string& s);

    // The declaration of + as a friend is not needed in the TM and may be harmful.
	// friend string operator+(const string&left, const string& right);
	// The = operator should be a member, but is not owing to a TM bug
	// string& operator=(const string& right);
	// The = operator should be a member, but is not owing to a TM bug
	// string& operator+=(const string& right);

private:
    // Invariant: If mLength == 0 then mpString == 0 
    //            Else mpString points to an array of length mLength+1
    //                 and mpString[mLength]=='\0'
	char* mpString;
	/*size_type*/unsigned mLength;
	
};

// = should be a member function.
string& operator=(string &left, string& right);

// This version of = should not be needed?
string& operator=(string &left, char *right);

// += should be a member function.
string& operator+=(string &left, const string& right);

// This version of = should not be needed?
string& operator+=(string &left, char *right);


bool operator==(const string& left, const string& right);
bool operator!= (const string& left, const string& right);
bool operator<(const string& left, const string& right);
bool operator <=(const string& left, const string& right);
bool operator>(const string& left, const string& right);
bool operator >=(const string& left, const string& right);


ostream& operator<<(ostream& s, const string& right);

void getline(istream& is, string& aString, char term);
void getline(istream& is, string& aString);

istream& operator>>(istream& is, string& aString);

/*********************** Implementation begins *************************/

#include <cstring>

/** Some implementation notes:
        * This implementation should obviously be in a file separate from
          the include file.  Have to figure out some automatic import mechanism
          similar to Java's.
        * MPBL's implementation threw exceptions.  Changed (TSN) to
          write out error message.
*/

/*size_type*/unsigned string::npos = -1;   // cheap C programmers trick to generate largest unsigned int

string::string(char* str){
	mLength = strlen(str);
	if (mpString = new(nothrow) char [mLength + 1] )
		strcpy(mpString, str);
	else {
	    mLength = 0;
	    cout << "Out of space" << endl ; }
}

string::string(char single){
	mLength = 1;
	if (mpString = new(nothrow) char[2] ) {
		mpString[0] = single;
		mpString[1] = '\0';
	} else {
	    mLength = 0;
	    cout << "Out of space" << endl ;
	}
}

string::string(){
	mLength = 0;
	mpString = new(nothrow) char[1];
	if (mpString != 0) *mpString = '\0';
	else {
	    mLength = 0;
	    cout << "Out of space" << endl ;
	}
}

string::string(const string& orig){
	if (orig.mpString > 0) {
	    if (mpString = new(nothrow) char [orig.mLength + 1]) {
		    strcpy(mpString, orig.mpString);
		    mLength = orig.mLength; }
		else {
	        mLength = 0;
	        cout << "Out of space" << endl ;
	    }
	} else {
		mLength = 0;
		mpString = 0;
	}
}

string::~string(){
	delete [] mpString;
}

/*size_type*/unsigned string::length() const{return mLength;}

char string::at(/*size_type*/unsigned p) const{
	if (p >= mLength) {
	    cout << "string position greater than string length" << endl; 
	    return 0 ;
	}
	return mpString[p];
}

/*size_type*/unsigned string::find(const string& substr, /*size_type*/unsigned start) const{
	if (start >= mLength) { 
	    cout << "starting find past end of string" << endl;
	    return 0 ;
	}
	char* pStart = strstr(mpString + start, substr.mpString);
	if (pStart == 0) return npos;
	return (pStart - mpString);
}

/*size_type*/unsigned string::find(char* pSubstr, /*size_type*/unsigned start) const{
	string substr(pSubstr);
	return find(substr, start);
}

///*size_type*/unsigned string::find(const string& substr) const {
//	return find(substr, 0);
//}

/*size_type*/unsigned string::find(char* pSubstr) const {
	string substr(pSubstr);
	return find(substr, 0);
}

string& string::insert(/*size_type*/unsigned p, const string& subStr){
	if (p >= mLength) {
	    cout << "Trying to insert beyond end of string" << endl ;
	    return *this ;
	}
	char* pTemp;
	if (pTemp = new(nothrow) char[mLength + subStr.mLength + 1]) {
		/*size_type*/unsigned i=0,j=0;
		while( i < p ) {
			pTemp[i] = mpString[j];
			++i ; ++j ;
	    }
		for (/*size_type*/unsigned k = 0; k < subStr.mLength; k++)
			pTemp[i++] = subStr.mpString[k];
		while( j < mLength )
			pTemp[i++] = mpString[j++];
		pTemp[i] = '\0' ;
		delete []mpString;
		mpString = pTemp;
		mLength = mLength + subStr.mLength;
	} else {
	    cout << "Out of space" << endl ;
	}
	return *this;
}

string& string::insert(/*size_type*/unsigned p, char* pSubStr){
	string temp(pSubStr);
	return insert(p,temp);
}


string& string::replace(/*size_type*/unsigned start, /*size_type*/unsigned count, const string& subStr){
	this->erase(start,count);
	this->insert(start, subStr);
	return *this;
}

string& string::replace(/*size_type*/unsigned start, /*size_type*/unsigned count, char* pSubstr){
	string temp(pSubstr);
	return replace(start, count,temp);
}


string& string::erase(/*size_type*/unsigned start, /*size_type*/unsigned count){
	if (start > mLength) { // TBD. Actually start==mLength is legit.
	    cout << "Trying to start erase beyond end of string" << endl ;
	} else {
    	if (start + count > mLength) mpString[start] = '\0';
    	else strcpy(mpString+start, mpString + start + count);
    	mLength = strlen(mpString);  // new length
    	char* pTemp;
    	if (pTemp = new(nothrow) char[mLength + 1]) {
    		strcpy(pTemp, mpString);
    		delete [] mpString;
    		mpString = pTemp;
    	} else {
    	    cout << "Out of space" << endl ;
    	}
    }
    return *this;
}

static void hackassign( string& lhs, const string &rhs ) {
    lhs = rhs ;
}

static void hackplusassign( string& lhs, const string &rhs ) {
    lhs += rhs ;
}

string& string::assign(const string& oldString, /*size_type*/unsigned start, /*size_type*/unsigned count){
	if (start >= oldString.mLength) {// TBD. Actually start==mLength is legit.
	    cout << "position beyond end of subString" << endl;
	} else {
	    string temp(oldString);
	    if (start + count < temp.mLength) temp.erase(start + count, temp.mLength - (start + count));
	    temp.erase(0, start) ;
	    hackassign( *this,  temp ) ;
	}
	return *this;
}

string& string::assign(char* pOldString, /*size_type*/unsigned start, /*size_type*/unsigned count){
	string temp(pOldString);
	return assign(temp, start, count);
}


string& string::append(const string& aString){
	char* pTemp = new(nothrow) char[mLength + aString.mLength + 1];
	if (pTemp) {
		strcpy(pTemp, mpString);
		strcat(pTemp,aString.mpString);
		delete[] mpString;
		mpString = pTemp;
		mLength = mLength + aString.mLength;
	} else {
        cout << "Out of space" << endl ;
    }
	return *this;
}


string& string::append(char* pString){
	string temp(pString);
	return append(temp);
}

string string::substr(unsigned start, unsigned count) const {
	string* pSubstr = this;
	if (start <= mLength) {
		 pSubstr = new(nothrow) string(mpString+start);
		if (pSubstr) {
			if (count < pSubstr->mLength)
				pSubstr->erase(count,pSubstr->mLength-count);
		} else {
			cout << "Out of space" << endl ;
			pSubstr = this;
		}
	}
	else
		cout << "Trying to start substr beyond end of string" << endl ;
	return *pSubstr;
}




void string::swap(string& s){
	/*size_type*/unsigned tempL = mLength;
	mLength = s.mLength;
	s.mLength = tempL;
	char* pTemp = mpString;
	mpString = s.mpString;
	s.mpString = pTemp;
}


string& operator=(string &left, char *p ) {
    string temp( p ) ;
    hackassign( left, temp ) ;
    return left ;
}

string& operator=(string &left, const string& right) {
	if (left.mLength != right.mLength) {
		delete [] left.mpString;
		if (left.mpString = new(nothrow) char[right.mLength + 1]) {
			left.mLength = right.mLength;
		} else {
		    cout << "Out of space" << endl ;
		    left.mLength = 0;
		}
	}
	if (left.mLength > 0) strcpy(left.mpString, right.mpString);
	return left;
}


string& operator+=(string &left, char* p){
	string temp(p);
	hackplusassign(left, temp);
	return left;
}


string& operator+=(string &left, const string& right){
	if (right.mLength > 0 ) {
		char* pTemp;
		if (pTemp = new(nothrow) char[left.mLength + right.mLength + 1]) {
			strcpy(pTemp, left.mpString);
			delete [] left.mpString;
			left.mpString = pTemp;
			strcat(left.mpString, right.mpString);
			left.mLength = left.mLength + right.mLength;
		} else {
            cout << "Out of space" << endl ;
        }
	}
	return left;
}

static int compare(const string& left, const string& right ) {
    /*size_type*/unsigned minSize ;
    if( left.length() < right.length() ) minSize = left.length() ;
    else minSize = right.length() ;
    for (/*size_type*/unsigned i=0 ; i<minSize ; ++i ) {
        if( left.at(i) < right.at(i) ) return -1 ;
        else if( left.at(i) > right.at(i) ) return +1 ; }
    if( left.length() < right.length() ) return -1 ;
    else if( left.length() > right.length() ) return +1 ;
    return 0 ;
}

bool operator==(const string& left, char* p){
	string temp(p);
	return compare(left,temp) == 0;
}

bool operator==(char* p, const string& right){
	string temp(p);
	return compare(temp, right) == 0;
}


bool operator==(const string& left, const string& right){
	return compare( left, right ) == 0 ;
}


bool operator!=(const string& left, char* p){
	return !(left == p);
}

bool operator!=(char* p, const string& right){
	return !(p == right);
}

bool operator!= (const string& left, const string& right){
	return !(left == right);
}

bool operator<(const string& left, const string& right){
	return compare( left, right ) < 0 ;
}

bool operator <=(const string& left, const string& right){
	return compare( left, right ) <= 0 ;
}

bool operator>(const string& left, const string& right){
	return compare( left, right ) > 0 ;
}

bool operator >=(const string& left, const string& right){
	return compare( left, right ) >= 0 ;
}

string operator+(const string& left, const string& right){
	string sum(left);
	sum.append(right);
	return sum;
}

static void hackoutput( ostream& s, char ch ) {
    s << ch ; 
}

ostream& operator<<(ostream& s, const string& right){
	for (/*size_type*/unsigned i = 0; i < right.length(); i++) {
 	    char ch = right.at(i);
 		hackoutput( s, ch ) ; }
 	return s;
}

/**************** cin>> support ***************/

class Terminator{
public:
	Terminator();
	Terminator(char t) ;
	bool isTerm(char c);
private:
	bool whitespace;
	char term;
};

Terminator::Terminator(){whitespace = true;}

Terminator::Terminator(char t) {whitespace = false; term = t;}

bool Terminator::isTerm(char c) {
	if(whitespace)
		return (c == ' ' || c == '\t' || c == '\n');
	return (c == term);
}


void getLine(istream& is, string& aString, Terminator& t);
void workaround(istream& is, string& aString, char term);


void getline(istream& is, string& aString, char term){
	Terminator t(term);
	getLine(is, aString, t);
}

void getline(istream& is, string& aString){
	workaround(is, aString, '\n');
}

void workaround(istream& is, string& aString, char term){
	getline(is, aString, term);
}


void getLine(istream& is, string& aString, Terminator& t){
	const int BUFFSIZE = 80;
	char* pTemp = new char [BUFFSIZE+ 1];
	string temp;
	int i = 0;
	do {
		cin.get( pTemp[i++] );
		if ((i == BUFFSIZE) && (!t.isTerm(pTemp[i-1]))) {
			pTemp[i] = '\0';
			string buffer( pTemp ) ; // Needed because char* to string conversion was not automatic
			temp.append(buffer); 
			i = 0;
		}
	} while (!t.isTerm(pTemp[i-1]));
	pTemp[i-1] = '\0';
	string buffer(pTemp) ;
	temp.append(buffer);
	aString = temp;
}

istream& operator>>(istream& is, string& aString){
	Terminator t;
	getLine(is, aString, t);
	return is;
}