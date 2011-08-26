#include <iostream>
using namespace std;

int middle1(int a, int b, int c);
int middle2(int a, int b, int c);
void test(int a1, int a2, int a3);

int main(){

	int first = 1;
	int second = 2;
	int third = 3;

	// Test most reasonable combinations.
	test(first, second, third);
	test(first, third, second);
	test(second, first, third);
	test(second, third, first);
	test (third, first, second);
	test(third, second, first);
	test(first, first, second);
	test(first, second, second);
	test(third, second, first);
	test(third, third, third);

	return 0;
}


void test(int a1, int a2, int a3){
	cout << "middle of (" << a1 << ", " << a2 << ", " << a3 << ") is ";
	cout << middle1(a1, a2, a3) << " by middle1 & ";
	cout << middle2(a1, a2, a3) << " by middle2." << endl;
}



int middle1(int a, int b, int c){
	int mid;
	if (b<a && a<c)
		mid = a;
	else if(a<b && b<c)
		mid = b;
	else if(a<c && c<b)
		mid = c;
	else if (c<a && a<b)
		mid = a;
	else if(c<b && b<a)
		mid = b;
	else if(b<c && c<a)
		mid = c;
	else if(a==b || a==c)
		mid = a;
	else mid = b;
	return mid;
}


int middle2(int a, int b, int c){
	if (b<=a && a<=c || c<=a && a<=b)
		return a;
	if (a<=b && b<=c || c<=b && b<=a)
		return b;
	return c;
}
