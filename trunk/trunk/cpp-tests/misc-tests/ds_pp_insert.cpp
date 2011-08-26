#include<new>

struct Node {
     int data ;
     Node *next ; } ;

int main() {

    // Build a list
    Node *aList = 0 ;
    for( int i = 0 ; i < 3 ; i = i+1 ) {
        Node *tmp = new(nothrow) Node ;
        tmp->data = 13+i ;
        tmp->next = aList ;
        aList = tmp ; }
    
    // Insert a 42 at position 2 
    {
    int n = 2 ;
    int newData = 42 ;
    
    // Insert newData at position n
    	//Let linkP point to link n
    	    Node ** linkP = & aList ;
    	    for(int i=0 ; i<n ; i=i+1 ) {
    	    	linkP = & (*linkP)->next ; }
    	//Create a new node
    	    Node newBorn = new(nothrow) Node ;
    	    newBorn->data = newData ;
    	//Link the new node in
    	    newBorn->next = *linkP ;
    	    *linkP = newBorn ;
    ;}
    // Insert a 24 at position 0 ;
    {
    int n = 0 ;
    int newData = 24 ;
    
    // Insert newData at position n
    	//Let linkP point to link n
    	    Node ** linkP = & aList ;
    	    for(int i=0 ; i<n ; i=i+1 ) {
    	    	linkP = & ((*linkP)->next) ; }
    	//Create a new node
    	    Node newBorn = new(nothrow) Node ;
    	    newBorn->data = newData ;
    	//Link the new node in
    	    newBorn->next = *linkP ;
    	    *linkP = newBorn ;
    ;}
    	
    return ;
 }
                                