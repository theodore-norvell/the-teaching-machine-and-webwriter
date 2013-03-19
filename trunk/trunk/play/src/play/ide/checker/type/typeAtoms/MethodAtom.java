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
	private Type returnType;

	public MethodAtom(Type rType, List<Type> l){
		returnType=rType;
		tlist=l;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public int getParamListSize(){
		return tlist.size();
	}
	
	public Type getParamTypeAt(int i){
		return tlist.get(i);
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}

	@Override
	public boolean isSuperAtomOf(TypeAtom ta){
		MethodAtom ma = (MethodAtom)ta;
		int size=this.getParamListSize();
		if(size!=ma.getParamListSize()){
			return false;
		}
		if(!this.getReturnType().isSuperTypeOf(ma.getReturnType())){
			return false;
		}
		for(int i=0;i<size;i++){
			if(!ma.getParamTypeAt(i).isSuperTypeOf(this.getParamTypeAt(i)))
				return false;
		}
		return true;
		
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("METHOD( ");
		sb.append("{");
		sb.append(returnType);
		sb.append("}");
		sb.append(",");
		for(Type t:tlist){
			sb.append("{");
			for(TypeAtom ta:t.getAtomsList()){
				sb.append(ta);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" )");
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(!o.toString().contains("METHOD"))
			return false;
		
		MethodAtom ma = (MethodAtom)o;
		int size=this.getParamListSize();
		if(size!=ma.getParamListSize()){
			return false;
		}
		
		if(!this.getReturnType().equals(ma.getReturnType())){
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
