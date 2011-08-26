package density;

public class Density {
	
/*#TA*/
	// This following code is polymorphic.
	// obj is a polymorphic reference.
	// obj.mass() and obj.volume() are polymorphic calls.
	static double density( PhysicalObject obj) {
		double mass = obj.mass();
		double volume = obj.volume() ;
		return mass / volume ; 
	}
/*#/TA*/

	// The following code is not polymorphic.
	// We know the type of object each reference
	// points to.
	public static void main(String[] args) {
		Sphere s = new Sphere( 90.4, 2.0 ) ;
		double sDen = density( s ) ;
		System.out.println( sDen ) ;
		
		Cube c = new Cube( 503.9, 4.0 ) ;
		double cDen = density( c ) ;
		System.out.println( cDen ) ;
	}
}
