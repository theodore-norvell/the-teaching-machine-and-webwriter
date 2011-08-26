//! Run. Expect error matches /.*Near or on line 17\. Apology: Pointer dereference fails.*/
/*#HA*/void strCpy(char* pDest, const char* pSource);

char* salutation = "Hello there!";

int main(){
	char* buffer;
	double pi = 3.14159;

	strCpy(buffer,salutation);
	// Some other stuff
	return 0;
}

/*#DA*/void strCpy(char* pDest, const char* pSource){
	while (*pSource)
		*pDest++ = *pSource++;
	*pDest = '\0';
}
