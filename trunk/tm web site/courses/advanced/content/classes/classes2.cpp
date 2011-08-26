class PubPriv{
	int	a,b;		// a,b and function f1 are private
	double f1();		//	by default

public:
	int c;
	double f2(int a1);

private:
	int e;
	int f3(double a1,double a2);
};
