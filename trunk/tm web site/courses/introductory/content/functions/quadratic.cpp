/*#H*/ #include <iostream> 
using namespace std;

void quadraticRoots(double a, double b, double c, double& bigRoot, double& smallRoot);
	

int main(){

	double a;
	double b;
	double c;

	double large;	// create two variables to hold the answer
	double small;

	cout << "Enter the a coefficient: ";
	cin >> a;
	cout << "& the b coefficient: ";
	cin >> b;
	cout << "& the c coefficient: ";
	cin >> c;

	quadraticRoots(a, b, c, large, small);

	cout <<endl << "The larger quadratic root is " << large;
	cout << " and the smaller is  " << small << endl;
	return 0;
}

/*#DA*/
#include <cmath>
// These must be declared so that quadraticRoots can call them
bool pathological(double a, double b, double c, double& root);
double radical(double a, double b, double c);


/** quadraticRoots ********************************************
*
* @params: a,b,c - three real nos @pre a and b cannot both be 0.
*                                      b^2 - 4ac cannot be negative
*          bigRoot - a variable in which to put the larger root
*          smallRoot - a variable in which to put the smaller root
*
* @modifies: bigRoot and smallRoot by setting them to the larger & smaller
*            roots respectively. If the roots are identical, both will
*            be set to that same single value
*
* @returns: nothing
****************************************************************/
void quadraticRoots(double a, double b, double c, double& bigRoot, double& smallRoot){
	if (pathological(a,b,c,bigRoot))
		smallRoot = bigRoot;
	else {
		double rad = radical(a,b,c);
		if (rad >= 0){
            if (a > 0){
			    bigRoot = (-b + rad) / (2*a);
			    smallRoot = (-b - rad) /(2*a);
             } else {
			    bigRoot = (-b - rad) / (2*a);
			    smallRoot = (-b + rad) /(2*a);
             }
		}
	}
}

/** pathological ********************************************
*
* @params: a,b,c - three real nos @pre a and b cannot both be 0.
*          root - a variable in which to put the root
*
* @modifies: root - sets to -c/b if a is 0.0 and b is non-zero
*
* @returns: true if a is 0.0
****************************************************************/
bool pathological(double a, double b, double c, double& root){
	bool isPath = false; // not pathological is default
	if (a == 0.0) {
		isPath = true;	// not actually a quadratic
		if (b!=0.0)
			root = -c/b;
	}
	return isPath;
}

/** radical ********************************************
*
* @params: a,b,c - three real nos @pre a and b cannot both be 0.
*
* @returns: root(b^2-4ac) if b^2-4ac is non-negative, -1.0 otherwise
****************************************************************/
double radical(double a, double b, double c){
	double rad = b*b - 4 * a * c;
	if (rad >= 0.0) return sqrt(rad);
	return -1.0;
}


       
