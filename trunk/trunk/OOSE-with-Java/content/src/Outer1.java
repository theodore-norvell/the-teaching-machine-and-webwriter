public class Outer {
	private int x = 0 ;
	
	public int get() { return x ; }
	
	public Runnable getIncrementer() {
		return new Runnable() {
			public void run() {
				x += 1 ; } } ;
	}
}