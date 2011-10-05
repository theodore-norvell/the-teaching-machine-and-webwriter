/*#T tm*/package pacc.chapter02;

/*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV;
*//*#T ww*/
public class DynamicArray {/*#I
public static final int X0 = 20;
public static final int Y0 = 20;
public static final int ONVALUEFILLCOLOR = PDV.BLUE;
public static final int OFFVALUEFILLCOLOR = PDV.GREY;
public static final int ONVALUETEXTCOLOR = PDV.WHITE;
public static final int OFFVALUETEXTCOLOR = PDV.BLACK;*/

	private int d;
	private int n;
	private int[] a;

	public DynamicArray() {
		n = 0;
		d = 1;
		a = new int[d];
		/*#I
		ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.Arrays", "PlacedNode");
		ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Visualization of dynamic arrays");
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
		ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.RECTANGLE);   
		makeNodes(a);
		colorArray(a);
		placeArray(a, X0, Y0);*/
	}

	/*#I
	public void makeNodes(int[] array) {
		for (int i = 0; i < array.length; i++) {
			ScriptManager.relay("HigraphManager", "makeNode", array[i]);
		}
	}

	public void placeArray(int[] array, int x, int y) {
		int xp = x;
		for (int i = 0; i < array.length; i++) {
			ScriptManager.relay("HigraphManager", "placeNode", array[i], xp, y);
			xp = xp + 30;
		}
	}

	public void colorArray(int[] array) {
		for (int i = 0; i < n; i++) {
			ScriptManager.relay("HigraphManager", "setNodeFillColor", array[i],
					ONVALUEFILLCOLOR);
			ScriptManager.relay("HigraphManager", "setNodeValueColor", array[i],
					ONVALUETEXTCOLOR);
		}
		for (int i = n; i < d; i++) {
			ScriptManager.relay("HigraphManager", "setNodeFillColor", array[i],
					OFFVALUEFILLCOLOR);
			ScriptManager.relay("HigraphManager", "setNodeValueColor", array[i],
					OFFVALUETEXTCOLOR);
		}
	}

	public void deleteNodes(int[] array) {
		for (int i = 0; i < array.length; i++)
			ScriptManager.relay("HigraphManager", "deleteNode", array[i]);
	}

	public void makeNewArray(int[] array) {
		makeNodes(array);
		placeArray(array, X0, Y0 + 80);
		colorArray(array);
	}

	public void replaceArray(int[] oldArray, int[] newArray) {
		deleteNodes(oldArray);
		colorArray(newArray);
		placeArray(newArray, X0, Y0);
	}*/

	public int get(int i) {
		return a[i];
	}

	public void set(int i, int x) {
		a[i] = x;
		/*#I
		ScriptManager.relay("HigraphManager", "setNodeFillColor", a[i], ONVALUEFILLCOLOR);
		ScriptManager.relay("HigraphManager", "setNodeValueColor", a[i], ONVALUETEXTCOLOR);*/
	}

	public void grow() {
		if (n == d) {
			int[] b = new int[2 * d];
			d = 2 * d;
			/*#I
			makeNewArray(b);*/
			for (int i = 0; i < n; ++i)
				b[i] = a[i];
			/*#I
			replaceArray(a, b);*/
			a = b;
		}
	}

	public void shrink() {
		if ((d > 1) && (n == d / 4)) {
			int[] b = new int[d / 2];
			d = d / 2;
			/*#I
			makeNewArray(b);*/
			for (int i = 0; i < n; ++i)
				b[i] = a[i];
			/*#I
			replaceArray(a, b);*/
			a = b;
		}
	}

	public static void main(String[] str) {
		DynamicArray e = new DynamicArray();
		int x = 12;
		for (int i = 0; i < 10; ++i) {
			e.grow();
			e.set(i, x);
			e.n = e.n + 1;
			++x;
		}

		for (int i = 0; i < 10; ++i) {
			e.n = e.n - 1;/*#I
			ScriptManager.relay("HigraphManager", "setNodeFillColor", e.a[e.n], OFFVALUEFILLCOLOR);
			ScriptManager.relay("HigraphManager", "setNodeValueColor", e.a[e.n], OFFVALUETEXTCOLOR);*/
			e.shrink();
		}
	}
}
