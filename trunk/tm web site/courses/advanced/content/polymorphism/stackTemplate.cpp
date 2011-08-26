#include <iostream>
using namespace std;

/*#TA*/template <class type>
class Stack{
public:
	Stack(int s=10);	// Constructor & null constructor
	Stack(const Stack &s);	// Copy constructor
	bool push(type d);
	type pop();
	bool empty() const;
	~Stack();
private:
	type *mpTos, *mpStart;
	int mSize;
};
/*#/TA*/
/*#TB*/template <class type> Stack<type>::Stack(int s){
    if ((mSize=s) < 0 || !(mpStart = new type[mSize]) ){
		mpStart = 0;
		mSize = 0;
	}
	mpTos = mpStart;
}

template <class type> Stack<type>::Stack(const Stack<type> &s){
	if (mpStart=new type[s.mSize]) {
		mSize = s.mSize;
		int offset = s.mpTos-s.mpStart;
		for (int i=0;i<offset;i++)
			*(mpStart+i)=*(s.mpStart+i);
		mpTos = mpStart + offset;
	}
	else mSize = 0;
}

template <class type> bool Stack<type>::push(type d){
    if (mpTos==mpStart+mSize) return false;		// stack full
    *mpTos++=d;
    return true;
}

template <class type> type Stack<type>::pop(){
    if (mpTos>mpStart)mpTos--;	// Check something in stack
    return *mpTos;
}
/*#/TB*/ 
/*#TC*/template <class type> bool Stack<type>::empty() const{return (mpTos==mpStart);}

template <class type> Stack<type>::~Stack(){delete[]mpStart;}
/*#/TC*/
/*#TD*/
struct complex{
	double re,im;
	complex(double r=0.,double i=0.){re=r;im=i;}
};


ostream& operator<<(ostream& s,	complex c);
/*#/TD*/ 

/*#TE*/ void main(){
    Stack<double> ds(5);
    Stack <int>  is(4);
    Stack <complex> cs;		// complex declared after stack!

//    clrscr();
    for(double x=0.;ds.push(x);x++)   //Push until full
        ;
    for(int i=0;is.push(i);i++)
        ;
    for (int j=0; cs.push(complex(j,-j));j++)
        ;

// Now empty each stack, outputting as we go
    cout <<"There were "<<x<<" items on the double stack\n";
    while (!ds.empty())
        cout << ds.pop() << "  ";

    cout <<"\n\nThere were " << i <<
        " items on the integer stack.\n";
    while (!is.empty())
        cout << is.pop() << "  ";
    cout << "\n\nThere were " << j <<
       " items on the complex stack.\n";

    complex temp;
    while (!cs.empty()){
        cout << cs.pop() << "  ";
    }
    cout << "\n";
}
/*#/TE*/
/* Overloading the insertion operator for complex struct */
ostream& operator<<(ostream& s, complex c) {
	s <<'(';
	if (c.re) s << c.re;
		if (c.im)
			if (c.im > 0) 
				s << " + " << c.im << "j";
			else s << " - " << -c.im << "j";
	if ( !(c.im) && !(c.re ))  s << "0";
	s << ')';
	return s;	// Allows concatenation of stream op
}
