// Fail. Actual run time error does not equal the expected.

//! Compile. Execute; expect error /.*Function not found.*/

void  notDefined() ;

int main() {
	notDefined() ;
	return 0 ;
}