/*#HA*/
#include <iostream> 
using namespace std;

double quadratic (double x);

void anxiousClient();
void independentClient();

int main(){
    anxiousClient();
    independentClient();
	return 0;
}

/* anxiousClient ********************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have the same names as the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void anxiousClient(){
    double x = 2.5;
    double answer = quadratic(x);
    cout << "For x = " << x << " x^2 + 2x + 1 is ";
    cout << answer << "\n\n";
}

/* independentClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have names different from the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void independentClient(){
    double time = 14.1;
    double answer = quadratic(time);
    cout << "For time = " << time << " time^2 + 2time + 1 is ";
    cout << answer << "\n\n";
}





/*#DA*/
/** quadratic *******************************
*
* @params: x - any double
*
* @returns: x^2 + 2x + 1
*******************************************/ 

double quadratic (double x){
	return x*x + 2 * x + 1;
}
