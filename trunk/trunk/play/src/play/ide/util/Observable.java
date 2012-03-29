/**
 * Observable.java - play.ide.util - PLAY
 * 
 * Created on 2012-03-29 by Kai Zhu
 */
package play.ide.util;

/**
 * @author Kai Zhu
 * 
 */
public interface Observable {

    public void addObserver(Observer observer);

    public void deleteObserver(Observer observer);

    public void setChanged();

    public void clearChanged();

    public boolean hasChanged();

    public void notifyObservers(Object arg);

}
