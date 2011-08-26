#include <iostream>

int a = 0;
int b = 0;
int c = 0;
int g = 0;

class C9 {
    public:
    static const int b = 1;
    static const int d = 0;
    static const int e = 0;
    static const int h = 0;
    static void f1 () {
        static const int c = 1;
        static const int d = 1;
        static const int f = 0;
        static const int i = 0;
        class C10 {
            public:
            int g, h, i, j;            
            void print () {
                g = h = i = 1; j = 0;
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
        C10 myc10;
        myc10.print ();
    }
};

void main () {
    C9::f1 ();
}
