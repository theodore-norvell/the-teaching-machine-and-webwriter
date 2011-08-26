#include <iostream>

int a = 0;
int b = 0;
namespace N {
    int a = 1;
    int d = 0;
    int e = 0;
    int f = 0;
    void f3 () ;
    class C {
        public:
        static const int f = 1;
        static const int g = 0;
        static const int h = 0;
        friend void f3 () {
            int a = 2;
            int c = 0;
            int d = 1;
            int h = 1;
            cout << " a is " << a << '\n';
            cout << " b is " << b << '\n';
            cout << " c is " << c << '\n';
            cout << " d is " << d << '\n';
            cout << " e is " << e << '\n';
            cout << " f is " << f << '\n';
            cout << " g is " << g << '\n';
            cout << " h is " << h << '\n';
        }
    };
}

void main () { 
    N::f3 ();
}

