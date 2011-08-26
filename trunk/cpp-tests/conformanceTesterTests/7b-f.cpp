// Fail. Runtime error expected, but does not happen.

//! compile. Run ; expect error equals ""

void  defined() ;
void defined() {
	return ;
}

int main() {
	defined() ;
	return 0 ;
}