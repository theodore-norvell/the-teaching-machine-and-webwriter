package play.ide.checker.type.typeAtoms;

import java.util.ArrayList;
import java.util.List;

import play.ide.checker.type.Type;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class MethodAtom extends TypeAtom{
	
	private List<Type> tlist;
	private static MethodAtom instance = new MethodAtom();
	
	public MethodAtom(){
		tlist = new ArrayList<Type>();
	}
	
	public MethodAtom(List<Type> l){
		tlist=l;
	}
	
	public static synchronized MethodAtom getInstance(){
		return instance;
	}
	
	public Type getTypeAt(int i){
		return tlist.get(i);
	}
	
	public int getAmountOfTypes(){
		return tlist.size();
	}
	
	@Override
	public boolean canonicalOver(TypeAtom ta){
		MethodAtom ma = (MethodAtom)ta;
		int size=this.getAmountOfTypes();
		if(size!=ma.getAmountOfTypes()){
			return false;
		}
		
		for(int i=0;i<size;i++){
			if(!this.getTypeAt(i).canonicalOver(ma.getTypeAt(i)))
				return false;
		}
		return true;
		
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("method( ");
		for(Type t:tlist){
			sb.append("{");
			for(TypeAtom ta:t.getAtomsList()){
				sb.append(ta.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");
		}
		sb.append(" )");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(!o.toString().contains("method"))
			return false;
		
		MethodAtom ma = (MethodAtom)o;
		int size=this.getAmountOfTypes();
		if(size!=ma.getAmountOfTypes()){
			return false;
		}
		
		for(int i=0;i<size;i++){
			if(!tlist.get(i).equals(ma.tlist.get(i))){
				return false;
			}
		}
		return true;
		
		
		
	}
}
