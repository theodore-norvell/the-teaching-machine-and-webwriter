/*#HA*/ /*#HB*/ /*#HC*/#include <iostream>
using namespace std;
// An example designed to show the problems with shallow copying

/*#DA*/ class Array{
public:
	Array(int s);				// regular constructor
	Array(Array& original);		// Copy constructor
	~Array();					// destructor - used for both
	int getSize() const;
	double read(int i) const;
	void write(int i, double item);
private:
	double* mpData;
	int mSize;
};/*#HA*/

// A function to set every element of an array to value
void set(Array array, double value);

/*#DB*/ int main(){
	Array A(10);
	set(A,3);
	Array B(A);		// B is a clone of A
	set(B,7);
/*#HB*/	
	cout << "A: {";
	for (int j = 0; j < A.getSize()-1; j++)
		cout << A.read(j)<< ", ";
	cout << A.read(A.getSize()-1) << '}' << endl;
	return 0;
}

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

/*#DC*/// An explicit version of the default copy constructor
// Just does a straight copy of all member data fields
Array::Array(Array& original){
	mSize = original.mSize;
	mpData = original.mpData;
}/*#HC*/

Array::~Array(){
	delete[] mpData;	// valid even if mpData is null
}


int Array::getSize() const { return mSize;}
double Array::read(int i)const {
	if (i<0 || i >= mSize) return 0.0;	// poor error detection
	return mpData[i];
}


void Array::write(int i, double item){
	if (i<0 || i >= mSize) return;
	mpData[i]=item;
}

void set(Array array, double val){
	for (int i = 0; i < array.getSize(); i++)
		array.write(i,val);
}
