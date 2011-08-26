#include <math.h>

/*#TA*/class Complex {
public:
	void set(double real,double imag);
	double getReal();
	double getImag();
	double getMag();
	double getAngle();
private:
	double re,im;	//rectangular co-ordinates
	double mag, ph;	// polar co-ordinates
};/*#/TA*/
int main(){
	Complex x,y,z;
	x.set(1.0,0.0);
	y.set(1.0,-1.0);
	z.set(3.2, 1.0);
}

void Complex::set(double real, double imag){
	re = real;
	im = imag;
	mag = sqrt(re*re + im*im);
	ph = atan2(re, im);
}

double Complex::getReal(){return re;}
double Complex::getImag(){return im;}
double Complex::getMag(){return mag;}
double Complex::getAngle(){return ph;}


