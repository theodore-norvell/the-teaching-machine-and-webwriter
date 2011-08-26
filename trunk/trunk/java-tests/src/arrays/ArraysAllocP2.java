// tests mixed dimensions on member arrays

//! run expect output = "000000" endl "012024" endl "024048" endl "0360612" endl "0480816" endl;
package arrays;

public class ArraysAllocP2 {
	private int a[][][] = new int[5][][];
	
	public ArraysAllocP2(){}
	
	public static void main(){
		ArraysAllocP2 f = new ArraysAllocP2();
		for (int i = 0; i < 5; i++)
			f.a[i] = new int [2][3];
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
