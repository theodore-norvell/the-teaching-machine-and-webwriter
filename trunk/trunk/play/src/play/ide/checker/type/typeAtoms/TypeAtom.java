package play.ide.checker.type.typeAtoms;

/**
 * 
 * @author Shiwei Han
 * 
 */
public abstract class TypeAtom {
	public abstract boolean isSuperAtomOf(TypeAtom ta);
}