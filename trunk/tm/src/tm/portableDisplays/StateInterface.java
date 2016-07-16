package tm.portableDisplays;

public interface StateInterface {

	public String getExpression();
	public CodeLine getSelectedCodeLine(SuperTMFile tmFile, boolean allowGaps, int i);
	public int getNumSelectedCodeLines(SuperTMFile tmFile, boolean allowGaps);
	public SuperSourceCoords getCodeFocus() ;
	public void setSelection(SelectionInterface selection) ;
	public SelectionInterface getSelection() ;
}
