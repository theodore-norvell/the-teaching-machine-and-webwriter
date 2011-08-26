#include <iostream>
using namespace std;

/****************************************************************
					String Comparison Example
					Michael Bruce-Lockhart
					02/08/26
*****************************************************************/

int cmpStr (const char* s1, const char* s2);

void main(){
	char* pStr1="This";    // Creates 5 char array
	char str2[10];

	do {
		cin >> str2;
		cout << cmpStr(str2,pStr1) << ", ";
	} while (cmpStr(str2,"done"));
}

/*#TA*//* A string compare function which returns 0 if strings are the same and
	 +1 if first string > second string (comes later in a dictionary),
	  -1 if first string < second string */

int cmpStr(const char* pS1, const char* pS2){
	while (*pS1 ==*pS2 && *pS1) {pS1++; pS2++;}
	if (*pS1==*pS2) return 0 ;
	else if (*pS1>*pS2) return 1 ;
	else return -1 ;
}/*#/TA*/
