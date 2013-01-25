package play.ide.checker.symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import play.ide.checker.type.Type;
import play.ide.checker.type.typeAtoms.UnknownAtom;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class SymbolTable {

	public List<HashMap<FieldKey,FieldValue>> frames;
	
	
	public SymbolTable() {
		frames = new ArrayList<HashMap<FieldKey,FieldValue>>();
	}
	
	public Type get(String i, Kind k){
		
		int top=frames.size()-1;
		FieldKey fk=new FieldKey(i,k);
		if(frames.get(top).containsKey(fk))
			return frames.get(top).get(fk).t;
		else{
			Type t =new Type();
			t.addTypeAtom(UnknownAtom.getInstance());
			return t;
		}
			
	}
	
	public void put(String i, Kind k,Constness c, Type t){
		frames.get(frames.size()-1).put(new FieldKey(i, k), new FieldValue(c,t));
	}
	
	public void pushFrame(){
		frames.add(frames.size(), new HashMap<FieldKey,FieldValue>());
	}
	
	public void popFrame(){
		frames.remove(frames.size()-1);
	}

	public class FieldKey{
		String i;
		Kind k;
		
		FieldKey(String i, Kind k){
			this.i=i;
			this.k=k;
		}
		
		@Override
		public int hashCode(){
			return i.hashCode()+k.hashCode();
		}
		
		@Override
		public boolean equals(Object o){
			FieldKey fk = (FieldKey)o;
			if(i.equals(fk.i)&&k.equals(fk.k))
				return true;
			else 
				return false;

			/*if(this.hashCode()==fk.hashCode())
				return true;
			else 
				return false;*/
		}
		
	}
	
	public class FieldValue {
		Constness c;
		
		Type t;
		
		FieldValue(Constness c, Type t){
			this.c=c;
			this.t=t;
		}

	}
}
