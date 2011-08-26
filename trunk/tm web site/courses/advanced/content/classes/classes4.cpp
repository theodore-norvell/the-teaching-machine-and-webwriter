class Complex {
public:
	double getReal(){return re;}
	double getImag(){return im;}
void set(double r, double i){re = r; im = i;}
private:
	double re,im;
};
