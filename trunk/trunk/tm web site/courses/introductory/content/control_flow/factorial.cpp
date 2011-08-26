/*#HA*/#include <iostream>
using namespace std;

long factorial(int num);

int main(){
    cout << "The factorial of 3 is " << factorial(3) << endl;
    cout << "while the factorial of 10 is " << factorial(10) << endl;
    return 0;
}
/*#DA*/ /***** factorial ****************************************
*	@param: num - a positve integer: number > 0
*
*	@returns: the factorial of num
*
***********************************************************/
long factorial(int num){
	long fact = 1;

    while (num > 1)  {
        fact *= num;
        num--;
    }
    return fact;     
}
