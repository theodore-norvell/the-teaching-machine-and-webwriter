#include <stdio.h>

#define oo 100

#define N 8 

int w[N][N] =
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

/*onCreateArray(w, N, N, "drawGraph");

void drawGraph() {
  int i, j;
  for (i = 0; i < N; i++) {
    assertNode(i);
  }
  for (i = 0; i < N; i++) {
    for (j = 0; j < N; j++) {
     if (w[i][j] != oo)
      assertEdge(i, j, i*N+j, w[i][j]);
  }
}*/

int D[N]; 

/*onChangeArray(D, N, "newDistance");

void newDistance(int i) {
	if (D[i] != oo) {
		assertNodeLabel(i, D[i]);
	}
}*/

int v[N];

/*onChangeArray(v, N, "nodeVisited");

void nodeVisited(int i) {
	if (v[i] == 1) {
		assertNodeColor(i, "YELLOW");
	}
}*/

int p[N];

/*onChangeArray(p, N, "newPredecessor");

void newPredecessor(int i) {
	if (p[i] >= 0) {
		int j;
		for (j = 0; j < N; j++) {
			if (w[j][i] != oo) {
				assertEdgeColor(j*N+i, "BLACK");
			}
		}
		assertEdgeColor(p[i]*N+i, "RED");
	}
}*/

/* 
  Simulates a priority queue.
 */
int next_vertex() {
  int i, r;
  for (i = 0; i < N; i++) 
     if (v[i] == 0) 
        break;
  r = i; 
  for (i++; i < N; i++)
     if (v[i] == 0  &&  D[i] < D[r])
        r = i;
  return r;
}

/*
 Determines whether there is an edge
*/
int succ(int u, int v) {
  return (w[u][v] != oo  &&  u != v);
}

/*
 Dijkstra algorithm
*/
void dijkstra(int s) {
  int i, u, j;
  
/*onChange(u, "nextVertex");

void nextVertex(int i) {
	assertNodeColor(i, "SKYBLUE");
}*/
  
  D[s] = 0; /*assertNodeColor(s, GREY);*/
  for (i = 0; i < N; i++) {
    if (i != s)
      D[i] = oo;
    v[i] = 0;
    p[i] = -1;
  }
  for (i = 0; i < N; i++) {
    u = next_vertex();
    if (D[u] == oo)
      continue;
    for (j = 0; j < N; j++)
      if (succ(u,j) && v[j] == 0 && D[u] + w[u][j] < D[j]) {
        D[j] = D[u] + w[u][j];
        p[j] = u;
      }
    v[u] = 1;
  }
}

/*
 The reverse path is printed
*/
int main() {
	dijkstra(0);
	int d = N-1;
	while (d >= 0) {
		printf("%d ", d);
		d = p[d];
	}
	printf("\n");
}