//! Run.
/*#HA*/ /*#HB*/#include <iostream>
using namespace std;

/*#DA*/class Array{
public:
	Array(int s);
	~Array();
	int getSize() const;
	double read(int i) const;
	void write(int i, double item);
private:
	double* mpData;
	int mSize;
};/*#HA*/

void fill(Array& array){
	for (int i = 0; i < array.getSize(); i++)
		array.write(i,i*i);	// An array of squares
}

void print(const Array& array){
	cout << '{';
	for (int i = 0; i < array.getSize()-1; i++)
		cout << array.read(i)<< ", ";
	cout << array.read(array.getSize() - 1) << '}' << endl;
}	

int main(){
	Array A(3);	// smallArray
	Array B(10);	// largeArray

	fill(A);
	fill(B);

	print(A);
	print(B);

	return 0;
}

/*#DB*/ // Implementation
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
