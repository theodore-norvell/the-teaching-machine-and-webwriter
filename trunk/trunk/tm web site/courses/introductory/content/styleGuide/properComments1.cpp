/*#DA*/ /*** assign1.cpp ************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * Assignment 1
 * 
 * Author: Jane Student
 * Student #: 200700111
 * Engineering login name: jstudent
 * Lab section: 4
 * Due date: 2008.01.21
 * 
 * Description: four simple functions as defined in the assignment statement. 
 * ***********************************************************************/
/*#HA*/
#include <iostream>
using namespace std;

int main(){
	cout << "Current world record 100 metre dash in 9.77 secs. " ;
    cout << << "is a speed of " << speed(100, 9.77) << " km/hr.\n";
	cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs.";
	cout <<  "is a speed of " << speed(42195, 7495) << " km/hr.\n";
    
    return 0;
}

/*#DB*/
/** speed *********************************************************
*  
* @params: distance - the distance run in meters @pre: non-negative
*          time - time taken in secs @pre: > 0.0
*
* @returns: the speed in km/hr.
*******************************************************************/

double speed(double distance, double time){
	distance = distance/1000;  // convert meters to kms.
	time = time / 3600;        // convert secs. to hrs.
	return distance/time;
}/*#H*/

