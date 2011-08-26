#include <iostream>
#include <math.h>
using namespace std;

/*#TA*/class Complex {
public:
	double getReal();
	double getImag();
	void set(double r, double i);
private:
	double re,im;
};/*#/TA*/

/*#TC*/int main() {
	Complex x,y;	// Declares objects of class Complex
	x.set(1.,1.);
	y.set(-1.5,3.);
	double z = sqrt( x.getReal()*x.getReal() +
					 x.getImag()*x.getImag() );

/* use real member function attached to objects
   of class Complex */
	cout << x.getReal() << " + j" << x.getImag();
	return 0;
} /*#/TC*/

/*#TB*/double Complex::getReal(){
	return re;
}

double Complex::getImag(){
	return im;
}

void Complex::set(double r, double i){
	re = r;  im = i;
}/*#/TB*/
