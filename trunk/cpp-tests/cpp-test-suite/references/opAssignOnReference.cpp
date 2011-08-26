//! Run. Expect output = "14.0\n".

#include <iostream>
using namespace std;

int main()
{

    double a = 7.0 ;
    double& b = a ;
    b *= 2.0 ;
    cout << a << endl ;
	return 0;
}
