#include <iostream>

class A { 
    public: 
    int a; 
    A () { a = 0; } 
};
class B : public A { };
namespace N1 {
    class C : public A { };
}
namespace N2 { 
    class C : public ::N1::C { 
        public: 
        int a;
        C () { a = 0; }
    };
    class N1 : public C { };
}

class D : public N1::C { };

class E : public N2::N1 { };

int main () { 
     
    B b;
    b.a = 5;
    b.B::a = 5;
    b.A::a = 5;
    b.::A::a = 5;

    D d;
    d.::N1::C::a = 5;
    d.N1::C::a = 5;
    d.C::a = 5;
    d.A::a = 5;
    d.a = 5;
    
    E e;
    e.N1::C::a = 5;
    cout << e.::A::a << '\n';
    cout << e.::N2::C::a << '\n';
}