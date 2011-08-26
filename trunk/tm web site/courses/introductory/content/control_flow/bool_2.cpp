/*#H*/ #include <iostream>
using namespace std;

/***** complex boolean expressions ********

  In this example we examine complex bool
  expressions. In particular we look at
  the SHORT-CIRCUIT property.

**********************************************/

bool checkRange(int number);
void printDecision(bool inRange);

int main(){
	cout << "The number 11 is ";
	printDecision(checkRange(11));
	cout << "The number 8 is ";
	printDecision(checkRange(8));
	cout << "The number 4 is ";
	printDecision(checkRange(4));
	return 0;
}

/** printDecision ***********************************
*
* @params: inRange - true if in range
*
* @returns: nothing
*****************************************************/
void printDecision(bool inRange){
	if (inRange)
		cout << "in";
	else
		cout << "out of";
	cout << " range." << endl;
}

/*#DA*/
/** checkRange ***************************************
*
* @params: number - any integer
*
* @returns: true if the integer is 4 or is on [7, 10]
*****************************************************/
bool checkRange(int number){
	return number == 4 || number >= 7 && number <= 10;
}

