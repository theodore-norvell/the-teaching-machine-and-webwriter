/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * faverage.cpp -- Calculate the average of a series of numbers in 
 *                 a file
 *
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
#include <fstream>
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
  double val;       // Value input from file
  double avg;       // Average of values
  ifstream infile;  // The file stream to read from

  cout << "This program calculates the average of the values\n";
  cout << "in the file \"test.txt\"" << endl;

  infile.open("test.txt");

  if (infile) {  // Check that the file has been opened successfully
    while (infile >> val && val > 0) {
      sum = sum + val;
      count++;
    }
    cout << count << " values read from \"test.txt\"\n";
    if (count > 0) {
      avg = sum / double(count);
      cout << "The average is: " << avg << endl;
    } else {
      cout << "No values were entered.\n";
    }
  } else {
    cout << "Failure opening file \"test.txt\"\n";
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
