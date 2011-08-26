package density;

public class Sphere implements PhysicalObject {
	
	private double mass ;
	
	private double radius ;
	
	public Sphere( double mass, double radius ) {
		this.mass = mass ;
		this.radius = radius ;
	}

	public double mass() {
		return mass ;
	}

	public double volume() {
		return radius * radius * radius * Math.PI / 0.75 ;
	}

}
