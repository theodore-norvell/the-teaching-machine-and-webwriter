#include <iostream>
#include <new>
using namespace std ;

struct Node {
    int data ;
    Node *next ;
} ;

int main() {
    
    Node *head = 0 ;
    
    // Make a list of length 4 
    for( int i=0 ; i < 4 ; ++i ) {
        Node *temp = head ;
        head = new(nothrow) Node ;
        if( head != 0 ) {
            head->data = i*i ;
            head->next = temp ; }
        else {
            head = temp ; } }

    
    //Print the values of the list.
    Node *curr = head ;
    while( curr != 0 ) {
    
        cout << curr->data << "\n" ;
        
        curr = curr->next ; }
        
    return 0;  
}











