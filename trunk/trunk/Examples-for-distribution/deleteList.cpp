/*#HA*/
#include <new>
#include <iostream>
using namespace std ;

struct Node {
	int data ;
	Node *next ;
} ;

int main() {
	
	// Now let's construct a list of length 3
	Node *head = new(nothrow) Node ;
	{ 
		Node *n2 = new(nothrow) Node ;
		Node *n3 = new(nothrow) Node ;
		if( head==0 || n2==0 || n3==0 ) return 1 ;
		head->data = 59 ;
		head->next = n2 ;
		n2->data = 63 ;
		n2->next = n3 ;
		n3->data = 99 ;
		n3->next = 0 ;
	}

	/*#DA*/
	// Loop invariant: 
	//  all nodes orignally between the original value of head
	//  and the current value of head have been deleted.
	while( head != 0 ) {
		// Make a link to the first node
			Node *zombie = head ;
		// Cut the first node out of the list
			head = head->next ;
		// Delete the former first node
			delete zombie ; }
	/*#HA*/

	return 0 ;
}