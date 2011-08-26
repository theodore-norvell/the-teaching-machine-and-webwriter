/*#H*/#include <iostream>
using namespace std;

void bubbleSort(double anArray[], int n);

/*#DA*/void bubblePass(double array[], int n, int top);
void swap(double& arg1, double& arg2);
/*#HA*/const int SIZE = 10;

int main(){
    double array[SIZE];  // = {12, 3, 8.9, 5.4, -1.1,14.4, 9.6, 17.2, 21.1, 27.7};
    // TM doesn't support array init yet
    array[0]=12; array[1]=3; array[2]=8.9; array[3]=5.4; array[4]=-1.1;
    array[5]=14.4; array[6]=-9.6; array[7]=-17.2; array[8]=21.1; array[9]=27.2;

    bubbleSort(array, SIZE);
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** bubbleSort **********************************************
* @params: anArray - array of arbitrary doubles
*          n       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*************************************************************/

void bubbleSort(double anArray[], int n){
     for(int i = 0; i < n - 1; i++){
         bubblePass(anArray,n,i);
     }
}

/** bubblePass *********************************************
* @params: array - array of arbitrary doubles
*          n       - the size of the array @pre: non-negative
*          top - the position in the array from which to
*                  start the pass
*
* @modifies: the smallest value in the subArray starting at
*            top will be located at top. Other values will
*             be moved as necessary
***********************************************************/
void bubblePass(double array[], int n, int top){
     for (int i = n-1; i > top; i--)
         if (array[i] < array[i-1])
            swap(array[i], array[i-1]);
}

void swap(double& arg1, double& arg2){
     double hold = arg1;
     arg1 = arg2;
     arg2 = hold;
}
