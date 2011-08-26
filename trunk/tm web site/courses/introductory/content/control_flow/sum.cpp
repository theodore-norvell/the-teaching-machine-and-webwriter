/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * sum.cpp -- Illustrate using while loops to sum some values.
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
  double val;       // Value input by user
  int num = 0;      // Number of values to sum
  int count = 0;    // Number of values input so far

  cout << "This program calculates the sum of the values entered.\n";
  cout << "How many values will you sum:";
  cin >> num;
  while (count < num) {
    cout << "Please enter a value: ";
    cin >> val;
    sum = sum + val;
    count++;
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
 * Log
 *
 ******************************************************************/
