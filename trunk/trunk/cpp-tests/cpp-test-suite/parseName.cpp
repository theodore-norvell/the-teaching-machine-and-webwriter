/*#H*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * parseName.cpp -- Demonstrate strings
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/
//#include <iostream>
#include <string>
using namespace std;

/*#DA*/ /*#DB*/const int MAX_NAMES = 8;
int parseName(string& name, string components[], int n);
/*#HA*/ /*#HB*/
int main() {
    string colleen( "Mary Frances Katherine O'Toole" ) ;
    string parts[MAX_NAMES];
    int names = parseName(colleen, parts, MAX_NAMES);
    cout << "Directory listing for colleen is ";
    cout << parts[0] << ", ";
    for (int i = 1; i < names; i++) {
        cout << parts[i];
        if (i< names-1) cout << " ";
    }
    cout << endl;
           
  return 0;
}
/*#DB*/
/** parseName *************************************************
*
* @params: name - a string representing a full name
*          @pre: western standard form, family name last,
*                single space separated, no extraneous spaces
*          components[] - an array of strings
*          @pre: large enough to hold all individual names
* 
* @modifies: components array will have individual names,
*            family name in postion 0, followed by given
*            names in order
*
* @returns: the number of names found
****************************************************************/
/*#DA*/           
int parseName(string& name, string components[], int n){
    int names = 1; // the number of name components - must be at least 1
    int start = 0;   // start from position 0;
    int next;
    do {
        next = name.find(" ", start);        // find NEXT space
        if (next == string::npos){ // at end, family name
            components[0] = name.substr(start,name.length()-start);
        } else {
            components[names] = name.substr(start, next-start);
            start = next + 1; // start past the space
            names++;
        }
    } while (next != string::npos);
    return names;
}/*#H*/
