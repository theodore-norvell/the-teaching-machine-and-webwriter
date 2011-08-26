#include <iostream>
using namespace std;

/*#/HA*/ class Array{
public:
	Array(int s);				// regular constructor
	Array(const Array& original);		// Copy constructor
	~Array();					// destructor - used for both
	
	int getSize() const;

// operator overloads
	double& operator[](int i); // This replaces read AND write!
	Array operator+(const Array& rhs);
private:
	double* mpData;
	int mSize;
};/*#/TA*/

/*#TD*/ int main(){
	Array smallArray(10);
	Array otherArray(10);
	for (int i = 0; i < smallArray.getSize(); i++) {
		smallArray[i] = i*i;	// An array of squares
		otherArray[i] = i;
	}
	for (int j = 0; j < smallArray.getSize(); j++)
		cout << smallArray[j]<< " ";
	return 0;
}/*#/TD*/

// Implementation
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
}


int Array::getSize() const { return mSize;}

/*#TC*/ // Iterator overload
double dummy;	// a static dummy variable
double& Array::operator[](int i) {
// error if outside array. return harmless dummy location
	if (i<0 || i>(mSize-1)) return dummy;
	return mpData[i];		// Note array notation used with pointers
}/*#/TC*/

/*#TB*/Array Array::operator+(const Array& rhs){
	if (rhs.mSize != mSize)
		return Array(0);
	Array sum(mSize);
	for (int i = 0; i < sum.mSize; i++)
		sum.mpData[i] = mpData[i] + rhs.mpData[i];
	return sum;
}/*#/TB*/ 
