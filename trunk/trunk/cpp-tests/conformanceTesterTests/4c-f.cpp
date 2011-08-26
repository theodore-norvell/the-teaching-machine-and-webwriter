// Fail.  Compile time error message expected, but there is none.

//! Compile
//! Expect error equals "C++ (TNG)Error: File \"compileShouldFailEqual.cpp\". "
//!                     "Near or on line 10. "
//!                     "Apology: ExpId expression of type ref to int : "
//!                     "not a modifiable lvalue"

void  someFunc() 
{
	int i = 0 ;
	i = 1 ;
}