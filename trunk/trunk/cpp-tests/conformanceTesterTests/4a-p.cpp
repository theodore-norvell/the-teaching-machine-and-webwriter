// Pass -- on TM. Compile time error message is equal.

//! Compile
//! Expect error equals "C++ (TNG)Error: File \"4a-p.cpp\". "
//!                       "Near or on line 11. Apology: ExpId expression "
//!                       "of type ref to int : not a modifiable lvalue"

void  someFunc() 
{
	const int i = 0 ;
	i = 1 ;
}