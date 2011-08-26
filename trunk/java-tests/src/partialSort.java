public class PartialSort {
	private static final int LENGTH = 10;

	private static final int MINIMUM = 1;

	private static final int MAXIMUM = 100;
	
	protected final static int ALGORITHMS = 3;

	protected final static int MIN_STEP = 5;

	protected final static int MAX_STEP = 15;
	
	protected final String names[] = {"bubbleSort", "insertionSort", "selectionSort"};
	
	private int numberOfSteps;
	

	public static void main() {
		PartialSort ps = new PartialSort();
		ps.run();
		
	}
	
	private void run() {
		int algorithm = (int) (Math.random() * ALGORITHMS);
		numberOfSteps = (int) (Math.random() * (MAX_STEP - MIN_STEP + 1)) + MIN_STEP;
		
		int[] a = generateArray(LENGTH, MINIMUM, MAXIMUM);
		int[][] sorted = new int[ALGORITHMS][];
		
		sorted[0] = bubbleSort(copyArray(a));
		sorted[1] = insertionSort(copyArray(a));
		sorted[2] = selectionSort(copyArray(a));
		
		for (int i = 0; i < ALGORITHMS; i++) 
			if (i == algorithm)
				snapShot(sorted[i], i);
			else
				checkState(sorted[i], i);
		runChecks();		
	}


//	private int[] snapshot;   //to be handled by the TM

/* Handled in WebWriter/html editor
  	protected String createQuestion(int[] a) {
		String question = "Consider the following array a.\n" + Main.toString(a)
				+ "\n";
		int algorithm = (int) (Math.random() * ALGORITHMS);
		numberOfSteps = (int) (Math.random() * (MAX_STEP - MIN_STEP + 1)) + MIN_STEP;
		switch (algorithm) {
		case 0:
			snapshot = bubbleSortSnapshot(a);
			break;
		case 1:
			snapshot = insertionSortSnapshot(a);
			break;
		case 2:
			snapshot = selectionSortSnapshot(a);
		}
		question = question + "Which of the following sorting algorithm, after " + numberOfSteps
				+ " steps, is in the following configuration?\n";
		question = question + Main.toString(snapshot) + "\n";
		question = question + "0. Bubblesort\n";
		question = question + "1. Insertion sort\n";
		question = question + "2. Selection sort\n";
		return question;
	} */

/* Handled in some combination of a TM plugin and a webwriter script
	protected boolean checkAnswer(int[] a, int answer) {
		int[] bss = bubbleSortSnapshot(Main.copyArray(a));
		int[] iss = insertionSortSnapshot(Main.copyArray(a));
		int[] sss = selectionSortSnapshot(Main.copyArray(a));
		if (Main.areEqual(snapshot, bss) && answer == 0)
			return true;
		if (Main.areEqual(snapshot, iss) && answer == 1)
			return true;
		if (Main.areEqual(snapshot, sss) && answer == 2)
			return true;
		return false;
	} */	

	protected int[] bubbleSort(int[] a) {
		int i, j, n = a.length, step = 0, tmp;
		for (i = 0; i < n; i++) {
			for (j = n - 1; j > i; j--) {
				step++;
				if (a[j - 1] > a[j]) {
					tmp = a[j];
					a[j] = a[j - 1];
					a[j - 1] = tmp;
				}
				/*#HS*/ // Start of instrumentation - doesn't have to be hidden
				step++;
				if (numberOfSteps == step) 
					return a;
				/*#DS*/
			}
		}
		return a;
	}

	protected int[] insertionSort(int[] a) {
		int i, j, n = a.length, next, step = 0;
		for (i = 0; i < n; i++) {
			next = a[i];
			j = i;
			while ((j > 0) && (a[j - 1] > next)) {
				a[j] = a[j - 1];
				j = j - 1;
				/*#HS*/ // Start of instrumentation - doesn't have to be hidden
				step++;
				if (numberOfSteps == step) 
					return a;
				/*#DS*/
			}
			a[j] = next;
			/*#HS*/ // Start of instrumentation - doesn't have to be hidden
			step++;
			if (numberOfSteps == step) 
				return a;
			/*#DS*/
		}
		return a;
	}

	protected int[] selectionSort(int[] a) {
		int i, j, minimum, minimumIndex, n = a.length, step = 0;
		for (i = 0; i < n; i++) {
			minimum = a[i];
			minimumIndex = i;
			for (j = i + 1; j < n; j++) {
				if (a[j] < minimum) {
					minimum = a[j];
					minimumIndex = j;
				}
				/*#HS*/  // Start of instrumentation - doesn't have to be hidden
				step++;
				if (numberOfSteps == step) 
					return a;
				/*#DS*/
			}
			a[minimumIndex] = a[i];
			a[i] = minimum;
			/*#HS*/  // Start of instrumentation - doesn't have to be hidden
			step++;
			if (numberOfSteps == step) 
				return a;
			/*#DS*/
		}
		return a;
	}

/** Encapsulation of script calls inside of standard function calls
 *  While this is convenient it is not necessary.
 */
	private void snapShot(int[] a, int answer){
		/*#HS*/ // ensures compilation on a standard compiler
		/* Note that relayCall is implemented with Java
		 * clearSelection is implemented in the visualizer DisplayAdaptor
		 * addToSelection is implemented in the visualizer DisplayAdaptor
		 * (where implemented means "built for and working with Java")
		 * dropSnapShot is to be implemented by MUN in the DisplayAdaptor
		 * setCorrect would be part of Unifi developed plugin
		 */
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer", "clearSelection");
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer", "addToSelection", a);
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer","dropSnapShot",names[answer]);
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer","setCorrect",answer);		
		/*#DS*/	}

	private void checkState(int[] a, int answer){
		/*#HS*/ // ensures compilation on a standard compiler
		/* Note that relayCall is implemented with Java
		 * (where implemented means "built for and working with Java")
		 * captureForCheck would be part of Unifi developed plugin
		 */
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer","captureForCheck", a, answer);		
		/*#DS*/	}

	private void runChecks(){
		/*#HS*/ // ensures compilation on a standard compiler
		/* Note that relayCall is implemented with Java
		 * (where implemented means "built for and working with Java")
		 * runChecks would be part of Unifi developed plugin
		 */
//		ScriptManager.relayCall("Unifi.PlugIn.ArrayVisualizer","runChecks");		
		/*#DS*/	}

/** These service routines have been moved from Pilu's main because the current version of
 * the TM doesn't work across multiple files. Thus they must be copied into every question
 *  separately.
 */
	
	protected static boolean areEqual(int[] a, int[] b) {
		int i, n = a.length;
		if (n != b.length) return false;
		for (i = 0; i < n; i++) {
			if (b[i] != a[i]) return false;
		}
		return true;
	}

	protected static int[] copyArray(int[] a) {
		int i, n = a.length;
		int[] b = new int[n];
		for (i = 0; i < n; i++) {
			b[i] = a[i];
		}
		return b;
	}

	private int[] generateArray(int n, int m, int M) {
		int i;
		int[] a = new int[n];
		for (i = 0; i < n; i++) {
			a[i] = (int) (Math.random() * (M - m + 1)) + m;
		}
		return a;
	}

}
