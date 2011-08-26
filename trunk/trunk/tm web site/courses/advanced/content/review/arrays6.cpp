	/************* Duplicating an array *******************/

int a[5] = {0,3,17,51,-6};
int b[5];

b = a;		/* This is ILLEGAL!!!
 Instead
*/
	for (int i = 0;i<5;i++)
		b[i] = a[i];
