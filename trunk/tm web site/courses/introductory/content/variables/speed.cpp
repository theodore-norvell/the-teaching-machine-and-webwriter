/*#HA*/ #include <iostream>
using namespace std;

double speed(double distance, double time);

int main(){
	cout << "Current world record 100 metre dash in 9.77 secs. is a speed of " << speed(100, 9.77);
	cout << " km/hr.\n";
	cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs. is a speed of " << speed(42195, 7495);
	cout << " km/hr.\n";
	return 0; 
}



/*#DA*/
/** speed *******************************************
*  
* @params: distance - the distance run in meters @pre: non-negative
*          time - time taken in secs @pre: > 0.0
*
* @returns: the speed in km/hr.
***********************************************************/
double speed(double distance, double time){
	distance = distance/1000;  // convert meters to kms.
	time = time / 3600;        // convert secs. to hrs.
	return distance/time;
}/*#H*/

        
