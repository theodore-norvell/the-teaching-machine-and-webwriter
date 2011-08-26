//! Run.
/*#HB*/ /*#HC*/ class Complex {
public:
// These three should be replaced by
//	Complex(double r = 0., double i = 0.); TM unsupported
	Complex(double r, double i);
	Complex(double r);
	Complex();
	Complex operator+(const Complex& rhs);
	Complex operator-(const Complex& rhs);
private:
	double re,im;
};/*#HA*/

/*#DC*/int main(){
	Complex x(-1., -1.);
	Complex y(2.2);
	Complex z;
	
	z = x + y;
	z = x - y;
	return 0;
}/*#HC*/

Complex::Complex(double r, double i){re=r; im=i;}
Complex::Complex(double r){re=r; im=0.;}
Complex::Complex(){re=0.; im=0.;}

/*#DB*/Complex Complex::operator+(const Complex& rhs){
	return Complex(re+rhs.re,im+rhs.im);
}
Complex Complex::operator-(const Complex& rhs){
	return Complex(re-rhs.re,im-rhs.im);
}/*#HB*/