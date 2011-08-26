/* This is just stuck in here
to syntax colouring */
#include <iostream.h>

long strLength(char* str);

void strReverse(char* reverse, const char* original);

char reverse[20];   // a place to put reversed stringsvoid

void main(){
    char* testString = "Test!\n";
    int length;
    length = strLength(testString);
    strReverse(reverse,testString);
}

long strLength(char* str){
	long i;
    for(i=0; *(str+i); i++)
        ;
	return i;
}

void strReverse(char* reverse, const char* original){
	char* tail = original;		// tail of the original string

    while (*tail) // move to the back of original string
        tail++;
    while(--tail>=original)		// Copy characters from tail of 
        *reverse++ = *tail;		// original to front of reverse
    *reverse = '\0' ;			// Don't forget to terminate!
}