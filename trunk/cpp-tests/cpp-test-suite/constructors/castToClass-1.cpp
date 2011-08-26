//! Run.  Expect output equals "a\n".

// Construct a temp String in the middle of an expression
// and pass it by reference to a parameter.

#include <iostream>
using namespace std ;

class String {
    public: char x ;
    public: String( char * ) ;
} ;

String::String(char *str ) {
    x = *str ;
}

void bebop( const String &a ) {
    cout << a.x << endl ;
}

int main() {
    bebop( String("a") ) ;
	return 0;
}
