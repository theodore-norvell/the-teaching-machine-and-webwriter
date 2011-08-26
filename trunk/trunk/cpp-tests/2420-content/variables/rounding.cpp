/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * rounding.cpp -- Demonstrate rounding of POSITIVE NOS
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.02
 *
 *******************************************************************/
#include <iostream>
using namespace std;


/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs some results
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main() {

  double x = 3.4999;
  double y = 3.6;

  cout << "Force " << x << " to int: " << (int)x << endl;
  cout << "Force " << y << " to int: " << (int)y << endl;
  cout << "Round " << x << " to int: " << (int)(x + .5) << endl;
  cout << "Round " << y << " to int: " << (int)(y + .5) << endl;

  return 0;
}/*#HA*/

/******************************************************************
 * Revision No. 
 * Date: 
 * By:  
 *
 *                     REVISION HISTORY
 *
 *  *
 ******************************************************************/
