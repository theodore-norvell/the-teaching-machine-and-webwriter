/*******  First Program ***********************
  To demonstrate  the teaching machine itself

**********************************************/
/*#HA*/ 
#include <iostream>
using namespace std;

bool biggerThan(int arg1, int arg2);

int main(){
	for (int i = 0; i < 5; i++) {
		int k = i*i*i-3*i*i +1;
		cout << "The proposition (" << i << " is bigger than " << k << ") is ";
		if (biggerThan(i,k))
			cout << "true";
		else cout << "false";
		cout << "." << endl;
	}
	return 0; 
}
 
/*#DA*/bool biggerThan(int arg1, int arg2){
	return arg1 > arg2;
}/*#H*/
