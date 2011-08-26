#include <iostream>

int i = 0;
int j = 0;

namespace N1 {
  int i = 1;
  void myregfn () ;
  void myregfn2 () ;
  int j = 1;
}

void N1::myregfn () {
    
}

void myotherfn () ;

class C2 {
public:
    void mymemfn3 () ;
};

class C1 {
public:
  static const int i = 2;
  static const int j = 2;
  
  void mymemfn1 () { 
    cout << "member function of class C1\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
  }
  
  void mymemfn2 () ;

  friend void N1::myregfn2 () {
    cout << "reg fn in ns scope, inline friend of class C1\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
  }

/*
  friend void C2::mymemfn3 ();
    cout << "mem fn of C2, inline friend of C1\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
  }
  */
  
  friend void myotherfn () {
    cout << "global reg fn, unqualified inline friend of C1\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
  }

};

void C2::mymemfn3 () {
    cout << "c2 member function defined outside\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
}

/* 
 *   ILLEGAL
namespace N2 {
int i = 4;
    void N1::myregfn () {
    cout << "reg fn, namespace N1, defined in non-enclosing ns\n" ;
    cout << "i is " << i << "\n" ;
    cout << "j is " << j << "\n" ;
    }
}
 */
 
void C1::mymemfn2 () {
    int j = 59;
  cout << "c1 member function defined outside\n" ;
  cout << "i is " << i << "\n" ;
  cout << "j is " << j << "\n" ;
}

int main (int argc, const char * argv[])
{
    N1::myregfn () ;
    N1::myregfn2 () ;
    myotherfn () ;
    C1 c1;
    c1.mymemfn1 () ;
    c1.mymemfn2 () ;
    C2 c2; 
    c2.mymemfn3 () ;
    return 0;
}
 
