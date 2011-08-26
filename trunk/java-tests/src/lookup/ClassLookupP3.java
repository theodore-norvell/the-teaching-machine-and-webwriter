//! Compile. Expect no error.

package lookup;


class GrandParent3 {
	public double x = -3.1;
	protected int i = 3;
	
	public GrandParent3(){}	
}

class Parent3 extends GrandParent3{
	public static double x = -4.1;	// should hide GrandParent3.x even though it's static
	protected int i = -1;
	
	public Parent3(){}
	
}

// Check the lookup of local classes
public class ClassLookupP3 extends Parent3{
	
	public ClassLookupP3(){}
	
	public static void main(){
		
		ClassLookupP3 p = new ClassLookupP3();
		p.f();
		p.x = 11.4;
		System.out.println(x);
    
    }
	
	void f(){
		i = 3;
		x = 7.1;
		System.out.println(i);
		System.out.println(x);
	}
        
}
