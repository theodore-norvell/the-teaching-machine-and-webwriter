//Tests conversion of sub class to base class
//! run expect output = "2" endl "4" endl;
public class SubClassWidening extends BaseClass{
	private int b;
	
	SubClassWidening(int b){super(b); this.b = b*b;}
	
	int getB(){return b;}
	
	BaseClass returnThis(){return this;}
	
	public static void main(){
		SubClassWidening w = new SubClassWidening(2);
		BaseClass b = w.returnThis();
		System.out.println(b.getA());
		System.out.println(w.getB());
		
	}

}

class BaseClass {
	private int a;
	BaseClass(int a){this.a = a;}
	public int getA(){return a;}
}