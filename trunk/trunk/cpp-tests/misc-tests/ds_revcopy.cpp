#include<new>

struct Node {
     int data ;
     Node *next ; } ;

void main() {

    // Build a list
    Node *aList = 0 ;
    for( int i = 0 ; i < 4 ; i = i+1 ) {
        Node *tmp = new(nothrow) Node ;
        tmp->data = 13+i ;
        tmp->next = aList ;
        aList = tmp ; }
    
    // Make a reversed copy
    Node *p = aList ;
    Node *newList = 0 ;
    while( p != 0  ) {
    	Node *tmp = new(nothrow) Node ;
    	tmp->data = p->data ;
    	tmp->next = newList ;
    	newList = tmp ;
    	p = p->next; }
    	
    return ;
 }
                                