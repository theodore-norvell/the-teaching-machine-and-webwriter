/*#T tm*/package pacc.chapter02;

/*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV;
*//*#T ww*/
public class FastIntegerProduct {/*#I
public static int DELTAX = 40;
public static int DELTAY = 40;
public static int XX = 20;
public static int YX = 5;
public static int XY = 20;
public static int YY = YX + DELTAY;
public static int XP = 20;
public static int YP = YX + 2*DELTAY;
public static final int NEXTFILLCOLOR = PDV.GREEN;
public static final int DONEFILLCOLOR = PDV.GREY;
public static final int STARTFILLCOLOR = PDV.BLUE;
public static final int ENDFILLCOLOR = PDV.RED;
public static final int NEXTTEXTCOLOR = PDV.BLACK;
public static final int DONETEXTCOLOR = PDV.BLACK;
public static final int STARTTEXTCOLOR = PDV.WHITE;
public static final int ENDTEXTCOLOR = PDV.WHITE;*/

	private int[] x;
	private int[] y;
	private int n;

	public FastIntegerProduct() {
		x = new int[] { 1, 1, 2, 3, 4 };
		y = new int[] { 1, 5, 6, 7, 8 };
		n = 4;
		/*#I
		ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.Arrays", "PlacedNode");
		ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Visualization of the fast integer product algorithm");
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
		ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", false, PDV.NORTH);
		ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
		ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.BLACK);
		ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.RECTANGLE);*/
	}

	/*#I
	public void makeNodes(int[] array) {
		for (int i = 0; i < array.length; i++) {
			ScriptManager.relay("HigraphManager", "makeNode", array[i]);
		}
	}

	public void deleteNodes(int[] array) {
		for (int i = 0; i < array.length; i++) {
			ScriptManager.relay("HigraphManager", "deleteNode", array[i]);
		}
	}

	public void placeArray(int[] array, int x, int y) {
		int xp = x;
		for (int i = 0; i < array.length; i++) {
			ScriptManager.relay("HigraphManager", "placeNode", array[i], xp, y);
			xp = xp + 30;
		}
	}*/

	private int[] extend(int[] x, int l) {
		int n = x.length - 1;
		int[] result = new int[l + 1];
		result[0] = x[0];
		for (int i = 1; i <= l - n; i = i + 1) {
			result[i] = 0;
		}
		for (int i = 1; i <= n; i = i + 1) {
			result[i + (l - n)] = x[i];
		}
		return result;
	}

	private int[] fastMultiplication(int[] cx, int[] cy, int n) {
		int[] product = new int[2 * n + 1];
		/*#I
		makeNodes(product);
		placeArray(product, XX, YX+2*DELTAY);
		String ps = "product";
		ScriptManager.relay("HigraphManager", "createString", ps);
		ScriptManager.relay("HigraphManager", "setStringBaseColor", ps, PDV.BLACK);
		ScriptManager.relay("HigraphManager", "placeString", ps, XX + (2*n+1)*DELTAX, YX+2*DELTAY);*/
		if (n == 1) {
			product[1] = cx[1] * cy[1] / 10;
			product[2] = cx[1] * cy[1] % 10;
		} else {
			int[] xs = new int[n / 2 + 1];
			/*#I
			makeNodes(xs);
			placeArray(xs, XX, YX + 3 * DELTAY);*/
			int[] xd = new int[n / 2 + 1];
			/*#I
			makeNodes(xd);
			placeArray(xd, XX, YX + 4 * DELTAY);*/
			int[] ys = new int[n / 2 + 1];
			/*#I
			makeNodes(ys);
			placeArray(ys, XY, YX + 5 * DELTAY);*/
			int[] yd = new int[n / 2 + 1];
			/*#I
			makeNodes(yd);
			placeArray(yd, XY, YX + 6 * DELTAY);*/
			xs[0] = 1;
			xd[0] = 1;
			ys[0] = 1;
			yd[0] = 1;
			for (int i = 1; i <= n / 2; i++) {
				xs[i] = cx[i];
				xd[i] = cx[i + n / 2];
				ys[i] = cy[i];
				yd[i] = cy[i + n / 2];
			}
			/*#I
			XX = XX + (2*n+1)*DELTAX;
			XY = XY + (2*n+1)*DELTAX;
			placeArray(xs, XX, YX);
			placeArray(ys, XY, YY);*/
			/*#T S*/int[] /*#/T S*/p1 = fastMultiplication(xs, ys, n / 2);
			/*#I
			XX = XX - (2*n+1)*DELTAX;
			XY = XY - (2*n+1)*DELTAX;
			placeArray(xs, XX, YX + 3 * DELTAY);
			placeArray(ys, XY, YX + 5 * DELTAY);
			makeNodes(p1);
			placeArray(p1, XX, YX + 7 * DELTAY);*/
			for (int i = 0; i <= n; i++) {
				product[i] = p1[i];
				product[i + n] = 0;
			}
			/*#I
			XX = XX + (2*n+1)*DELTAX;
			XY = XY + (2*n+1)*DELTAX;
			placeArray(xd, XX, YX);
			placeArray(yd, XY, YY);*/
			int[] p2 = fastMultiplication(xd, yd, n / 2);
			/*#I
			XX = XX - (2*n+1)*DELTAX;
			XY = XY - (2*n+1)*DELTAX;
			placeArray(xd, XX, YX + 4 * DELTAY);
			placeArray(yd, XY, YX + 6 * DELTAY);
			makeNodes(p2);
			placeArray(p2, XX, YX+8*DELTAY);*/
			xd[0] = -1;
			yd[0] = -1;
			int[] sum1 = sum(xs, xd, n / 2);
			/*#I
			XX = XX + (2*n+1)*DELTAX;
			XY = XY + (2*n+1)*DELTAX;
			makeNodes(sum1);
			placeArray(sum1, XX, YX);*/
			int[] sum2 = sum(ys, yd, n / 2);
			/*#I
			makeNodes(sum2);
			placeArray(sum2, XY, YY);*/
			int[] p3 = fastMultiplication(sum1, sum2, n / 2);
			/*#I
			XX = XX - (2*n+1)*DELTAX;
			XY = XY - (2*n+1)*DELTAX;
			deleteNodes(sum1);
			deleteNodes(sum2);
			deleteNodes(xs);
			deleteNodes(xd);
			deleteNodes(ys);
			deleteNodes(yd);
			placeArray(p1, XX, YX+3*DELTAY);
			placeArray(p2, XX, YX+4*DELTAY);
			makeNodes(p3);
			placeArray(p3, XX, YX+5*DELTAY);*/
			p3[0] = -p3[0];
			int[] add = sum(sum(p1, p2, 2 * n), extend(p3, 2 * n), 2 * n);
			/*#I
			makeNodes(add);
			placeArray(add, XY, YX+6*DELTAY);*/
			int[] partial = new int[2 * n + 1];
			/*#I
			makeNodes(partial);
			placeArray(partial, XY, YX+7*DELTAY);*/
			partial[0] = add[0];
			for (int i = 1; i <= 3 * (n / 2); i++) {
				partial[i] = add[i + n / 2];
				partial[i + n / 2] = 0;
			}
			/*#I
			deleteNodes(p1);
			deleteNodes(p3);
			deleteNodes(add);
			placeArray(partial, XY, YX+3*DELTAY);
			placeArray(p2, XY, YX+4*DELTAY);
			int[] tmp = product;*/
			product = sum(sum(product, partial, 2 * n), extend(p2, 2 * n), 2 * n);
			/*#I
			deleteNodes(p2);
			deleteNodes(partial);
			deleteNodes(tmp);
			makeNodes(product);
			placeArray(product, XY, YX+2*DELTAY);*/
		}
		product[0] = cx[0] * cy[0];
		return product;
	}

