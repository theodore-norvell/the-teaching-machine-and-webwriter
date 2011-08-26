/*#H*/ /******* 

*******************************************/

/*#DA*/ #include <iostream>
using namespace std;    // cout is in the std namespace

int euclid(int larger, int smaller);

int main(){
    int larger = 25;
    int smaller = 15;
    int big = 27;
    int little = 12;
    int result;
    
    /* We do a whole bunch of different calls to illustrate
    that C++ normally passes by value.
    In each case, use the teaching machine to see what gets
    passed into the euclid function.
    */

// pass in a number    
    result = euclid(31, 17);    
    cout << "euclid(31, 17) is " << result << endl;

// pass in variables with the same name as the parameters    
    result = euclid(larger, smaller);    
    cout << "euclid(larger, smaller) is " << result << endl;

// pass in variables with names different from parameters    
    result = euclid(big, little);    
    cout << "euclid(big, little) is " << result << endl;

// pass in expressions   
    result = euclid(2*big + 4, 3*little);    
    cout << "euclid(2*big + 4, 3*little) is " << result << endl;

	return 0;
}/*#HA*/


/*#DB*/ /** euclid **************************************************

* @param: larger - Any positive integer
*         smaller - a positive integer, @pre: smaller <= larger
*
* @returns: the largest common divisor
****************************************************************/
int euclid(int larger, int smaller){
   while (smaller != 0) {
         int remainder = larger % smaller;
         larger = smaller;
         smaller = remainder;
   }
    return larger;
}/*#HB*/	

        
