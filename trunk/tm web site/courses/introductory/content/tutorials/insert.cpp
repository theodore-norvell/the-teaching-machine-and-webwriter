/*#H*/ #include <iostream>
using namespace std;
void runTest();

int main(){
    runTest();
    return 0;
}

void insert(double value, double sorted[], int size);


void runTest(){
     const int SIZE = 20;
     double array[SIZE];
     double number;
// create a sorted array by creating random numbers and
// inserting them into the array one at a time     
     for (int i = 0; i < SIZE; i++){
         // generate a random number on [-100., 100.]
         number = -100.0 + 200.0 *((double)rand())/RAND_MAX;
         // insert it into the array which is now size i
         insert(number, array, i);
     }
     cout << '{';
     for (int j = 0; j < SIZE-1; j++)
         cout << array[j] << ", ";
     cout << array[SIZE-1] << "}\n";
     cout << endl;
}
         


void insertAt(int pos, double val, double array[], int size);

/*
The basic idea is to find where in the array we should insert the value
then shift all the values above that point up by one
then insert the value.
Note, however
1. the array could be empty in which case we just put the value in
2. It could be less than every value in which case it goes in front.
3. it could be larger than every value in which case it goes at back

if size is 0
    sorted[0] = value
otherwise
    for each position in the array, starting at 0
        if  value less than the one at the position
            insert it at position and QUIT
    if no insertion was made, add the value to the end
    
How do we insert at position?
insertAt(int position, double val, double array[], int size) --->

starting with the last element and working down until we get to position
    move element up by one
insert new value at position
*/


/** insert ***************************************************
*
* @params: value - any value
*          sorted - a sorted array of doubles
*          size - the current size of the array @pre: >= 0
*
* @modifies: the size of the array will increase by 1
*            the array will contain value
*            the array will still be sorted
*
* @returns: nothing
*****************************************************************/
/*#DA*/void insert(double value, double sorted[], int size) {
       if (size == 0)  // empty array, just stick value in
          sorted[0] = value;
       else {
            bool inserted = false; // haven't put it in yet
            for(int i = 0; i < size && !inserted; i++)
                if (value < sorted[i]){
                   insertAt(i, value, sorted, size);
                   inserted = true; // time to quit
                }
            if (!inserted) sorted[size] = value;
       }
}

/** insertAt ***************************************************
*
* @params: pos - place where new value to be inserted
*              @pre: 0 <= pos <= size
*          value - value (any) to be inserted
*          array - an array of doubles @pre: actual size >= size + 1
*          size - the current size of the array @pre: >= 0
*
* @modifies: the size of the array will increase by 1
*            the array will contain value at pos
*            all values from pos up shifted up by 1
*
* @returns: nothing
*****************************************************************/
void insertAt(int pos, double val, double array[], int size){
     for (int i = size; i > pos; i--)
         array[i] = array[i-1];
     array[pos] = val;
}/*#HA*/
