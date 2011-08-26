/*#H*/ #include <iostream> 
using namespace std;

double bigQuadraticRoot(double a, double b, double c);
	
int main(){
    double a;	// Made external to remove it from TM memory
    double b;
    double c;

	cout << "Enter the a coefficient: ";
	cin >> a;
	cout << "& the b coefficient: ";
	cin >> b;
	cout << "& the c coefficient: ";
	cin >> c;

	cout <<endl << "The largest quadratic root is " << bigQuadraticRoot(a,b,c) << endl;
	return 0;
}

/*#DA*/
#include <cmath>

/** bigQuadraticRoot ********************************************
*
* @params: a,b,c - three real nos @pre a and b cannot both be 0.
*                                      b^2 - 4ac cannot be negative
*
* @returns: the largest real root of the quadratic defined by ax^2 + bx + c
****************************************************************/
double bigQuadraticRoot(double a, double b, double c){
	double answer;
	if (a == 0.0)
		if (b == 0.0)
			answer = -1.0;
		else
			answer = -c/b;
	else {
		answer = -b;
		double rad = b*b-4*a*c;
		if (rad > 0)
			if (a > 0)
               answer += sqrt(rad);
            else
               answer -= sqrt(rad);
		answer /= 2*a;
	}
	return answer;
}/*#HA*/
       
