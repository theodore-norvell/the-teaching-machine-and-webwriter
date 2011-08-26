//! Run. Expect error matches /.*Near or on line 17\. Apology: Pointer dereference fails.*/.
/*#HA*/void charSwap(char* pLeft,char* pRight);


int main(){
	char a = 'a';
	char b = 'b';
	double pi = 3.14159;

	charSwap(&a, &b);
	// Some other stuff
	return 0;
}

/*#DA*/void charSwap(char* pLeft,char* pRight) {
	char* pTemp;
	*pTemp = *pLeft;
	*pLeft = *pRight;
	*pRight = *pTemp;
}

