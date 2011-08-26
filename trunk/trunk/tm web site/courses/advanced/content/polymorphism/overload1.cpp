/*#TA*/ /* A pair of declarations with identical signature.
  The compiler will complain! */
int f1(int i);		// signature is int
double f1(int a);	// signature is int

//	Order of arguments does matter.

void f2(double a); 				// double
void f2(int a,double b);		// int,double
void f2(double a,int b);		// double,int
/*#/TA*/
/*#TB*/f2(3.14159);	// exact match to double
f2(6);	// promotes and uses double
f2(3.7,9);	// matches double,int
f2(1,13.1);	// matches int,double
long i = 3;
f2(i,4.1);	// converts and uses int,double
f2(3,4);	// ambiguous, no match
f2(1.7,-3.2);	// ambiguous, no match
f2(3,4,5);	// no match for three arguments/*#/TB*/
