long strlen(char str[]);

int main(){
	char* hello = "Hello world!";
	long length = strlen(hello);
	// ...etc
	return 0;
}
/*#TA*/
long strlen(char str[]){// Array notation
	long i=0;

	while (str[i])
		i++;
	return i;
}/*#/TA*/
