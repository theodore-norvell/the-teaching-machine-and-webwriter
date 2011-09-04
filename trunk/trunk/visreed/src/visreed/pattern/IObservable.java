package visreed.pattern;

/**
 * IObservable provides common features for Observer Pattern
 * @author Xiaoyu Guo
 *
 * @param <T>
 */
public interface IObservable<T extends IObservable<T>> {
    /**
     * Register an observer to this object of type T
     * @param o the observer
     */
    void registerObserver(IObserver<T> o);
    
    /**
     * De-Register an observer to this object of type T
     * @param o
     */
    void deRegisterObserver(IObserver<T> o);
    
    /**
     * Notify the observers that the content of the object of type T is changed.
     */
    void notifyObservers();
}
