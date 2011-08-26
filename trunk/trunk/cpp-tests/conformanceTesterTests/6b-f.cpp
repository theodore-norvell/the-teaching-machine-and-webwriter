// Fail. Runtime error expected, but does not happen.

//! compile. Run ; expect error ~ /.*Function definition not found.*/

void  defined() ;
void defined() {
	return ;
}

int main() {
	defined() ;
	return 0 ;
}