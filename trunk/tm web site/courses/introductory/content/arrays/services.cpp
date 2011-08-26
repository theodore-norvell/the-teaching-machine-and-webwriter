#include <iostream>
#include <string>
#include <fstream>
#include "services.h"
using namespace std;

void loadFromFile(float theArray[], int& size) {
	string fileName;
	ifstream theData;
	int maxSize = size;

	cout << "Please input the name of the data file: ";
	cin >> fileName;

	theData.open(fileName.c_str());
	theData >> size;	// Read in the actual size
	if (size > maxSize) {
		cout << "I'm sorry. The file contains more than " << maxSize << " items" << endl;
		exit(-1);
	}

	for (int i = 0; i < size; i++) {
		theData >> theArray[i];
		if (theData.eof()) {
			cout << "I'm sorry. The file contains less than the " << size << " items it promised." << endl;
			exit(-1);
		}
			
	}
}

void sort(float theArray[], int n){
	for (int j = 0; j < n-1; j++) {
		for (int i = j+1; i < n; i++) {
			if (theArray[j] > theArray[i])
				swap(theArray[j], theArray[i]);
		}
	}
}

void swap(float& arg1, float& arg2){
	float hold;
	hold = arg1;
	arg1 = arg2;
	arg2 = hold;
}
