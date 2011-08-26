/*#HA*/ /*#HB*/ /***************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * A demonstration of searching. 
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 ******************************************/

#include <iostream>
using namespace std;

int getLargest(double data[], int size);
const int SIZE = 10;

int main(){
	double scores[SIZE];
	scores[0] = 111.8;
	scores[1] = 134.2;
	scores[2] = 123.6;;
	scores[3] = 141.7;
	scores[4] = 119.6;
	scores[5] = 139.1;
	scores[6] = 127.6;;
	scores[7] = 132.0;
	scores[8] = 134.6;;
	scores[9] = 137.4;


	cout << "The winner of the Olympic figure skating pairs short program"<< endl;
	cout << " is pair number " << getLargest(scores, SIZE) << endl;

	return 0;
}

/*#DA*/	
int getPositionOf(double data[]. int size, double value, int pStart);
int getLargest(double data[], int size);


/** howManyLargest **************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: the number of times the largest value
 *             occurs in data
 *******************************************************/
int howManyLargest(double data[]. int size){
	int position = getLargest( data, size);
	int count = 1;	// There has to be at least one largest value
	double largest = data[position];
	while (position > -1) { // while there is still data to search
		position = getPositionOf(data, size, posotion + 1, largest);
		if (position > -1) count++;
	}
	return count;
}

/** getPositionOf **************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *           pStart - the position from which to start
 *                       the search @pre non-negative
 *           value -  the value of the double whose
 *                          position is being sought
 *
 * Modifies: nothing
 *
 * @returns: the position of the first occurrence of
 *                 value at or after pStart
 *******************************************************/
int getPositionOf(double data[]. int size, double value, int pStart){
	int position = pStart;
	for (int i = pStart + 1; i < size; i++)
		if (data[i] == value)
			return position;
	return -1;
}

/** getLargest *****************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: the position of the first occurrence of the
 *                largest value at or after pStart
 *******************************************************/

int getLargest(double data[], int size){
	int position = 0;
	for (int i = 1; i < size; i++)
		if (data[i] > data[position])
			position = i;
	return position;
}

