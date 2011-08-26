/*#TA*/template <class T>

/* Restrictions on T:
	1. must be capable of being an l-value
	2. must copy construct properly
	3. must assign properly
*/

void swap(T& a1,T& a2) {
    T temp = a1;  
    a1 = a2;
    a2 = temp;
} /*#/TA*/


void main(){
/*#TB*/    int i=3,j=5;
	const int ONE =1;
	const int TWO = 2;
    char a='c',b='\n';
    double pi=3.14159,x=1/3.;
    char *s1="Hello ", *s2="world!";

    swap(i,j);		// legal, integer version
    swap(pi,x);		// legal, double version
    swap(a,b);		// legal, character version
//    swap(i,a);		// illegal, no mixed version, can't convert references
	swap(s1,s2);	// legal, only the pointers are swapped
//    swap(ONE, TWO);		// illegal, 1 & 2 can't be l-values
/*#/TB*/}
