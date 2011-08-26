//! Compile. Expect no error.

package lookup;


class Parent2 {
	
    class A {
        int i ;
        A() {}
     }
        
	
}

// Check the lookup of local classes
public class ClassLookupP2 extends Parent2{
	
	public ClassLookupP2(){}
	
	public static void main(){
		
		ClassLookupP2 p = new ClassLookupP2();
    
 
        A a0 = p.new A() ;
        a0.i = 0 ;
    }
        
}
