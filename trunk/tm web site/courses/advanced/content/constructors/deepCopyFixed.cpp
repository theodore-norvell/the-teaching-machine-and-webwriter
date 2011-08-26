#include <iostream>
using namespace std;
// An example designed to show how to deep copy

class Array{
public:
	Array(int s);				// regular constructor
	Array(const Array& original);		// Copy constructor
	~Array();					// destructor - used for both
	int getSize() const;
	double read(int i) const;
	void write(int i, double item);
private:
	double* mpData;
	int mSize;
};

/*#TB*/ // A function to set every element of an array to value
void set(Array& array, double value);

int main(){
	Array A(10);
	set(A,3);
	Array B(A);		// B is a clone of A
	set(B,7);
/*#/TB*/	
	cout << "A: {";
	for (int j = 0; j < A.getSize()-1; j++)
		cout << A.read(j)<< ", ";
	cout << A.read(A.getSize()-1) << '}' << endl;
	return 0;
}

/*#TA*/ // Implementation
Array::Array(int s){
	if (s > 0 && (mpData = new double[s])) {
		mSize = s;
		for (int i=0; i < mSize; i++)
			mpData[i] = 0.;
	}
	else {
		mpData = 0;
		mSize = 0;
	}
}

// A "good" copy constructor, that is one which
// does a proper deep copy. Note contrast to constructor 
Array::Array(const Array& original){
	if (mpData = new double[original.mSize]){ // request same amt. of space
		mSize = original.mSize;		// success! we got the space
		for (int i = 0; i < mSize; i++)	// Copy original's heap data
			*(mpData+i) = *(original.mpData+i);
	}
	else mSize = 0;		// Oops! no space. Note the problem
}

Array::~Array(){	// Works for both kinds of constructor
	delete[] mpData;	// valid even if mpData is null
}/*#/TA*/


int Array::getSize() const { return mSize;}
double Array::read(int i)const {
	if (i<0 || i >= mSize) return 0.0;	// poor error detection
	return mpData[i];
}


void Array::write(int i, double item){
	if (i<0 || i >= mSize) return;
	mpData[i]=item;
}

void set(Array& array, double val){
	for (int i = 0; i < array.getSize(); i++)
		array.write(i,val);
} 
