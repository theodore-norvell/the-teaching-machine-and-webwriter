#include <stdio.h>

#define oo 100

#define N 8 

int weight[N][N] =
  {
    {    0,   9,  11,    oo,    14,    20,    oo,    oo},
    {   oo,    0,   13,  18,    oo,    oo,    oo,    oo},
    { oo,  oo,     0,  7,  oo,    oo,    oo,    oo}, 
    {   oo, oo,  oo,     0,   oo,    oo,   oo,    17},  
    {   oo,   oo,  9,   oo,     0,  oo,    oo,  oo}, 
    {   14,   oo,    oo,    oo,  oo,     0,   oo,  oo}, 
    {   oo,   8,    8,   oo,    oo,   21,     0,  oo}, 
    {   oo,   oo,    oo,    oo,  11,  oo,   22,    0}
  };

int D[N]; 
int done[N];
int pred[N];

/* 
  No priority queue.
 */

int next_vertex() {
  int i, r;
  for (i = 0; i < N; i++) 
     if (done[i] == 0) 
        break;
  r = i; 
  for (i++; i < N; i++)
     if (done[i] == 0  &&  D[i] < D[r])
        r = i;
  return r;
}

int succ(int u, int v) {
  return (weight[u][v] != oo  &&  u != v);
}

void dijkstra(int s) {
  int i, u, j;
  D[s] = 0;  pdv::nodeLabel(D[s], 0) ;
             pdv::nodeColour( D[s], pdv::GREY ) ;
  for (i = 0; i < N; i++) {
    if (i != s)
      D[i] = oo;  pdv::nodeLabel(D[i], "oo") ;
                  pdv::nodeColour( D[i], pdv::WHITE ) ) ;
    done[i] = 0;
    pred[i] = -1;
  }
  for (i = 0; i < N; i++) {
    u = next_vertex();
    if (D[u] == oo)
      continue;  // [Wonder if Pilu meant "break" here.  TSN.]
    done[u] = 1;
    pdv::nodeHighlight(D[u], true) ;
    for (j = 0; j < N; j++)
      if( (succ(u,j) && done[j] == 0 ) {
        pdv::nodeHighlight( D[j], true ) ;
        pdv::edgeHighlight( D[u], D[j], true ) ;
        if( D[u] + weight[u][j] < D[j] ) {
          D[j] = D[u] + weight[u][j];
          pdv::nodeLabel(D[j], D[j]) ;
          pdv::nodeColour( D[j], pdv::GREY ) ;
          if( pred[j] != -1 )
            pdv::edgeStroke( D[pred[j]], D[j], pdv::DEFAULT ) ;
          pred[j] = u;
          pdv::edgeStroke( D[u], D[j], pdv::THICK ) ;
        }
        pdv::edgeHighlight( D[u], D[j], false ) ;
        pdv::nodeHighlight( D[j], false ) ;
      }
    pdv::nodeHighlight(D[u], false) ;
    pdv::nodeColour(D[u], pdv::BLACK) ;
  }
}

void setUpGraph() {
    for( int i=0 ; i < N ; ++i ) {
        pdv::node(D[i]) ; }
    for( int i=0 ; i<N ; ++i )
        for( int j=0 ; j<N ; ++j )
            if( succ( i, j) ) {
                pdv::edge( D[i], D[j] ) ;
                pdv::edgeLabel( D[i], D[j], weight[i][j] ) ; 
                pdv::edgeArrowHead( D[i], D[j], pdv::TARGET ) ; }
}
                

int main() {
    setUpGraph() ;
	dijkstra(0);
	int d = N-1;
	while (d >= 0) {
		printf("%d ", d);
		d = pred[d];
	}
	printf("\n");
}