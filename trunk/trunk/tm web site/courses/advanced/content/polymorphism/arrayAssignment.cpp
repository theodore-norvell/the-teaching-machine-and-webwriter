#include <iostream>
using namespace std;

 class Array{
public:
	Array();					// "default" constructor
	Array(int s);				// regular constructor
	Array(const Array& original);		// Copy constructor
	~Array();					// destructor - used for both
	
	int getSize() const;

// operator overloads
	double& operator[](int i); // This replaces read AND write!
	Array operator+(const Array& rhs);
/*#TA*/	Array& operator=(const Array& rhs);	// Assignment
/*#/TA*/private:
	double* mpData;
	int mSize;
};

/*#TD*/ int main(){
	Array A(10);
	Array B(10);
	for (int i = 0; i < A.getSize(); i++) {
		A[i] = i*i;	// An array of squares
		B[i] = i;
	}
	Array C = A + B;	//Why no copy constructor call here
	Array D;
	D = A + B;
	for (int j = 0; j < D.getSize(); j++)
		cout << D[j]<< " ";
	return 0;
}/*#/TD*/

// Implementation
// "Default" constructor. Create null array object
Array::Array(){mpData=0; mSize = 0;}

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
/*#TC*/Array::Array(const Array& original){
	if (mpData = new double[original.mSize]){ // request same amt. of space
		mSize = original.mSize;		// success! we got the space
		for (int i = 0; i < mSize; i++)	// Copy original's heap data
			*(mpData+i) = *(original.mpData+i);
	}
	else mSize = 0;		// Oops! no space. Note the problem
}/*#/TC*/

Array::~Array(){	// Works for both kinds of constructor
	delete[] mpData;	// valid even if mpData is null
}

/*#TB*/Array& Array::operator=(const Array& rhs){
	if (mSize != rhs.mSize) { // Oops, not the same size
		delete []mpData;		// release old storage
		if (mpData = new double[rhs.mSize]) mSize = rhs.mSize;		// and get new storage
			else mSize = 0;
	}
	for(int i=0;i<mSize;i++)	// copy rhs data to lhs
		mpData[i] = rhs.mpData[i];
	return (*this);		// i.e. return the object itself
} // This allows MULTIPLE assignment
/*#/TB*/


int Array::getSize() const { return mSize;}

// Iterator overload
double dummy;	// a static dummy variable
double& Array::operator[](int i) {
// error if outside array. return harmless dummy location
	if (i<0 || i>(mSize-1)) return dummy;
	return mpData[i];		// Note array notation used with pointers
}

Array Array::operator+(const Array& rhs){
	if (rhs.mSize != mSize)
		return *this;
	Array sum(mSize);
	for (int i = 0; i < mSize; i++)
		sum.mpData[i] = mpData[i] + rhs.mpData[i];
	return sum;
} 
