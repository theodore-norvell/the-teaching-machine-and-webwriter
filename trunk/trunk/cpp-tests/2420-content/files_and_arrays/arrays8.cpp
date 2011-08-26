double sumOf(double xArray[],int r, int c) {
	double sum;
	for (int i = 0;i<r;i++)
	for (int j=0;j<c;j++)
		sum += xArray[i+r*j];
	return sum;
}
