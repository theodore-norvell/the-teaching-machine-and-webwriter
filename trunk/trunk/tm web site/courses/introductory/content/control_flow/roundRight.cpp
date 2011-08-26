/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * rounding.cpp -- Demonstrate rounding of POSITIVE NOS
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.01.18
 *
 *******************************************************************/
#include <iostream>
using namespace std;


int truncate(double x);
int round(double x);


int main() {
    
    double x = 3.499999;
    double y = 7.5;
    
    cout << "Round " << x << " to int: " << round(x) << endl;
    cout << "Round " << y << " to int: " << round(y) << endl;
    cout << "Let's try negative numbers!"<<endl;
    cout << "Round " << -x << " to int: " << round(-x) << endl;
    cout << "Round " << -y << " to int: " << round(-y) << endl;
    
    return 0;
}


/*#DA*/ /** round *******************************
*
* @params: x - any double
*
* @returns: rounded integer version of x
*******************************************/ 

int round (double x){
	if (x < 0)
		x -= 0.5;
	else
		x += 0.5;
	return x;
}/*#HA*/
