#include <fstream>
using namespace std;


int openFile(ifstream& is);
int readDoubles(ifstream& is, double anArray[], int maxSize);

void sortDoubles(double theTable[], int size);
void swapDoubles(double& arg1, double& arg2);
void outArrayofDoubles(double theTable[], int size);
