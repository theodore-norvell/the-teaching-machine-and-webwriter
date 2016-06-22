package tm.portableDisplaysGWT;

public interface StateInterface {

	public String getExpression();
	public CodeLine getSelectedCodeLine(boolean allowGaps, int i);
	public int getNumSelectedCodeLines(boolean allowGaps);
}
