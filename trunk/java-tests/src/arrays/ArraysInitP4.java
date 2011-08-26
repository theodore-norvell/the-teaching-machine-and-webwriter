//Tests initialization of multi-dimensional local arrays
//! run expect output = "0.1" endl "-3.2" endl "6.7" endl "0.1" endl "-2.3" endl "4.56" endl "001122" endl "3344" endl;
package arrays;

public class ArraysInitP4 {
	private AI4 a[] = {new AI4(0), new AI4(1), new AI4(2)};
	private double c[] = {0.1, -2.3, 4.56};
	
	public ArraysInitP4(){}
	
	public static void main(){
		ArraysInitP4 f = new ArraysInitP4();
		double b[] = {0.1, -3.2, 6.7};
		AI4 aLocal[] = {new AI4(3), new AI4(4)};
		for (int i = 0; i < b.length; i++)
			System.out.println(b[i]);
		for (int i = 0; i < f.c.length; i++)
			System.out.println(f.c[i]);
		for (int i = 0; i < f.a.length; i++){
			System.out.print(f.a[i].b);
			System.out.print(f.a[i].getB());
		}
		System.out.println();
		for (int i = 0; i < aLocal.length; i++){
			System.out.print(aLocal[i].b);
			System.out.print(aLocal[i].getB());
		}
		System.out.println();
	}
	

}

class AI4{
	public int b;
	public int getB(){
		return b;
	}
	public AI4(int i){b=i;}
}
