#include <math.h>
#include <iostream>
using namespace std;
/* Run under Symantec java runtime engine
	Issues: Slow loading (18 secs)
		Slow response to window selection
		Mouse disappears when datums are expanded, brought back by movement but writes portion of old image under mouse(symantec problem)?
		All constructor calls resolve to no argument constructor
*/
class Complex {
public:
	Complex(double re, double im);
	Complex(double re);
	Complex();
	double getReal() const;
	double getImag() const;
//	double getMag() const;
//	double getPhase() const;
private:
	double re;
	double im;
};


Complex::Complex(double re, double im){
	this->re = re;
	this->im = im;
}
Complex::Complex(double re){
	this->re = re;
	this->im = 0.0;
}Complex::Complex(){
	this->re = 0.;
	this->im = 0.;
}
double Complex::getReal()const {return re;}
double Complex::getImag() const {return im;}
//double Complex::getMag() const {return sqrt(re*re + im*im);}
//double Complex::getPhase() const {return atan2(re,im);}

void printComplex(const Complex x);

void main(){
	Complex x1(1,2);
	Complex x2(3);
	Complex x3;

	printComplex(x1);
	//cout << endl;
	printComplex(x2);
	//cout << endl;
	printComplex(x3);

}

void printComplex(const Complex x){
	/*cout << '(';
	if (x.getReal()==0.0 && x.getImag() == 0.0)
		//cout << 0.0;
	else if (x.getImag() == 0.0)
		//cout << x.getReal();
	else if (x.getReal() == 0.0)
		if (x.getImag() < 0.0)
			//cout << "- j" << -x.getImag();
		else
			//cout << 'j' << x.getImag();
	else {
		//cout << x.getReal();
		if (x.getImag() < 0.0)
			//cout << " - j" << -x.getImag();
		else //cout << " + j" << x.getImag();
	}
	//cout << ')';*/
}
