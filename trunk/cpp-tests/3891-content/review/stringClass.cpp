//! Run.
/*#H*/#include <iostream>
#include <string>
using namespace std;

int main(){
/*#DA*/	/*#DC*/string first, last, name;	// declare a pair of strings
/*#HC*/	string greetings = "Hello";	// declare and initialize
/*#HA*/
/*#DB*/	cout << "Here are the characters in our greeting: " << endl;
	for (int i = 0; i < greetings.length(); i++)
		cout << greetings.at(i) << ' ';
	cout << endl;/*#HB*/
	
	cout << "The length of first is " << first.length() << endl;
/*#DC*/	first = "Michael";		// Assignment
	cout << "And now its length is "<< first.length() << endl;
	
	last = "Bruce-Lockhart";
	name = first + ' ' + last;
	cout << name << endl;/*#HC*/
/*#DD*/	greetings += " world!";
	cout << greetings << endl;/*#HD*/
	return 0;
}
