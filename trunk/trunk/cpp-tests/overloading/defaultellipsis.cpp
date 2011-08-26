
#include <iostream>

// void foo (bool, bool) { cout << "bool bool\n"; }
void foo (bool, ...) {cout << "bool ...\n"; }
void foo (bool, bool, bool = true) { cout << "bool bool bool=true\n"; }

int main () { 
    // this call is ambiguous
    foo (true, true);
}