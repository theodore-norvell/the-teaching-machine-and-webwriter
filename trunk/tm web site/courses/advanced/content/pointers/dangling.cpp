#include <iostream>
using namespace std;

void moveToEnd(char*& pnt);		// Move pnt to null at end of string
void strCpy(char* pDest, const char* pSource);	// copy string at source to destination
char* errorReport(int e); 	//Build an errr report string for error e
int factorial(int i);

int main(){
	char* pError;
	int fact;
	pError = errorReport(3);	// here we set pointer to the error report
	fact = factorial(3);	// This is silly, it merely represents an interfering function
	cout << pError;			// Now we try to use the report
	return 0;
}

/*#TA*/ /* A function to report a single error line */

char* errorReport(int e) {
	char buffer[81];	// A place to put the string

	char* pBuff = buffer;  // A pointer to the string
	strCpy(buffer,"Error #"); // Start with "Error "
	moveToEnd(pBuff);
	// Do some other stuff here we don't need to worry about
	return buffer;
}
/*#/TA*/
 // Move pointer to end of the string
void moveToEnd(char*& pnt){
	while (*pnt)
		pnt++;
}

void strCpy(char* pDest, const char* pSource){
	while (*pSource)
		*pDest++ = *pSource++;
	*pDest = '\0';
}

// A simple factorial function
int factorial(int i){
	if (i < 2) return 1;
	return i * factorial(i-1); 
}
