/*#H*/#include <iostream>
using namespace std;

void selectionSort(int anArray[], int n);
/*#DA*/int findSmallest(int array[], int n, int top);
void swap(int& arg1, int& arg2);
/*#HA*/const int SIZE = 10;
void dumpArray(int anArray[], int n);
void makeLine(int l);

int main(){
    int array[SIZE];  // = {27, 3, 17, 6, 12, 14, 9, 1, 21, 27};
    // TM doesn't support array init yet
    array[0]=27; array[1]=3; array[2]=17; array[3]=6; array[4]=12;
    array[5]=14; array[6]=9; array[7]=1; array[8]=21; array[9]=27;

    selectionSort(array, SIZE);
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** selectionSort *******************************************
* @params: anArray - array of positive integers
*          n       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*************************************************************/

void selectionSort(int anArray[], int n){
     int posSmallest;     // position of smallest element in sub-array
     dumpArray(anArray, n);

     for(int i = 0; i < n; i++){
     // find position of smallest element left in sub-array between i and n
         posSmallest = findSmallest(anArray, n, i);
         if (anArray[posSmallest] < anArray[i])
            swap(anArray[posSmallest], anArray[i]);
         dumpArray(anArray, n);
     }
}/*#HA*/

/** findSmallest ********************************************
* @params: array - array of positive integers
*          n       - the size of the array @pre: non-negative
*          top    - the top of the sub-array to bubble
*
* @returns: position of first occurrence of smallest element 
*           in sub-array defined by locations top through n-1
*************************************************************/
int findSmallest(int array[], int n, int top){
    int mark = top;
    for(int i = top + 1; i < n; i++)
         if (array[i] < array[mark])
             mark = i;
    return mark;
}

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

/*#DB*//** dumpArray *************************************
* @params: anArray - array of positive integers
*           #pre: all values in array on [1,80]
*          n - size of the array
*
* @modifies: cout - prints each element of array as a line
*                    of length anArray[i] 
*********************************************************/
void dumpArray(int anArray[], int n){
     for (int i = 0; i < n; i++)
         makeLine(anArray[i]);
     cout << endl << endl;
}

/** makeLine ********************************************
* @params: l - an integer @pre: 1 <= l <= 80
*
* @modifies: cout - prints l '-' followed by an endl
*********************************************************/

void makeLine(int l){
     for (int i = 0; i < l; i++)
         cout << '-';
     cout << endl;
}
