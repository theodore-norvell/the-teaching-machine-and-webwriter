/*
 * elastic_array.cpp
 *
 *  Created on: Jun 15, 2011
 *      Author: theo
 */

#include <assert.h>

class ElasticArray {
	// Represents: a sequence of integers s
	// Invariant: cap > 0 and p points to the first element of an array of size cap in the heap
	// Invariant: cap/4 <= size and size <= cap
	// Representation condition: s.size = size and for all i : {0,..size}. s[i]==p[i].
	private: int size ;
	private: int cap ;
	private: int *p ;

	public: ElasticArray() ;

	// get(i)
	// Precondition:   0 <= i < s.size
	// Postcondition:  result == s[i]
	public: int get( int i ) ;
	// set(i)
	// Precondition:   0 <= i < s.size()
	// Changes s[i]
	// Postcondition s'[i] == x
	public: void set( int i, int x) ;
	// getSize()
	// Postcondition result == s.size()
	public: int getSize() ;
	// grow()
	// Postcondition: s'.size == s.size+1 amd s'[size]==0
	public: void grow() ;
	// shrink()
	// Precondition s.size > 0
	// Postcondition s'.size == s.size-1
	public: void shrink() ;
};

ElasticArray::ElasticArray() {
	size = 0 ;
	cap = 1 ;
	p = new int[1] ;
}

int ElasticArray::get( int i ) {
	assert( 0 <= i && i < size ) ;
	return p[i] ;
}

void ElasticArray::set( int i, int x) {
	assert( 0 <= i && i < size ) ;
	p[i] = x ;
}

int ElasticArray::getSize( ) {
	return size ;
}

void ElasticArray::shrink( ) {
	assert( size > 0 ) ;
	if( size-1 < cap/4 ) {
		cap = cap /2 ;
		int* q = new int[cap] ;
		for(int i=0 ; i<size-1 ; ++i) q[i]=p[i] ;
		int* t = p ;
		p = q ;
		delete [] t ;
	}
	--size  ;
}

void ElasticArray::grow( ) {
	if( size == cap ) {
		cap = cap * 2 ;
		int* q = new int[cap] ;
		for(int i=0 ; i<size ; ++i) q[i]=p[i] ;
		int* t = p ;
		p = q ;
		delete [] t ;
	}
	p[size] = 0 ;
	++size ;
}


int main() {
	ElasticArray a ;
	int x = 12 ;
	for( int i=0 ; i < 10 ; ++i ) {
		a.grow() ;
		a.set(i, x) ;
		++x ;
	}

	for( int i=0 ; i < 10 ; ++i ) {
		a.shrink() ;
	}
}
