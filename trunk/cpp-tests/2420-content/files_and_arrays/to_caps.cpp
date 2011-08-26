/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * to_caps.cpp -- Filter program to capitalize all letters 
 *
 * Input: the input stream
 *
 * Output: the output stream
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
using namespace std;


/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: input, output streams
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/	
int main(){
	char next;

	cin.get(next);
	while (!cin.fail()) {
		if (next >= 'a' && next <= 'z')
			next += 'A' - 'a';
		cout << next;	// Copy next to output
		cin.get(next);
	}

	return 0;
}

