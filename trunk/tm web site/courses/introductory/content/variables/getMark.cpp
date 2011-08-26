/*#DM*/ /******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * getMark.cpp -- Demonstrate cin
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.05.03
 *
 *******************************************************************/
#include <iostream>
using namespace std;

int grade(double assigns, double test, double exam);

int main(){
    double midterm;
    double final;
    double assigns;
    cout << "Please input the assignment mark [0, 12.0]: ";
    cin >> assigns;
    cout << endl << "and the midTerm mark [0, 30.0]: ";
    cin >> midterm;
    cout << endl << "and the final mark [0, 50.0]: ";
    cin >> final;
    cout << "This student got " << grade(assigns, midterm, final);
    cout << "% in the course.\n";
	return 0;
}/*#HM*/

/*#DA*/ /** grade ************************************************
 * @params: assigns - assignment mark @pre: on [0,10]
 *          test - midterm test @pre: on [0, 30]
 *          exam - final exam @pre: on [0, 100]
 *             
 * Returns: a grade on [0, 100]
 *********************************************************/
int grade(double assigns, double test, double exam){
    double mark = 10 * assigns/12 + test + 58 * exam/50;
    return (int) (mark + .5);
}/*#HA*/
