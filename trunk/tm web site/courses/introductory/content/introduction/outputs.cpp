/*#HA*/ /** output.cpp **********************************************
*
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 *
 * Author: Michael Bruce-Lockhart
 *   Date: 2006.05.01
 *
 *******************************************************************/

#include <iostream>
using namespace std;
/*#DA*/int main() {
    cout << "You can insert ints and doubles into the output stream like this: ";
    cout << 1 << 2 << 3 << 4 << 3.75 << endl;
    cout << "But notice how they run together. If you want them separated, ";
    cout <<  "You will have to do it yourself!\n";
    cout << "Like this: " << 1 << ' ' << 2 << ' ' << 3 << ' ' << 4;
    cout << ' ' << 3.75 << endl;
    cout << "Notice the use of single quotes to ouput single space characters!"<<endl;
    return 0;
} /*#HA*/

