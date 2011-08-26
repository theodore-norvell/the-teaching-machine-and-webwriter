// Fail. The third run fails. The fourth should not happen.

//! Run with input "1"; expect output "42".
//! Run with input "2"; expect output "84".
//! Run with input "3"; expect output "84".
//! Run with input "1"; expect output "42".

#include <iostream>
using namespace std ;

int main() {
	int i ; cin >> i ;
    cout << i*42  ;
	return 0 ;
}