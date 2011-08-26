/*#TA*/ class X {
public:
	X();		// Null constructor
	X(int,int);	// Two argument constructor
	X(X&);		// Copy constructor
private:
	int f;
	int g;
};/*#/TA*/

/*#TC*/ int main(){
	X one;				// one.f = one.g = 0
	X two(1,2);		// two.f = 1, two.g = 2
	//X three = {3,4};		// three.f = 3, three.g = 4
	X four(two);		// four.f = 1, four.g = 2
//	X five = three;		// five.f = 3, five.g = 4
	return 0;
}/*#/TC*/


/*#TB*/ // constructor implementations
X::X(){			// default
	f = g = 0;
}
						
X::X(int a1, int a2){	// 2 arg
	f = a1;
	g = a2;
}

X::X(X& orig){			// copy constructor
	f = orig.f;
	g = orig.g;
}/*#/TB*/
