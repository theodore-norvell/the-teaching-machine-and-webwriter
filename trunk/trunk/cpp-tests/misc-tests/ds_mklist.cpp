#include<new>

struct Node {
     int data ;
     Node *next ; } ;

int main() {

    // Build a list
    Node *aList = 0 ;
    for( int i = 0 ; i < 4 ; i = i+1 ) {
        Node *tmp = new(nothrow) Node ;
        tmp->data = i ;
        tmp->next = aList ;
        aList = tmp ; }
    
    return ;
 }
                                