/*******  Expression Evaluation ********

	A simple line equation
  
*******************************************/
#include <iostream.h>   // info from standard library
using namespace std;    // cout is in the std namespace

void main(){
	double x = 2.4;
	double y;
	
	y = x*x + 2 * x + 1;
	cout << "y is " << y << " when x is " << x << '\n';
}
        