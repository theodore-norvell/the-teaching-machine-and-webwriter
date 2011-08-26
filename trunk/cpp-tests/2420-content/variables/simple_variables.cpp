/*******  Simple Variables Demonstration ********
    To demonstrate on the teaching machine the four
    fundamental attributes of simple C variables
    name, type, value, location

*******************************************/

#include <iostream>
using namespace std;

int main(){
	int i = 110;
	char letter;
	bool flag;
	double pi;
    
	letter = 'n';
	flag = false;
	pi = 3.14159;
    
    
	cout << "i = " << i << '\n';
	cout << "flag = " << flag << '\n';
	cout << "letter = " << letter << '\n';
	cout << "pi = " << pi << '\n';
	return 0;
}
        
