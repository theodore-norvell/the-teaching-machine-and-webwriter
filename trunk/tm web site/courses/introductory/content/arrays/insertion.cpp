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


void insertionSort(int anArray[], int n);
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

    insertionSort(array, SIZE);
    
    for (int i = 0; i < SIZE; i++)
        cout << array[i] << "  ";
    cout << endl;
    return 0;
}/*#DA*/
/** insertionSort *******************************************
* @params: anArray - array of positive integers
*          size       - the size of the array @pre: non-negative
*
* @modifies: values in anArray are sorted in ascending order
*************************************************************/

void insertionSort(int anArray[], int size){

     for(int i = 1; i < size; i++){
         int j = i;
/*#HA*/         setColour(j, MAGENTA);
/*#DA*/         int next = anArray[j];
         while (j > 0 && anArray[j-1] > next){
/*#HA*/               setColour(j, j-1, RED);
/*#DA*/               anArray[j] = anArray[j-1];
               j--;
/*#HA*/               setColour(j, GREEN);
/*#DA*/               
         }
         anArray[j] = next;
/*#HA*/         setColour(0, i, YELLOW);
/#DA*/     }
}
/*#HA*/

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

