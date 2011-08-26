long strlen(char* p);

int main(){
	char* pHello = "Hello world!";
	long length = strlen(pHello);
	// ...etc
	return 0;
}/*#TA*/
long strlen(char* p){// Pointer notation
	long i=0;

	while (*(p+i))
		i++;
	return i;
}/*#/TA*/
