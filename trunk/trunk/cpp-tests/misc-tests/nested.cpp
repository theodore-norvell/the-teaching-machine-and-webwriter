#include <iostream>   /* include info from standard library */
using namespace std;

struct Node {
     int data[3];
     char name[20];
     Node *next;
};

struct ComplexNode {
    Node left;
    Node right;
};


void main() {

int a[5];
ComplexNode b[3];

	for (int i = 0; i < 5; i++)
		a[i] = i;
	for (int j = 0; j < 3; j++){
	    for (int k = 0; k < 3; k++) {
	        b[j].left.data[k] = 10*j + k;
	        b[j].right.data[k] = 10*j - k;
	    }
	    b[j].left.next = &b[j].right;
		b[j].right.next = &b[j].left;
	}
	int c = 3;
	int d = 4;
	int e = 5;
	cout << "a[0] = "  << a[0] << " & b[0] = " << b[0];

}
