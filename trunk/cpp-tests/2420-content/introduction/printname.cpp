/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * printname.cpp -- Demonstrate string expressions.
 *
 *
 * Author: Dennis Peters
 * Date: 2000.01.15
 *
 *******************************************************************/
#include <iostream>
#include <string>
using namespace std;

string FIRST("Dennis");      // My first name
string LAST("Peters");       // My last name
char INITIAL('K');           // My initial

/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cout -- outputs the name in various forms.
 *
 * Returns: 0
 *******************************************************************/
int main() {
  string firstLast;                 // Name in First Last format

  firstLast = FIRST +  LAST;
  cout << "My name is: " << firstLast << endl;

  string lastFirst;                 // Name in Last, First format
  lastFirst = LAST +  FIRST;
  cout << "in last, first format: " << lastFirst;
  cout << ", and with my initial: " << lastFirst << " " 
       << INITIAL << "." << endl;

  return 0;
}

/******************************************************************
 * 
 * 
 *  
 *
 *                     REVISION HISTORY
 *
 * 
 *
 ******************************************************************/
