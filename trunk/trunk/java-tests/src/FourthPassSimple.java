
public class FourthPassSimple{
    static public void main(){
        int a = 23;
    int b = -17;
    double x = 3.14159;;
    x = 1.21314;
//	"This is a string constant!";
    x = 23;
    char ch = 'c';
//	true;
//	false;
    x = 1.3e-4;
    a = 2;
        x = 3+b + x+b*a;
    a+=x;
    b = (int)x;
    a = ++b;
    a = b++;
    B myB;
    myB = new B(a,b);
    int c = myB.max(a,b);
    myB.a = myB.b;
//	B myB = new B();
//	boolean check = myB instanceof A;

    float y = 1.7f;
    B.foo(a);
    
//	double z = max(x, y);


    }
}

class B{	//line 34
    static int s;
    int a;
    int b;
    static void foo(int a1){
        s = a1;
    }

    B(long ai, int a2){
        long i,j;
        i = ai;	// line 34
        j = a2;
    }
    B(int ai, int a2){
        a = ai;
        b = max(ai,a2);
    }
    int f1(){
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

/*class C extends B {
    int c;
    C(){
        c = 3;
    }


}*/