/*#HB*/#include <iostream>
using namespace std;

/**************** functions ********************

  A program to find the minimum of a pair
  of integers.

************************************************/

// function DECLARATION which forms an INTERFACE
int minimum(int a1, int a2);

int main(){
	int first, second;

	cout << "Input the first number: ";
	cin >> first;
	cout << "  & the second one: ";
	cin >> second;

	cout << "\nThe minimum of the two numbers is ";
	cout << minimum(first,second) << '\n';
	return 0;
}/*#HA*/
/*#DB*/
// function DEFINITION (its IMPLEMENTATION)
int minimum(int a1, int a2){
	if (a1 < a2)
		return a1;
	else
		return a2;
}
/*#HB*/
