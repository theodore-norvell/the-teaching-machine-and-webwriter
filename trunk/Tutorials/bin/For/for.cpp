#include<iostream>
#include<string>
using namespace std;
/*#TS*/#include"PDV_final.h" /*#/TS*/

	bool isPrime(int);
	int calcSum(int);
	


int main() {
	//for loop example1
	bool b=isPrime(7);
	//for loop example2
	//calculating the sum of first n natural numbers
	int sum=0;
	sum = calcSum(6);

}

void blank(){
	
	return;
}
/** Precondition: num > 1 */
	bool isPrime(int num){ 
	bool result = true ;
	for(int div=2; div<num; div++){ 
		result = result && num%div != 0; 
	} 
	return result ; 
}


int calcSum(int n){
	int result=0;
	for(int i=1;i<=n;i++)
	{
		result=result+i;
	}
	
	return result;
}




