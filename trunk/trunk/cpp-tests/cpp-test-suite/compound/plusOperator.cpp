//! Run.

struct complex {
	double  re, im;
	void operator+( complex& operand ) ;
};

void complex::operator+( complex& operand ) {
    return ;
}

int main(){
    complex x;
    x + x ;
    return 0;
}
