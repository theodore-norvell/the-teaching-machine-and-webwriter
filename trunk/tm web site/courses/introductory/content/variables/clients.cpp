/*#H*/ /*******************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * clients.cpp -- Demonstrate function calling
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.04.22
 *
 ***************************************************************/
#include <iostream>
using namespace std;

int byTwo(int n);
void constantClient();
void anxiousClient();
void independentClient();

/*#DM*/ int main() {
// Call each of the clients
    constantClient();
    anxiousClient();
    independentClient();
	return 0;
}/*#HM*/

/*#DA*/ /* constantClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function using literal
*               constants. 
*
* @returns: nothing
************************************************************/
void constantClient(){
    cout << "increase\tby two\n";
    cout << 10 << "\t\t" << byTwo(10)<<'\n';
    cout << 7 << "\t\t" << byTwo(7)<<'\n';
    cout << -120 << "\t\t" << byTwo(-120)<<'\n';
}/*#HA*/

/*#DB*/ /* anxiousClient ********************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have the same names as the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void anxiousClient(){
    int n = 10;      // The byTwo prototype uses n
    cout << "increase\tby two\n";
    cout << n << "\t\t" << byTwo(n)<<'\n';
    n = 7;
    cout << n << "\t\t" << byTwo(n)<<'\n';
    n = -120;
    cout << n << "\t\t" << byTwo(n)<<'\n';
}/*#HB*/

/*#DC*/ /* independentClient *****************************************
*
* @description: a model client for our functions whose style
*               is to always call the function with variable
*               arguments that have names different from the  
*               function parameters. 
*
* @returns: nothing
************************************************************/
void independentClient(){
    int kids = 10;
    cout << "increase\tby two\n";
    cout << kids << "\t\t" << byTwo(kids)<<'\n';
    kids = 7;
    cout << kids << "\t\t" << byTwo(kids)<<'\n';
    kids = -120;
    cout << kids << "\t\t" << byTwo(kids)<<'\n';
}/*#HC*/



/*#DD*/ /** byTwo **************************************************
*
* @params: n - any integer
*
* @returns: the original integer plus 2
*************************************************************/

int byTwo(int n){
    n = n + 2;
    return n;
}/*#HD*/
    
