#include<new>

struct Node {
     int data ;
     Node *next ; } ;

int main() {

    // Build a list
    Node *aList = 0 ;
    for( int i = 0 ; i < 4 ; i = i+1 ) {
        Node *tmp = new(nothrow) Node ;
        tmp->data = 13+i ;
        tmp->next = aList ;
        aList = tmp ; }
    
    // Calulate the length
    int len = 0 ;
    Node *p = aList ;
    while( p != 0 ) {
    	p = p->next ;
    	len = len + 1 ; }
    	
    return ;
 }
                                