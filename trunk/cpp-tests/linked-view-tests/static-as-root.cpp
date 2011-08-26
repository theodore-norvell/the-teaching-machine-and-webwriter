#include <iostream>

using namespace std;


static int *p ;

int main() {
   int *q ;
   p = new int ;
   q = new int ;
   *p = 9 ;
   *q = 10 ;
   return 0 ;
}
    