/*#T tm*/package pacc.chapter02;

/*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV;
*//*#T ww*/
public class SegmentSum {/*#I
public static final int X0 = 20;
public static final int Y0 = 20;
public static final int NEXTFILLCOLOR = PDV.GREEN;
public static final int DONEFILLCOLOR = PDV.GREY;
public static final int STARTFILLCOLOR = PDV.BLUE;
public static final int ENDFILLCOLOR = PDV.RED;
public static final int NEXTTEXTCOLOR = PDV.BLACK;
public static final int DONETEXTCOLOR = PDV.BLACK;
public static final int STARTTEXTCOLOR = PDV.WHITE;
public static final int ENDTEXTCOLOR = PDV.WHITE;*/

	private int[] a;

	public SegmentSum() {
		a = new int[] { 31, -41, 59, 26, -53, 58, 97, -93, -95, 14, -10, 20 };
		/*#I
		ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.Arrays", "PlacedNode");
		ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Visualization of the segment sum algorithm");
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
		ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.NORTH);
		ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.BLACK);
		ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.RECTANGLE);   
		makeNodes(a);
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
	}*/

	public void run() {
		int max = 0;
		/*#I
		ScriptManager.relay("HigraphManager", "makeNode", max) ;
		ScriptManager.relay("HigraphManager", "setNodeNameLabel", max, "max" ) ;
		ScriptManager.relay("HigraphManager", "setNodeNamePosition", max, PDV.NORTH ) ;
		ScriptManager.relay("HigraphManager", "placeNode", max, 20, 100) ;*/
		int sum = max;
		/*#I
		ScriptManager.relay("HigraphManager", "makeNode", sum) ;
		ScriptManager.relay("HigraphManager", "setNodeNameLabel", sum, "sum" ) ;
		ScriptManager.relay("HigraphManager", "setNodeNamePosition", sum, PDV.NORTH ) ;
		ScriptManager.relay("HigraphManager", "placeNode", sum, 100, 100) ;
		int startMax = 0, startNext = 0, endMax = 0;*/
		int j;
		for (j = 0; j < a.length; j++) {
			/*#I 
			ScriptManager.relay("HigraphManager", "setNodeFillColor", a[j], NEXTFILLCOLOR);
			ScriptManager.relay("HigraphManager", "setNodeValueColor", a[j], NEXTTEXTCOLOR);*/
			if (sum > 0) {
				sum = sum + a[j];
			} else {
				sum = a[j];
				/*#I
				startNext = j;*/
			}
			if (sum > max) {
				max = sum;
				/*#I
				ScriptManager.relay("HigraphManager", "setNodeFillColor", a[startMax], DONEFILLCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeValueColor", a[startMax], DONETEXTCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeFillColor", a[endMax], DONEFILLCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeValueColor", a[endMax], DONETEXTCOLOR);
				startMax = startNext;
				endMax = j;
				ScriptManager.relay("HigraphManager", "setNodeFillColor", a[startMax], STARTFILLCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeValueColor", a[startMax], STARTTEXTCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeFillColor", a[endMax], ENDFILLCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeValueColor", a[endMax], ENDTEXTCOLOR);*/
			}
			/*#I 
			if (j != startMax && j != endMax) {
				ScriptManager.relay("HigraphManager", "setNodeFillColor", a[j], DONEFILLCOLOR);
				ScriptManager.relay("HigraphManager", "setNodeValueColor", a[j], DONETEXTCOLOR);
			}*/
		}
	}

	public static void main(String[] str) {
		SegmentSum e = new SegmentSum();
		e.run();
	}
}
