/*#H*/#include <iostream>
using namespace std;

void improvedBubbleSort(int anArray[], int n);
/*#DA*/bool bubbleOnce(int array[], int n, int top);
void swap(int& arg1, int& arg2);
/*#HA*/const int SIZE = 10;
void dumpArray(int anArray[], int n);
void makeLine(int l);

int main(){
    int array[SIZE];  // = {27, 3, 17, 6, 12, 14, 9, 1, 21, 27};
    // TM doesn't support array init yet
    array[0]=27; array[1]=3; array[2]=17; array[3]=6; array[4]=12;
    array[5]=14; array[6]=9; array[7]=1; array[8]=21; array[9]=27;

    improvedBubbleSort(array, SIZE);
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** improvedBubbleSort **************************************
* @params: anArray - array of positive integers
*          n       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
************************************************************/

void improvedBubbleSort(int anArray[], int n){
     dumpArray(anArray, n);
     bool notDone = true;
     for(int i = 0; i < n && notDone; i++){
         notDone = bubbleOnce(anArray, n, i);
         dumpArray(anArray, n);
     }
}

/** bubbleOnce **********************************************
* @params: array - array of positive integers
*          n       - the size of the array @pre: non-negative
*          top    - the top of the sub-array to bubble
*
* @modifies: element in array from top to n-1 (sub-array)
*            pairs in sub-array will be swapped if uppermost
*            larger. Smallest value will bubble to top
* @returns: true if any swaps were carried out 
************************************************************/
bool bubbleOnce(int array[], int n, int top){
     bool swapsMade = false;
     for(int i = n-1; i > top; i--)
         if (array[i-1] > array[i]){
             swap(array[i-1], array[i]);
             swapsMade = true;
         }
     return swapsMade;
}/*#HA*/

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

/*#DB*//** dumpArray **************************************
* @params: anArray - array of positive integers
*           #pre: all values in array on [1,80]
*          n - size of the array
*
* @modifies: cout - prints each element of array as a 
*                    line of length anArray[i] 
**********************************************************/
void dumpArray(int anArray[], int n){
     for (int i = 0; i < n; i++)
         makeLine(anArray[i]);
     cout << endl << endl;
}

/** makeLine ********************************************
* @params: l - an integer @pre: 1 <= l <= 80
*
* @modifies: cout - prints l '-' followed by an endl
********************************************************/

void makeLine(int l){
     for (int i = 0; i < l; i++)
         cout << '-';
     cout << endl;
}
