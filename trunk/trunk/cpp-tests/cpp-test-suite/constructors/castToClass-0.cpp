//! Run.  Expect output equals "a\n".

// Construct a String.

#include <iostream>
using namespace std ;

class String {
    public: char x ;
    public: String( char * ) ;
} ;

String::String(char *str ) {
    x = *str ;
}

int main() {
    String a("a") ;
    cout << a.x << endl ;
	return 0;
}
