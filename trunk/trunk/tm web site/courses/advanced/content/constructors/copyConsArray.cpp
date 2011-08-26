#include <iostream>
using namespace std;
// An example designed to show where implicit copying gets done.

/*#TA*/ class Array{
public:
	Array(int s);
	int getSize() const;
	double read(int i) const;
	void write(int i, double item);
	Array add(Array other);	// add other to this and return sum
private:
	double* mpData;
	int mSize;
};/*#/TA*/

/*#TB*/ int main(){
	Array smallArray(10);
	Array otherArray(10);
	for (int i = 0; i < smallArray.getSize(); i++) {
		smallArray.write(i,i*i);	// An array of squares
		otherArray.write(i,i);
	}
	smallArray.add(otherArray);  // But what do I do with the sum?
/*#/TB*/	for (int j = 0; j < smallArray.getSize(); j++)
		cout << smallArray.read(j)<< " ";
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

/*#TC*/Array Array::add(Array other){
	if (other.mSize != mSize)
		return *this;
	Array sum(mSize);
	for (int i = 0; i < mSize; i++)
		sum.mpData[i] = mpData[i] + other.mpData[i];
	return sum;
}/*#/TC*/
