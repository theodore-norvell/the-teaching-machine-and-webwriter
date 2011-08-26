/***********************************************************************
             MEMORIAL UNIVERSITY OF NEWFOUNDLAND
         Faculty of Engineering and Applied Science
           Engineering 3891(Advanced Programming)
     Assignment #6   Instructor: Michael Bruce-Lockhart
	 
	   Out: 2004.11.04							Due: 2004.11.10.8:55
***********************************************************************/

/* For this assignment, we combine a number of earlier assignments to build a small,
rudimentary family of containers */


/* The base Container class, which goes to the heap to get mSize doubles */
class Container{
public:
	Container(int size);	// Constructor, assert size >= 0
	Container();	// size = 0
	Container(const Container& original);	// copy constructor
	~Container();

	int size() const;
	bool inError() const;	// True if LAST operation (constructor or modifier) was in error, false otherwaise

protected:
	int mSize;	// The number of doubles in the container
	double *mpData;	// Pointer to the data held in the container
	bool mFault;		// An illegal operation was attempted
};


/* Array objects ARE  container objects. Container objects which are accessed via
 an index (random access). Using an illegal index is a fault.*/ 

class Array : public Container {
public:
	Array(int size);	// Constructor, assert size >= 0. All elements should be 0.
	Array();	// Size = 0.

// retrieve item at index (fault if index < 0 or >= mSize)
	double get(int index); // Not const because fault state of object can be changed

// put item at index (fault if index < 0 or >= mSize)
	void put(int index, double item);
};


/* Stack objects ARE  container objects. Container objects which are accessed
in a last-in - first-out manner by popping and pushing. Pushing onto a full
stack or popping from an empty one are faults. */ 

class Stack : public Container {
public:
	Stack(int size);	// Constructor, assert size >= 0
	Stack();	// size = 0
	Stack (const Stack& original);	// copy constructor  *** See NOTE at bottom

	bool isFull() const;	// is stack full?
	bool isEmpty() const;	// is stack empty?

// push item onto the Stack (if stack is full item, should not be pushed on and fault should be set)
	void push(double item);

// pop item from the Stack (if stack is empty, top item should be left on stack, and fault should be set)
	double pop();
private:
	double* mpTos;
};


const int  UNDERFLOW = -1;
const int  EMPTY = 0;
const int  PENDING = 1;
const int  FULL = 2;
const int  OVERFLOW = 3;



/* Queue objects ARE  container objects. Container objects which are accessed
in a first-in - first-out manner by adding items to the end and fetching them
from the beginning.Adding to a full queue or fetching from an empty one are faults. */ 

class Queue : public Container {
public:
	Queue(int size);	// Constructor, assert size >= 0
	Queue();	// Constructor, assert size >= 0
	Queue (const Queue& original);	// copy constructor  *** See NOTE at bottom

	int getStatus()const;

// add item to the Queue (fault if queue is full)
	void add(double item);

// fetch item from the Queue (fault if queue is empty)
	double fetch();
private:
	void incPtr(double* & ptr);  // Increment pointer circularly
	double* mpHead;
	double* mpTail;
	int mStatus;

};




/************************************* NOTE on Copy Constructors for derived classes ******

  If you are implementing the copy constructor for a derived class and you want to
  utilize the code in the copy constructor for the base class, do the following

  Derived::Derived(const Derived& original) : Base(original) {
	// Now put any EXTRA code needed to copy construct derived class here.
	// The copy constructor for the base class is guaranteed to be called
	// before arriving here

  }

  The initialization call passes the original object (which IS a Base object because
  it IS a Derived object) to the Base class constructor. Because it IS a base class object
  the copy constructor of the Base class is invoked instead of the regular constructor.
  Remember - a Copy Constructor is just a constructor with an object of the same class
  as the constructor argument.
  ******************************************************************************************/