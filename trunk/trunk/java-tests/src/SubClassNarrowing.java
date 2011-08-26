//Tests conversion of sub class to base class
//! run expect output = "2" endl "4" endl;
public class SubClassNarrowing extends BaseNClass{
	private int b;
	
	SubClassNarrowing(int b){super(b); this.b = b*b;}
	
	int getB(){return b;}
	
	BaseNClass returnThis(){return this;}
	
	public static void main(){
		SubClassNarrowing w = new SubClassNarrowing(2);
		BaseNClass b = w.returnThis();
		SubClassNarrowing w2 = (SubClassNarrowing)b;
		w2.getB();
//		System.out.println(b.getA());
//		System.out.println(w.getB());
		
	}

}

class BaseNClass {
	private int a;
	BaseNClass(int a){this.a = a;}
	public int getA(){return a;}
}