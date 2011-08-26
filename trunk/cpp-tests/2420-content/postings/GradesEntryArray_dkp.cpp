/**************************************************************************
 * Memorial University of Newfoundland
 * Structured Programing 2420
 * Program to calculate the course grade
 *
 * Author: Dennis Peters
 * Date: 2004.03.01
 *
 **************************************************************************/

#include <iostream>
#include <fstream>
#include <string>
using namespace std;

double inputDouble(double min, double max, const string& prompt);
double finalMark(double cMax[], double cPer[], double cGrade[], int numC);
int getComponentSetup(double cMax[], double cPer[], string cName[]);
string& stripLeadingWhitespace(string& s);

const int MAX_COMPONENTS = 20; // Maximum number of component
const int MAX_STUDENTS = 200;  // Maximum number of students in the class

/**************************************************************************
 * main -- top level of calculatign the mass of washers
 *
 * parameters: none 
 * modifies: cin 
 *           cout
 *
 * returns: 0
 **************************************************************************/
int main() {
    double componentMax[MAX_COMPONENTS]; // Max scores for grade components
    double componentPer[MAX_COMPONENTS]; // Percent of final grade for component
    string componentName[MAX_COMPONENTS]; // component name
    double componentMark[MAX_COMPONENTS]; // component grade

    double finalGrade[MAX_STUDENTS]; // student final grade
    string studentName[MAX_STUDENTS];
    int numStudents; // total number of students in the class
    int numComponents = 0;
    ifstream marksFile;

    numComponents = getComponentSetup(componentMax, componentPer, componentName);

    marksFile.open("marks.txt");
    marksFile >> numStudents; // First line is number of students to follow
	
    for (int stud = 0; stud < numStudents; stud++) {
        for (int i = 0; i < numComponents; i++) {
            marksFile >> componentMark[i];
        }
        getline(marksFile, studentName[stud]);
        stripLeadingWhitespace(studentName[stud]);
        finalGrade[stud] = finalMark(componentMax, componentPer, componentMark, 
                              numComponents);
    } //	end for
    cout << "Name,\t\tGrade" << endl;
    cout << "-------------- \t-----" << endl;
    for (int stud = 0; stud < numStudents; stud++) {
      cout << studentName[stud] << ",\t" << finalGrade[stud] << endl;
    }

    return 0;
}

/****************************************************************************
 * inputDouble - input double with range checking
 *
 * Parmaeters: min -- minimum value
 *             max -- maximum value
 *             prompt -- text of the prompt
 *
 * Modifies: cout -- prompt for the value
 *           cin -- reads value input by user
 *
 * returns: the value input
 ****************************************************************************/
double inputDouble(double min, double max, const string& prompt) {
	double value = 0.; 

	if (max > min) { 
		cout << "Please enter the " << prompt << " (between " << min << " and " << max << "): ";
		cin >> value;
		while (value < 0 || value > max) {
			cout << value << " is out of range." << endl;
			cout << "Please re-enter the " << prompt << " (between " << min << " and " << max << "): ";
			cin >> value;
		}
	}	
	return value;
}

/***************************************************************************
 * finalMark -- calculate the final mark
 *
 * Parameters: 
 *    cMax -- maximum grade for component
 *    cPer -- % of final grade for component
 *    cGrade -- grade for component
 *    numC -- number of components
 *
 * returns: the computed final grade
 ***************************************************************************/
double finalMark(double cMax[], double cPer[], double cGrade[], int numC) {
	double grade = 0.0;
    for (int i = 0; i < numC; i++) {
        if (cMax[i] != 0) {
            grade += cGrade[i]/cMax[i] * cPer[i];
        }
    }
    return grade;
}

/***************************************************************************
 * getComponentSetup -- get max grade and percentage for a grade component
 *
 * Parameters: max -- maximum score for component (pass out)
 *             percent -- percent of final score for this component (pass out)
 *             what -- component name for prompt
 *
 * returns: number of components read
 ***************************************************************************/
int getComponentSetup(double cMax[], double cPer[], string cName[]) {
	ifstream compFile;
	int numComp = 0;

	compFile.open("components.txt");
	if (compFile) {
		while (numComp < MAX_COMPONENTS && 
			   compFile >> cPer[numComp] >> cMax[numComp]) {
			getline(compFile, cName[numComp]);
                        stripLeadingWhitespace(cName[numComp]);
			// cout << "\"" << cName[numComp] << "\" Max = " << cMax[numComp];
			// cout << ", % = " << cPer[numComp] << endl; 
			numComp++;
		}
		compFile.close();
	} else { 
		cout << "Error opening components.txt." << endl;
	}
	return numComp;
}


/***************************************************************************
 * stripLeadingWhitespace -- strip leading whitespace from a string
 *
 * Parameters: s -- string to strip
 *
 * returns: s
 ***************************************************************************/
string& stripLeadingWhitespace(string& s) {
  while (s.length() > 0 && isspace(s[0])) {
    s.erase(0, 1);
  }
  return s;
}
