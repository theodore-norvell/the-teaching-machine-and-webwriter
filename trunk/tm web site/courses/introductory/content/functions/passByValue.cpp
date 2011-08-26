/*#H*/ /*******  for loop demonstration ********
    A little program to output a table
    of factorials

*******************************************/

/*#DA*/ #include <iostream>
using namespace std;    // cout is in the std namespace

double cosh(double x);

int main(){
    double x = 1.5;
    double y = -3.2;
    int k = 4;
    double result;
    
    /* We do a whole bunch of different calls to illustrate
    that C++ normally passes by value.
    In each case, use the teaching machine to see what gets
    passed into the cosh function.
    */

// pass in a number    
    result = cosh(2.5);    
    cout << "cosh(2.5) is " << result << endl;

// pass in a variable with the same name as the parameter    
    result = cosh(x);    
    cout << "cosh(x) is " << result << endl;

// pass in a variable with a name different from the parameter    
    result = cosh(y);    
    cout << "cosh(y) is " << result << endl;

// pass in a int variable    
    result = cosh(k);    
    cout << "cosh(k) is " << result << endl;

// pass in an expression   
    result = cosh(k*x+2*y);    
    cout << "cosh(kx+2y) is " << result << endl;

	return 0;
}/*#HA*/


/*#DB*/ /** cosh(x) **************************************************

* @param: x - the value for which the cosh should be computed
*
* @returns: an approximation of cosh(x)
****************************************************************/
double cosh(double x){
    const int N = 10; // the no. of terms in the series
	double term = 1.; // the 0'th term
	double sum = term;  // the 0'th sum
	for (int i = 1; i < N; i++){
        term *= x*x/(2*i*(2*i-1));
        sum += term;
    }
    return sum;
}/*#HB*/	

        
