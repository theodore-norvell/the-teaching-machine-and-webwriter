#include <iostream>
using namespace std;

/********* Pass-by-Value Demo *******

   We look at a FAULTY swap function
   to see how C++ passes by value
*************************************/

void swap(int a1, int a2);

void main () {
    int a = 1;
    int b = 2;

    cout << a << b;
    swap(a,b);
    cout << a << b;
}

void swap(int a1, int a2){
    int temp;

    temp = a1;
    a1 = a2;
    a2 = temp;
}
