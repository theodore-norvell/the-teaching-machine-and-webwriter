/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * precedent.cpp -- Demonstrate operator precedence, and the increment
 *                  statement.
 *
 * Author: Dennis Peters
 * Date: 2000.01.18
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
  cout << "5 + 6 * 2 = " << 5 + 6 * 2 << endl;
  cout << "(5 + 6) * 2 = " << (5 + 6) * 2 << endl;
  cout << "10 / (2 * 5) = " << 10 / (2 * 5) << endl;
  cout << "10 / 2 * 5 = " << 10 / 2 * 5 << endl;
  cout << "5 + -6 * 2 = " << 5 + -6 * 2 << endl;
  cout << "5.0 / 2.0 = " << 5.0 / 2.0 << endl;
  cout << "5 / 2 = " << 5 / 2 << endl;

  return 0;
}/*#HA*/

/******************************************************************
 * Revision No. 1
 * Date: 2004.01.02
 * By: mpbl 
 *
 *                     REVISION HISTORY
 *
 * Description: Ammended for TM and shortened
 *  *
 ******************************************************************/
