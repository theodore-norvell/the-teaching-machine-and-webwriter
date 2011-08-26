#include <iostream>
using namespace std ;

/* Static Storage Objects */

static  int    k=25; /* Declaration of a static variable 'k' */
        int    l;    /* Declaration of a global variable 'l' */

int f1();
 
void main() 
{         
  int i; /* Declaration of local(not static) variable  */
  i=1;
 l=f1();
 l=f1();
 cout<<l<<'\n';
}
 
int f1()
{ static int m=0;
  int n;
  m++;
  cout<<m<<'\n';
  return(m);
}