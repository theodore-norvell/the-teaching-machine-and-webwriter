// Tests non-primitive arrays
//! run expect output = "0" endl "1" endl "2" endl "3" endl "4" endl;
package arrays;

public class ArraysAllocP3 {
	private AA3 a[] = new AA3[5];
	
	public ArraysAllocP3(){}

	public static void main(){
		ArraysAllocP3 f = new ArraysAllocP3();
		for (int i = 0; i < f.a.length; i++)
			f.a[i] = new AA3(i);
		for (int j = 0; j < f.a.length; j++)
			System.out.println(f.a[j].b);
	}
	

}


class AA3{
	public int b;
	public int getB(){
		return b;
	}
	public AA3(int i){b=i;}
}
