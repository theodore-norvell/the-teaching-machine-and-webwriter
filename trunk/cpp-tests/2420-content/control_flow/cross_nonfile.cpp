/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * cross_nonfile.cpp -- Find the first zero-crossing in a stream of data
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
int
main()
{
  int count = 0;    // Counter for number of values
  double val;       // Value input from file
  bool prevPos = false; // Was the previous data value positive
  bool foundCrossing = false; // Set to true when a 0-crossing is found

  cout << "This finds the first zero crossing";
  cout << " in the input data." << endl;

  cin >> val;
  prevPos = (val > 0.0); // Initialize prevPos for first value
  count++;
  
  while (!foundCrossing) {
    cin >> val;
    foundCrossing = (val > 0.0 && !prevPos) || (val <= 0.0 && prevPos);
    prevPos = (val > 0.0);
    count++;
  }
  cout << "Zero crossing found at item # " << count << endl;

  return 0;
}

/******************************************************************
 * $RCSfile$   $Revision$
 * $Date$
 * $State$ 
 *
 *                     REVISION HISTORY
 *
 * $Log$
 *
 ******************************************************************/
