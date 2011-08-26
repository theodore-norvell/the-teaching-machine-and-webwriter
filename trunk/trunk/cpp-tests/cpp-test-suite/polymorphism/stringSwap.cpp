void swap(char* s1,char* s2) {	// special string swap
    int l1=strlen(s1),l2=strlen(s2);
    l1 = (l1>l2)?l1:l2;			// l1 is length of longest
    char* temp = new char[l1+1];
    strcpy(temp,s1);	// Now swap the actual strings
    strcpy(s1,s2);		// Note, fails badly if location
    strcpy(s2,temp);	// holding shorter string can't
    delete[] temp;		// hold the longer one!!!!
}
