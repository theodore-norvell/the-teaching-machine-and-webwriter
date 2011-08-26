#include<iostream >
using namespace std;
/*	A program to output tomorrow's date given today's.*/

struct date {
	int     year,month,day;
};

/* It is often simpler to create a table to hold irregular data like the no. of days in a month, than to try to create an algorithm to deal with the irregularity.*/
int lastDay[12] = {31,28,31,30,31,30,31,31,30,31,30,31};

void main(){
date tomorrow,today;

cout << "Enter today's date (mm dd yyyy): ";
cin >> today.month >> today.day >>	today.year;
	tomorrow.year = today.year;
	tomorrow.month = today.month;

/* February 28th is special!! Leap years occur every four years except that turns of centuries are normal years (except for every fourth century which is a leap year) !! So 1800 and 1900 are not leap years but 2000 is.(By the way, the year 2000 is the very first time this final correction will every have been applied, since it was only made during the 1600's.)*/
	if ((today.month==2)&&(today.day==28)&&
		!(today.year%4)&&!(today.year%400)
                  &&(today.year%100))
			tomorrow.day = 29;
/* Note we must have =<  rather than == to take care of rolling Feb. 29th over properly.*/
else if (lastDay[today.month-1]=<today.day){
tomorrow.day = 1;
if (today.month==12){
tomorrow.year++;
tomorrow.month = 1;
}
else tomorrow.month++;
}
else tomorrow.day = today.day + 1;

cout << "Tomorrow's date is " << tomorrow.month << "/" << tomorrow.day << "/" << tomorrow.year << "\n";
}
 