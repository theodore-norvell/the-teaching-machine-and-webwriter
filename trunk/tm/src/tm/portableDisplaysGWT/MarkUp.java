package tm.portableDisplaysGWT;

public class MarkUp {

	public int column;

	public int command;

	public MarkUp(int col, short comm) {
		column = col;
		command = comm;
	}

	public static final short NORMAL = 0, KEYWORD = 1, COMMENT = 2, PREPROCESSOR = 3, CONSTANT = 4, CHANGE_TAG_SET = 5;

	public String toString() {
		return "(" + column + ", " + command + ")";
	}
}