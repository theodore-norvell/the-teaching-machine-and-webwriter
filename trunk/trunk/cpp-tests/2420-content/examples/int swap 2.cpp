#include <iostream>   // info from standard library
using namespace std;    // cout is in the std namespace

/*******  Pass-by-Reference Demo ********
    
	We look at a good swap function
	to see how C passes by reference

*******************************************/

void swap(int& a1, int& a2);

void main(){
	int a = 1;
	int b = 2;
	cout << "a: " << a << "  & b: " << b <<'\n';
	swap(a,b);
	cout << "a: " << a << "  & b: " << b <<'\n';
}





void swap(int& a1, int& a2){
	int temp = a1;
	a1 = a2;
	a2 = temp;
}
        
