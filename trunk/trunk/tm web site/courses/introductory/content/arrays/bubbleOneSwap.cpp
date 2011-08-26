/*#H*/#include <iostream>
using namespace std;

void selectionSort(double anArray[], int n);

int findSmallest(double array[], int n, int top);

/*#DA*/void swap(double& arg1, double& arg2);
/*#HA*/const int SIZE = 10;

int main(){
    double array[SIZE];  // = {12, 3, 8.9, 5.4, -1.1,14.4, 9.6, 17.2, 21.1, 27.7};
    // TM doesn't support array init yet
    array[0]=12; array[1]=3; array[2]=8.9; array[3]=5.4; array[4]=-1.1;
    array[5]=14.4; array[6]=-9.6; array[7]=-17.2; array[8]=21.1; array[9]=27.2;

    selectionSort(array, SIZE);
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** bubbleSort ******************************************************
* @params: anArray - array of arbitrary doubles
*          n       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*********************************************************************/

void selectionSort(double anArray[], int n){
     int smallest;
     for(int i = 0; i < n; i++){
         smallest = findSmallest(anArray,n,i);
         if (smallest != i)
            swap(anArray[smallest, anArray[i]);
     }
}

/** findSmallest ******************************************************
* @params: array - array of arbitrary doubles
*          n       - the size of the array @pre: non-negative
*          top - the position in the array from which to start the search
*
* @returns: the position in the array >= top holding the first
*           occurrence of the smallest value in the searched portion
*********************************************************************/
int findSmallest(double array[], int n, int top){
    int mark = top;   // smallest so far
     for (int i = top + 1; i < n; i++)
         if (array[i] < array[top])
            mark = i;
     return mark;
}

void swap(double& arg1, double& arg2){
     double hold = arg1;
     arg1 = arg2;
     arg2 = hold;
}
