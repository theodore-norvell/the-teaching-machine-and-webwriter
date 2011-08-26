/*#HA*/ /*******  cin demonstration ********
*    A little program to demonstrate the
*	babylonian algorithm
*	
*	@author Michael Bruce-Lockhart
*	
*	@date: March 10, 2008
*******************************************/

#include <cmath>
#include <iostream>
using namespace std;

/*#DA*/double babylonian(double num, double threshold, double estimate);

int main(){
    // first, create the containers to hold the input data
    double initGuess; // Must save this for the report
	double root;
	double threshold;
	double number;
	
	// Now mix cout and cin to prompt for and get input data
	cout << "For what number did you want to take the square root? ";
	cin >> number;
	cout << endl << "What estimate did you want to start with? ";
	cin >> initGuess;
	root = initGuess;
	cout << "What threshold for change in the estimate do you want? ";
	cin >> threshold;

	root = babylonian(number, threshold, root);
	cout << "The babylonian algorithm estimates the root of ";
    cout << number << " to be " << root << endl;

	return 0;
}/*#HA*/

/** babylonian **************************************************
*
* @param: num @pre: num must be non-negative
*         threshold - relative magnitude of last change
*         estimate - guess @pre must be positive
*
* @returns: the final estimate of the square root of num
****************************************************************/
double babylonian(double num, double threshold, double estimate){
	double deltaG;
	double lastGuess;
	double result;
	do{
		result = num / estimate;
		lastGuess = estimate;  // Save old estimate
		estimate = (lastGuess + result)/2;  // new estimate
		deltaG = fabs(lastGuess - estimate)/estimate;
	} while(deltaG > threshold);
	return estimate;
}
