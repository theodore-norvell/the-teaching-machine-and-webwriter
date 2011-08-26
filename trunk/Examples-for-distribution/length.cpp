/*#HA*/
#include <new>
#include <iostream>
using namespace std ;

struct Node {
	int data ;
	Node *next ;
} ;

/*#DA*/
int len( Node *const head ) {
	int len = 0 ;
	Node *p = head ;
	// Loop invariant: 
	//  * p is the value of some link in the list, and
	//  * len is the number of nodes between head and p
	while( p != 0 ) {
		len = len + 1 ;
		p = p->next ; }

	return len ; }
/*#HA*/

int main() {
	// First let's test the len function on an empty list.
	cout << len( 0 ) << endl ;

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
	cout << len( head ) << endl ; 
	return 0 ;
}