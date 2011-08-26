/*#HA*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * day_number.cpp -- program to compute day number of a date 
 *
 * Input: the day, month & year
 *
 * Output: the number of the day in the year
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/

#include <iostream>
#include<string>
using namespace std;

bool isLeapYear(int year);
void getDate(int& day, int& month, int& year);
string getNumericSuffix(int number);

/*#DA*/	
// Global constant arrays
const int daysInMonth[12] = {31,28,31,30,31,30,31,31,30,31,30,31};
const string monthName[12] = {"January", "February", "March", "April",
							"May", "June", "July", "August",
							"September", "October", "November" ,"December"
						};

/*#HA*/
/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: input, output streams
 *
 * Returns: 0
 *******************************************************************/
/*DA*/
int main(){
	int day;
	int month;
	int year;
	int dayNumber;

	getDate(day, month, year);

	dayNumber = day;

	for (int m = 1; m < month; m++)
		dayNumber += daysInMonth[m-1];
	
	if (isLeapYear(year) && dayNumber > 59)
		dayNumber++;

	cout << monthName[month - 1];
	cout << ' ' << day << getNumericSuffix(day) << " is the " ;
	cout << dayNumber << getNumericSuffix(dayNumber) << " day of " << year << ".\n";
	return 0;
}
/*#HA*/

bool isLeapYear(int year){
	return year % 4 == 0 && (year % 100 > 0 || year % 400 == 0);
}

void getDate(int& day, int& month, int& year) {
	bool invalid = true;  // to get the loop started

	while (invalid) {
		cout << "Please enter a date after 1600 AD in the format dd mm yyyy: ";
		cin >> day;
		cin >> month;
		cin >> year;
		invalid = year <= 1600 ||
			month < 1 || month > 12 ||
			day < 1 || day >  daysInMonth[month-1];
		if (year > 1600 && isLeapYear(year) && month == 2 && day == 29)
			invalid = false;

		if (invalid) cout << "\nDate is not valid. ";
	}
}


string getNumericSuffix(int number) {
	int remainder = number % 10;

	if (number > 10 && number < 20) return "th";
	if (remainder == 1) return "st";
	if (remainder == 2) return "nd";
	if (remainder == 3) return "rd";
	return "th";
}
 

