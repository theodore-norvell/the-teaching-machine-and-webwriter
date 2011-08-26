int cmpStr(const char* pS1, const char* pS2){
	char* pS3 = pS1;		// copy string1
	while (*pS1 ==*pS2 && *pS1) {pS1++; pS2++;}
	while (*pS3) *pS3++ = ' '; // overwrite string1!!!!
	return (*pS1==*pS2) ? 0 : (*pS1>*pS2) ? 1 : -1 ;
}
