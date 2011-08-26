/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * downcast.cpp 
 * Demonstrate loss of precision due to "downcasting".
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.02
 *
 *******************************************************************/

 /******************************************************************
 * main
 *
 * Parameters: none
 *
 * Returns: 0
 *******************************************************************/
/*#DA*/int main(){
	double x = 3.7;
	int i;
	i = 3 * (int) x;
	return 0;
}
