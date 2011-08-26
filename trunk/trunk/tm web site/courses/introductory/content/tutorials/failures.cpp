#include <iostream>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

int failures(int marks[], int classSize);


void runTest(){
     const int SIZE = 20;
     int grades[SIZE];
     
     for (int g = 0; g < SIZE; g++)
         grades[g] = 100 * (int)((double)rand()/RAND_MAX + 0.5);
     cout << "The number of failures is " << failures(grades, SIZE);
     cout << endl;
}
         


/** failures ***************************************************
*
* @params: marks- an array of marks @pre: each mark on [0,100]
*          size - the class size @pre: > 0
*
* @returns: the number of marks < 50
*****************************************************************/
/*
failures = 0
for each mark
    if < 50
        failures ++
*/

int failures(int marks[], int classSize) {
       int countF = 0;
       for(int m = 0; m < classSize; m++)
           if (marks[m] < 50)
              countF++;
       return countF; 
}
