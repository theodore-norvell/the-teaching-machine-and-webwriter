#include <iostream>

int a = 0;
int b = 0;
namespace N {
    int a = 1;
    int d = 0;
    void f2 () ;
}

void N::f2 () {
    int a = 1;
    int c = 0;	
    if (true) {
        int c = 1;
        cout << "function inner block scope \n";    
        cout << " a is " << a << '\n';
        cout << " b is " << b << '\n';	
        cout << " c is " << c << '\n';
        cout << " d is " << d << '\n';
    }
    cout << " function outer scope \n";
    cout << " a is " << a << '\n';
    cout << " b is " << b << '\n';
    cout << " c is " << c << '\n';
    cout << " d is " << d << '\n';
}

void main () { 
    N::f2 ();
}
