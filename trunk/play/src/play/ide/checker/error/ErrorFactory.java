package play.ide.checker.error;

import play.higraph.model.PLAYNode;
import play.ide.checker.type.Type;

/**
 * 
 * @author Shiwei Han
 * 
 */
public abstract class ErrorFactory {
	public abstract PLAYError duplicateField(String className,String fieldName);
    public abstract PLAYError typeOfInitializerMismatchTheDeclaredType(Type lhsType,Type rhsType);
    public abstract PLAYError fieldNotFound(String fieldName);
    public abstract PLAYError classNotFound(String className);
    public abstract PLAYError expectClassTypeButNotFound(Type t);
    public abstract PLAYError localVarNotFound(String varName);
    public abstract PLAYError worldVarNotFound(String varName);
    public abstract PLAYError fieldNotFoundInClass(String i, String c);
    public abstract PLAYError paramInitialization(String str);
    public abstract PLAYError paramMustHaveType(String str);
    public abstract PLAYError methodTypeMismatch(Type tb, Type tr);
    public abstract PLAYError incompleteDeclaration(String str);
    public abstract PLAYError conditionNotBoolean();
    public abstract PLAYError parsingNumber(String str);
}
