//! Run. Expect output "1" endl "2" endl "3" endl "3" endl.
#include <iostream>
using namespace std;

/*#DA*/ class X {
public:
	X(int);	// Two argument constructor
	X(X&);
	~X();		// destructor needed to correct count
	int howMany() const;
	X& operator=(const X&);
private:
	int f;
	static int count;	//Keep track of how many objects there are
};/*#HA*/

// A silly function which simply returns its own arguement
X foo(X anX){
	cout << anX.howMany() << endl ;
	return anX;
}

int main(){
	X one( 10 ) ;
	cout << one.howMany() << endl ;
	X two( 20 ) ;
	cout << two.howMany() << endl ;
	foo( two ) ;
	cout << two.howMany() << endl ;
	return 0;
}/*#HC*/


int X::count = 0;
						
X::X(int a1){
	f = a1;
	count++;
}

X::X( X& other ) {
	f = other.f+1 ;
	count++ ;
}

X& X::operator=(const X& other){
	f = other.f+1 ;
	return *this ;
}

X::~X(){			// destructor
	count--;
}


int X::howMany() const {
	return count;
}
