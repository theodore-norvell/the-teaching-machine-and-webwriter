//! Run.
/*#HA*/ /*#HB*/#include <iostream>
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
	Complex operator*(const Complex& rhs);
	Complex operator/(const Complex& rhs);
	Complex& operator=(const Complex& rhs);
	bool operator==(const Complex& rhs);
	bool operator!=(const Complex& rhs);

	friend ostream& operator<<(ostream& s,const Complex& c);

private:
	double re,im;
};/*#HA*/

int main(){
	Complex x(-1., -1.);
	Complex y(2.2,3.14159);
	Complex z;
	
	z = x + y;
	cout << x << " + " << y << " = " << z << endl;

	z = x - y;
	cout << x << " - " << y << " = " << z << endl;

	z = x * y;
	cout << x << " * " << y << " = " << z << endl;

	z = x / y;
	cout << x << " / " << y << " = " << z << endl;

	return 0;
}

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

Complex Complex::operator*(const Complex& rhs){
	return Complex(re*rhs.re-im*rhs.im,re*rhs.im+im*rhs.re);
}

Complex Complex::operator/(const Complex& rhs){
	double denom = rhs.re*rhs.re + rhs.im*rhs.im;
	return Complex((re*rhs.re+im*rhs.im)/denom,(-re*rhs.im+im*rhs.re)/denom);
}

Complex& Complex::operator=(const Complex& rhs){
	re = rhs.re;
	im = rhs.im;
	return *this;
}


bool Complex::operator==(const Complex& rhs){
	return (re==rhs.re && im==im*rhs.im);
}

bool Complex::operator!=(const Complex& rhs){
	return !(*this==rhs);
}

/*#DB*/ /* Overloading the insertion operator for class complex */
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
}/*#HB*/
