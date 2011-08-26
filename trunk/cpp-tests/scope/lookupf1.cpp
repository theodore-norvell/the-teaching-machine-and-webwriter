#include <iostream>

int a = 0;
int b = 0;
void f1 () {
    int a = 1;
    int c = 0;
    if (true) {
        int c = 1;
        cout << "function inner block scope \n";    
        cout << " a is " << a << '\n';
        cout << " b is " << b << '\n';
        cout << " c is " << c << '\n';
    }
    cout << " function outer scope \n";
    cout << " a is " << a << '\n';
    cout << " b is " << b << '\n';
    cout << " c is " << c << '\n';
}

void main () {
    f1 ();
}

