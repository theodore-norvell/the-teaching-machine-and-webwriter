//! Run.
/*#H*/ #include <iostream>
using namespace std;

class Complex {
public:
	double real() const;
	double imag() const;
	// These are an alternative to the more compact 
	// Complex(double r=0.,double i=0.);
	Complex(double r,double i); // 2 argument constructor
	Complex(double r);		// explicit 1 arg constructor
	Complex();			// explicit no arg (default) constructor

private:
	double re,im;
};/*#/H*/
int main(){
	Complex x;				// Complex zero
	Complex y(3.4);			// Complex real
	Complex z(1.0,-1.0);	// Full Complex

	cout << "The real part of z is ";
	cout << z.real();
	return 0;
}
/*#H*/
Complex::Complex(double r, double i){
	re = r;
	im = i;
}

// Each explicit constructor must be implemented separately
Complex::Complex(double r){
	re = r;
	im = 0.;
}

Complex::Complex(){
	re = im = 0.;
}

double Complex::real() const{return re;}
double Complex::imag() const{return im;}/*#/H*/

