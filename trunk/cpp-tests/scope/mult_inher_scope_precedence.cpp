<#include iostream>

int a = 0;
class A { };
class B {
    public:	
    static const int a = 1;
};
class C : public A, public B {
    int b;
    void f () { 
        b = a;
    }
};

void main () {     
}
