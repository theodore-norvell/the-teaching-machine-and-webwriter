#include <iostream>

int a = 0;
int b = 0;
class C1 {
    public:
    static const int b = 1;
    static const int c = 0;
    static void print () {
        cout << " a is " << a << '\n';
        cout << " b is " << b << '\n';
        cout << " c is " << c << '\n';
    }
};

void main () {
    C1::print ();
}
