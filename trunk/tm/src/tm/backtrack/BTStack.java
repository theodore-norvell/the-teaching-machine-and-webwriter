package tm.backtrack;

import tm.utilities.Assert;

public class BTStack<E>  extends BTVector<E> {

    public BTStack(BTTimeManager tm) {
    	super(tm);
    }

    public void push(E o) {
        insertElementAt(o, size()) ;
    }

    public void pop() {
    	Assert.check( size() > 0 ) ;
        removeElementAt(size()-1); }


    public E top() { return get(size() - 1) ; }

    public void trimTo( int newSize ) {
        int amountToTrim = size() - newSize ;
        Assert.check( amountToTrim >= 0 ) ;
        for( ; amountToTrim > 0 ; --amountToTrim ) pop() ;
    }
}
