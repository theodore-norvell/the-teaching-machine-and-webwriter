/*#HA*/ #include <iostream>
#include <cmath>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

void outputReport(double vPeak, double thetaV, double iPeak, double thetaI);
double phasorPower(double vPeak, double thetaV, double iPeak, double thetaI);
const double PI = 4 * atan(1.0);

void runTest(){
     double v = 10;
     double angleV = 0;
     double i = 30;
     double angleI = PI/3;
     outputReport(v, angleV, i, angleI);
     angleV = angleI + PI/2;
     outputReport(v, angleV, i, angleI);
     cout << endl;
}

void outputReport(double vPeak, double thetaV, double iPeak, double thetaI){
     cout << "power generated for a voltage of " << vPeak << "L " << thetaV;
     cout << " and a current of " << iPeak << "L " << thetaI << " is ";
     cout << phasorPower(vPeak, thetaV, iPeak, thetaI) << " watts.\n";
}

         
/*#DA*/ /** phasorPower ***************************************************
*
* @params: vPeak - peak phasor voltage in volts
*          iPeak - peak phasor current in amps
*          thetaV - the angle of the phasor voltage in radians
*          thetaI- the angle of the phasor current in radians
*
* @returns: the value of the phasor power in watts
*****************************************************************/
/*#DA*/double phasorPower(double vPeak, double thetaV, double iPeak, double thetaI) {
       return sqrt(vPeak*iPeak) * cos (thetaV - thetaI);
}/*#HA*/
