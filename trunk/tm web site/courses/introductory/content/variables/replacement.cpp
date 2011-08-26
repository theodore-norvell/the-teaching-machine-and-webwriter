/*#H*/ /*******************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * clients.cpp -- Demonstrate function calling
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.04.22
 *
 ***************************************************************/
/*#DB*/ #include <iostream>
using namespace std;

int byTwo(int n);

int main() {
    cout << "increase\tby two\n";
    cout << 10 << "\t\t" << byTwo(10)<<'\n';
    cout << 7 << "\t\t" << byTwo(7)<<'\n';
    cout << -120 << "\t\t" << byTwo(-120)<<'\n';
    return 0;
}/*#HB*/

/*#DA*/ /** byTwo **************************************************
*
* @params: n - any integer
*
* @returns: the original integer plus 2
*************************************************************/

int byTwo(int n){
    n = n + 2;
    return n;
}/*#HA*/
    
