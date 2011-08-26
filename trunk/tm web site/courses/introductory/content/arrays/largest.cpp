/*#HA*/  /***********************************
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
 ********************************************/

#include <iostream>
using namespace std;

int getLargest(double data[], int size);
void test1();
void test2();

int main(){
	test1();
	test2();
	return 0;
}

const int SEATS = 7;
void test1(){
	double seats[SEATS];
	seats[0] = 0.;
	seats[1] = 185.2;
	seats[2] = 171.1;;
	seats[3] = 193.4;
	seats[4] = 181.4;
	seats[5] = 174.8;
	seats[6] = 0.;
	int pos;

	cout << "Test 1:" << endl;
	cout << "The tallest student in the first row sits in seat #"; 
	pos = getLargest(seats, SEATS);
	cout << pos << endl;
	cout << "He is " << seats[pos] << " cm. tall." << endl;
}

const int SCORES = 10;
void test2(){
	int pos;
	double scores[SCORES];
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

	cout << "Test 2:" << endl;
	cout << "The winner of the Olympic figure skating pairs short program"<< endl;
	pos = getLargest(scores, SCORES); 
	cout << " is pair number " << pos << " with a score of " << scores[pos] << endl;
}

/*#DA*/	
/** getLargest ******************************************
 *
 *  @params: data - an array of doubles
 *           size: number of elements in the data array
 *
 * Modifies: nothing
 *
 * @returns: the position of the first occurrence of the
 *                  largest value
 ********************************************************/

int getLargest(double data[], int size){
	int position = 0;
	for (int i = 1; i < size; i++)
		if (data[i] > data[position])
			position = i;
	return position;
}

