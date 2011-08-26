// Pass. Expected run time error equals exactly

//! Compile. Execute; expect error
//!     "C++ (TNG) Error: File \"7a-p.cpp\". "
//!     "Near or on line 11. Compilation error: "
//!     "Definition for function or method not found. Key is ::notDefined$void()"

void  notDefined() ;

int main() {
	notDefined() ;
	return 0 ;
}