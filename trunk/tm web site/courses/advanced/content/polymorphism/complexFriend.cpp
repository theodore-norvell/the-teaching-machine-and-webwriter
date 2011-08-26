class Complex {
public:
// These three should be replaced by
//	Complex(double r = 0., double i = 0.); TM unsupported
	Complex(double r, double i);
	Complex(double r);
	Complex();
	Complex operator=(const Complex&rhs);
/*#TA*/	friend Complex operator+(const Complex& lhs, const Complex& rhs);
	friend Complex operator-(const Complex& lhs, const Complex& rhs);
	friend Complex operator+(double lhs, const Complex& rhs);
	friend Complex operator+(const Complex& lhs,double  rhs);
	friend Complex operator-(double lhs, const Complex& rhs);
	friend Complex operator-(const Complex& lhs,double  rhs);/*#/TA*/
private:
	double re,im;
};

/*#TC*/int main(){
	Complex x(-1., -1.);
	Complex y(2.2);
	Complex z;
	
	z = x + y;
	z = x - y;

	return 0;
}/*#/TC*/

Complex::Complex(double r, double i){re=r; im=i;}
Complex::Complex(double r){re=r; im=0.;}
Complex::Complex(){re=0.; im=0.;}
Complex Complex::operator=(const Complex&rhs){
	re=rhs.re;im=rhs.im;
	return *this;
}

/*#TB*/Complex operator+(const Complex& lhs, const Complex& rhs){
	return Complex(lhs.re+rhs.re, lhs.im+rhs.im);
}
Complex operator-(const Complex& lhs, const Complex& rhs){
	return Complex(lhs.re-rhs.re, lhs.im-rhs.im);
}
Complex operator+(double lhs, const Complex& rhs){
	return Complex(lhs + rhs.re, rhs.im);
}
Complex operator+(const Complex& lhs, double rhs){
	return Complex(lhs.re + rhs, lhs.im);
}
Complex operator-(double lhs, const Complex& rhs){
	return Complex(lhs - rhs.re, rhs.im);
}
Complex operator-(const Complex& lhs, double rhs){
	return Complex(lhs.re - rhs, lhs.im);
}/*#/TB*/

