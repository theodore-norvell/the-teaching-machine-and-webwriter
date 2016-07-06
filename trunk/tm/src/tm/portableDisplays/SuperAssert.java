package tm.portableDisplays;

public class SuperAssert {
	public static void check(boolean proposition) {
		if (!proposition)
			check("");
	}

	public static void check(String message) {
//		throw new Exception("Assertion failed " + message);
	}
}
