package density;

public class Cube implements PhysicalObject {
	
	private double mass ;
	
	private double width ;
	
	public Cube( double mass, double width ) {
		this.mass = mass ;
		this.width = width ;
	}

	public double mass() {
		return mass ;
	}

	public double volume() {
		return width * width * width ;
	}

}
