/*
 * parenthesization.cpp
 *
 *  Created on: 6-Aug-2009
 *      Author: theo
 */

#include <climits>
#include <iostream>
using namespace std ;

// We wish to multiply N matrices
const int N = 6 ;

// p stores the array indices. Matrix A_i
// has p[i] rows and p[i+1] columns.
int p[N+1] ;

// The cost matrix is as follows:
// cost[i][j] where 0 <= i < N and 0 <= j < N-i
// is the optimal cost of multiplying a segment of
// matrices A_i through A_(i+j) inclusive.
// Note the length of the segment is j+1.
//
// Note that
//   cost[i][0] == 0 for all 0 <= i < N
//   cost[i][j] where 0 < j < N-i ==
//          min_{k : {0,..j}} ( cost[i][k] + cost[i+k+1][j-k-1]
//                               + p[i]*p[i+k+1]*p[i+j+1] )
// Note on k: The k value is one less than the length of the initial
// segment. I.e we split the segment A_i ... A_i+j
// into two segments  A_i ... A_i+k and A_i+k+1 ... A_i_j
// of lengths k+1 and k-j respectively.  The length of
// the initial segment ranges from 1 to j (inclusive).
// So k : {0,..j}.
int cost[N][N] ;

// The witness array contains the values for k that lead
// to the optimal cost. The idea is that:
// for all i, j, k,
//    if 0 <= i < N and 0 < j < N-i and k == witness[i][j]
//    then cost[i][j]==
//          ( cost[i][k] + cost[i+k+1][j-k-1]
//          + p[i]*p[i+k+1]*p[i+j+1] )
int witness[N][N] ;

void computeCosts() {
	// Fill in the first column.
	for( int i = 0 ; i < N ; ++i )
		cost[i][0] = 0 ;
	// Fill in the rest of the array in order of
	// increasing segment length.
	for( int j = 1 ; j < N ; ++j )
		for( int i = 0 ; i < N-j ; ++i ) {
			int minCost =INT_MAX ;
			int best_k ;
			for( int k = 0 ; k < j ; ++k ) {
				int thisCost = cost[i][k] + cost[i+k+1][j-k-1]
				             + p[i]*p[i+k+1]*p[i+j+1] ;
				if( thisCost < minCost ) {
					minCost = thisCost ;
					best_k = k ; } }
			cost[i][j] = minCost ;
			witness[i][j] = best_k ; }
}

void print( int i, int j ) ;

void print1(int i, int j) {
	int k = witness[i][j] ;
	if( k==0 ) cout << "A_" << i ;
	else print(i, k) ;
	cout << " " ;
	if( k == j-1 ) cout << "A_" << i+j ;
	else print(i+k+1, j-k-1) ;
}

void print( int i, int j ) {
	cout << "(" ; print1(i,j) ; cout << ")" ;
}

int main() {
	p[0] = 10 ;
	p[1] = 20 ;
	p[2] = 5 ;
	p[3] = 10 ;
	p[4] = 20 ;
	p[5] = 15 ;
	p[6] = 20 ;
	computeCosts() ;
	print(0, N-1) ;
	cout << endl ;
	return 0 ;
}
