#include <iostream>
using namespace std;
long strlen(char str[]);

int main(){
	char strArray[15]  = "Hello world!\n";
	cout << "The length of the string is " << strlen(strArray);
	return 0;
}

/*#TA*/long strlen(char str[]){
	long i=0;

	while (str[i++])
		;
	return (i - 1);
}/*#/TA*/
