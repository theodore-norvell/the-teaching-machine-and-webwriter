#include <stdio.h>

#define N 4

unsigned long p[N+1] = { 100, 20, 1000, 2, 50 };
unsigned long m[N][N];
unsigned long s[N][N];

void compute_cost() {
	int d, i, j, r;
	unsigned long c;
    for (i = 0; i < N; i++) {
       m[i][i] = 0;
       s[i][i] = i;
    }
    for (d=1; d<N; d++) {
        for (i=0; i<N-d; i++) {
            j = i + d;
            m[i][j] = -1;
			for (r=i; r<j; r++) {
				c = m[i][r] + m[r+1][j];
				c = c + p[i]*p[r+1]*p[j+1];
				if (m[i][j] < 0 || c < m[i][j]) {
					m[i][j] = c;
					s[i][j] = r;
				}
			}
        }
    }
}

int main() {
	compute_cost();
	printf("%d\n",m[0][N-1]) ;
}