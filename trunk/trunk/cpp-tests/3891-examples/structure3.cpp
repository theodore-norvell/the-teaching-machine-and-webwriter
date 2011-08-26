//! Run.
/*#HA*/ #include <iostream>
using namespace std;

/*#DA*/struct complex {
	double  re, im;
};
/* This example has been adapted to work with TM which does not
yet support initialization of structures.*/

complex	compAdd(complex x,complex y);

int main(){
	// TM doesn't support this initialization of structures
	//  complex x = {0.0,0.0}, y = {1.0,1.0},z;
	/*#HA*/complex x, y, z;
	x.re = x.im = 0.;
	y.re = 1.0;
	y.im = -1.0;/*#DA*/
	z = compAdd(x,y);
	cout << "z is (" << z.re << " + j" << z.im << ")\n";
	return 0;
}

complex compAdd(complex a1, complex a2) {
	complex z;
	z.re = a1.re + a2.re;
	z.im = a1.im + a2.im;
	return z;
}
