//! Compile; expect error matches /TODO/
void f1()
{ 
	static int m=0;
	return 0 ;
}
 
void main() 
{     
    f1();
}