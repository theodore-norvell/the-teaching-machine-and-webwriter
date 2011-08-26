#include <iostream>
#include <new>
//Hello
using namespace std ;

struct Node {
	int data ;
	Node *next ;
} ;

void main() {
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
	
	//Make a reversed copy of the list.
	Node *curr = head ;
	Node *newHead = 0 ;
	
	while( curr != 0 ) {
		//Create a new Node and copy data
			Node * newBorn = new(nothrow) Node ;
			newBorn->data = curr->data ;
			newBorn->next = newHead ;
		//Re-establish the invariant
			newHead = newBorn ;
			curr = curr -> next ; }
}

