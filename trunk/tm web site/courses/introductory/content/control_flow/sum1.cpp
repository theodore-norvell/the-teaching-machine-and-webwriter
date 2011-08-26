/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * sum1.cpp -- Illustrate using while loops
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/
#include <iostream>
using namespace std;
double pSeries(double p, double error);


int main() {
	cout << "pSeries for p = 1.5, within .00001 is " << pSeries(1.5, .00001) << endl;
  return 0;
}
/*#DA*/
#include <cmath>

/** p series **************************************************
*
* @param: p @pre: p > 1
*         error - allowable error in calculation
*
* @returns: the factorial of num
****************************************************************/
double pSeries(double p, double error){
	double sum = 1.0;
	if (p > 1) {
		double newTerm = 1;
		int k = 1;
		while (newTerm > error) {
			sum += newTerm;
			k++;
			newTerm = 1/pow(k,p);
		}
	}
	return sum;
}


