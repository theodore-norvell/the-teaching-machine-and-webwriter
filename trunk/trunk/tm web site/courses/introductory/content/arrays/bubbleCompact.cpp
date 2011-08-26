/*#H*/ #include <iostream>
#include <stdlib.h>
#include <ScriptManager>
using namespace std;

// ArrayBar display colours
const int BLACK = 0;
const int BLUE = 1;
const int GREEN = 2;
const int RED = 4;
const int CYAN = 3;
const int MAGENTA = 5;
const int YELLOW = 6;
const int WHITE = 7;

// Teaching Machine control routines  
void setColour(int i, int colour); // set i'th element to colour
void setColour(int from, int to, int colour); // set all elements between i and j to colour

void bubbleSort(int anArray[], int n);
void swap(int& arg1, int& arg2);

const int SIZE = 10;

// Values in array will be between MIN_VALUE and MAX_VALUE
const int MIN_VALUE = 10;
const int MAX_VALUE = 50;

int main(){
    int array[SIZE];

// For technical reasons this is actually quite poor
    for (int i = 0; i < SIZE; i++)
        array[i]= MIN_VALUE + rand()%(MAX_VALUE - MIN_VALUE);

    bubbleSort(array, SIZE);
    
   for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}

/*#DA*/ /** bubbleSort **********************************************
* @params: anArray - array of arbitrary doubles
*          size       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*************************************************************/

void bubbleSort(int anArray[], int size){
     /* Start with a subArray equal to the whole array,
        then move the top down on each pass */
     for(int top = 0; top < size - 1; top++){
         /* starting from the bottom, bubble the smallest
            value to the top of the sub-array */
         for (int i = size-1; i > top; i--){
/*#HA*/         int k = i-1;
         setColour(k, i, CYAN); 
/*#DA*/         if (anArray[i] < anArray[i-1]){
/*#HA*/         setColour(k, i, RED);
/*#DA*/                     swap(anArray[i], anArray[i-1]);
                 }
/*#HA*/                 setColour(k, i, GREEN);
/*#DA*/         }
/*#HA*/         setColour(top, YELLOW);     
/*#DA*/   }
/*#HA*/     setColour(size-1, YELLOW); // make sure last one reverts to yellow
/*#DA*/ }
/*#HA*/

void swap(int& arg1, int& arg2){
     int hold = arg1;
     arg1 = arg2;
     arg2 = hold;
}

/*** Teaching Machine control routines ****************************************/ 

// set i'th element to colour     
void setColour(int i, int colour){
     ScriptManager::relay("MUN.PlugIn.ArrayBar", "setColour", i, colour);
}

 // set all elements between i and j to colour      
void setColour(int from, int to, int colour){
     ScriptManager::relay("MUN.PlugIn.ArrayBar", "setColour", from, to, colour);
}
