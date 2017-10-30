package tm.gwt.client;

import java.util.ArrayList ;

public class Observable {
    private ArrayList<Observer> observers = new ArrayList<Observer>() ;
    private boolean changed = false ;
    
    public void setChanged() {
        changed = true ;
    }
    
    public void notifyObservers( ) {
        notifyObervers( null ) ;
    }

    private void notifyObervers(Object extra) {
        for( Observer o : observers ) o.update( this, extra );
    }
    
    public void addObserver( Observer obs ) {
        observers.add( obs ) ;
    }
    
    public void removeObserver( Observer obs ) {
        observers.remove( obs ) ;
    }
}
