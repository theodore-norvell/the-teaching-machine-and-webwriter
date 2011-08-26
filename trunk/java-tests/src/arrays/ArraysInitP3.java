//Tests initialization of multi-dimensional, non-square, non-primitive arrays
//! run expect output = "012" endl "3" endl "45" endl;
package arrays;

public class ArraysInitP3 {
	private AI3 a[][] = {{new AI3(0), new AI3(1), new AI3(2)}, {new AI3(3)},{new AI3(4), new AI3(5)}};
	
	public ArraysInitP3(){}
	
	public static void main(){
		ArraysInitP3 f = new ArraysInitP3();
		for (int i = 0; i < f.a.length; i++){
			for (int j = 0; j < f.a[i].length; j++)
				System.out.print(f.a[i][j].b);
			System.out.println();
		}
	}
	

}

class AI3{
	public int b;
	public int f(){
		return b;
	}
	public AI3(int i){b=i;}
}
