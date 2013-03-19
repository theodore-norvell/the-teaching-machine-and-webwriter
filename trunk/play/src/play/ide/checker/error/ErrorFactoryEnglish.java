package play.ide.checker.error;

import play.higraph.model.PLAYNode;
import play.ide.checker.type.Type;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class ErrorFactoryEnglish extends ErrorFactory {
	
	@Override
    public PLAYError duplicateField( String className, String fieldName ) {
        return new PLAYError( "Class " +className+ " has two fields named "
        						+fieldName+ "." ) ; }
    
	@Override
    public PLAYError typeOfInitializerMismatchTheDeclaredType( Type lhsType, Type rhsType) {
        return new PLAYError(  "type of initializer "+ lhsType+" does not match the declared var type "+ rhsType) ; }
	
	@Override
	public PLAYError fieldNotFound(String fieldName) {
		return new PLAYError("Field "+fieldName+" not found");
	}
	
	@Override
	public PLAYError classNotFound(String className) {
		return new PLAYError("Class "+className+" not found");
	}
	
	@Override
	public PLAYError expectClassTypeButNotFound(Type t) {
		return new PLAYError("Expected a class type, but found "+t+" instead");
	}
	
	@Override
	public PLAYError localVarNotFound(String varName) {
		return new PLAYError("Local variable "+varName+" not found");
	}
	
	@Override
	public PLAYError worldVarNotFound(String varName) {
		return new PLAYError("World variable "+varName+" not found");
	}
	
	@Override
	public PLAYError fieldNotFoundInClass(String i, String c) {
		return new PLAYError("Field "+i+" was not found in class " +c);
	}
	
	@Override
	public PLAYError paramInitialization(String str) {
		// TODO Auto-generated method stub
		return new PLAYError( "Parameter "+str+" should not be initialized");
	}
	
	@Override
	public PLAYError paramMustHaveType(String str) {
		// TODO Auto-generated method stub
		return new PLAYError( "Parameter "+str+" must have a type");
	}
	
	@Override
	public PLAYError methodTypeMismatch(Type tb, Type tr) {
		// TODO Auto-generated method stub
		return new PLAYError("method body result is type " +tb+ ". But the declared method result type is " +tr);
	}
	
	@Override
	public PLAYError incompleteDeclaration(String str) {
		return new PLAYError("variable "+ str +" needs either an initialization or a type");
	}
	
	@Override
	public PLAYError conditionNotBoolean() {
		// TODO Auto-generated method stub
		return new PLAYError("condition may not be boolean");
		
	}

	@Override
	public PLAYError parsingNumber(String str) {
		// TODO Auto-generated method stub
		return new PLAYError("Syntax error in number "+str);
	}
	
	@Override
	public PLAYError missingCode() {
		// TODO Auto-generated method stub
		return new PLAYError("Missing code");
	}

	@Override
	public PLAYError typeErrorInAssignment(Type te0, Type te1) {
		// TODO Auto-generated method stub
		return new PLAYError( "Type error in assignment. "+ te1 +" is not a subtype of "+te0);
	}

	@Override
	public PLAYError expNotAssignable() {
		// TODO Auto-generated method stub
		return new PLAYError( "Expression not assignable.");
		
	}
	
	@Override
	public PLAYError cannotDeduceTypeForField(String payloadValue) {
		// TODO Auto-generated method stub
		return new PLAYError("Could not deduce the type of field "+payloadValue);
	}

	@Override
	public PLAYError cannotDetermineTypeForVar(String payloadValue) {
		// TODO Auto-generated method stub
		return new PLAYError( "Can not determine the type for variable "+payloadValue);
	}

	@Override
	public PLAYError paramlistLenNotMatch(int n1, int m) {
		// TODO Auto-generated method stub
		return new PLAYError(n1+ " arguments were expected, but there are" +m+ "." );
	}

	@Override
	public PLAYError paramTypeNotMatch() {
		// TODO Auto-generated method stub
		return new PLAYError("The type of argument does not match the type of the parameter.");
	}

	@Override
	public PLAYError onlyMethodsMayBeCalled() {
		// TODO Auto-generated method stub
		return new PLAYError( "Only methods may be called.");
	}


}
