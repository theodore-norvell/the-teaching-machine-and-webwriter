/*#H*/ /*#DA*/ class Complex {
public:
	Complex(double r, double i=0.) { re = r; im = i;}
	Complex operator+(const Complex& rhs) const {
	return Complex(re+rhs.re,im+rhs.im);}
	Complex operator-(const Complex& rhs) const {
		return Complex(re-rhs.re,im-rhs.im);}
private:
	double re,im;
};/*#HA*/
/*#DB*/class Complex {
public:
	Complex(double r, double i=0.) { re = r; im = i;}
	friend Complex operator+(const Complex& lhs, const Complex& rhs);
	friend Complex operator-(const Complex& lhs, const Complex& rhs);
private:
	double re,im;
};

Complex operator+( const Complex& lhs, const Complex& rhs){
	return Complex(lhs.re+rhs.re,lhs.im+rhs.im);
}

Complex operator-( const Complex& lhs, const Complex& rhs){
	return Complex(lhs.re-rhs.re,lhs.im-rhs.im);
}/*#HB*/
/*#DC*/class Complex {
public:
	Complex(double r, double i=0.) { re = r; im = i;}
	Complex operator+(const Complex& rhs) const {
		return Complex(re+rhs.re,im+rhs.im);}
	Complex operator+(int a) const {
		return Complex(re+a,im);}
private:
	double re,im;
};/*#HC*/

