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

float getContribution(float max, float& percent, const string prompt);

float getFloat(string promptPart);

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

	float maxMTerm1 = 0;
	float maxMTerm2 = 0;
	float maxLabs = 0;
	float maxAssigns = 0;
	float maxFinal = 0;

	float percentMTerm1 = 0;
	float percentMTerm2 = 0;
	float percentLabs = 0;
	float percentAssigns = 0;
	float percentFinal = 0;

	string mt1Prompt = "first midterm";
	string mt2Prompt = "second midterm";
	string labPrompt = "labs";
	string asnPrompt = "assignments";
	string fnlPrompt = "final exam";


	float mark;		// The final mark for an individual student

	// First we get the data for the course
	setUpComponent(maxMTerm1, percentMTerm1, mt1Prompt);
	setUpComponent(maxMTerm2, percentMTerm2, mt2Prompt);
	setUpComponent(maxLabs, percentLabs, labPrompt);
	setUpComponent(maxAssigns, percentAssigns, asnPrompt);
	setUpComponent(maxFinal, percentFinal, fnlPrompt);


	cout << "Please enter the number of students in the course: ";
	cin >> numStudents;

 // enter mark for each component 
// compute course mark

	for( int student = 1; student <= numStudents; student++) {
		cout << "for student no. " << student << endl;

		mark = getContribution(maxMTerm1, percentMTerm1, mt1Prompt);
		mark += getContribution(maxMTerm2, percentMTerm2, mt2Prompt);
		mark += getContribution(maxLabs, percentLabs, labPrompt);
		mark += getContribution(maxAssigns, percentAssigns, asnPrompt);
		mark += getContribution(maxFinal, percentFinal, fnlPrompt);

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
