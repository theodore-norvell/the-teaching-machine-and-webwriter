#include <iostream>
#include <string>
using namespace std;

int main(){
/*#TA*/	/*#TC*/string first, last, name;	// declare a pair of strings
/*#/TC*/	string greetings = "Hello";	// declare and initialize
/*#/TA*/
/*#TB*/	cout << "Here are the characters in our greeting: " << endl;
	for (int i = 0; i < greetings.length(); i++)
		cout << greetings.at(i) << ' ';
	cout << endl;/*#/TB*/
	
	cout << "The length of first is " << first.length() << endl;
/*#TC*/	first = "Michael";		// Assignment
	cout << "And now its length is "<< first.length() << endl;
	
	last = "Bruce-Lockhart";
	name = first + ' ' + last;
	cout << name << endl;/*#/TC*/
/*#TD*/	greetings += " world!";
	cout << greetings << endl;/*#/TD*/
	return 0;
}
