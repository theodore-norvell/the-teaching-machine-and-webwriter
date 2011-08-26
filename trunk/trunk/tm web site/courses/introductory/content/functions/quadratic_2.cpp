/*#H*/ #include <iostream> 
using namespace std;

bool quadraticRoots(double a, double b, double c, double& bigRoot, double& smallRoot);
	

int main(){

	double a;
	double b;
	double c;

	double firstRoot;	// create two variables to hold the answer
	double scndRoot;
	bool real;          // will be true if answer is real

	cout << "Enter the a coefficient: ";
	cin >> a;
	cout << "& the b coefficient: ";
	cin >> b;
	cout << "& the c coefficient: ";
	cin >> c;

	real = quadraticRoots(a, b, c, firstRoot, scndRoot);
    if (real) {
	    cout <<endl << "The larger quadratic root is " << firstRoot;
	    cout << " and the smaller is  " << scndRoot << endl;
    } else {
	    cout <<endl << "The roots are (" << firstRoot << " + " << scndRoot;
	    cout << "i) and ("<< firstRoot << " - " << scndRoot << "i).\n";
    } 
	return 0;
}

/*#DA*/
#include <cmath>
bool pathological(double a, double b, double c, double& root);
bool radical(double a, double b, double c, double& rad);

/** quadraticRoots ********************************************
*
* @params: a,b,c - three real nos @pre a and b cannot both be 0.
*          root1 - a variable in which to put the first root
*          root2 - a variable in which to put the second root
*
* @modifies: root1 - set to larger root if real, or real part if not
*            root2 - set to smaller root is real, or imag part if not
*                  NOTE: if roots identical both set to the single value
*
* @returns: true if roots are complex
*           NOTE: if true, actual roots are root1 + j root2
*                                           root1 - j root2
****************************************************************/
bool quadraticRoots(double a, double b, double c, double& root1, double& root2){
	bool isReal;
	if (pathological(a,b,c,root1) ){
		root2 = root1;  // linear, not quadratic, both roots the same
		isReal = true;  // and real
	}
	else {	// proper quadratic
		double rad;		// variable to hold the radical
		isReal = radical(a,b,c, rad);
		if (isReal){
            if (a > 0) {
			    root1 = (-b + rad) / 2*a;	// large root
			    root2 = (-b - rad) /2*a;	// small root
            } else {
			    root1 = (-b - rad) / 2*a;	// large root
			    root2 = (-b + rad) /2*a;	// small root
            }
		} else {
			root1 = -b/(2*a);		// real part
			root2 = rad/(2*a);		// imaginary part
		}
	}
	return isReal;
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
*          rad - an output variable
*
* @modifies: rad - contains sqrt(|b^2 - 4ac|)
*
* @returns: true if real, false if imaginary
****************************************************************/
bool radical(double a, double b, double c, double& rad){
	bool isReal;

	rad = b*b - 4 * a * c;
	if (rad >= 0.0){
		isReal = true;
		rad = sqrt(rad);
	}
	else {
		isReal = false;
		rad = sqrt(-rad);
	}
	return isReal;
}


       
