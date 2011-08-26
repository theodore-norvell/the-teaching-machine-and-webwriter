void myFunction(int x, int y) {
	double myArray[1000]	// Dangerous! Just asked for 8000 bytes
	// ...etc			//	from stack
}

double myArray[1000]	// Safe. All external variables are static
void myFunction(int x, int y) {
	// ...etc
}

void myFunction(int x, int y) {
static double myArray[1000]	// Safe. Is in static store.
	// ... etc
}