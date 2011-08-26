/*
 * heapSort.cpp
 *
 *  Created on: 4-Aug-2009
 *      Author: theo
 */

#include <iostream>
using namespace std ;


const int MAXLEN = 100 ;
double heap[MAXLEN];
int len = 0, heapLen = 0 ;

void swap( int i, int j ) {
	double t = heap[i] ; heap[i] = heap[j] ; heap[j] = t ;
}

int parent( int n ) { return (n-1)/2 ; }

void heapSort( ) {
	for( heapLen = 1 ; heapLen < len ; heapLen++ ) {
		// Inv. For all i : {1,..heapLen} .
		//        heap[i] <= heap[ parent(i) ]
		// Add item heapLen to the heap
		int n = heapLen ;
		while( n != 0 ) {
			int p = parent(n)  ;
			if( heap[p] >= heap[n] ) break ;
			swap( n, p ) ;
			n = p ; }  }
	// Now all elements are in the heap
	while( heapLen > 1 ) {
		// Inv. For all i : {1,..heapLen} .
		//        heap[i] <= heap[ parent(i) ]
		// and  For all j : {heapLen, .. len } .
		//        For all i : {0, .. j } .
		//          heap[i] <= heap[j]
		heapLen-- ;
		swap( 0, heapLen ) ;
		// Now reheapify.
		int p = 0 ;
		while( 2*p+1 < heapLen ) {
			// Inv. The whole heap satisfies the heap
			// condition, except that node p may contain
			// a value smaller than one or both of its
			// children. I.e.
			//     For all i : {1,..heapLen} .
			//        parent(i) == p or
			//        heap[i] <= heap[ parent(i) ]

			// From the guard, node p has at least one child.
			int l = 2*p + 1 ;  // The left child
			int r = l + 1 ;  // The right child, if any.
			// Out of the parent node and its 1 or 2 children,
			// which holds the biggest number?
			// If it is node p, break.
			int c ;
			if( heap[p] >= heap[l]
			    && (r == heapLen || heap[p] >= heap[r] ) )
				// heap[p] is the biggest
				break ;
			else if( r == heapLen || heap[l] >= heap[r] )
				// heap[l] is the biggest
				c = l ;
			else
				// heap[r] is the biggest
				c = r ;
			swap( c, p) ;
			p = c ; } }
}

// Test it
int main() {
	len = 20 ;
	heap[0] = 1.0 ;
	heap[1] = 2.0 ;
	heap[2] = 3.0 ;
	heap[3] = 10.0 ;
	heap[4] = 9.0 ;
	heap[5] = 8.0 ;
	heap[6] = 7.0 ;
	heap[7] = 6.0 ;
	heap[8] = 20.0 ;
	heap[9] = 21.0 ;
	heap[10] = 19.0 ;
	heap[11] = 22.0 ;
	heap[12] = 18.0 ;
	heap[13] = 23.0 ;
	heap[14] = 17.0 ;
	heap[15] = 1.0 ;
	heap[16] = 4.0 ;
	heap[17] = 3.0 ;
	heap[18] = 2.0 ;
	heap[19] = 9.0 ;

	heapSort() ;
	for( int i=0 ; i < len ; ++i )
		cout << heap[i] << endl ;
}
