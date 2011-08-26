/*#H*/ /***********************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * Program to compute the weight of a number of washers.
 *
 * Input: Washer outer diameter in mm.
 *        Washer hole diameter in mm.
 *        Washer thickness in mm.
 *        Number of washers in a batch
 *        Material density in kg/m cubed
 * Output: Weight of the batch. in gms
 *
 * Author: Michael Bruce-Lockhart
 *
 *****************************************************/
/*#DM*/
#include <iostream>
#include <cmath>
using namespace std;

const double PI = 4 * atan(1.0);
const double STEEL_DENSITY = 8030; // Type 304, in kg/cu.m

// Functions used by main
double batchWeight(int count, double density, double thickness,
                       double innerR, double outerR);
// Input fetching functions
double getDouble(double lowerLimit, double upperLimit);
int getInt(int lowerLimit, int upperLimit);

int main(){
	double inside;  //diameter in mm
	double outside; //diameter in mm
	double thickness; // in mm
	int quantity;     // no. of washers / batch
	char yesNo;       // 'Y' or 'y' to enter another batch
	
	do {
    	cout << "\n\nPlease input the inside diameter of the washer in mm. ";
        inside = getDouble( 0.0, -1);
    	cout << "\nPlease input the outside diameter of the washer in mm. ";
        outside = getDouble(inside, -1);
    	cout << "\nPlease input the thickness of the washer in mm. ";
        thickness = getDouble(0.0, -1);
    	cout << "\nPlease input the number of washers in the batch. ";
        quantity = getInt(0, -1);
        
    	cout << "The weight of a batch of " << quantity << " steel "
                 << thickness << "mm. thick washers ";
    	cout << "of " << inside << " and " << outside  
              <<  " mm. inner and outer radii is ";
    	cout << batchWeight(quantity, STEEL_DENSITY, thickness, inside, outside)
              << " kgs." << endl;
        cout << "\n\nDo you want to calculate another batch? (y/n) ";
        cin >> yesNo;
    } while (yesNo == 'Y' || yesNo == 'y');
    return 0;
}
/*#HM*/
/*#DB*/
/** getDouble *******************************************
 *
 * @params: lowerLimit - of the input being fetched
 *          upperLimit - of the input being fetched
 *                     no upper limit if < lowerLimit
 *
 * @description: Fetches a double from the standard input
 *               stream that is on the specified interval
 *
 * @returns: a double from the standard input that is on 
 *            (lowerLimit, upperLimit)
 *********************************************************/
double getDouble(double lowerLimit, double upperLimit){
       double response;
       bool fail = false;
       bool twoLimit = upperLimit > lowerLimit;
       do {
           cin >> response;
           fail = (response <= lowerLimit);
           if (!fail && twoLimit)
              fail = (response >= upperLimit);
           if (fail){
              cout << "Response must be > " << lowerLimit;
              if (twoLimit)
                 cout << " and < " << upperLimit;
              cout << "\nPlease try again: ";
           }
       }while (fail);
       return response;          
}/*#HB*/

 
/** getInt *********************************************
 *
 * @params: lowerLimit - of the input being fetched
 *          upperLimit - of the input being fetched
 *                     no upper limit if < lowerLimit
 *
 * @description: Fetches an int from the standard input
 *               stream that is on the specified interval
 *
 * @returns: an int from the standard input that is on 
 *            (lowerLimit, upperLimit)
 ********************************************************/
int getInt(int lowerLimit, int upperLimit){
       int response;
       bool fail = false;
       bool twoLimit = upperLimit > lowerLimit;
       do {
           cin >> response;
           fail = (response <= lowerLimit);
           if (!fail && twoLimit)
              fail = (response >= upperLimit);
           if (fail){
              cout << "Response must be > " << lowerLimit;
              if (twoLimit)
                 cout << " and < " << upperLimit;
              cout << "\nPlease try again: ";
           }
       }while (fail);
       return response;          
}


/*#DA*/
// batchWeight assistant function declarations 
double washerWeight(double density, double thickness,
                           double innerR, double outerR); 
double washerArea(double innerR, double outerR);
double circleArea(double radius);

/** batchWeight ****************************************
 *
 * @params: count - the number of washers in the batch
 *          density of washer material in kg/m cubed
 *          thickness: of washer in mm.
 *          innerR: inner radius in mm.
 *          outerR: outer radius in mm.
 *
 * @pre: all parameters should be non-negative
 *
 * @returns: the weight of the washer in kg.
 *******************************************************/

double batchWeight(int count, double density, double thickness,
                       double innerR, double outerR){
	return count * 
           washerWeight(density, thickness, innerR, outerR);
}/*#HA*/


/** washerWeight ***************************************
 *
 * @params: density of washer material in kg/m cubed
 *           thickness: of washer in mm.
 *           innerR: inner radius in mm.
 *           outerR: outer radius in mm.
 *
 * @pre: all parameters should be non-negative
 *
 * @returns: the weight of the washer in kg.
 *******************************************************/
/*#DA*/
double washerWeight(double density, double thickness,
                           double inner, double outer){
// convert linear dimensions from mm to m
	thickness /= 1000.;
	inner /= 1000.;
	outer /= 1000.;
	return density * thickness * washerArea(inner, outer);
}/*#HA*/

/** washerArea ******************************************
 *
 * @params: innerR - inner radius in m.
 *          outerR - outer radius in m.
 *
 * @pre: all parameters should be non-negative
 *
 * @returns: the area of the washer in m. squared.
 ********************************************************/

/*#DA*/
double washerArea(double innerR, double outerR){
	return circleArea(outerR) - circleArea(innerR);
}/*#HA*/

/** circleArea *************************************
 *
 * @params: radius - in m. @pre: non-negative
 *
 * @returns: the area of the circle in m. squared.
 ***************************************************/

/*#DA*/
double circleArea(double radius){
	return PI * radius * radius;
}/*#HA*/
