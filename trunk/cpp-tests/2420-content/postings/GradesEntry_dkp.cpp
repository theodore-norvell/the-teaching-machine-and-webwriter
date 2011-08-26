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
#include <string>
using namespace std;

double inputDouble(double min, double max, string prompt);
double finalMark(double aM, double t1M, double t2M, double lM, double eM, 
				 double aP, double t1P, double t2P, double lP, double eP,
				 double a, double t1, double t2, double l, double e);

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
	double assgtsMax, test1Max, 
		test2Max, labMax, finalMax; // Max scores for grade components
	double assgtsPer, test1Per, 
		test2Per, labPer, finalPer; // Percent of final grade for component
	double assgts, test1, test2, lab, exam; // student grade components
	double finalGrade; // student final grade
	int numStudents; // total number of students in the class

//    output some instructions
//	Input percentage and maximum grade for assgts, tests, labs and final
	assgtsMax = inputDouble(0, 100, "maximum assignments score");
	test1Max = inputDouble(0, 100, "maximum test 1 score");
	test2Max = inputDouble(0, 100, "maximum test 2 score");
	labMax = inputDouble(0, 100, "maximum lab score");
	finalMax = inputDouble(0, 100, "maximum exam score");
	assgtsPer = inputDouble(0, 100, "percent of final grade for assignments");
	test1Per = inputDouble(0, 100, "percent of final grade for test 1");
	test2Per = inputDouble(0, 100, "percent of final grade for test 2");
	labPer = inputDouble(0, 100, "percent of final grade for lab");
	finalPer = inputDouble(0, 100, "percent of final grade for exam");
	cout << "Please input the number of students in the class: ";
	cin >> numStudents;
	
	for (int stud = 1; stud <= numStudents; stud++) {
		assgts = inputDouble(0, assgtsMax, "grade for assignments"); //		input assgts mark
		test1 = inputDouble(0, test1Max, "grade for Test 1"); //		input test1 mark
		test2 = inputDouble(0, test2Max, "grade for Test 2"); //		input test2 mark
		lab = inputDouble(0, labMax, "grade for labs");//		input lab mark
		exam = inputDouble(0, finalMax, "grade for exam");//		input final exam mark
		finalGrade = finalMark(assgtsMax, test1Max, test2Max, labMax, finalMax,
								assgtsPer, test1Per, test2Per, labPer, finalPer,
								assgts, test1, test2, lab, exam); //		calculate final mark.
		cout << "The final grade is:" << finalGrade << endl; //		output final mark.
	} //	end for

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
double inputDouble(double min, double max, string prompt) {
	double value = 0.; 

	if (max > min) { 
		cout << "Please enter the " << prompt << "(between " << min << " and " << max << "):";
		cin >> value;
		while (value < 0 || value > max) {
			cout << value << " is out of range." << endl;
			cout << "Please re-enter the " << prompt << "(between " << min << " and " << max << "):";
			cin >> value;
		}
	}	
	return value;
}

/***************************************************************************
 * finalMark -- calculate the final mark
 *
 * Parameters: 
 *
 * returns: the computed final grade
 ***************************************************************************/
double finalMark(double aM, double t1M, double t2M, double lM, double eM, 
				 double aP, double t1P, double t2P, double lP, double eP,
				 double a, double t1, double t2, double l, double e) {
	double grade = 0.0;
	// Illustrate three styles of if -- you should choose one and use it consistently.
	if (aM != 0) {
		grade += a/aM * aP;
	}
	if (t1M !=0) 
		grade += t1/t1M * t1P;
	if (t2M != 0) grade += t2/t2M * t2P;
	grade += (lM != 0) ? l/lM * lP : 0.; // conditional expression (not on exam)
	// My preferred style
	if (eM != 0) {
		grade += e/eM * eP;
	}
	return grade;
}
