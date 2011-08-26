//! Run. Expect output = "13\n".
#include <iostream>

int p[10][10] ;

int g( int&i ) {
	i = 13 ;
}

int f( int * a ) {
	g(*a) ;
}

void main() {
	f( & p[0][0] ) ;
	cout << p[0][0] << endl ;
}


