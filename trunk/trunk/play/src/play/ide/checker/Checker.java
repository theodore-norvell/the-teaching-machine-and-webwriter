
package play.ide.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import play.higraph.model.*;
import play.ide.checker.symbolTable.Constness;
import play.ide.checker.symbolTable.FieldValue;
import play.ide.checker.symbolTable.Kind;
import play.ide.checker.symbolTable.SymbolTable;
import play.ide.checker.type.Type;
import play.ide.checker.type.typeAtoms.AnyAtom;
import play.ide.checker.type.typeAtoms.BooleanAtom;
import play.ide.checker.type.typeAtoms.ClassAtom;
import play.ide.checker.type.typeAtoms.MethodAtom;
import play.ide.checker.type.typeAtoms.NullAtom;
import play.ide.checker.type.typeAtoms.NumberAtom;
import play.ide.checker.type.typeAtoms.StringAtom;
import play.ide.checker.type.typeAtoms.UnknownAtom;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class Checker {

	PLAYWholeGraph whole;
	SymbolTable st;
	HashMap<PLAYNode,FieldValue> map;
	
	public Checker(PLAYWholeGraph whole){
		this.whole = whole;
		st = new SymbolTable();
		map = new HashMap<PLAYNode,FieldValue>();
	}
	
	public void check(){
		pass0();
		pass1();
	}
	
	private void pass0(){
		
		checkClass0(whole.getTop(0), st, map);
	}
	
	private void pass1(){
		
	}
	
	private void checkClass0(PLAYNode n, SymbolTable st, HashMap<PLAYNode,FieldValue> map){

		switch(n.getTag()) {
		case CLASS:
			List<PLAYNode> fields = n.getChildren();//get all VARDECL nodes
			int numOfFields = fields.size();
			
			/**check that all the field names are distinct*/
			java.lang.String[] names = new java.lang.String[numOfFields];
			System.out.print("Names of all fields: ");
			for(int i=0;i<numOfFields;i++){
				names[i] = n.getChild(i).getPayload().getPayloadValue();
				System.out.print(names[i]+"  ");
			}
			System.out.println();
			boolean repeat = false;
			for(int i=0;i<names.length;i++){
				for(int j=i+1;j<names.length;j++){
					if(names[i].equals(names[j])){
						System.out.println("Name Repetition Error:");
						System.out.println("Field " +i+" and Field "
											+j+" have the \nidentical names in Class "
											+n.getPayload().getPayloadValue());
						
						repeat = true;
					}
				}
			}
			if(repeat)
				return;
			else
				System.out.println("No Name Repetitions Detected.");
			
			/**Done--check that all the field names are distinct*/

			st.pushFrame();

			for(PLAYNode pn:fields){
				FieldValue fv = new FieldValue( pn.getPayload().getConstness(),
												checkType0(pn.getChild(0))	);
				st.put(pn.getPayload().getPayloadValue(), Kind.THIS, 
						fv.getConstness(), fv.getType());
				
				map.put(pn, fv);
			}

			for(PLAYNode pn:fields){
				if(map.get(pn).getType().isUnknown()){
					checkField0(pn, st, map);
				}	
			}
			st.popFrame();

			System.out.println("Map content:");
			for(PLAYNode pn:fields){
				System.out.print(pn.getPayload().getPayloadValue()+"\t");
				System.out.print(map.get(pn).getConstness()+"\t");
				System.out.println(map.get(pn).getType());	
			}
		
			break;
			
		default:
			System.out.println("Error: not a class");
			return;
		}
	}

	private void checkField0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		// TODO Auto-generated method stub
		FieldValue fv = checkVar0(pn, st, map);
		
		map.put(pn, fv);

		st.put(pn.getPayload().getPayloadValue(), Kind.THIS, fv.getConstness(), fv.getType());
	}

	private FieldValue checkVar0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		Type t;
		if(pn.getChild(0).getTag().equals(PLAYTag.NOTYPE)){
			
			t = checkSeq0(pn.getChild(1), st, map);	
		}else{
			t = checkType0(pn.getChild(0));
		}
		return new FieldValue(pn.getPayload().getConstness(),t);
	}

	private Type checkExp0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		// TODO Auto-generated method stub
		String payloadStr=pn.getPayload().getPayloadValue();
		
		switch(pn.getTag()){
		case NUMBERLITERAL:
			/*try {
				Double.parseDouble(payloadStr);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("Error: Value in NUMBERLITERAL cannot be converted.");
			}*/
			
			return new Type(NumberAtom.getInstance());
		case TRUE:
		case FALSE:
			return new Type(BooleanAtom.getInstance());
		case STRINGLITERAL:
			return new Type(StringAtom.getInstance());
		case NULL:
			return new Type(NullAtom.getInstance());
		case THISVAR:		
			return st.get(payloadStr, Kind.THIS);
		case LOCALVAR:
			return st.get(payloadStr, Kind.LOCAL);
		case WORLDVAR:
			return st.get(payloadStr, Kind.WORLD);
		case DOT:
			return new Type(UnknownAtom.getInstance());
		case THIS:
			PLAYNode n = pn;
			while(!n.getTag().equals(PLAYTag.CLASS)){
				if(!whole.isTop(n))
					n=n.getParent();
				/*else{
					System.out.println("Error: Cannot find THIS Class");
					break;
					
				}*/
			}
			
			return new Type(new ClassAtom(n.getPayload().getPayloadValue()));		
		case CALLCLOSURE:
		case CALLWORLD:
			return new Type(UnknownAtom.getInstance());
		case NEW:
			String i2 = pn.getChild(0).getPayload().getPayloadValue();
			return new Type(new ClassAtom(i2));
		case METHOD:
			List<FieldValue> fvs = new ArrayList<FieldValue>();
			
			PLAYNode param = pn.getChild(0);
			PLAYNode ot = pn.getChild(1);
			PLAYNode seq = pn.getChild(2);
			
			/*//temporary type value at 0
			fvs.add(new FieldValue(param.getChild(0).getPayload().getConstness(),
									checkType0(param.getChild(0).getChild(0))
									));*/
			
			for(int i=0;i<param.getChildren().size();i++){
				
				PLAYNode n2 = param.getChild(i);
				
				FieldValue fv = checkVar0(n2, st, map);

				if(fv.getType().isUnknown()){
					System.out.println("HERE");
					return new Type(UnknownAtom.getInstance());
				}else{
					fvs.add(new FieldValue(n2.getPayload().getConstness(),
											fv.getType()));
				}
			}
			
			if(!ot.getTag().equals(PLAYTag.NOTYPE)){
				fvs.set(0, new FieldValue(fvs.get(0).getConstness(),checkType0(ot)));
			}else{
				st.pushFrame();
				
				for(int i=0;i<fvs.size();i++){
					
					st.put( param.getChild(i).getPayload().getPayloadValue(), 
							Kind.LOCAL, fvs.get(i).getConstness(), fvs.get(i).getType()
							);
					
					
				}
				
				fvs.set(0, new FieldValue(fvs.get(0).getConstness(),
											checkSeq0(seq,st,map) ));
				
				st.popFrame();
			}
			if(fvs.get(0).getType().isUnknown())
				return new Type(UnknownAtom.getInstance());
			else{
				List<Type> types = new ArrayList<Type>();
				for(int i=0;i<fvs.size();i++){
					types.add(fvs.get(i).getType());
				}
				return new Type(new MethodAtom(types));
			}
				
		case SEQ:
			return checkSeq0(pn,st,map);
		case IF:
			Type t0=checkSeq0(pn.getChild(1),st,map);
			Type t1=checkSeq0(pn.getChild(2),st,map);
			Type t = t0.merge(t1);
			t.canonicalize();
			return t;
		case WHILE:
		case ASSIGN:	
			return new Type(NullAtom.getInstance());
		case EXPPLACEHOLDER:
			return new Type(UnknownAtom.getInstance());		

		default:
			System.out.println("default");
			return null;
		}
		
	}

	private Type checkSeq0(PLAYNode seq, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		// TODO Auto-generated method stub
		
		if(seq.getChildren().size()==0)
			return new Type(NullAtom.getInstance());
		//st.pushFrame(); //to be sure
		int c=0;
		FieldValue fv=null;
		
		for(PLAYNode pn:seq.getChildren()){
			switch(pn.getTag()){
			case VARDECL:
				fv = checkVar0(pn,st,map);
				st.pushFrame();
				c++;
				st.put(pn.getPayload().getPayloadValue(), Kind.LOCAL, 
						fv.getConstness(), fv.getType());
				break;
			default:
				
				fv = new FieldValue(pn.getPayload().getConstness(),checkExp0(pn,st,map));
				
				break;
			}
		}
		for(int i=0;i<c;i++){
			st.popFrame();
		}
		
		return fv.getType();
	}

	/**
	 * 
	 * @param n a PLAYTag node
	 * @return the type corresponding to this tag
	 */
	private Type checkType0(PLAYNode n) {
		
		Type t = new Type();
		
		switch(n.getTag()){
		case NOTYPE:
			t.addTypeAtom(UnknownAtom.getInstance());
			break;
		case BOOLEANTYPE:
			t.addTypeAtom(BooleanAtom.getInstance());
			break;
		case STRINGTYPE:
			t.addTypeAtom(StringAtom.getInstance());
			break;
		case NUMBERTYPE:
			t.addTypeAtom(NumberAtom.getInstance());
			break;
		case ANYTYPE:
			t.addTypeAtom(AnyAtom.getInstance());
			break;
		case NULLTYPE:
			t.addTypeAtom(NullAtom.getInstance());
			break;
		case ALTTYPE:
			List<PLAYNode> nodes = n.getChildren();
			for(PLAYNode pn: nodes){
				Type tempT = checkType0(pn);
				t=tempT.merge(t);
			}
			t.canonicalize();	
			break;
			
		case CLASSTYPE:
			t.addTypeAtom(new ClassAtom(n.getPayload().getPayloadValue()));
			break;
		
		default:
			t=null;
			break;
		}
		return t;
		
	}
}
