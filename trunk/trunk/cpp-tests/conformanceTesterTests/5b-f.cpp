// Fail. Runtime error expected, but does not happen.

//! compile. execute; expect any error.

void  defined() ;
void defined() {
	return ;
}

int main() {
	defined() ;
	return 0 ;
}