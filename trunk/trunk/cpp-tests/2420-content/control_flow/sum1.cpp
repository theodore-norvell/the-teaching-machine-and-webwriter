/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * sum1.cpp -- Illustrate using while loops to sum some values.
 *
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
using namespace std;

/******************************************************************
 * main -- sum values input by user
 *
 * Arguments: none
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main() {
  double sum = 0.0; // Accumulator for sum of values
  char ans = 'y';   // Answer from user
  double val;       // Value input by user

  cout << "This program calculates the sum of the values entered.\n";
  while (ans == 'y') {
    cout << "Please enter a value: ";
    cin >> val;
    sum = sum + val;
    cout << "Do you want to add another value? (Enter y or n) ";
    cin >> ans;
  }
  cout << "The sum is: " << sum << endl;

  return 0;
}
/*#HA*/
/******************************************************************
 * Revision: 0
 * Date:
 * 
 *
 *                     REVISION HISTORY
 *
 * LOG
 *
 ******************************************************************/
