// Tests initialization of primitive arrays
//! run expect output = "0" endl "1" endl "4" endl "9" endl "16" endl "25" endl "36" endl "49" endl "0" endl "1" endl "2" endl "3" endl "4" endl "5" endl "6" endl "7" endl "8" endl "9" endl;
package arrays;

/**
 * Tests simple initialization of member and local arrays, both using and
 * not using the new operator.
 *
 * @author mpbl
 */
public class ArraysInitP0 {
	private int a[] = new int[]{0,1,4,9,16};
	private int b[] = {25,36,49};
	
	public ArraysInitP0(){}
	
	public static void main(){
		int c[] = new int[]{0,1,2,3,4,5};
		int d[] = {6,7,8,9};
		ArraysInitP0 f = new ArraysInitP0();
		for (int i = 0; i < f.a.length; i++)
			System.out.println(f.a[i]);
		for (int i = 0; i < f.b.length; i++)
			System.out.println(f.b[i]);
		for (int i = 0; i < c.length; i++)
			System.out.println(c[i]);
		for (int i = 0; i < d.length; i++)
			System.out.println(d[i]);
	}
	

}
