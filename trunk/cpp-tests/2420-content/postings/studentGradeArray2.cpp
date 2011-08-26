/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * gradeEntry.cpp -- enter the grades for a course.
  *                   in a course.
 *
 * Input: number of students in the course
 *        maxes mark and percentage of total value for
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
 * Date: March 19, 2005
 *
 *******************************************************************/

#include <string>
#include <iostream>
using namespace std;

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: cin and cout
 *
 * Returns: 0
 *******************************************************************/

int getComponents(string names[]);	// get the names of the course components
void getMaxes (string names[], double maxes[], int size);
void getPercents(string names[], double maxes[], double percents[], int size);
double getMark(double maxes[], double percents[], string names[], int size);

const int MAX_COMPONENTS = 12;

int main(){
	int students;					// Total no. of students in a course
	int components;					// actual no. of components to course

	double maxes[MAX_COMPONENTS];		// array of maximum score for each course component
	double percents[MAX_COMPONENTS];	// array of weight for each course component
	string names[MAX_COMPONENTS];	// array of names for each course component

	components = getComponents(names);
	getMaxes (names, maxes, components);
	getPercents (names, maxes, percents, components);


	cout << "\nPlease enter the number of students in the course: ";
	cin >> students;

	cout << "\nNow, computing the final score for the class...";

	double mark;	// place to hold computed mark
	for (int i=0; i < students; i++) {
		cout << "\n\n*** For student " << i << " ***";
		mark = getMark(maxes, percents, names, components);
		cout << "\nThe final score for student " << i << " is " << mark << endl;
	}

	return 0;
}


// Function implementations

/******************************************************************
 * getComponents -- 
 *
 * Parameters: names - an empty array of strings 
 *
 * Modifies: cout, cin, names
 *
 * Precondition: names contains enough room for all the components
 *
 * Returns: the name for each component
 *******************************************************************/

int getComponents(string names[]){
	int count = 0;

	cout << "Please enter the name of the components, one per line, starting with the first: ";
	getline(cin, names[0]);
	count++;
	for(int i = 1; i < MAX_COMPONENTS; i++){
		cout << "Next component (or return to end): ";

		getline(cin, names[i]);
		if (names[i].length() < 1) return count;
		count++;
	}
	cout << "Can't hold any more components!\n";
	return count;
}


/******************************************************************
 * getMaxes -- 
 *
 * Parameters: names - an array of strings specifying the components for
 *                      which each max is being sought 
 *
 * Modifies: cout, cin, maxes
 *
 * Precondition: names contains the correct prompts for each component
 *
 * Returns: the maximum mark for each component asspecified by its prompt
 *******************************************************************/

void getMaxes (string names[], double maxes[], int size) {
	for (int i=0; i < size; i++) {
		cout << "\nPlease input the maximum mark for the " << names[i];
		cout << " : ";
		cin >> maxes[i];
	}
}

/******************************************************************
 * getMPercent -- 
 *
 * Parameters: prompt - a string specifying for which component of
 *                      of the mark the percentage is being sought 
 *
 * Modifies: cout, cin, percents
 *
 * Precondition: none
 *
 * Returns: the percentage of the total grade assigned to the component
 *             specified by names
 *******************************************************************/

void getPercents(string names[], double maxes[], double percents[], int size){

	double leftover = 100.0;
	for (int i=0; i < size - 1; i++) {
		cout << "\nPlease input the percentage for the " << names[i];
		cout << ": ";
		cin >> percents[i];
		leftover -= percents[i];
		}
	percents[size - 1] = leftover;
	cout << "The calculated percent for the " << names[size - 1]
			<< " is " << leftover << endl;

}

/******************************************************************
 * getMark -- 
 *
 * Parameters: maxes - the maximum mark permitted for each component
 *             percents - the percentages of the total grade assigned to each component
 *             names - strings specifying the names of each component                     
 *
 * Modifies: cout, cin
 *
 * Precondition: 0 < maxes <= 100, 0. < percents <= 100.
 *
 * Returns: the final grade
 *******************************************************************/

double getMark(double maxes[], double percents[], string names[], int size){

	double courseMark = 0;			//variable to hold the final score
	double mark;						//mark for each course component

	for (int i=0; i < size; i++) {

		cout << "\nPlease input the mark for the " << names[i];
		cout << ": ";
		cin >> mark;

		while (mark > maxes[i] || mark < 0){ // while mark is out of range
			cout << "\nMark must be between 0 and " << maxes[i];
		// note this chunk of code repeats the chunk above - what's a better way?
			cout << "\nPlease input the mark for the " << names[i];
			cout << ": ";
			cin >> mark;
		}
		courseMark += (double) mark / (double) maxes[i] *  percents[i];
	}
	// Now round to 1 decimal point
	courseMark *=10;
	courseMark = (int)(courseMark + 0.5); // round 10 x mark to integer
	courseMark /= 10.0;		// and divide by 10 to get 1 decimal place
	return courseMark;
}
