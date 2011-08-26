void strCpy(char* pDest, const char* pSource);/*#TA*/

struct mark {
	char course[5];
	int grade;
};

struct term {
	char startDate[11];
	int termno;
	double avg;
	mark marks[6];
};

struct student {
	char name[20];
	int id;
	term terms[10];
};

/*#/TA*/int main(){
/*#TB*/	student mary,john;

	strCpy(mary.name,"Mary Mathias");
	strCpy(john.terms[3].marks[2].course,"3891");
	john.terms[3].marks[2].grade=65;
	strCpy(mary.terms[3].startDate,"09.08.1992");
/*#/TB*/	// ...etc.
	return 0;
}
	
void strCpy(char* pDest, const char* pSource){
	while(*pDest++=*pSource++)
		;
}

