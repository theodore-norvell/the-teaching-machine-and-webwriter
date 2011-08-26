/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * median.cpp -- Demonstrate Arrays & Files & modularization
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>

#include <string>
#include "services.h"

using namespace std;


const int MAX_SIZE = 250;

/******************************************************************
 * main -- Find the median grade in a list of grades in a file.
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int openFile(ifstream& is);

int main() {
	double grades[MAX_SIZE];
	ifstream theFile;
	int classSize;
	double median;

	if (openFile(theFile) == EXIT_FAILURE)
		return EXIT_FAILURE;

	classSize = readDoubles(theFile, grades, MAX_SIZE);
	theFile.close();

	cout << "Here are the unsorted grades:" << endl;
	outArrayofDoubles(grades, classSize);

	sortDoubles(grades, classSize);

	cout << "Here are the sorted grades:" << endl;
	outArrayofDoubles(grades, classSize);

	if (classSize % 2  == 0)  // even
		median = (grades[classSize/2-1] + grades[classSize/2]) / 2;
	else
		median = grades[classSize/2];

	cout << "The median grade in the class is " << median << endl;


	return 0;
}




