/*#HA*/
#include <iostream> 
using namespace std;

double quadratic (double x);
double x;	// Keep off the stack so students won't see

int main(){
	x = quadratic(2.4);
	cout << "The equation result for 2.4 is " << x << endl;
	x = quadratic(-3.47);
	cout << "The equation result for -3.47 is " << x << endl;
	return 0;
}/*#DA*/
/** quadratic *******************************
*
* @params: x - any double
*
* @returns: x^2 + 2x + 1
*******************************************/ 

double quadratic (double x){
	return x*x + 2 * x + 1;
}