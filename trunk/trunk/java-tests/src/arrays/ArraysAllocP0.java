// Tests member arrays
//! run expect output = "0" endl "1" endl "4" endl "9" endl "16" endl;
package arrays;

public class ArraysAllocP0 {
	private int a[] = new int[5];
	
	public ArraysAllocP0(){}
	
	public static void main(){
		ArraysAllocP0 f = new ArraysAllocP0();
		for (int i = 0; i < 5; i++)
			f.a[i] = i*i;
		for (int j = 0; j < 5; j++)
			System.out.println(f.a[j]);
	}
	

}
