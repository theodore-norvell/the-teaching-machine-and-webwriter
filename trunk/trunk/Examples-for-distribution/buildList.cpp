/*#HA*/
#include <new>
#include <iostream>
using namespace std ;

struct Node {
	int data ;
	Node *next ;
} ;

int main() {

/*#DA*/
	Node *newHead = 0 ;
	int val ;
	cin >> val ;
	while( val > 0 ) {
		// Allocate a new node
			Node *temp = new(nothrow) Node ;
		// Set its fields
			temp->data = val ;
			temp->next = newHead ;
		// Add it to the list
			newHead = temp ;
		// Get the next value from input
			cin >> val ; }
/*#HA*/

	Node *p = newHead ;
	while( p != 0 ) {
		cout << p->data << endl ;
		p = p->next ; }

	return 0 ;
}