public class Outer {
	private int x = 0 ;
	
	private class Inner implements Runnable {
		public void run() {
			x += 1 ; }
	}
	
	public int get() {
		return x ; }
	
	public Runnable getIncrementer() {
		return new Inner() ; }
}