//! Run.
int main() {
	typedef int *T ;
	T p ;
	int i ;
	p = & i ;
	*p = 1 ;
	return i ;
}