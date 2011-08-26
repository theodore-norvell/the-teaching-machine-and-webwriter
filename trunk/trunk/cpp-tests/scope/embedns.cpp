#include <iostream>

int i = 0;
int j = 0;

namespace N3 {
    int i = 5;
    namespace N4 {
        int i = 6;
        int j = 6;
        void myregfn3 () ;
    }
    void N4::myregfn3 () {
        cout << "reg fn, namespace N4, defined in enclosing ns N3\n" ;
        cout << "i is " << i << "\n" ;
        cout << "j is " << j << "\n" ;
    }
}


int main (int argc, const char * argv[])
{
    N3::N4::myregfn3 () ;
    return 0;
}

 