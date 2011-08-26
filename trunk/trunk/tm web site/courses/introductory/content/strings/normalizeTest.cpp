#include <iostream>
using namespace std;
#include "normalize.h"

int main(){
	string michael =  "  Michael   Patrick  Bruce-Lockhart   ";
	normalize(michael);
	cout << michael;
	system("PAUSE");
	return 0;
}
