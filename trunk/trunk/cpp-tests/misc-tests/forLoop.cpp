/*******  for loop demonstration ********
    NOTE: the loop itself is written in an
    abnormal style. Normally we would write
    it on one line, like this--
        
    for (int i=0; i<10; i++)
    
    but this should help you see how the
    three statements work
*******************************************/
#include <iostream.h>   // info from standard library
using namespace std;    // cout is in the std namespace

void main(){
    int factorial = 1;

// Output a table heading
    cout << "Table of Factorials\n  i\t  i!\n\n";

    for (int i = 0;     // initialisation
            i < 10;     // test before each loop
                i++)   //increment after each loop
    {
        if (i !=0)
            factorial *= i;
        cout << i << "\t" << factorial << '\n';
    }
}
        