//! Run.
/*#HA*/ /*#HB*/ /*#HC*/#include <iostream>
#include <math.h>
using namespace std;

/*#DA*/class Complex {
public:
// These three should be replaced by
//	Complex(double r = 0., double i = 0.); TM unsupported
	Complex(double r, double i);
	Complex(double r);
	Complex();
	double real() const;
	double imag() const;

	Complex operator+(const Complex& rhs);
	Complex operator-(const Complex& rhs);
	operator double() const;
	friend ostream& operator<<(ostream& s,const Complex& c);
private:
	double re,im;
};/*#HA*/

/*#DB*/int max(int a, int b) { return (a>b?a:b);}
double max(double a, double b) { return (a>b?a:b);}
Complex max(const Complex& a, const Complex& b) {return (a>b?a:b);}/*#HB*/

int main(){
	Complex x(-1., -1.);
	Complex y(2.2);
	double a = 4.1, b = 7.2;
	int i = 3, j = 5;
	cout << "max of" << i << " & " << j << " is " << max(i,j) << endl;
	cout << "max of" << a << " & " << b << " is " << max(a,b) << endl;
	cout << "max of" << x << " & " << y << " is " << max(x,y) << endl;

	
	return 0;
}


/**********************************************************************
		Implementation of Class Complex
***********************************************************************/
Complex::Complex(double r, double i){re=r; im=i;}
Complex::Complex(double r){re=r; im=0.;}
Complex::Complex(){re=0.; im=0.;}

double Complex::real() const {return re;}
double Complex::imag() const {return im;}

Complex Complex::operator+(const Complex& rhs){
	return Complex(re+rhs.re,im+rhs.im);
}
Complex Complex::operator-(const Complex& rhs){
	return Complex(re-rhs.re,im-rhs.im);
}

Complex::operator double() const {return sqrt(re*re + im * im);}

/* Overloading the insertion operator for class complex */
ostream& operator<<(ostream& s,const Complex& c) {
	s <<'(';
	if (c.real()) s << c.real();
		if (c.imag())
			if (c.imag() > 0) 
				s << " + " << c.imag() << "j";
			else s << " - " << -c.imag() << "j";
	if ( !(c.imag()) && !(c.real()) )  s << "0";
	s << ')';
	return s;	// Allows concatenation of stream op
}

