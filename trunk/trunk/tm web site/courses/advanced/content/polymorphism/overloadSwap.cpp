/*#TA*/ // The declarations
void swap(char& left, char& right);
void swap(int& left, int& right);
void swap(long& left, long& right);
void swap(float& left, float& right);
void swap(double& left, double& right);/*#/TA*/

// And some calls to various swaps
int main(){
/*#TC*/	int i1 = 3;
	int i2 = 5;
	char c1 = 'a';
	char c2 = 'z';
	float f1 = 3.14;
	float f2 = 0.;
	double d1 = 3.14;
	double d2 = 0.;

	swap(i1,i2);
	swap(c1,c2);
	swap(d1,d2);
//	swap(d1,i2);	//ILLEGAL! Compiler will complain
	swap(f1,f2);/*#/TC*/
}

// The implementations
/*#TB*/void swap(char& left, char& right){
	char temp;
	temp = left;
	left = right;
	right = temp;
}

void swap(int& left, int& right) {
	int temp;
	temp = left;
	left = right;
	right = temp;
}/*#/TB*/

void swap(long& left, long& right){
	long temp;
	temp = left;
	left = right;
	right = temp;
}
void swap(float& left, float& right){
	float temp;
	temp = left;
	left = right;
	right = temp;
}
void swap(double& left, double& right){
	double temp;
	temp = left;
	left = right;
	right = temp;
}

