//! Compile. Run.
package expressions;

public class Casts1 {

    static public void main(){

        int a, b;
        double c;

        b = -17;
        a = 2;
        c = 3.14159;
        c = 3+b + c+b*a;
        b = (int)c;
        a = ++b;
        c = 3.1;
    }
}