//! Run.
/*#HA*/long strlen(char* p);

int main(){
	char* pHello = "Hello world!";
	long length = strlen(pHello);
	// ...etc
	return 0;
}/*#DA*/
long strlen(char* p){// Pointer notation
	long i=0;

	while (*(p+i))
		i++;
	return i;
}
