// Tests multi-dimensional member arrays
//! run expect output = "000000" endl "012024" endl "024048" endl "0360612" endl "0480816" endl;
package arrays;

public class ArraysAllocP1 {
	private int a[][][] = new int[5][2][3];
	
	public ArraysAllocP1(){}
	
	public static void main(){
		ArraysAllocP1 f = new ArraysAllocP1();
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 2; j++)
				for (int k = 0; k < 3; k++)
					f.a[i][j][k] = i*(j+1)*k;
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 2; j++)
				for (int k = 0; k < 3; k++)
					System.out.print(f.a[i][j][k]);
			System.out.println();
		}
	}
	

}