	private boolean isGreaterThan(int[] x, int[] y) {
		int i = 1;
		while ((i < x.length) && (x[i] == y[i])) {
			i = i + 1;
		}
		if ((i < x.length) && (x[i] > y[i])) {
			return true;
		}
		return false;
	}

	private int[] sum(int[] cx, int[] cy, int l) {
		int[] result = new int[l + 1];
		int iXY = cx.length - 1;
		int iResult = l;
		int carry = 0;
		int digitSum;
		if (cx[0] == cy[0]) {
			while (iXY > 0) {
				digitSum = cx[iXY] + cy[iXY] + carry;
				result[iResult] = digitSum % 10;
				carry = digitSum / 10;
				iXY = iXY - 1;
				iResult = iResult - 1;
			}
			result[iResult] = carry;
			iResult = iResult - 1;
			while (iResult > 0) {
				result[iResult] = 0;
				iResult = iResult - 1;
			}
			result[0] = cx[0];
		} else {
			int[] firstOperand, secondOperand;
			boolean yGreaterThanX = isGreaterThan(cy, cx);
			int sign;
			if (!yGreaterThanX) {
				firstOperand = cx;
				secondOperand = cy;
				if (cx[0] > 0) {
					sign = 1;
				} else {
					sign = -1;
				}
			} else {
				firstOperand = cy;
				secondOperand = cx;
				if (cy[0] > 0) {
					sign = 1;
				} else {
					sign = -1;
				}
			}
			while (iXY > 0) {
				digitSum = firstOperand[iXY] - secondOperand[iXY] + carry;
				if (digitSum >= 0) {
					result[iResult] = digitSum;
					carry = 0;
				} else {
					result[iResult] = 10 + digitSum;
					carry = -1;
				}
				iXY = iXY - 1;
				iResult = iResult - 1;
			}
			while (iResult > 0) {
				result[iResult] = 0;
				iResult = iResult - 1;
			}
			result[0] = sign;
		}
		return result;
	}

	private void run() {
		/*#I
		makeNodes(x);
		placeArray(x, XX, YX);
		makeNodes(y);
		placeArray(y, XY, YY);
		String sx = "x";
		ScriptManager.relay("HigraphManager", "createString", sx);
		ScriptManager.relay("HigraphManager", "setStringBaseColor", sx, PDV.BLACK);
		ScriptManager.relay("HigraphManager", "placeString", sx, XX+100, XY);
		ScriptManager.relay("HigraphManager", "createString", "y");
		ScriptManager.relay("HigraphManager", "placeString", "y", YX-10, YY);*/
		int[] p = fastMultiplication(x, y, n);
		/*#I
		makeNodes(p);
		placeArray(p, XP, YP);*/
	}

	public static void main(String[] str) {
		FastIntegerProduct e = new FastIntegerProduct();
		e.run();
	}
}
