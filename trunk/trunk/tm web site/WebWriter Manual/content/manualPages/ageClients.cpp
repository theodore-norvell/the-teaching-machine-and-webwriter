#include <iostream>
using namespace std;

void createProblem(double george, double samantha, double moreYears);

void constantClient();
void anxiousClient();
void independentClient();
void mixedUpClient();

int main() {
// Call each of the clients
    constantClient();
    anxiousClient();
    independentClient();
    mixedUpClient();
	return 0;
}

/* constantClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function using literal
*               constants. 
*
* @returns: nothing
************************************************************/
void constantClient(){
     createProblem(12.0, 6.0, 6.0);  // George older than Samantha
     createProblem(6.0, 12.0, 6.0);  // George younger than Samantha
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
    double george = 10.0;
    double samantha = 5.0;
    double moreYears = 5.0;
    createProblem(george, samantha, moreYears);  // George older than Samantha
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
    double moi = 20.0;
    double toi = 12.0;
    double tomorrow = 4.0;    //moi older than toi
}

/* mixedUpClient ********************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have the same names as the  
*               function parameters, but in a different order. 
*
* @returns: nothing
************************************************************/
void mixedUpClient(){
    double george = 10.0;
    double samantha = 5.0;
    double moreYears = 5.0;
    createProblem(samantha, george, moreYears);  // George older than Samantha
}




/** createProblem *********************************************
*
* @params: george - his age @pre: > 0.0
*          samantha - her age @pre: > 0.0
*          moreYears - no. of years to advance  @pre: > 0.0
*
* @returns: nothing
*************************************************************/

void createProblem(double george, double samantha, double moreYears){
    cout << "George is " << george/samantha << " times as old as Samantha.\n";
    cout << "In " << moreYears << " years George will be " ;
    cout << (george + moreYears)/(samantha + moreYears) << " times as old as Samantha.\n";
    cout << "How old is Samantha?\n";
}
    
