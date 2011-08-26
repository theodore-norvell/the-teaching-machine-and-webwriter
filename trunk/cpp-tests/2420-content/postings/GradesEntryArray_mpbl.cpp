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


void setUpComponent(float& max, float& percent, const string& prompt);

float getContribution(float max, float percent, const string& prompt);

float getFloat(string promptPart);

float getMark(string what, float max);

const int COMPONENTS = 3;


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

	float maxima[COMPONENTS];

	float percentages[COMPONENTS];

	string prompts[COMPONENTS] = {"the mid term", "the assignments", "the final"};


	float mark;		// The final mark for an individual student

	// First we get the data for the course
	for(int i = 0; i < COMPONENTS ; i ++)
		setUpComponent(maxima[i], percentages[i], prompts[i]);


	cout << "Please enter the number of students in the course: ";
	cin >> numStudents;

 // enter mark for each component 
// compute course mark

	for( int student = 1; student <= numStudents; student++) {
		cout << "for student no. " << student << endl;
		mark = 0.;
		for (int j = 0; j < COMPONENTS; j++){
			mark += getContribution(maxima[j], percentages[j], prompts[j]);
		}

		cout << "The mark is " << mark << endl;
	}

	return 0;
}



/******************************************************************
 * setUpComponent -- get the max and (if warranted) % contribution of a component
 *
 * Parameters: max: the maximum mark (or 0 for no component)
 *             percent: the percentage contribution
 *
 * Modifies: max, percent
 *
 * Precondition: none
 *
 * Returns: nothing
 *******************************************************************/
void setUpComponent(float& max, float& percent, const string& prompt){

	// enter the max for the component(or 0 if none) and percentage if max > 0

	max = getFloat("maximum mark for the " + prompt + " (or 0 if none)");
	if (max > 0) percent = getFloat("percentage for " + prompt);

}


/******************************************************************
 * getContribution -- get contribution of a particular component
 *                     to the total mark
 *
 * Parameters: max: the maximum mark for the component
 *             percent: the percentage the component is worth
 *             prompt: string saying what the component is
 *
 * Modifies: nothing
 *
 * Precondition: max, percent are non-negative
 *
 * Returns: the contribution of the component
 *******************************************************************/
float getContribution(float max, float percent, const string& prompt){
	if (max == 0) return 0.;
	return percent * getMark(prompt, max) / max;
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
