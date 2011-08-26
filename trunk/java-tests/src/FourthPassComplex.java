import thirdPass.A;

public class FourthPassComplex{
    static public void main(){
        int a = 23;
	int b = -17;
	double x = 3.14159;;
	char c = 'c';
	boolean flag = true;
	x = 23;   // Implicit conversion
	flag = false;
        flag = !flag;
	x = 1.3e-4;
	a = 2;
        x = 3+b + x+b*a;
	a+=x;
	b = (int)x;
	a = ++b;
	a = b++;
	A myA;
	myA = new A(a,b);
	int d = myA.max(a,b);
	myA.a = myA.b;
//	B myB = new B();
//	boolean check = myB instanceof A;
	float y = 1.7f;
//	double z = max(x, y);


    }
}

class SuperB{
    int sBa = 3;
    static int sBb = 7;
    SuperB(int sBa, int sBb){
        this.sBa = sBa;
        this.sBb = sBb;
    }
    void foo(int fa){
        sBa = fa;
    }
}

class A{	//line 34
	static int s;
	int a;
	int b;
        B myB = new B(1,2,3.1);

        class B extends SuperB{
            double x;
            B(int Ba, int Bb, double Bx){
                super(Ba, Bb);
                x = Bx;
            }
            void foo(int Bx){
                x = Bx;
            }
                
        }
        

	static void foo(int a1){
		s = a1;
	}
		
	A(long a1, int a2){
		long i,j;
		i = a1;	// line 34
		j = a2;
                myB.x = -myB.x;
	}
        
	A(int a1, int a2){
		a = a1;
		b = max(a1,a2);
		myB = new B(a1, a2, -3.14159);
	}
	int f1(){
            myB.sBa = 77;
            return 3;
	}

	int max(int a1, int a2) {
		if (a1 > a2) return a1;
		return a2;
	}
	double max(double a1, double a2) {
		if (a1 > a2) return a1;
		return a2;
	}
}

/*class B extends A {
	int c;
	B(){
		c = 3;
	}


}*/