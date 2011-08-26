// Fail. Expected output no errors, but one happens.

//! Compile. Execute any output.

#include <iostream>
using namespace std ;

void notDefined() ;

int main() {
    notDefined() ;
	return 0 ;
}