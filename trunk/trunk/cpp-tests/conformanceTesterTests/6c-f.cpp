// Fail. Actual run time error does not match the expected.

//! Compile. Execute; expect error matches /.*Function not found.*/

void  notDefined() ;

int main() {
	notDefined() ;
	return 0 ;
}