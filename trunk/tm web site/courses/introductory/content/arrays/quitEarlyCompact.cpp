/*#H*/#include <iostream>
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



void improvedBubbleSort(int anArray[], int n);
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

    improvedBubbleSort(array, SIZE);
    
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** improvedBubbleSort **************************************
* @params: anArray - array of positive integers
*          size       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
************************************************************/

void improvedBubbleSort(int anArray[], int size){
     bool swapMade = true;  // Make sure the loop is done once
     /* start with subarray = whole array then move
        top down. Quit when all subarrays done or no
        swaps occur in a subarray */
     for(int top = 0; top < size-1 && swapMade; top++){
             // no swaps made yet on this subarray
         swapMade = false;
         /* Starting at bottom, bubble smallest value
            to top of subarray */
         for(int i = size - 1; i > top; i--){
    /*#HA*/         int k = i - 1;
             setColour(k, i, CYAN); 
    /*#DA*/         if (anArray[i-1] > anArray[i]){
    /*#HA*/                   setColour(k, i, RED); 
    /*#DA*/             swap(anArray[i-1], anArray[i]);
                 swapMade = true;
             }
    /*#HA*/                   setColour(k, i, GREEN); 
    /*#DA*/     }
/*#HA*/         setColour(top, YELLOW);     
/*#DA*/     }
/*#HA*/     setColour(size-1, YELLOW);     
/*#DA*/}
/*#HA*/

/** swap **************************************************
* @params: arg1 - reference to any integer
*          arg2 - reference to any integer
*
* @modifies: the values in the original variables referred 
*            to by arg1 and arg2 are swapped
***********************************************************/
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
