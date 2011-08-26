/*#HA*/ /******************************************************************
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

bool isWhiteSpace(char c);

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
	long wordCount = 0;
	char next;
	bool inWord = false;

	cin.get(next);
	while (!cin.fail()) {
		if (isWhiteSpace(next))
			inWord = false;
		else if (!inWord) { // First letter in word
			inWord = true;
			wordCount++;
		}
		cin.get(next);
	}

	cout << "\n\nThere were " << wordCount << " words." << endl;

	return 0;
}
/*#DA*/

/** isWhiteSpace ********************************
 * @params: c - the character to be tested
 *
 * Modifies: nothing
 *
 * Precondition: none
 *
 * Returns: true if c is a whitespace character
 ************************************************/
bool isWhiteSpace(char c){
	return c == ' ' || c == '\n' || c == '\t';
}
