//! Run. Expect output = "13\n".

void func( int & r, int & s ) {
	r = s+1 ;
}

#include <iostream> 
using namespace std ;

int main(){
	int a = 42;
	int b = 12;
	func( a, b ) ;
	cout << a << endl ;
}