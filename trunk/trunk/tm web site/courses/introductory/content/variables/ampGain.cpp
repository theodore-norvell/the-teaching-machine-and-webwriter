/*#HA*/
#include <iostream> 
using namespace std;

double ampGain (double rF, double rG, double r1, double r2);


int main(){
    cout << "The gain of an amplifier for which Rf and Rg are 10K, and ";
    cout << "R1 and R2 are 1K is " << ampGain(10000, 10000, 1000, 1000);
	return 0;
}

/*#DA*/
/** ampGain *******************************
*
* @params: rF - feedback resistor in ohms @pre > 0
*          rG - ground resistor in ohms @pre >0
*          r1 - -ve input resistor in ohms, @pre > 0
*          r2 - +ve input resistor in ohms, @pre > 0
*
* @returns: Differential amplifier gain
*******************************************/ 

double ampGain (double rF, double rG, double r1, double r2){
    double term1 = rG*rF;
    double term2 = rF*r2;
    
	return (rF/r1)*(2*term1 + rG*r1 + term2)/(term1 + term2);
}
