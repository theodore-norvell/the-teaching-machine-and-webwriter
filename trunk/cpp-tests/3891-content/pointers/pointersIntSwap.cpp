//! Run.
/*#H*/#include <iostream >
using namespace std;
void intSwap(int* px,int* py); // declaration prototype

void main() {
	int	x1 = 4, x2 = 8; 	
	int* px1 = &x1; 	// Pointers to x1 and x2
	int* px2 = &x2;		// Pointer VALUES are addresses 

	intSwap(&x1,&x2);	// Passing constants to intswap
	cout << x1 << "\t" << x2 << "\n";

	intSwap(px1,px2);	// Passing pointers to intswap
	cout << x1 << "\t" << x2 << "\n";

	int tmp = x1;		// local swap code
	x1 = x2;
	x2 = tmp;
	cout << x1 << "\t" << x2 << "\n";
}
/*#/H*/
void intSwap(int* px,int* py){
	int	hold;

	hold = *px;
	*px = *py;
	*py = hold;
}
