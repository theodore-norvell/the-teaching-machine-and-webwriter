/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * largest.cpp -- Demonstrate Arrays & Files.
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include <fstream>
#include <string>
using namespace std;

int getLargest(double grades[], int size);

int openFile(ifstream& is);   // We're going to modify is, so pass by ref

int readDoubles(ifstream& is, double anArray[], int maxSize);


const int A_SIZE = 250;

/******************************************************************
 * main -- Test some vector operations.
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/

int main() {
	double grades[A_SIZE];
	ifstream theFile;
	int classSize;

	if (openFile(theFile) == EXIT_FAILURE)
		return EXIT_FAILURE;

	classSize = readDoubles(theFile, grades, A_SIZE);
	theFile.close();

	cout << "The highest grade in the class was " << grades[getLargest(grades, A_SIZE)] << endl;
	return 0;
}




/*set position to 0

for i = 1, i < size     // Notice adjustment to get started 

if data[i] > data[position]

position = i */


/******************************************************************
 * getLargest -- get the position of the largest grade in the array
 *
 * Parameters: 
 *   grades[]:   all grades in a class
 *   size:       class size
 *
 * Returns: the position of the first occurrence of the highest 
 *             grade in the class
 *******************************************************************/
int getLargest(double grades[], int size){
	int position = 0;
	for (int i = 1; i < size; i++)
		if (grades[i] > grades[position])
			position = i;

	return position;
}



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
	for (int i = 0; i < size; i++)
		is >> anArray[i];
	return size;
}
