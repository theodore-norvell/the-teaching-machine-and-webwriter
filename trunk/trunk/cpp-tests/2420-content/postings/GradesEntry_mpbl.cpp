/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * GradesEntry.cpp -- Program to compute the final grade for every student 
 *                   in a course.
 *
 * Input: number of students in the course
 *        max mark and percentage of total value for
 *            first midterm
 *            second midterm
 *            labs
 *            assignments
 *            final
 *         for each student, mark for each defined component
 *
 * Output: for each student, total mark in course
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include <string>
using namespace std;

float getFloat(string promptPart);
float getAndComputeMarks(float maxMTerm1, float maxMTerm2, float maxLabs, float maxAssigns, float maxFinal,
						 float percentMTerm1, float percentMTerm2, float percentLabs, float percentAssigns, float percentFinal);
float getMark(string what, float max);

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int main(){
	int numStudents;	// total number of students

	float maxMTerm1;
	float maxMTerm2;
	float maxLabs;
	float maxAssigns;
	float maxFinal;

	float percentMTerm1;
	float percentMTerm2;
	float percentLabs;
	float percentAssigns;
	float percentFinal;

	float mark;		// The final mark for an individual student

	// First we get the data for the course

	cout << "Please enter the number of students in the course: ";
	cin >> numStudents;

// enter the midterm1, midterm2, assignments, labs and final max (or 0 if none) and percentage 
	maxMTerm1 = getFloat("maximum mark for mid term # 1 (or 0 if none)");
	maxMTerm2 = getFloat("maximum mark for mid term # 2 (or 0 if none)");
	maxLabs = getFloat("maximum mark for the labs (or 0 if none)");
	maxAssigns = getFloat("maximum mark for the assigns (or 0 if none)");
	maxFinal = getFloat("maximum mark for the final (or 0 if none)");

	if (maxMTerm1 > 0) percentMTerm1 = getFloat("percentage for mid term # 1");
	if (maxMTerm2 > 0) percentMTerm2 = getFloat("percentage for mid term # 2");
	if (maxLabs > 0) percentLabs = getFloat("percentage for the labs");
	if (maxAssigns > 0) percentAssigns = getFloat("percentage for the assigns");
	if (maxFinal > 0) percentFinal = getFloat("percentage for the final");

 // enter mark for each component 
// compute course mark

	for( int student = 1; student <= numStudents; student++) {
		mark = getAndComputeMarks(maxMTerm1, maxMTerm2, maxLabs, maxAssigns, maxFinal,
				percentMTerm1, percentMTerm2, percentLabs, percentAssigns, percentFinal);

		cout << "The mark is " << mark << endl;
	}

	return 0;
}


/******************************************************************
 * getFloat -- Fetch a floating point number from input stream
 *
 * Parameters: string what: used to help prompt the user as to what the number is for
 *
 * Modifies: cin
 *
 * Precondition: none
 *
 * Returns: the floating point number input by the user
 *******************************************************************/
float getFloat(string what){
	float entryValue;
	cout << "Please input a value for the " << what << ": ";
	cin >> entryValue;
	return entryValue;
}


float getAndComputeMarks(float maxMTerm1, float maxMTerm2, float maxLabs, float maxAssigns, float maxFinal,
						 float percentMTerm1, float percentMTerm2, float percentLabs,
						 float percentAssigns, float percentFinal){
	float totalMark = 0;
	if (maxMTerm1 > 0)
		totalMark += getMark("mid term 1", maxMTerm1) * percentMTerm1/maxMTerm1;
	if (maxMTerm2 > 0)
		totalMark += getMark("mid term 2", maxMTerm2) * percentMTerm2/maxMTerm2;
	if (maxLabs > 0)
		totalMark += getMark("labs", maxLabs) * percentLabs/maxLabs;
	if (maxAssigns > 0)
		totalMark += getMark("assignments", maxAssigns) * percentAssigns/maxAssigns;
	if (maxFinal > 0)
		totalMark += getMark("final", maxFinal) * percentFinal/maxFinal;
	return totalMark;
}

float getMark(string what, float max){
	float grade;
	cout << "Please input the grade for " << what << ": ";
	cin >> grade;
	while (grade < 0 || grade > max) {
		cout << "Grade must be between 0 and " << max << " .Please re-enter: ";
		cin >> grade;
	}
	return grade;
}
