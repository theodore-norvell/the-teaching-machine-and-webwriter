#include <iostream>

int a = 0;
int b = 0;
int c = 0;
int g = 0;

namespace NS {
    int b = 1;
    int d = 0;
    int e = 0;
    int h = 0;
    class C3 {
        public:
        static const int c = 1;
        static const int d = 1;
        static const int f = 0;
        static const int i = 0;
    };
    class C4 : public C3 {
        public:
        static const int g = 1;
        static const int h = 1;
        static const int i = 1;
        static const int j = 0;
            
        static void print () {
            cout << " a is " << a << '\n';
            cout << " b is " << b << '\n';
            cout << " c is " << c << '\n';
            cout << " d is " << d << '\n';
            cout << " e is " << e << '\n';
            cout << " f is " << f << '\n';
            cout << " g is " << g << '\n';
            cout << " h is " << h << '\n';
            cout << " i is " << i << '\n';
            cout << " j is " << j << '\n';
        }
    };
}

void main () {
    NS::C4::print ();
}
