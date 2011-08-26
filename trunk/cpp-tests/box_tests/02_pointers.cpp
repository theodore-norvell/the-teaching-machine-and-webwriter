#include <iostream>
#include <new>
using namespace std ;

int main() {
    int i =243, k ;
    int *p ;
    p = &i ;
    k = *p ;
    *p = 81 ;
    
    p = new(nothrow) int ;
    *p = 27 ;
    k = *p ;

    int *(*pp) ;
    pp = & p ;
    k = *(*pp) ;
    *(*pp) = 9 ;
    
    return 0 ;
}