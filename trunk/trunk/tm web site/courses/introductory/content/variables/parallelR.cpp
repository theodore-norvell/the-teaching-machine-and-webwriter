/*#HA*/ #include <iostream>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

double parallel(double r1, double r2);


void runTest(){
     double r = 1000.;    // 1K resistor
     cout << "r is " << r/1000 << " Kohms" << endl;
     cout << "r||r is " << parallel(r, r)<< " ohms" << endl;
     cout << "r||10r is " << parallel(r, 10*r)<< " ohms" << endl;
     cout << "r||0 is " << parallel(r, 0)<< " ohms" << endl;
     cout << "r||really big resistor is " << parallel(r, 1e12*r)<< " ohms" << endl;
     cout << endl;
}
         
/*#DA*/ /** parallel ***************************************************
*
* @params: r1, r2 - any resistor values @pre: >= 0, same units
*
* @returns: the value of r1 || r2 in the same units as r1, r2
*****************************************************************/
/*#DA*/double parallel(double r1, double r2) {
       return r1*r2/(r1+r2);
}/*#HA*/
