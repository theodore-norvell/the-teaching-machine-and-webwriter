//Tests initialization of multi-dimensional, "non-square" primitive arrays
//! run expect output = "012345" endl "0123" endl "012345678" endl "012" endl;
package arrays;

public class ArraysInitP2 {
	private int a[][][] = new int[][][]{
			{{0,1,2},{3,4,5}},
			{/*null*/{0},{1,2,3}},
			{{0,1,2},{3,4,5},{6,7,8}},
			{{0},{1,2}}
	};
	
	public ArraysInitP2(){}
	
	public static void main(){
		ArraysInitP2 f = new ArraysInitP2();
		for (int i = 0; i < f.a.length; i++){
			for (int j = 0; j < f.a[i].length; j++)
				for (int k = 0; k < f.a[i][j].length ; k++)
					System.out.print(f.a[i][j][k]);
			System.out.println();
		}
	}
}
