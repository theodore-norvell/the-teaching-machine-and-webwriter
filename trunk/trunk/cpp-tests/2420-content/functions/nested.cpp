#include <iostream.h>   /* include info from standard library */

/*#B="nodeScope"*/struct /*#b="nodeScope"*/Node/*#/b*/ {
     int data[3];
     char name[20];
     Node *next;
};

/*#B="complexNodeScope"*/struct /*#b="complexNodeScope"*/ComplexNode/*#/b*/ {
    Node left;
    Node right;
};


void main() {

/*#B="aScope"*/int /*#b="aScope"*/a[5]/*#/b*/;
/*#B="bScope"*/ComplexNode /*#b="bScope"*/b[3]/*#/b*/;

	for (int /*#B="iScope"*/ /*#b="iScope"*/i/*#/b*/ = 0; i < 5; i++)
		a[i] = i;/*#/B*/
	for (int /*#B="jScope"*/ /*#b="jScope"*/j/*#/b*/ = 0; j < 3; j++){
	    for (int /*#B="kScope"*/ /*#b="kScope"*/k/*#/b*/ = 0; k < 3; k++) {
	        b[j].left.data[k] = 10*j + k;
	        b[j].right.data[k] = 10*j - k;
	    }/*#/B*/
	    b[j].left.next = &b[j].right;
		b[j].right.next = &b[j].left;
    }/*#/B*/
    b[0].left.name = "Theo";
    b[1].left.name = "Michael";
    b[2].left.name = "Dennis";
    b[0].right.name = "Gary";
    b[1].right.name = "Ted";
    b[2].right.name = "Alice";
    int /*#B="cScope"*/ /*#b="cScope"*/c/*#/b*/ = 3;
	int /*#B="dScope"*/ /*#b="dScope"*/d/*#/b*/ = 4;
	int /*#B="eScope"*/ /*#b="eScope"*/e/*#/b*/ = 5;
	swap(a[2],b[2]);
	swap(b[0],c);
	swap(d,e);
	swap(e,a[4]);
	cout << "a[0] = "  << a[0] << " & b[0] = " << b[0];

}/*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/ /*#/B*/

void swap(int& /*#B="a1Scope"*/ /*#b="a1Scope"*/a1/*#/b*/, int& /*#B="a2Scope"*/ /*#b="a2Scope"*/a2/*#/b*/){
	int /*#B="tempScope"*/ /*#b="tempScope"*/temp/*#/b*/;
	temp = a1;
	a1 = a2;
	a2 = temp;
}/*#/B*/ /*#/B*/ /*#/B*/
/*#/B*/ /*#/B*/