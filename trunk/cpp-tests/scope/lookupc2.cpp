#include <iostream>

int a = 0;
int b = 0;
int c = 0;

namespace NS {
    int b = 1;
    int d = 0;
    int e = 0;
    class C2 ;
}

class NS::C2 {
    public:
    static const int c = 1;
    static const int d = 1;
    static const int f = 0;
    static void print () {
        cout << " a is " << a << '\n';
        cout << " b is " << b << '\n';
        cout << " c is " << c << '\n';
        cout << " d is " << d << '\n';
        cout << " e is " << e << '\n';
        cout << " f is " << f << '\n';
    }
};

void main () {
    NS::C2::print ();
}
