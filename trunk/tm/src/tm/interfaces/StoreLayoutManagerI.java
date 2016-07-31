package tm.interfaces;

public interface StoreLayoutManagerI {
	public static final int ADDRESS_W = 55;  // minimum widths for display boxes
	public static final int VALUE_W = 150;  // value boxes can be less since insets taken OUT of this width
	public static final int NAME_W = 120;  // memory display boxes
	public void layoutDisplay();
}
