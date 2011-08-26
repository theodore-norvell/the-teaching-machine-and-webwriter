// Fail.  Error message is not equal

//! Compile
//! Expect error equals "C++ (TNG)Error: File \"4b-f.cpp\". "
//!                       "Near or on line 11. Apology: ExpId expression "
//!                       "of type ref to int : not a modifiable lvalu"

void  someFunc() 
{
	const int i = 0 ;
	i = 1 ;
}