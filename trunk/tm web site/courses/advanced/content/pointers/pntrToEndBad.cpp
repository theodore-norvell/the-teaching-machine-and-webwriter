#include<iostream>
using namespace std;

void toEnd(char* pString); // move pointer to end of string

void main() {
	char* pHi = "Hello world!\n";
	toEnd(pHi);		//Apology: pointer dereference fails
	pHi--;	// Back pointer up to the !
	pHi--;
	cout << pHi;
}
/*#TA*/
void toEnd(char* pString){
	while(*pString)
			pString++;
}/*#/TA*/
