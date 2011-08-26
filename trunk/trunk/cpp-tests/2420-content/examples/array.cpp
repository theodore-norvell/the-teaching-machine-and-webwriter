#include <iostream>
#include <math.h>
using namespace std;

/************************************
      an Array Demo
*************************************/

void fillUp(double anArray[], int n);
double average(double anArray[], int n);

void main(){
//	const int SIZE = 10;
	// create an actual array
	double theArray[10];

	fillUp(theArray,10);
	cout << "The average of the array is ";
	cout << average(theArray, 10) << endl;
}

// fill up an array of size n with doubles
void fillUp(double anArray[], int n){
	for (int i = 0; i < n; i++)
		anArray[i] = sin(i/10.0);
}

// return the average of an array of n doubles
double average(double anArray[], int n){
	double sum = 0.;
	for (int i = 0; i < n; i++)
		sum += anArray[i];
	return sum/n;
}
