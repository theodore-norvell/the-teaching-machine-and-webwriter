/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * name.cpp -- Demonstrate strings
 *
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
#include <string>
using namespace std;

/******************************************************************
 * main
 *
 * Parameters: none
 * Modifies: cin, cout -- prompts for and reads some strings.
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main() {
  string name;   // User name, input
  string middle; // middle name, input
  int spacePos = 0; // Position of space in name.
  cout << "Please enter your name, followed by the \"Enter\" key: ";
  getline(cin, name);
  cout << "Your name is: \"" << name << "\"\n";
  cout << "Your name contains " << name.length() << " characters." << endl;

  spacePos = name.find(" ");
  cout << "Formal form: " << name.at(0) << ". ";
  cout << name.substr(spacePos+1, name.length()) << endl;

  cout << "Please enter your middle name: ";
  cin >> middle;

  name.insert(spacePos+1, middle);
  name.insert(spacePos+middle.length()+1, " ");
  cout << "Full name: " << name << endl;

  return 0;
}
/*#HA*/
/******************************************************************
 * Revision: 1.3
 * Date: 2001-01-30
 *
 *
 *                     REVISION HISTORY
 *
 * Log: 
 * Revision 1.3  2001-01-30   dpeters
 * Fixed main header block.
 *
 * Revision 1.2  2001-01-30   dpeters
 * Use some other name functions.
 *
 * Revision 1.1  2001-01-30   dpeters
 * Initial revision
 *
 *
 ******************************************************************/
