/*#HA*/ /*#HB*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * A demonstration of searching. 
 *
 * Input: none
 *
 * Output: the average
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
using namespace std;

/*#DA*/	/*#DB*/
int getPositionOf(double data[], int size, int pStart, double value);
int howManyLargest(double data[], int size);
int getLargest(double data[], int size);
/*#HA*/ /*#HB*/
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
	scores[8] = 141.7;;
	scores[9] = 137.4;

	int p = getLargest(scores, SIZE);

	cout << "The winner of the Olympic figure skating pairs short program"<< endl;
	cout << " is pair number " <<  p << endl;
	int c = howManyLargest(scores, SIZE);
	if (c > 1)
		cout << "There actually is a tie. " << c << " pairs got a score of " << scores[p] << endl;

	return 0;
}

/*#DA*/	/*#DB*/
/** howManyLargest *************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: number of times the largest value occurs
 *******************************************************/
int howManyLargest(double data[], int size){
	int count = 0;
	int position = getLargest( data, size);
	double largest = data[position];
	while (position > -1) { // while there is still data to search
		count++;
		position = getPositionOf(data, size, position + 1, largest);
	}
	return count;
}

/** getPositionOf ***************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *           pStart - the position from which to start the
 *                      search @pre non-negative
 *           value -  the value of the double whose
 *                      position is being sought
 *
 * Modifies: nothing
 *
 * @returns: position of the first occurrence of value
 *                at or after pStart
 *******************************************************/
int getPositionOf(double data[], int size, int pStart, double value){
	for (int i = pStart; i < size; i++)
		if (data[i] == value)
			return i;
	return -1;
}
/*#HA*/
/** getLargest ******************************************
 *
 *  @params: data - an array of doubles
 *           size - number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: position of the first occurrence of the
 *                largest value at or after pStart
 ********************************************************/

int getLargest(double data[], int size){
	int position = 0;
	for (int i = 1; i < size; i++)
		if (data[i] > data[position])
			position = i;
	return position;
}

