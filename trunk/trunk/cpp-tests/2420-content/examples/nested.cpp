#include <iostream.h>   /* include info from standard library */

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
    b[0].left.name = "Theo";
    b[1].left.name = "Michael";
    b[2].left.name = "Dennis";
    b[0].right.name = "Gary";
    b[1].right.name = "Ted";
    b[2].right.name = "Alice";
    int c = 3;
	int d = 4;
	int e = 5;
	swap(a[2],b[2]);
	swap(b[0],c);
	swap(d,e);
	swap(e,a[4]);
	cout << "a[0] = "  << a[0] << " & b[0] = " << b[0];

}

void swap(int& a1, int& a2){
	int temp;
	temp = a1;
	a1 = a2;
	a2 = temp;
}
