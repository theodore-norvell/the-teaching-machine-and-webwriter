void strCpy(char* pDest, const char* pSource);

char* salutation = "Hello there!";

int main(){
	char* buffer;
	double pi = 3.14159;

	strCpy(buffer,salutation);
	// Some other stuff
	return 0;
}

/*#TA*/void strCpy(char* pDest, const char* pSource){
	while (*pSource)
		*pDest++ = *pSource++;
	*pDest = '\0';
}/*#/TA*/
