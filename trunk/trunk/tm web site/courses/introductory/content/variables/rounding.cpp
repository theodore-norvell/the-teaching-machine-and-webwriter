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

    cout << "Truncate " << x << " to int: " << truncate(x);
    cout << "\nTruncate " << y << " to int: " << truncate(y);
    cout << "\nRound " << x << " to int: " << round(x);
    cout << "\nRound " << y << " to int: " << round(y);
    cout << "\nLet's try negative numbers!";
    cout << "\nTruncate " << -x << " to int: " << truncate(-x);
    cout << "\nTruncate " << -y << " to int: " << truncate(-y);
    cout << "\nRound " << -x << " to int: " << round(-x);
    cout << "\nRound " << -y << " to int: " << round(-y);

return 0;
}
/*#DA*/
/** truncate *******************************
*
* @params: x - any double
*
* @returns: truncated integer version of x
*******************************************/ 

int truncate (double x){
	return x;
}


/** round *******************************
*
* @params: x - any double
*
* @returns: rounded integer version of x
*******************************************/ 

int round (double x){
	return (x+0.5);
}
