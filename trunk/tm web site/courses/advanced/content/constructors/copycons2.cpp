#include <iostream>
using namespace std;

/*#TA*/ class X {
public:
	X();		// Null constructor
	X(int,int);	// Two argument constructor
	~X();		// destructor needed to correct count
	int howMany() const;
private:
	int f;
	int g;
	static int count;	//Keep track of how many objects there are
};/*#/TA*/

/*#TC*/ X foo(X anX);

int main(){
	X one;				// one.f = one.g = 0
	cout << one.howMany() << endl;
	X two(1,2);		// two.f = 1, two.g = 2
	cout << two.howMany() << endl;
	foo(two);		// just returns two
	cout << two.howMany() << endl;
	return 0;
}/*#/TC*/

// A silly function which simply returns its own arguement
X foo(X anX){
	return anX;
}


/*#TB*/ // constructor implementations
int X::count = 0;	//defines the class integer count

X::X(){			// default
	f = g = 0;
	count ++;
}
						
X::X(int a1, int a2){	// 2 arg
	f = a1;
	g = a2;
	count++;
}

X::~X(){			// destructor
	count--;
}/*#/TB*/

int X::howMany(){
	return count;
}
