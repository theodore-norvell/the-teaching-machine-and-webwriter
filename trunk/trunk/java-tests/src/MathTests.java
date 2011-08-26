/*
 * Created on 1-Apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author mpbl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MathTests {
	
	public MathTests(){
	}
	
	public void doTests(){
		//double x = Math.PI;
		//double y = Math.E;
		double z = Math.sqrt(2.);
		z = Math.abs(-z);
		System.out.print("z=");
		System.out.println(z);
		z = Math.random();
		System.out.print(z);
		float f = Math.abs(-3.17f);
		System.out.print(f);
		int i = Math.abs(-4);
		System.out.print(i);
		long l = Math.abs(-7346524789l);
		System.out.print(l);
		i = Math.max(i,17);
		l = Math.max(l,234567890l);
		System.out.print(i);
		System.out.print(l);
		
		
//		z = Math.sin(x - Math.PI / Math.sqrt(y));
		System.out.print(z);
	}
	
	

	public static void main() {
	MathTests mt = new MathTests();
	mt.doTests();
		
	}
}
