#include <iostream>

int a = 0, b = 0, c = 0, g = 0, k = 0, p = 0;

class C1 {
    public:
    static const int k = 1, l = 0, m = 0, n = 0, o = 0, q = 0;
};

namespace NS {
    int b = 1, d = 0, e = 0, h = 0, m = 1, r = 0;
    class C3 {
        public:
        static const int c = 1, d = 1, f = 0, i = 0, n = 1, s = 0;
    };
    class C4 : public C3 {
        public:
        static const int g = 1, h = 1, i = 1, j = 0, o = 1, t = 0;
    };
    class C7 : public C4, public C1 {
        public:
        static const int p = 1, q = 1, r = 1, s = 1, t = 1;
        static void print () {
            cout << "a is " << a << '\n';
            cout << "b is " << b << '\n';
            cout << "c is " << c << '\n';
            cout << "d is " << d << '\n';
            cout << "e is " << e << '\n';
            cout << "f is " << f << '\n';
            cout << "g is " << g << '\n';
            cout << "h is " << h << '\n';
            cout << "i is " << i << '\n';
            cout << "j is " << j << '\n';
            cout << "k is " << k << '\n';
            cout << "l is " << l << '\n';
            cout << "m is " << m << '\n';
            //cout << "n is " << n << '\n';
            //cout << "o is " << o << '\n';
            cout << "p is " << p << '\n';
            cout << "q is " << q << '\n';
            cout << "r is " << r << '\n';
            cout << "s is " << s << '\n';
            cout << "t is " << t << '\n';
        }
    };
}


void main () {
    NS::C7::print ();
}
