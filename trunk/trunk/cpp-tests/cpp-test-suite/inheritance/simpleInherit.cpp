//! Run.
/*#HB*/class A { 
public:
	void a() ;
protected:
	int x1;
private:
	int x2;
} ;

class B : public A {
     public:  void b() ;
	 protected: int y1;
     private: int y2;
} ;

class C : public B {
     public:  void c() ;
     private: int z ;
} ;/*#HA*/

/*#DB*/void A::a() {
	x2 = 53;
}

void B::b() {
    x1 = 7;
	y2 = 9;
	a();
}

void C::c() {
    x1 = 13;
	y1 = 72;
	z = 99;
	b();
}/*#HB*/

int main() {
	A a;	// Give me an a!
	B b;	// Give me a b!
    C c;	// Give me a c!
    c.c() ;	// call c's c routine
    return 0 ;
}
