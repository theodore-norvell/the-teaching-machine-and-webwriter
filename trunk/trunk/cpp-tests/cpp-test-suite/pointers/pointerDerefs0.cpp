//! Run. Expect output = "13\n".
#include <iostream>

int p[10][10] ;


void main() {
	int *a = &p[0][0] ;
	*a = 13 ;
	cout << p[0][0] << endl ;
}


