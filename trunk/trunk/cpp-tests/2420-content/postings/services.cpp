 /******************************************************************
 * services.cpp -- a file containing a number of functions useful
 * for creating programs.
 *
 * Each function constitutes a "service" that can be used in
 *		different programs
 *
 * Thus this services file constitutes a library of function services
 *
 *
 *******************************************************************/
#include "services.h"
#include <iostream>
#include <string>

using namespace std;

 /******************************************************************
 * openFile -- prompt for a filename & open file for reading
 *
 * Parameters: 
 *   is:   the input stream to which to attach the file
 *
 * Modifies: attaches a file to the input stream, is
 *
 * Returns: EXIT_FAILURE if file can't be opened, 0 if successful
 *******************************************************************/
int openFile(ifstream& is){
	string fileName;

	cout << "Please specify the name of the file to open: ";
	cin >> fileName;
	is.open(fileName.c_str());
	if (is.fail()){
		cout << "Unable to open file " << fileName << endl;
		return EXIT_FAILURE;
	}
	return 0;
}


 /******************************************************************
 * readDoubles -- read a list of doubles from a stream to an array
 *
 * Parameters: 
 *   is:        the input stream to which to attach the file
 *   anArray:   the array into which the file data is to be read   
 *   maxSize:   the maximum size the array can hold
 *
 * Modifies: loads data into anArray
 *
 * Returns: the actual size of the array or -1 if too much data
 *******************************************************************/

int readDoubles(ifstream& is, double anArray[], int maxSize){
	int size;   // actual size
	is >> size;
	if (size > maxSize) {
		cout << "Sorry! Can only handle arrays of up to " << maxSize << endl;
		return -1;
	}
	for (int i = 0; i < size; i++) {
		is >> anArray[i];
		if(is.fail()) {
			cout << "Sorry. Unable to read file after " << i << "'th entry." << endl;
			return 0;
		}
	}
	return size;
}


 /******************************************************************
 * sortDoubles -- sort an array of doubles in descending order
 *
 * Parameters: 
 *   theTable:   holds the data to be sorted   
 *   size:       the number of doubles in the table
 *
 * Modifies: the data in the array is sorted in descending order
 *
 * Returns: nothing
 *******************************************************************/

void sortDoubles(double theTable[], int size){

	for(int i = 0; i < size-1; i++) {
		for (int j = i+1; j < size; j++) {
			if (theTable[j] > theTable[i])
				swapDoubles(theTable[i], theTable[j]);

		}
	}
}



 /******************************************************************
 * swapDoubles -- swap a pair of doubles
 *
 * Parameters: 
 *   arg1:   the first of the two doubles
 *   arg2:   the other double   
 *
 * Modifies: the data in the parameters is swapped
 *
 * Returns: nothing
 *******************************************************************/

void swapDoubles(double& arg1, double& arg2){
	double hold;
	hold = arg1;
	arg1 = arg2;
	arg2 = hold;
}





 /******************************************************************
 * outArrayofDoubles -- output an array of doubles 
 *
 * Parameters: 
 *   theTable:   holds the data to be sorted   
 *   size:       the number of doubles in the table
 *
 * Modifies: nothing
 *
 * Returns: nothing
 *******************************************************************/

void outArrayofDoubles(double theTable[], int size){
	cout << '{';
	for (int i = 0; i < size-1; i++)
		cout << theTable[i] << ". ";
	cout << theTable[size - 1] << '}';
}