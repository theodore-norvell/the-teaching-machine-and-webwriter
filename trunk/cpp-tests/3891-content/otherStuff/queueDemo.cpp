//! Run.
/*#HA*/ /*#HB*/#include "assign6.h"
#include <iostream>
using namespace std;

bool testQueue();

int main(){
	if (testQueue())
		cout << "Your Queue class works|\n";
	else
		cout << "Your Queue class doesn't work.\n";

	return 0;
}

/*#DA*/bool testQueue(){
	const int SIZE = 5;
	Queue queue(SIZE);
	bool passed = !queue.inError();

	queue.fetch();	// queue empty, can't fetch
	passed = queue.inError();

	// rotate Queue a bit
	queue.add(0); queue.add(0);	queue.add(0);
	queue.fetch(); queue.fetch(); queue.fetch();
	passed = passed && !queue.inError();
	if (passed) {
		int count = 0;
		while(queue.getStatus() < FULL) {
			queue.add(count*count-count);
			count++;
			passed = passed && !queue.inError();
		}
		if (passed) {
			queue.add(100.0); // error
			passed = passed && queue.inError();
			count = 0;
			while (queue.getStatus() > EMPTY) {
				passed = passed && (queue.fetch() == count*count - count) && !queue.inError();
				count++;
			}
			passed = passed && count == SIZE;
		}
	}
	return passed;
}
/*#HA*/

Container::Container(int size ) {	// Constructor, assert size >= 0
	if (size >= 0  && (mpData = new double[size]) ) {
			mSize = size;
			mFault = false;
	} else {
			mSize = 0;
			mFault = true;
			mpData = 0;
	}
}

Container::Container() {	// Null Constructor
	mSize = 0;
	mFault = false;
	mpData = 0;
}


Container::Container(const Container& original){	// copy constructor
	if (mpData = new double[original.mSize]) {
		mSize = original.mSize;
		for (int i = 0; i < mSize; i++)
			*(mpData+i) = *(original.mpData + i);
		mFault = false;
	}else {
		mFault = true;
		mSize = 0;
	}
}

Container::~Container() {
	delete [] mpData;
}

int Container::size() const { return mSize;}

bool Container::inError() const {return mFault;}	


Array::Array(int size) : Container(size) {	// Constructor, assert size >= 0
	for (int i = 0; i < mSize; i++)
		*(mpData + i) = 0.;
}

Array::Array() : Container() {}	// Null Constructor

// retrieve item at index (mFault if index < 0 or >= mSize)
double Array::get(int index){
	mFault = (index < 0 || index >= mSize);
	if (mFault) return 0.;
	return *(mpData + index);
}


// put item at index (mFault if index < 0 or >= mSize)
void Array::put(int index, double item){
	mFault = (index < 0 || index >= mSize);
	if (!mFault)
		*(mpData + index) = item;
}


Stack::Stack(int size) : Container(size) {	// Constructor, assert size >= 0
	mpTos = mpData;
}

Stack::Stack() : Container() {	// Null Constructor
	mpTos = mpData;
}

Stack::Stack (const Stack& original) : Container(original) {	// copy constructor
	if (!mFault)
		mpTos = mpData + (original.mpTos - original.mpData);
}

bool Stack::isFull() const { return !(mpTos < mpData + mSize);}
bool Stack::isEmpty() const { return (mpTos <= mpData);}

// push item onto the Stack (mFault if stack is full)
void Stack::push(double item){
	if (mpTos < mpData + mSize) {
		*mpTos++ = item;
		mFault = false;
	}
	else mFault = true;
}

// pop item from the Stack (mFault if stack is empty)
double Stack::pop(){
	if (mpTos > mpData) {
		mFault = false;
		return *(--mpTos);
	}
	mFault = true;
	return 0;
}


Queue::Queue(int size) : Container(size){	// Constructor, assert size >= 0
	mpHead = mpTail = mpData;
	mStatus = EMPTY;
}

Queue::Queue() : Container(){	// Null onstructor
	mpHead = mpTail = mpData;
	mStatus = EMPTY;
}

Queue::Queue (const Queue& original) : Container(original) {	// copy constructor
	if (!mFault) {
		mpHead = mpData + (original.mpHead - original.mpData);
		mpTail = mpData + (original.mpTail - original.mpData);
		mStatus = original.mStatus;
	}
}

int Queue::getStatus() const { return mStatus;}

// add item to the Queue (mFault if queue is full)
void Queue::add(double item){
	if (mStatus < FULL) {
		*mpTail = item;
		incPtr(mpTail);
		if (mpTail == mpHead) mStatus = FULL;
			else mStatus = PENDING;
		mFault = false;
	}
	else {
		mStatus = OVERFLOW;
		mFault = true;
	}
}

// fetch item from the Queue (mFault if queue is empty)
double Queue::fetch(){
	double item = 0.;

	if (mStatus > EMPTY) {
		item = *mpHead;
		incPtr(mpHead);
		if (mpTail == mpHead) mStatus = EMPTY;
			else mStatus = PENDING;
		mFault = false;
	} else {
		mStatus = UNDERFLOW;
		mFault = true;
	}
	return item;
}



void Queue::incPtr(double* & ptr){
	ptr++;
	if (ptr >= mpData + mSize)
		ptr = mpData;
}
