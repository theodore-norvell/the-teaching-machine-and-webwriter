//! Run. Expect output = "13\n".

#include <iostream> 
using namespace std ;

// Really we need stdargs to properly test this.
void func( int i, ... ) {
	cout << i << endl ;
}

int main(){
    int a = 13 ;
	func( a, a, &a ) ;
}