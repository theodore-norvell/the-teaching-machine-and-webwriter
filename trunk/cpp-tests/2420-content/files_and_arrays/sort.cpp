/*#HA*/ /*#HB*/ /*#HC*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * median.cpp -- Demonstrate sorting
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include <string>

using namespace std;

void sortDoubles(double theTable[], int size);
void swapDoubles(double& arg1, double& arg2);
void outArrayofDoubles(double theTable[], int size);
int fakeGrades(double grades[], int maxSize);

const int MAX_SIZE = 25;

/******************************************************************
 * main -- Find the median grade in a list of grades in a file.
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/
int main() {
	double grades[MAX_SIZE];
	int classSize;
	double median;

	classSize = fakeGrades(grades, MAX_SIZE);

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
/*#HA*/
/******************************************************************
 * fakeGrades -- TeachingMachine workaround
 * The TM can't work with files or initialize arrays so we fake
 * a file reading function here and fill up the grades array
 * with sample data.
 *
 * Parameters: 
 *   grades:   holds the grades to be faked   
 *   maxSize:  the largest number of grades the array can hold
 *
 * Modifies: the grades array has data put into it
 *
 * Returns: the actual no. of grades
 *******************************************************************/
int fakeGrades(double grades[], int maxSize){
	grades[0] = 56.5;
	grades[1] = 91;
	grades[2] = 74.5;
	grades[3] = 63;
	grades[4] = 91;
	grades[5] = 81.5;
	grades[6] = 72;
	grades[7] = 68;
	grades[8] = 72;
	grades[9] = 64;
	grades[10] = 89;
	grades[11] = 44;
	grades[12] = 74.5;
	grades[13] = 66;
	grades[14] = 79;
	grades[15] = 51;
	grades[16] = 33.5;
	grades[17] = 82;
	grades[18] = 83.5;
	grades[19] = 73;
	return 20;
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
/*#DB*/
void sortDoubles(double theTable[], int size){

	for(int i = 0; i < size-1; i++) {
		for (int j = i+1; j < size; j++) {
			if (theTable[j] > theTable[i])
				swapDoubles(theTable[i], theTable[j]);

		}
	}
}
/*#HB*/


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
/*#DC*/
void swapDoubles(double& arg1, double& arg2){
	double hold;
	hold = arg1;
	arg1 = arg2;
	arg2 = hold;
}
/*#HC*/




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
	cout << theTable[size - 1] << '}'<<endl;
}