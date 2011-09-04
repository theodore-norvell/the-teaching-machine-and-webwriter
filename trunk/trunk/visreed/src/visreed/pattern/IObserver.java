package visreed.pattern;

/**
 * IObserver provides a generic wrapper for Observer Pattern.
 * @author Xiaoyu Guo
 *
 * @param <T>
 */
public interface IObserver<T extends IObservable<T>> {
	/**
	 * @param object
	 */
	void changed(T object);
}
