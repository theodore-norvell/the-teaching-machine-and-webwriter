//! Run.

double function1(double arg1, double arg2);

int main(){
	double x=3., y= 17.5;
	double w, z;

	w = function1(x,y);
	z = function1(y,x);
	return 0;
}

double function1(double a1, double a2){
	if (a1 > a2)
		return a1*a2;
	else
		return -a2;
}
