/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * gradeEntry.cpp -- enter the grades for a course.
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
 * Date: feb. 9, 2005
 *
 *******************************************************************/

#include <iostream>
#include <string>
using namespace std;



/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: cin and cout
 *
 * Returns: 0
 *******************************************************************/

int getMax(string prompt);
double getPercent(int max, string prompt);
double getMark(int max, double percent, string prompt);


int main(){
	int students;  // Total no. of students in a course
	double courseMark; // The mark for the entire course

	// Course data max for each component (0 if no component)
	int midterm1Max;
	int midterm2Max;
	int assignsMax;
	int labsMax;
	int finalMax;

// percentage for defined componets
	double midterm1Percent;
	double midterm2Percent;
	double assignsPercent;
	double labsPercent;
	double finalPercent;

	// First enter the data that characterizes the course

	cout << "Please enter the number of students in the course: ";
	cin >> students;

	midterm1Max = getMax("first midterm");
	midterm2Max = getMax("second midterm");
	assignsMax = getMax("assignments");
	labsMax = getMax("labs");
	finalMax = getMax("final");

	midterm1Percent = getPercent(midterm1Max, "first midterm");
	midterm2Percent = getPercent(midterm2Max, "second midterm");
	assignsPercent = getPercent(assignsMax, "assignments");
	labsPercent = getPercent(labsMax, "labs");
	finalPercent = getPercent(finalMax, "final");

	// The main loop - for each student in the class

	for (int s = 0; s < students; s++) {
		/* get the mark for each component, divide the mark by the max
		multiply by the percent and add to the total
		*/
		courseMark = getMark(midterm1Max, midterm1Percent, "first midterm");
		courseMark += getMark(midterm2Max, midterm2Percent, "second midterm");
		courseMark += getMark(assignsMax, assignsPercent, "assignments");
		courseMark += getMark(labsMax, labsPercent, "labs");
		courseMark += getMark(finalMax, finalPercent, "final");
		cout << "Course mark is " << courseMark << endl;

	}

	return 0;
}


// Function implementations

/******************************************************************
 * getMax -- 
 *
 * Parameters: prompt - a string specifying for which component of
 *                      of the mark the max is being sought 
 *
 * Modifies: cout, cin
 *
 * Precondition: none
 *
 * Returns: the maximum mark for the component specified by prompt
 *******************************************************************/

int getMax(string prompt){
	int maximum;
	cout << "Please input the maximum mark for the " << prompt;
	cout << " (or 0 if there is none): ";
	cin >> maximum;
	return maximum;
}

/******************************************************************
 * getMPercent -- 
 *
 * Parameters: prompt - a string specifying for which component of
 *                      of the mark the percentage is being sought 
 *
 * Modifies: cout, cin
 *
 * Precondition: none
 *
 * Returns: the percentage of the total grade assigned to the component
 *             specified by prompt
 *******************************************************************/

double getPercent(int max, string prompt){
	double percent = 0.;
	if (max > 0) {
		cout << "Please input the percentage for the " << prompt;
		cout << ": ";
		cin >> percent;
	}
	return percent;
}

/******************************************************************
 * getMark -- 
 *
 * Parameters: max - the maximum mark permitted for the specified component
 *             percent - the percentage of the total grade assigned to the component
 *             prompt - a string specifying the grade component being calculated                      
 *
 * Modifies: cout, cin
 *
 * Precondition: 0 < max <= 100, 0. < percent <= 100.
 *
 * Returns: the contribution of the mark to the total grade
 *******************************************************************/


double getMark(int max, double percent, string prompt){
	double mark = 0.;
	// Get the mark
	cout << "Please input the mark for the " << prompt;
	cout << ": ";
	cin >> mark;

	while (mark > max || mark < 0){ // while mark is out of range
		cout << "Mark must be between 0 and " << max << endl;
	// note this chunk of code repeats the chunk above - what's a better way?
		cout << "Please input the mark for the " << prompt;
		cout << ": ";
		cin >> mark;
	}
	// return mark's contribution to the total grade
	return	mark * percent / max;
}

	/* A recursive alternative to the function above
double getMark(int max, double percent, string prompt){
	double mark = 0.;
	cout << "Please input the mark for the " << prompt;
	cout << ": ";
	cin >> mark;
	if (mark > max || mark < 0) {
		cout << "Mark must be between 0 and " << max << endl;
		mark = getMark(max, percent, prompt);
		}
	else
		return mark * percent / max;
}
*/		




