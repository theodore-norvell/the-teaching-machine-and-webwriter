/*
 * quickSort.cpp
 *
 *  Created on: 4-Aug-2009
 *      Author: theo
 */

#include <iostream>
using namespace std ;


const int MAXLEN = 100 ;
double A[MAXLEN];
int len = 0 ;

int partition( int p, int q ) {
	// Pre: p < q-1 ;
	// Post: A'[p,..q] is a permutation of A[p,..q]
	//       and p <= result and result < q
	//       and A[p,..result] <= A[result]
	//       and A[result+1,..q] >= A[result]

	// Pick a point in the middle and put its value in pivot.
	int mid = (p+q) / 2 ;
	double pivot = A[mid] ;
	// Move the value at A[p] to the middle, creating a (conceptual) hole at A[p]
	A[mid] = A[p] ;
	while(  true ) {
		// The hole is at A[p].
		// Search from the right for a value smaller than pivot
		while( p < q-1 && A[q-1] >= pivot ) q-- ;
		// If we reach the hole, we are done
		if( p==q-1) break ;
		// Move the value found to the hole.
		A[p] = A[q-1] ;
		p++ ;
		// The hole is at A[q-1].
		// Search from the left for a value larger than the pivot
		while( p < q-1 && A[p] <= pivot ) p++ ;
		// If we reach the hole, we are done
		if( p==q-1 ) break ;
		// Move the value found to the hole.
		A[q-1] = A[p] ;
		q-- ; }
	A[p] = pivot ;
	return p ;
}

void quickSort( int p, int q ) {
	if( p < q-1 ) {
		int m = partition( p, q ) ;
		quickSort( p, m ) ;
		quickSort( m+1, q ) ; }
}

// Test it
int main() {
	len = 20 ;
	A[0] = 1.0 ;
	A[1] = 2.0 ;
	A[2] = 3.0 ;
	A[3] = 10.0 ;
	A[4] = 9.0 ;
	A[5] = 8.0 ;
	A[6] = 7.0 ;
	A[7] = 6.0 ;
	A[8] = 20.0 ;
	A[9] = 21.0 ;
	A[10] = 19.0 ;
	A[11] = 22.0 ;
	A[12] = 18.0 ;
	A[13] = 23.0 ;
	A[14] = 17.0 ;
	A[15] = 1.0 ;
	A[16] = 4.0 ;
	A[17] = 3.0 ;
	A[18] = 2.0 ;
	A[19] = 9.0 ;

	cout << "quicksort" << endl ;
	quickSort(0, len) ;
	for( int i=0 ; i < len ; ++i )
		cout << A[i] << endl ;
}
