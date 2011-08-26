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


/*#DA*/void selectionSort(int anArray[], int n);
int findSmallest(int array[], int n, int top);
void swap(int& arg1, int& arg2);
/*#HA*/const int SIZE = 10;
// Values in array will be between MIN_VALUE and MAX_VALUE
const int MIN_VALUE = 10;
const int MAX_VALUE = 50;

int main(){
    int array[SIZE];
// For technical reasons this is actually quite poor
    for (int i = 0; i < SIZE; i++)
        array[i]= MIN_VALUE + rand()%(MAX_VALUE - MIN_VALUE);

    selectionSort(array, SIZE);
    
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** selectionSort *******************************************
* @params: anArray - array of positive integers
*          size       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*************************************************************/

void selectionSort(int anArray[], int size){
     int posSmallest;

     for(int i = 0; i < size; i++){
         posSmallest = findSmallest(anArray, size, i);
/*#HA*/         setColour(i, CYAN);
/*#DA*/         if (anArray[posSmallest] < anArray[i]){
/*#HA*/            setColour(posSmallest, RED);
            setColour(i, RED);
/*#DA*/            swap(anArray[posSmallest], anArray[i]);
         }
/*#HA*/         setColour(posSmallest, GREEN);
         setColour(i, YELLOW);
/*#DA*/     }
}

/** findSmallest ********************************************
* @params: array - array of positive integers
*          n       - the size of the array @pre: non-negative
*          top    - the top of the sub-array to bubble
*
* @returns: position of first occurrence of smallest element 
*           in sub-array defined by locations top through n-1
*************************************************************/
int findSmallest(int array[], int n, int top){
/*#HA*/    setColour(top, n, GREEN);
/*#DA*/    int mark = top;
/*#HA*/    setColour(mark, MAGENTA);
/*#DA*/
    for(int i = top + 1; i < n; i++){
/*#HA*/            setColour(i, CYAN);
/*#DA*/            if (array[i] < array[mark]){
/*#HA*/               setColour(mark, GREEN);
/*#DA*/               mark = i;
/*#HA*/               setColour(mark, MAGENTA);
/*#DA*/            } /*#HA*/else setColour(i, GREEN);/*#DA*/
    }
    return mark;
}/*#HA*/

/** swap ***************************************************
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

