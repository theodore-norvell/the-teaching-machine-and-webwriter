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
 * Author: Cheng Li
 *
 * Date: March 19, 2005
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

void getMax (string name[], int max[], int number);
int getComponents(string name[]);	// Get the names of the various components of thecourse
void getPercent(string name[], int max[], int percent[], int number);
void getMark(int max[], int percent[], string name[], int number, int student);

const int MAX_COMPONENTS = 12;

int main(){
	int students;					// Total no. of students in a course
	int max[MAX_COMPONENTS];		// array of maximum score for each course component
	int percent[MAX_COMPONENTS];	// array of weight for each course component
	string name[MAX_COMPONENTS];	// array of name for each course component
	int components;					// the actual no of components in the course

	components = getComponents(name);	// Get the names of the various components of thecourse
	getMax (name, max, components);
	getPercent (name, max, percent, components);

	cout << "\nPlease enter the number of students in the course: ";
	cin >> students;


	cout << "\nNow, computing the final score for the class...";
	for (int i=0; i<students; i++) {
		cout << "\n\n*** For student " << i << " ***";
		getMark(max, percent, name, components, i);
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

void getMax (string name[], int max[], int number) {
	for (int i=0; i < number; i++) {
		cout << "\nPlease input the maximum mark for the " << name[i];
		cout << " (or 0 if there is none): ";
		cin >> max[i];
	}
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

void getPercent(string name[], int max[], int percent[], int number){

	bool flag = false;
	int total = 0;
	int i = 0;

	while (!flag) {
		for (i=0; i < number; i++) {
			if (max[i] > 0) {
				cout << "\nPlease input the percentage for the " << name[i];
				cout << ": ";
				cin >> percent[i];
			} else {
				percent[i] = 0;
			}
		}

		total = 0;
		for (i=0; i<MAX_COMPONENTS; i++) {
			total += percent[i];
		}

		if (total == 100) {
			flag = true;
		} else {
			flag = false;
			cout << "\nTotal weight should be 100, not valid! Please enter again!";
		}
	}
}

/******************************************************************
 * getComponents -- 
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

int getComponents(string name[]){	// Get the names of the various components of the course
	int count = 0;
	for (int i = 0; i < MAX_COMPONENTS; i++){
		cout << "Please enter the name of the next component (or \"\" to end): ";
		cin >> name[i];
		if (name[i] == "")
			return count;
		count++;
	}
	return count;
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

void getMark(int max[], int percent[], string name[], int number, int student){

	double courseMark = 0;			//variable to hold the final score
	int mark;						//mark for each course component

	for (int i=0; i<number; i++) {

		if (max[i] > 0) {
			cout << "\nPlease input the mark for the " << name[i];
			cout << ": ";
			cin >> mark;

			while (mark > max[i] || mark < 0){ // while mark is out of range
				cout << "\nMark must be between 0 and " << max[i];
			// note this chunk of code repeats the chunk above - what's a better way?
				cout << "\nPlease input the mark for the " << name[i];
				cout << ": ";
				cin >> mark;
			}
			courseMark += (double) mark / (double) max[i] * (double) percent[i];
		}
	}
	cout << "\nThe final score for student " << student << " is " << courseMark << endl;
}
