//! Run.
double function1(double arg1, double arg2);	// Declaration

int main(){
	double x=3., y= 17.5;
	double w, z;

	w = function1(x,y);		// Call
	z = function1(y,w);		// Another call
	return 0;
}

double function1(double arg1, double arg2) {		// Definition
	if (arg1 > arg2)
		return arg1*arg2;
	else
		return -arg2;
}
