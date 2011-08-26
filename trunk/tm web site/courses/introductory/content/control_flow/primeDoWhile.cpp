/*#HA*/ /*******  for Exercise 2b ********
    A function to test whether a number is prime

*******************************************/

#include <iostream>
using namespace std;
#include <cmath>

bool isPrime(long num);

int main(){
    long number;
    cout << "Input a positive integer (>1) to see if it is prime: ";
    cin >> number;
    if (isPrime(number))
       cout << "\nIt is a prime number.\n";
    else
       cout << "\nIt is not a prime number.\n";
//    system("PAUSE");   uncomment for dev-cpp
	return 0;
}/*#HM*/

/*#DA*/ /** isPrime **************************************************
*
* @param: num @pre: num must be a non-negative integer
*
* @returns: true if num is prime, false otherwise
****************************************************************/
bool isPrime(long num){
	bool prime = true;
	long root = sqrt((double)num);
	if (num == 1) return true;
	int i = 2;
	do{
		if (num % i == 0) prime = false;
		i++;
    } while(i <= root);
    return prime;
}      
