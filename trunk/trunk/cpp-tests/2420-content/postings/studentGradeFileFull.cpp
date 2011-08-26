/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * studentGradeFileArray.cpp -- Fairly complete version 
 *                   of our grading program
 *
 * Input: from standard file called "course.txt"
 *		  - number of students & number of components in the course
 *        - names of each component
 *		  - maximum mark for each component
 *		  - percentage mark for each component
 *        - for each student in the course, a line
 *				- name and id
 *				- marks for each component
 *
 * Output: a file called "results.txt" containing
 *			- a header row
 *			- a maximum mark row
 *			- a percentage row
 *			- a row for each student with name, id, component marks
 *					 and course mark
 *			- course statistics
 *
 * Author: Michael Bruce-Lockhart
 *
 * Date: April 2, 2005
 *
 *******************************************************************/

#include <string>
#include <iostream>
#include <fstream>

using namespace std;

/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: cin and cout and "results.txt"
 *
 * Returns: 0
 *******************************************************************/

void getComponents(string names[], ifstream& inFile, int number);	// get the names of the course components
void getMaxes (string names[], double maxes[], int size, ifstream& inFile);
void getPercents(string names[], double maxes[], double percents[], int size, ifstream& inFile);
double getMark(double maxes[], double percents[], string names[], int size, ifstream& inFile, ofstream& outFile);
void accumulateLetterGrades(int lGrades[], double mark);	// Add count to letter grade depending on mark

const int MAX_COMPONENTS = 12;
const int LETTER_GRADES = 5;

int main(){
	int students;					// Total no. of students in a course
	int numComponents;				// actual no. of numComponents to course

	string names[MAX_COMPONENTS];	// array of names for each course component
	double maxes[MAX_COMPONENTS];		// array of maximum score for each course component
	double percents[MAX_COMPONENTS];	// array of weight for each course component
	int letterGrades[LETTER_GRADES] =	// Keeps track of number of F's, D'd, .. A's
		{0,0,0,0,0};

	ifstream inData;
	ofstream outData;

	int i;		// counting variable used in multiple for loops

// try to open the data file
	inData.open("coursesFull.txt");
	if (inData.fail()) {
		cout << "Can't open file courses.txt!\n";
		return -1;		// tell operating system its an error
	}

// try to open an output file
	outData.open("results.txt");
	if (outData.fail()) {
		cout << "Can't create file results.txt!\n";
		return -1;		// tell operating system its an error
	}

// If we get here, the files have been successfully opened
	cout << "Fetching the number of students in the course: ";
	inData >> students;
	if (inData.fail()) {
		cout << "Can't find the number of students!\n";
		return -1;		// tell operating system its an error
	}

	cout << "Fetching the number of components in the course: ";
	inData >> numComponents;
	if (inData.fail()) {
		cout << "Can't find the number of components!\n";
		return -1;		// tell operating system its an error
	}

	getComponents(names, inData, numComponents);
	getMaxes (names, maxes, numComponents, inData);
	getPercents (names, maxes, percents, numComponents, inData);

// Output headings on one line
	outData << "Student name\tid\t";
	for (i = 0; i < numComponents; i++){
		outData << names[i] << '\t';
	}
	outData << "overall mark\n";

// maxima on one line
	outData << "\tmaximum\t\t";
	for (i = 0; i < numComponents; i++)
		outData << maxes[i] << '\t';
	outData << endl;

// percentages on one line
	outData << "\tpercentage\t\t";
	for (i = 0; i < numComponents; i++)
		outData << percents[i] << '\t';
	outData << endl;

	double mark;	// place to hold computed mark
	double sum = 0;	// sum of all class marks
	string studentName;
	string studentId;

	for (i = 0; i < students; i++) {	// for each student
	// pass name and id through to the output file
		getline(inData, studentName, '\t');
		getline(inData, studentId, '\t');
		outData << studentName << '\t' << studentId << '\t';

	// get the data, compute final mark and report progress to standard out
		mark = getMark(maxes, percents, names, numComponents, inData, outData);
		cout << "\nThe final score for student " << studentName << " is " << mark << endl;
		outData << mark << endl;

	// compute class statistics
		sum += mark;
		accumulateLetterGrades(letterGrades,mark);
	}

// Finish up by reporting class statistics and closing files
	inData.close();
	outData << "The class average is " << sum / students << endl;
	outData << "F\tD\tC\tB\tA" << endl;
	for (i = 0; i < LETTER_GRADES; i++)
		outData << letterGrades[i] << '\t';
	outData << endl;
	outData.close();

	return 0;
}


