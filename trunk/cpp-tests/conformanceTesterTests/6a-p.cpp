// Pass. Expected run time error matches the pattern

//! Compile. Execute; expect error ~ /.*Definition for function or method not found.*/

void  notDefined() ;

int main() {
	notDefined() ;
	return 0 ;
}