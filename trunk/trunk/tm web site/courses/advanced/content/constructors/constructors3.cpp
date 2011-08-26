class Complex {
public:
	double real() const;
	double imag() const;
	Complex(double r,double i=0.);
private:
	double re,im;
};
