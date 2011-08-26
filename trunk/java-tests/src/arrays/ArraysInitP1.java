//Tests initialization of multi-dimensional primitive arrays
//! run expect output = "000000" endl "012024" endl "024048" endl "0360612" endl "0480816" endl;
package arrays;

public class ArraysInitP1 {
	private int a[][][] = {{{0,1,2},{0,1,2}},{{0,1,2},{0,1,2}},{{0,1,2},{0,1,2}},{{0,1,2},{0,1,2}},{{0,1,2},{0,1,2}}};
	
	public ArraysInitP1(){}
	
	public static void main(){
		ArraysInitP1 f = new ArraysInitP1();
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
