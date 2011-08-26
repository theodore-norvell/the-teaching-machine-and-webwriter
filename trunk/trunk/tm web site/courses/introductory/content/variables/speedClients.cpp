/*#H*/ #include <iostream>
using namespace std;

double speed(double distance, double time);

void constantClient();
void anxiousClient();
void independentClient();
void confusedClient();


/*#DM*/ int main(){
    // Call each of the clients
    constantClient();
    anxiousClient();
    independentClient();
    confusedClient();
	return 0; 
}/*#HM*/

/*#DB*/ /* constantClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function using literal
*               constants. 
*
* @returns: nothing
************************************************************/
void constantClient(){
    cout << "Current world record 100 metre dash in 9.77 secs. ";
    cout << "is a speed of " << speed(100, 9.77)<< " km/hr.\n";
    cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs. ";
    cout << "is a speed of " << speed(42195, 7495) << " km/hr.\n";
}/*#HB*/


/*#DC*/ /* anxiousClient ********************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have the same names as the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void anxiousClient(){
    double distance = 100.;  // 100m world record
    double time = 9.77;
    cout << "Current world record 100 metre dash in 9.77 secs. ";
    cout << "is a speed of " << speed(distance, time) << " km/hr.\n";
    distance = 42195;      // marathon record
    time = 7495;
    cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs. ";
    cout << "is a speed of " << speed(distance, time) << " km/hr.\n";
}/*#HC*/


/*#DD*/ /* independentClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have names different from the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void independentClient(){
    double length = 100.;  // 100m world record
    double elapsed = 9.77;
    cout << "Current world record 100 metre dash in 9.77 secs. ";
    cout << "is a speed of " << speed(length, elapsed) << " km/hr.\n";
    length = 42195;      // marathon record
    elapsed = 7495;
    cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs. ";
    cout << "is a speed of " << speed(length, elapsed) << " km/hr.\n";
}/*#HD*/

/*#DE*/ /* confusedClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have the same names as the  
*               function parameters, but in a different order. 
*
* @returns: nothing
************************************************************/
void confusedClient(){
    double distance = 100.;  // 100m world record
    double time = 9.77;
    cout << "Current world record 100 metre dash in 9.77 secs. ";
    cout << "is a speed of " << speed(time, distance) << " km/hr.\n";
    distance = 42195;      // marathon record
    time = 7495;
    cout << "Current world record marathon of 2 hrs, 4 mins, 55 secs. ";
    cout << "is a speed of " << speed(time, distance) << " km/hr.\n";
}/*#HE*/

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

        
