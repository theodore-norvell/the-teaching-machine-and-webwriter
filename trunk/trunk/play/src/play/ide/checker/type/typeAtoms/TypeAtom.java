package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public abstract class TypeAtom {
	public abstract boolean canonicalOver(TypeAtom ta);
}