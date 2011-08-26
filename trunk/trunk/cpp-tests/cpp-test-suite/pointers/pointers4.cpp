//! Run.
#include <iostream>
using namespace std;

const int ASIZE = 7;
int main(){
	double array[ASIZE];
	// Also recall that array is a synonym for &array[0] 
	double* pBeg = &array[0];
	double* pEnd = array + ASIZE - 1;
	while (pBeg <= pEnd)
		cout << *pBeg++;
	return 0;
}