// Function implementations

/******************************************************************
 * getComponents -- 
 *
 * Parameters: names - an empty array of strings
 *				inFile - the file containing the components
 *				number - the number of components
 *
 * Modifies: inFile stream, names
 *
 * Preconditions: names contains enough room for all the number,
 *					components are tab separated
 *
 * Returns: the name for each component
 *******************************************************************/

void getComponents(string names[], ifstream& inFile, int number){
	int i;		// we need i after the loop
	for(i = 0; i < number - 1; i++)
		getline(inFile, names[i], '\t');
	getline(inFile, names[i]);	// newline after last component name
}


/******************************************************************
 * getMaxes -- 
 *
 * Parameters: names - an array of strings specifying the components for
 *                      which each max is being sought 
 *
 * Modifies: inFile stream, maxes
 *
 * Precondition: names contains the correct prompts for each component
 *					there is room in maxes for all maximum marks
 *
 * Returns: the maximum mark for each component as specified by its prompt
 *******************************************************************/

void getMaxes (string names[], double maxes[], int size, ifstream& inFile) {
	for (int i=0; i < size; i++) {
		inFile >> maxes[i];
	}
}

/******************************************************************
 * getMPercent -- 
 *
 * Parameters: prompt - a string specifying for which component of
 *                      of the mark the percentage is being sought 
 *
 * Modifies: inFile stream, percents
 *
 * Precondition: names contains the correct prompts for each component
 *					there is room in percents for all percentages
 *
 * Returns: the percentage of the total grade assigned to the component
 *             specified by names
 *******************************************************************/

void getPercents(string names[], double maxes[], double percents[], int size, ifstream& inFile){

	double leftover = 100.0;
	for (int i=0; i < size - 1; i++) {
		inFile >> percents[i];
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
 * Modifies: inFile and outFile streams
 *
 * Precondition: 0 < maxes <= 100, 0. < percents <= 100.
 *
 * Returns: the final grade
 *******************************************************************/

double getMark(double maxes[], double percents[], string names[], int size, ifstream& inFile, ofstream& outFile){

	double courseMark = 0;			//variable to hold the final score
	double mark;						//mark for each course component

	for (int i=0; i < size; i++) {
		inFile >> mark;
		outFile << mark << '\t';
		courseMark += (double) mark / (double) maxes[i] *  percents[i];
	}

	// Now round to 1 decimal point
	courseMark *=10;
	courseMark = (int)(courseMark + 0.5); // round 10 x mark to integer
	courseMark /= 10.0;		// and divide by 10 to get 1 decimal place
	return courseMark;
}

/******************************************************************
 * accumulateLetterGrades -- 
 *
 * Parameters: lGrades - counter for each letter grade from 'F' to 'A'
 *             mark - the mark used to increment 1 letter grade count                     
 *
 * Modifies: lGrades
 *
 * Precondition: 0 <= mark <= 100
 *
 * Returns: updated lGrades array
 *******************************************************************/
void accumulateLetterGrades(int lGrades[], double mark){
	if (mark >= 79.5)
		lGrades[4]++;
	else if (mark >= 64.5)
		lGrades[3]++;
	else if (mark >= 54.5)
		lGrades[2]++;
	else if (mark >= 49.5)
		lGrades[1]++;
	else
		lGrades[0]++;
}
