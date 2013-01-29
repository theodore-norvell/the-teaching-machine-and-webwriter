package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class ClassAtom extends AnyAtom {
	private  String i;
	private static ClassAtom instance = new ClassAtom();
	
	public ClassAtom(){
		this.i="";
	}
	
	public ClassAtom(String i){
		this.i=i;
	}
	
	public static synchronized ClassAtom getInstance(){
		return instance;
	}
	
	@Override
	public boolean canonicalOver(TypeAtom ta) {
		// TODO Auto-generated method stub
		if(ta.equals(ClassAtom.getInstance())){
			return true;
		}else
			return false;		
	}
	
	@Override
	public String toString(){
		return "Class "+i;
	}
	
	@Override
	public boolean equals(Object o){
		if(!o.toString().contains("Class"))
			return false;
		else{
			ClassAtom ca = (ClassAtom)o;
			if(ca.toString().equals(this.toString()))
				return true;
			else
				return false;
		}
		
		
	}
}
