package tm.portableDisplays;

public interface StateInterface {

	public String getExpression();
	public CodeLine getSelectedCodeLine(TMFileI tmFile, boolean allowGaps, int i);
	public int getNumSelectedCodeLines(TMFileI tmFile, boolean allowGaps);
	public void setSelection(SelectionInterface selection) ;
	public SelectionInterface getSelection() ;
}
