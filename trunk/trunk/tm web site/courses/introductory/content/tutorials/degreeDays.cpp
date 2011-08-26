#include <iostream>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

double degreeDays(double averages[], int days);


void runTest(){
     const int DAYS = 20;
     double temps[DAYS];
     
     for (int d = 0; d < DAYS; d++)
         temps[d] = -40 + 70 * ((double)rand())/RAND_MAX;
     cout << "The number of degree days is " << degreeDays(temps, DAYS);
     cout << endl;
}
         


/** degreeDays ***************************************************
*
* @params: averages- an array of daily average temps
*          days - the number of days represented by the array
*
* @returns: the number of degree-days represented by the array
*****************************************************************/
/*
degreeDays = 0
for each day of the year
    if average temp < 20
          add 20-average to degreeDays
*/

double degreeDays(double averages[], int days) {
       double dDays=0;
       for(int d = 0; d < days; d++)
           if (averages[d] < 20)
              dDays += (20-averages[d]);
       return dDays; 
}
