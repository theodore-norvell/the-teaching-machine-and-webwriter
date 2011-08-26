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
	Node *newHead ;

	Node *p = head ;
	newHead = 0 ;
	
	// Loop invariant: 
	//   * p has the same value as one of the links of
	//     the list headed by head, and
	//   * newHead is the head of a list that contains
	//     copies of all the nodes between head and p,
	//     but in reverse order. 
    while( p != 0 ) {
		// Allocate a new node
			Node *temp = new(nothrow) Node ;
		// Set its fields
			temp->data = p -> data ;
			temp->next = newHead ;
		// Add it to the list
			newHead = temp ;
		// Advance p
			p = p->next ; }
/*#HA*/

	return 0 ;
}