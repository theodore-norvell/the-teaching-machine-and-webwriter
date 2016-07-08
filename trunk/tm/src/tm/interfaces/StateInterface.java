package tm.interfaces;


public interface StateInterface {

    /** A string representation of the current stat of 
     * the expression that is currently being
     * evaluated.
     * <P>
     * The string returned is marked up using constants from {@link ExpressionDisplay}.
     * @return A marked up string representing a partially evaluated expression.
     */
	public String getExpression();
	
    /** Get a selected line from a file.
     *  @precond 0 &lt;= index &amp;&amp; index &lt; getNumSelectedCodeLines( tmFile )
     *  when */
	public CodeLineI getSelectedCodeLine(TMFileI tmFile, boolean allowGaps, int i);
	
	/** The number of code lines in a file that are visible in the current selection.
     * This may include gaps, depending on whether gaps are allowed .
     */
	public int getNumSelectedCodeLines(TMFileI tmFile, boolean allowGaps);
    
    /** Get the current selection expression.
     * <p>The selection determines which code lines are visible and
     * which parts of selected lines are visible.
     * @return The current selection expression.
     */
	public SelectionInterface getSelection() ;
}
