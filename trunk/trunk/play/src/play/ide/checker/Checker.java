
package play.ide.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import play.higraph.model.*;
import play.ide.checker.error.*;
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
	ErrorMap errorMap;
	ErrorFactoryEnglish errFTYEng;
	
	public Checker(PLAYWholeGraph whole){
		this.whole = whole;
		st = new SymbolTable();
		map = new HashMap<PLAYNode,FieldValue>();
		errorMap = new ErrorMap();
		errFTYEng = new ErrorFactoryEnglish();
	}
	
	public void check(){
		pass0();
		pass1();
	}
	
	private void pass0(){
		for(PLAYNode pn:whole.getTops()){
			checkClass0(pn, st, map);
		}
	}
	
	private void pass1(){
		for(PLAYNode pn:whole.getTops()){
			checkClass1(pn, st, map, errorMap);
		}
	}
	
	private void checkClass0(PLAYNode n, SymbolTable st, HashMap<PLAYNode,FieldValue> map){

		switch(n.getTag()) {
		case CLASS:
			List<PLAYNode> vardecls = n.getChildren();//get all VARDECL nodes
			vardecls.remove(vardecls.size()-1);
			st.pushFrame();

			for(PLAYNode vardecl:vardecls){
				FieldValue fv = new FieldValue( vardecl.getPayload().getConstness(),
												checkType(vardecl.getChild(0),null) );
				
				st.put(vardecl.getPayload().getPayloadValue(), Kind.THIS, 
						fv.getConstness(), fv.getType());
				
				map.put(vardecl, fv);
			}

			for(PLAYNode pn:vardecls){
				if(map.get(pn).getType().isUnknown()){
					
					checkField0(pn, st, map);
				}	
			}
			
			st.popFrame();

			/*print map*/
			System.out.println("Map content:");
			for(PLAYNode pn:vardecls){
				System.out.print(pn.getPayload().getPayloadValue()+"\t");
				System.out.print(map.get(pn).getConstness()+"\t");
				System.out.println(map.get(pn).getType());	
			}
		
			break;
			
		default:
			System.out.println("Error: not a class");
		}
	}

	private void checkField0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		
		FieldValue fv = checkVar0(pn, st, map);
		
		if(fv.getType().isUnknown()){
			errorMap.addError(pn, errFTYEng.cannotDeduceTypeForField(pn.getPayload().getPayloadValue()));
		}
		map.put(pn, fv);
		st.put(pn.getPayload().getPayloadValue(),Kind.THIS, fv.getConstness(), fv.getType());
	
	}

	private FieldValue checkVar0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		Type t;
		if(pn.getChild(0).getTag().equals(PLAYTag.NOTYPE)){
			t = checkExp0(pn.getChild(1), st, map);	
		}else{
			t = checkType(pn.getChild(0),null);
		}
		return new FieldValue(pn.getPayload().getConstness(),t);
	}

	private Type checkExp0(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		
		String payloadStr=pn.getPayload().getPayloadValue();
		
		switch(pn.getTag()){
		case NOEXP:
			return new Type(UnknownAtom.getInstance());
		case NUMBERLITERAL:
			return new Type(NumberAtom.getInstance());
		case TRUE:
		case FALSE:
			return new Type(BooleanAtom.getInstance());
		case STRINGLITERAL:
			return new Type(StringAtom.getInstance());
		case NULL:
			return new Type(NullAtom.getInstance());
		case THISVAR:			
			if(st.hasEntry(payloadStr, Kind.THIS)){
				return st.get(payloadStr, Kind.THIS);
			}else{
				return new Type(UnknownAtom.getInstance());
			}
		case LOCALVAR:
			if(st.hasEntry(payloadStr, Kind.LOCAL)){
				return st.get(payloadStr, Kind.LOCAL);
			}else{
				return new Type(UnknownAtom.getInstance());
			}
		case WORLDVAR:
			if(st.hasEntry(payloadStr, Kind.WORLD)){
				return st.get(payloadStr, Kind.WORLD);
			}else{
				return new Type(UnknownAtom.getInstance());
			}
		case DOT:
			return new Type(UnknownAtom.getInstance());
		case THIS:
			PLAYNode n = pn;
			while(!n.getTag().equals(PLAYTag.CLASS)){
				if(!whole.isTop(n)){
					n=n.getParent();
				}
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
			
			for(int i=0;i<param.getNumberOfChildren();i++){
				PLAYNode n2 = param.getChild(i);
				FieldValue fv = checkVar0(n2, st, map);
				fvs.add(new FieldValue(n2.getPayload().getConstness(),fv.getType()));
			}
			
			Type t;
			if(!ot.getTag().equals(PLAYTag.NOTYPE)){
				t=checkType(ot,null);	
			}else{
				st.pushFrame();
				
				for(int i=0;i<fvs.size();i++){
					
					st.put( param.getChild(i).getPayload().getPayloadValue(), 
							Kind.LOCAL, fvs.get(i).getConstness(), fvs.get(i).getType()
							);					
				}
				
				t=checkSeq0(seq,st,map);
				
				st.popFrame();
			}
			List<Type> types = new ArrayList<Type>();
			
			for(int i=0;i<fvs.size();i++){
				types.add(fvs.get(i).getType());
			}
			return new Type(new MethodAtom(t,types));
				
		case SEQ:
			return checkSeq0(pn,st,map);
		case IF:
			Type t0=checkSeq0(pn.getChild(1),st,map);
			Type t1=checkSeq0(pn.getChild(2),st,map);
			Type t2 = t0.merge(t1);

			return t2;
		case WHILE:
		case ASSIGN:	
			return new Type(NullAtom.getInstance());
		case EXPPLACEHOLDER:
			return new Type(UnknownAtom.getInstance());		

		default:
			System.out.println("Error: default exp");
			return null;
		}
		
	}

	private Type checkSeq0(PLAYNode seq, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map) {
		
		if(seq.getChildren().size()==0){
			return new Type(NullAtom.getInstance());
		}
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
	private Type checkType(PLAYNode n, ErrorMap errorMap) {
		
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
				Type tempT = checkType(pn, errorMap);
				t=tempT.merge(t);
			}

			break;
			
		case CLASSTYPE:
			String className=n.getPayload().getPayloadValue();

			List<PLAYNode> classNodes = whole.getTops();
			
			boolean foundClass=false;
			for(PLAYNode c:classNodes){
				if(className.equals(c.getPayload().getPayloadValue())){
					foundClass=true;
					t.addTypeAtom(new ClassAtom(className));
					break;
				}
			}
			
			if(!foundClass){
				t.addTypeAtom(UnknownAtom.getInstance());
				
				if(errorMap!=null){//pass 1 only
					errorMap.addError(n, errFTYEng.classNotFound(className));
				}
			}
			
			break;
			
		case METHODTYPE:
			
			List<PLAYNode> tlist = n.getChildren();
			List<Type> types = new ArrayList<Type>();
			
			for(PLAYNode tpn: tlist){
				Type tempT = checkType(tpn, errorMap);
				types.add(tempT);
			}
			
			Type tr = types.get(types.size()-1);
			types.remove(types.size()-1);
			t= new Type(new MethodAtom(tr,types));
			
			break;
		
		default:
			System.out.println(n.getTag());
			System.out.println("checkType error!!!");
			t=null;
		}
		return t;
		
	}
	
	//=====================================================================
	
	private void checkClass1(PLAYNode n, SymbolTable st, 
			HashMap<PLAYNode,FieldValue> map, ErrorMap errorMap){

		switch(n.getTag()) {
		case CLASS:
			List<PLAYNode> fields = n.getChildren();//get all VARDECL nodes
			fields.remove(fields.size()-1);
			int numOfFields = fields.size();
			
			/**check that all the field names are distinct*/
			java.lang.String[] names = new java.lang.String[numOfFields];
			
			for(int i=0;i<numOfFields;i++){
				names[i] = n.getChild(i).getPayload().getPayloadValue();
			}

			for(int i=0;i<names.length;i++){
				for(int j=i+1;j<names.length;j++){
					if(names[i].equals(names[j])){
						errorMap.addError(n, 
							errFTYEng.duplicateField(n.getPayload().getPayloadValue(),
							names[i]));	
					}
				}
			}
			/**Done--check that all the field names are distinct*/

			st.pushFrame();

			for(PLAYNode pn:fields){
				FieldValue fv = map.get(pn);
				
				
				st.put(pn.getPayload().getPayloadValue(), Kind.THIS, 
						fv.getConstness(), fv.getType());
			}
			
			for(PLAYNode pn:fields){
				checkVar1( pn, st, map, errorMap );
				//FieldValue fv = checkVar1( pn, st, map, errorMap );
				
				/*if(!fv.getConstness().equals(map.get(pn).getConstness())||
						!fv.getType().equals(map.get(pn).getType())){
					System.out.println("Sth wrong in checkClass1 algorithm!!!");	
				}*/
			}
			st.popFrame();

			break;
			
		default:
			System.out.println("TBD:checkClass1");
		}

	}

	private FieldValue checkVar1(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map, ErrorMap errorMap) {
		
		Type t;
		if(pn.getChild(0).getTag().equals(PLAYTag.NOTYPE)){
			
			if(pn.getChild(1).getTag().equals(PLAYTag.NOEXP)){
				errorMap.addError(pn,errFTYEng.cannotDetermineTypeForVar(pn.getPayload().getPayloadValue()));
			}
			
			t = checkExp1(pn.getChild(1), st, map, errorMap);	
		}else{
			
			t = checkType(pn.getChild(0), errorMap);
			
			Type te = checkExp1(pn.getChild(1), st, map, errorMap);	
			
			if( (!te.isUnknown()) && (!t.isSuperTypeOf(te)) ){
				errorMap.addError(pn, 
						errFTYEng.typeOfInitializerMismatchTheDeclaredType(te,t));	
			}
		}
		
		return new FieldValue(pn.getPayload().getConstness(),t);
	}

	private Type checkExp1(PLAYNode pn, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map, ErrorMap errorMap) {
		Type t = null;
		
		String i=pn.getPayload().getPayloadValue();
		
		switch(pn.getTag()){
		case NOEXP:
			t=new Type(UnknownAtom.getInstance());
			break;
		case NUMBERLITERAL:
			try {
				Double.parseDouble(i);
			} catch (NumberFormatException e) {
				errorMap.addError(pn,errFTYEng.parsingNumber(i) );
			}
			t= new Type(NumberAtom.getInstance());
			break;
		case TRUE:
		case FALSE:
			t= new Type(BooleanAtom.getInstance());
			break;
		case STRINGLITERAL:
			t= new Type(StringAtom.getInstance());
			break;
		case NULL:
			t= new Type(NullAtom.getInstance());
			break;
		case THISVAR:			
			if(st.hasEntry(i, Kind.THIS)){
				t= st.get(i, Kind.THIS);
			}else{
				errorMap.addError(pn,errFTYEng.fieldNotFound(i) );
				t= new Type(UnknownAtom.getInstance());
			}	
			
			break;
			
		case LOCALVAR:
			if(st.hasEntry(i, Kind.LOCAL)){
				t= st.get(i, Kind.LOCAL);
			}else{
				errorMap.addError(pn, errFTYEng.localVarNotFound(i));
				t= new Type(UnknownAtom.getInstance());
			}
			
			break;
			
		case WORLDVAR:
			if(st.hasEntry(i, Kind.WORLD)){
				t= st.get(i, Kind.WORLD);
			}else{
				errorMap.addError(pn, errFTYEng.worldVarNotFound(i));
				t= new Type(UnknownAtom.getInstance());
			}
			
			break;
			
		case DOT:
			Type t0=checkExp1(pn.getChild(0),st,map,errorMap);
			if(t0.isUnknown()){
				t= new Type(UnknownAtom.getInstance()); 
			}
			
			// t0 is not unknown, then
			List<PLAYNode> classNodes = whole.getTops();
			
			boolean foundClass=false;
			PLAYNode c = null;
			for(PLAYNode n:classNodes){	
				Type t2 = new Type(new ClassAtom(n.getPayload().getPayloadValue()));				
				if(t0.equals(t2)){
					foundClass=true;
					c=n;
					break;
				}
			}
			if(!foundClass){	
				errorMap.addError(pn, errFTYEng.expectClassTypeButNotFound(t0));
				t= new Type(UnknownAtom.getInstance()); 
			}else{
				List<PLAYNode> nodes=c.getChildren();//all vardecl nodes in class c
				for(PLAYNode n:nodes){
					if(n.getPayload().getPayloadValue().equals(i)){
						t= map.get(n).getType();	
					}else{			
						errorMap.addError(pn, errFTYEng.fieldNotFoundInClass(i,c.getPayload().getPayloadValue()));
						t= new Type(UnknownAtom.getInstance()); 
					}	
				}	
			}
			
			break;
			
		case THIS:
			PLAYNode n = pn;
			while(!n.getTag().equals(PLAYTag.CLASS)){
				if(!whole.isTop(n)){
					n=n.getParent();
				}
			}
			
			t= new Type(new ClassAtom(n.getPayload().getPayloadValue()));
			
			break;
			
		case METHOD:
			
			List<FieldValue> fvs = new ArrayList<FieldValue>();
			
			PLAYNode param = pn.getChild(0);
			PLAYNode ot = pn.getChild(1);
			PLAYNode seq = pn.getChild(2);
			
			for(PLAYNode n2:param.getChildren()){
				
				if(!n2.getChild(1).getTag().equals(PLAYTag.NOEXP)){
					errorMap.addError(pn, errFTYEng.paramInitialization(n2.getPayload().getPayloadValue()));
				}
				
				if(n2.getChild(0).getTag().equals(PLAYTag.NOTYPE)){
					errorMap.addError(pn, errFTYEng.paramMustHaveType(n2.getPayload().getPayloadValue()));
				}
				
				FieldValue fv = checkVar1(n2, st, map, errorMap);
				
				fvs.add(new FieldValue(n2.getPayload().getConstness(),fv.getType()));
				
				map.put(n2, fv);
			}
			
			
			st.pushFrame();
				
			for(int j=0;j<fvs.size();j++){
				
				st.put( param.getChild(j).getPayload().getPayloadValue(), 
						Kind.LOCAL, fvs.get(j).getConstness(), fvs.get(j).getType()
						);					
			}
			
			Type tb=checkSeq1(seq,st,map, errorMap);
			
			st.popFrame();
			
			Type tr;
			
			if(ot.getTag().equals(PLAYTag.NOTYPE)){
				tr=tb;
			}else{
				tr=checkType(ot, errorMap);	
				if(!tr.isSuperTypeOf(tb) && !tb.isUnknown()){
					errorMap.addError(pn, errFTYEng.methodTypeMismatch(tb,tr));
				}
			}
			
			List<Type> types = new ArrayList<Type>();
			
			for(int j=0;j<fvs.size();j++){	
				types.add(fvs.get(j).getType());
			}
			
			t= new Type(new MethodAtom(tr,types));

			break;
			
		case NEW:
			t = checkType(pn.getChild(0),errorMap);
			break;
			
		case IF:

			Type te = checkExp1( pn.getChild(0), st, map, errorMap );

			if(!te.equals(new Type(BooleanAtom.getInstance()))){
				errorMap.addError(pn, errFTYEng.conditionNotBoolean());
			}

			Type ty=checkSeq1(pn.getChild(1),st,map, errorMap);
			Type tn=checkSeq1(pn.getChild(2),st,map, errorMap);
			t = ty.merge(tn);
			
			break;
			
		case WHILE:
			Type tee = checkExp1( pn.getChild(0), st, map, errorMap );
			if(!tee.equals(new Type(BooleanAtom.getInstance()))){
				errorMap.addError(pn, errFTYEng.conditionNotBoolean());
			}
	        checkSeq1( pn.getChild(1), st, map, errorMap );
	        t=new Type(NullAtom.getInstance());
	        
	        break;
	        
		case EXPPLACEHOLDER:
			errorMap.addError(pn, errFTYEng.missingCode());
			t=new Type(UnknownAtom.getInstance());
			break;
		
		case ASSIGN:	
			Type te0 = checkExp1( pn.getChild(0), st, map, errorMap );
			Type te1 = checkExp1( pn.getChild(1), st, map, errorMap );
			if(!te0.isSuperTypeOf(te1)){
				errorMap.addError(pn, errFTYEng.typeErrorInAssignment(te0,te1));
			}
			if(!isAssignable(pn.getChild(0),st)){
				errorMap.addError(pn.getChild(0), errFTYEng.expNotAssignable());
			}

	        t =  new Type(NullAtom.getInstance());
			break;
			
		case CALLCLOSURE:
	        
	       	List<Type> p = new ArrayList<Type>();
	        int n1 = pn.getNumberOfChildren()-1;
	        for(int j=0;j<=n1;j++){
	        	p.set(j, checkExp1( pn.getChild(1), st, map, errorMap ));
	        }
	        Type tm = p.get(0);
	        if(tm.getAtomsAmount()==1 
	        		&&tm.toString().contains("METHOD")){
	        	MethodAtom ma = (MethodAtom)tm.getAtomsList().get(0);
	        	int m = ma.getParamListSize();
	        	if(m!=n1){
	        		errorMap.addError(pn, errFTYEng.paramlistLenNotMatch(n1,m));
	        	}else{	
	        		for(int j=1;j<=m;j++){
	        			if(!ma.getParamTypeAt(j-1).isSuperTypeOf(p.get(j))){
	        				errorMap.addError(pn.getChild(j), errFTYEng.paramTypeNotMatch());
	        			}
	        		}
	        	}
	        }else{
	        	errorMap.addError(pn, errFTYEng.onlyMethodsMayBeCalled());
	        }

			break;
			
		case CALLWORLD:
			break;
		default:
			System.out.println("TBD");
		}

		map.put(pn, new FieldValue(Constness.CON,t));
		return t;

	}

	private boolean isAssignable(PLAYNode n, SymbolTable st) {
		String i = n.getPayload().getPayloadValue();
		switch(n.getTag()){
		case THISVAR: 
			return st.hasEntry( i, Kind.THIS) 
					&& st.getConst(i,Kind.THIS).equals(Constness.VAR);
    	case LOCALVAR: 
    		return st.hasEntry( i, Kind.LOCAL) 
					&& st.getConst(i,Kind.LOCAL).equals(Constness.VAR);
    	case WORLDVAR: 
    		return st.hasEntry( i, Kind.WORLD) 
					&& st.getConst(i,Kind.WORLD).equals(Constness.VAR);
    	default: 
    		return false;
		}
    	
	}

	private Type checkSeq1(PLAYNode seq, SymbolTable st,
			HashMap<PLAYNode, FieldValue> map, ErrorMap errorMap) {
		
		if(seq.getChildren().size()==0){
			map.put(seq, new FieldValue(Constness.CON, new Type(NullAtom.getInstance())));
			return new Type(NullAtom.getInstance());
		}
		
		int c=0;
		FieldValue fv=null;
		
		for(PLAYNode pn:seq.getChildren()){
			switch(pn.getTag()){
			case VARDECL:
				
				if(pn.getChild(0).getTag().equals(PLAYTag.NOTYPE)
						&&pn.getChild(1).getTag().equals(PLAYTag.NOEXP)){
					errorMap.addError(pn, errFTYEng.incompleteDeclaration(pn.getPayload().getPayloadValue()));
				}
				
				fv = checkVar1(pn,st,map, errorMap);
				map.put(pn, fv);
				st.pushFrame();
				c++;
				st.put(pn.getPayload().getPayloadValue(), Kind.LOCAL, 
						fv.getConstness(), fv.getType());
				break;
				
			default:
				fv = new FieldValue(pn.getPayload().getConstness(),checkExp1(pn,st,map, errorMap));	
			}
		}
		
		for(int i=0;i<c;i++){
			st.popFrame();
		}
		
		map.put(seq, new FieldValue(Constness.CON, new Type(NullAtom.getInstance())));
		return fv.getType();
	}

}
