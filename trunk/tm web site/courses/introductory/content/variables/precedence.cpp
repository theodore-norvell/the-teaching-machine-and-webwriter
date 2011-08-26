/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * precedent.cpp -- Demonstrate operator precedence.
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.04.30
 *
 *******************************************************************/
#include <iostream>
using namespace std;

int main() {
    int a = 5;
    int b = 6;
    int c = 2;
    cout << "5 + 6 * 2 = " << a + b * c;
    cout << "\n(5 + 6) * 2 = " << (a + b) * 2 ;
    cout << "\n6 / 2 * 5 = " << b/c*a;
    cout << "\n6 / (2 * 5) = " << b/(c*a);
    cout << "\n5 + -6 * 2 = " << a+-b*c;

  return 0;
}
