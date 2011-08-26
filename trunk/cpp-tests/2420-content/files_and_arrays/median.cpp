#include <iostream>
using namespace std;
#include "services.h"

const int MAX_SIZE = 100;

int main(){
	float values[MAX_SIZE];
	int size = MAX_SIZE;		// actual array size
	float median;

	loadFromFile(values, size);
	sort(values, size);

	cout << "Here are the sorted values:" << endl;
	for (int i = 0; i < size; i++)
		cout << values[i] << endl;


	if (size % 2 == 0)
		median = (values[size/2-1] + values[size/2])/2;
	else
		median = values[size/2];

	cout << "The median was " << median << endl;
	return 0;
}