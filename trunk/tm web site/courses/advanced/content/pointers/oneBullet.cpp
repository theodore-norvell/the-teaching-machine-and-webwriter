void charSwap(char* pLeft,char* pRight);


int main(){
	char a = 'a';
	char b = 'b';
	double pi = 3.14159;

	charSwap(&a, &b);
	// Some other stuff
	return 0;
}

/*#TA*/void charSwap(char* pLeft,char* pRight) {
	char* pTemp;
	*pTemp = *pLeft;
	*pLeft = *pRight;
	*pRight = *pTemp;
}/*#/TA*/

