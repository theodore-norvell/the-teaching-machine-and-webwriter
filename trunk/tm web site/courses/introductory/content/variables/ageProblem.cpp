/** ageProblem ****************************************
*
* @descript: - a program to demonstrate the use of
*              variable declarations
*
* @author: Michael Bruce-Lockhart
* @date:   2006.04.22
******************************************************/
#include <iostream>
using namespace std;

int main(){
    double George = 12.0;
    double Samantha = 6.0;
    double years = 6.0;
    
    cout << "George is " << George/Samantha << " times as old as Samantha.\n";
    cout << "In " << years << " years George will be " ;
    cout << (George + years)/(Samantha + years) << " times as old as Samantha.\n";
    cout << "How old is Samantha?\n";
    
    return 0;
}
