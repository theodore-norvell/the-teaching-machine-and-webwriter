/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * word_count.cpp -- Program to count words in a pure text file 
 *
 * Input: the input stream
 *
 * Output: word count
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
int main(){
	char next;

	cin.get(next); // fetch first character
	while (!cin.fail()) { // until out of characters 
		if ('a' <= next && next <= 'z') // if next is a lowercase letter
		   next += 'A' - 'a'; // add uppercase offset to letter's code
		cout << next;
        cin.get(next); // fetch next character
	}


	return 0;
}
