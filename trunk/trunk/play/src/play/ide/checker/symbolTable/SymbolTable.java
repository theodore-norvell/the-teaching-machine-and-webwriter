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

	private List<HashMap<FieldKey,FieldValue>> frames;
	
	
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

	public boolean topFrameContains(String i, Kind k){
		int top=frames.size()-1;
		FieldKey fk=new FieldKey(i,k);
		return frames.get(top).containsKey(fk);
	}
	
}
