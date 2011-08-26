/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * average.cpp -- Program to compute the average of an array of nos. 
 *
 * Input: none
 *
 * Output: the average
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
 * Modifies:  output streams
 *
 * Returns: 0
 *******************************************************************/
double average (int grades[], int n);

int marks[20];
int i;

int main(){
	marks[0] = 65;
	marks[1] = 73;
	marks[2] = 81;
	marks[3] = 47;
	marks[4] = 61;
	marks[5] = 77;
	marks[6] = 51;
	marks[7] = 67;
	marks[8] = 75;
	marks[9] = 73;
	marks[10] = 85;
	marks[11] = 42;
	marks[12] = 66;
	marks[13] = 77;
	marks[14] = 75;
	marks[15] = 64;
	marks[16] = 74;
	marks[17] = 58;
	marks[18] = 66;
	marks[19] = 71;

	cout << "The class average is " << average(marks, 20) << endl;
	return 0;
}
/*#DA*/
/** average ********************************************************
*
* @params: grades - an array of marks #pre each mark is on [0,100]
*          size - the number of grades in the array. @pre size > 0
*
* @return: the average of the marks
********************************************************************/
double average (int grades[], int size){
	double sum;
	for (int i = 0; i < size; i++)
		sum += grades[i];
	return sum/size;
}

