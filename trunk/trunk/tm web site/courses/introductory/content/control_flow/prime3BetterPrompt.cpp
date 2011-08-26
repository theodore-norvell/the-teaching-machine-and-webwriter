/*#HA*/ /*******  for loop demonstration ********
    A function to test whether a number is prime

*******************************************/

#include <iostream>
using namespace std;
#include <cmath>

bool isPrime(long num);

int main(){
    long number;
    cout << "Input a positive integer (>1) to see if it is prime: ";
    do {
       cin >> number;
       if (number < 2) cout << "That is less than 2. Please try again. ";
    } while (number < 2);
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
	for(long i = 2; i <= root; i++)
		if (num % i == 0) prime = false;
	return prime;
}      
