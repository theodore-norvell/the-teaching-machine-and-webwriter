/*#HA*/ #include <iostream>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

double divider(double vS, double ru, double rl);


void runTest(){
     double r = 1000.;    // 1K resistor
     cout << "upper r is " << r/1000 << " Kohms" << endl;
     cout << "source voltage is 5 volts" << endl;    
     cout << "output for low r of 0 is " << divider(5, r, 0)<< " volts" << endl;
     cout << "output for low r of .1r is " << divider(5, r, .1*r)<< " volts" << endl;
     cout << "output for low r of r is " << divider(5, r, r)<< " volts" << endl;
     cout << "output for low r of 10r is " << divider(5, r, 10*r)<< " volts" << endl;
     cout << "output for really big low r is " << divider(5, r, 1e12*r)<< " volts" << endl;
     cout << endl;
}
         
/*#DA*/ /** divider ***************************************************
*
* @params: vs - source voltage
*          ru - the value of the upper resistor in the divider #pre: >= 0
*          rl - the value of lower resistor @pre: >= 0, same units as ru
*
* @returns: the value of the output voltage in the same units as vs
*****************************************************************/
/*#DA*/double divider(double vs, double ru, double rl) {
       return vs*rl/(ru+rl);
}/*#HA*/
