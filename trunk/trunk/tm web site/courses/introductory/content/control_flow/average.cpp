/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * average.cpp -- Calculate the average of a series of numbers
 *
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
using namespace std;

/******************************************************************
 * main -- average values input by user
 *
 * Arguments: none
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main() {
  double sum = 0.0; // Accumulator for sum of values
  int count = 0;    // Counter for number of values
  double val;       // Value input by user
  double avg;       // Average of values

  cout << "This program calculates the average of a sequence of" << endl;
  cout << "positive values.\n";
  cout << "Please enter the values (enter a negative value to exit): ";
  cin >> val;
  while (val > 0) {
    sum = sum + val;
    count++;
	cin >> val;
  }
  if (count > 0) {
    avg = sum / double(count);
    cout << "The average is: " << avg << endl;
  } else {
    cout << "No values were entered.\n";
  }

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
 *  Log
 *
 ******************************************************************/
