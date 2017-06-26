package tm.interfaces;

public interface StateInterface {
    
    /** Marks following characters as representing values */
    public static final char MARKER1 = '\uffff';    
    /** Marks following characters as representing the selected node. */
    public static final char MARKER3 = '\ufffd';    
    /** Marks following characters as representing a lvalue. */
    public static final char MARKER4 = '\ufffc';
    /** Ends a previous markup. */
    public static final char ENDMARKER = '\ufffb';

    /** A string representation of the current state of 
     * the expression that is currently being
     * evaluated.
     * <P>
     * The string returned is marked up using constants from {@link StateInterface}.
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
    
	public SourceCoordsI getCodeFocus() ;
	
    /** Get the current selection expression.
     * <p>The selection determines which code lines are visible and
     * which parts of selected lines are visible.
     * @return The current selection expression.
     */
	public SelectionInterface getSelection() ;
	
    /** Returns the static region.
     */
    public RegionInterface getStaticRegion() ;
    
    /** Returns the stack region.
     */
    public RegionInterface getStackRegion() ;
    
    /** Returns the heap region.
     */
    public RegionInterface getHeapRegion() ;
    
    /** Returns the scratch region.
     * <p>
     * The scratch region is used mostly for compiler temporary variables (temps).
     */
    public RegionInterface getScratchRegion() ;

    /** The number of lines that are currently on the console. */
    public int getNumConsoleLines() ;
    
    /** One line of the console
     * <p>
     * The string returned is marked up using constants from {@link OldConsoleDisplay}.
     * */
    public String getConsoleLine(int l);
    
    /** Indicate console characters are normal (i.e. output) */
    public static final char NORMAL_MARK = '\uffff';
    
    /** Indicate console characters are input */
    public static final char INPUT_MARK = '\ufffe';
    
}
