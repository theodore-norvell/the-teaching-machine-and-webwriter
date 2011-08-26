const int n = 25
int grades[n]		// Grades array shared between these routines

bool changeGrade(int mark, int i){
	if ( (i < 0) || (i >= n) ) return false;
	grades[i] = mark;
	return true;
}
double classAverage() {
	int i, sum;
	for (i=0, sum = 0; i<n;i++)
		sum += grades[i];
	return sum / n;
}
