/*#HA*//******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * complex_print_aargh.cpp -- Demonstrate bad function implementation
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2004.01.02
 *
 *******************************************************************/

/*#DA*/void printComplex(double re, double im){    // function DEFINITION

	// Please note, these 4 lines are wrong, Wrong, WRONG!
	cout << "Please input real part: " ;
    cin >> re;
	cout << "And imaginary part: ";
	cin >> im;
	// end of wrong, Wrong, WRONG

	cout << '(' << re ;
	cout << " + j" << im;
	cout << "j)";
}
