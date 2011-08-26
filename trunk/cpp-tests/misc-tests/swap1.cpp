#include <iostream>
using namespace std ;

void swap( int blah, int blahblah );

void main(){

	int a = 13 ;
	int b = 42 ;

	swap( a, b ) ;
	cout << "a is " << a << "\n" ;
	cout << "b is " << b << "\n" ;
	return ;

}


void swap( int x, int y ) {
	int temp ;

	temp = x ;
	x = y ;
	y = temp ;


	// Note that x and y have been swapped,
	// but not a and b.
	return ;

}

