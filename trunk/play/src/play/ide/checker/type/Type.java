package play.ide.checker.type;

import java.util.ArrayList;
import java.util.List;
import play.ide.checker.type.typeAtoms.TypeAtom;
import play.ide.checker.type.typeAtoms.UnknownAtom;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class Type {
	private List<TypeAtom> atoms;
	
	public Type(){
		atoms= new ArrayList<TypeAtom>();
	}
	
	public Type(TypeAtom ta){
		atoms= new ArrayList<TypeAtom>();
		atoms.add(ta);
	}
	
	public void addTypeAtom(TypeAtom ta){
		atoms.add(ta);
	}
	
	public void removeTypeAtom(TypeAtom ta){
		atoms.remove(ta);
	}
	
	public boolean containsAtom(TypeAtom ta){
		if(atoms.contains(ta))
			return true;
		else return false;
	}
	
	public Type merge(Type t){
		for(TypeAtom ta: atoms){
			t.addTypeAtom(ta);
		}
		return t;
	}
	
	public void canonicalize(){
		for(int i=0;i<getAtomsAmount()-1;i++){
			for(int j=i+1;j<getAtomsAmount();j++){
				if(atoms.get(i).canonicalOver(atoms.get(j)))
					removeTypeAtom(atoms.get(j));
				else if(atoms.get(j).canonicalOver(atoms.get(i)))
					removeTypeAtom(atoms.get(i));		
			}
		}
	}
	
	public boolean isUnknown(){
		if(getAtomsAmount()==1&&atoms.get(0).equals(UnknownAtom.getInstance()))
			return true;
		else
			return false;
	}
	
	public int getAtomsAmount(){
		return atoms.size();
	}
	
	@Override
	public boolean equals(Object o){
		Type t = (Type)o;
		if(this.getAtomsAmount()==t.getAtomsAmount()){
			for(TypeAtom ta:this.atoms){
				if(!t.containsAtom(ta))
					return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(TypeAtom ta:atoms){
			sb.append(ta+"  ");
		}
		return sb.toString();
	}
	
}
