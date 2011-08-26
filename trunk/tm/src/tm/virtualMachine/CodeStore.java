//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.virtualMachine;

import java.util.* ;

import tm.interfaces.CodeLine;
import tm.interfaces.ExternalCommandInterface;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMFile;


/**
 * <p>Title: </p>
 * <p>Description: Storage for Colourized source files.</p>
 * <p>Company: </p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class CodeStore {

    // Table to map files to Vectors. Each vector element is a line of code
    private Hashtable<TMFile, Vector<VMCodeLine> > fileToVector ;
    { fileToVector = new Hashtable<TMFile, Vector<VMCodeLine> >() ;
      startNewFile( SourceCoords.UNKNOWN.getFile() ) ;
    }
    private TMFile currentFile ;
    private Vector<VMCodeLine> current ; // Vector of CodeLine
    private SelectionInterface currentSelection = SelectionParser.parse( ExternalCommandInterface.DEFAULT_SELECTION ) ;

    /** Invariant: selectedLines is the subsequence of lines in Vector current that have some
     * visible characters in the current selection. If allowGaps is true, any gaps in the sequence are
     * represented by null elements. So if the lines of the file are {1,2,...,10}
     * but the selected lines are only {3,4,6,7} then the selected lines
     * contains (null, 3, 4, null, 6, 7, null). However if allowGaps is false, then
     * there will be no nulls. In the example, the selected lines would be (3,4,6,7).
     */
    private Vector<VMCodeLine> selectedLines; // Vector of CodeLine
    private boolean allowGaps = true ;


    /** Create a new file.
     *  If a previous file has the same name, it is overwritten.
     *  The new file becomes the current file. */
    public void startNewFile( TMFile file ) {
        current = new Vector<VMCodeLine>() ;
        selectedLines = new Vector<VMCodeLine>() ;
        currentFile = file ;
        fileToVector.put( file, current ) ; }

    /** Add a new line of code to the current file.
     *
     *  <P>Precond: The file must have been previously created.
     *  <p>Precond: If this is the first line then it must have a line number of 1.
     *              Otherwise it must be the next line number after the previous line.
     */
    public void addCodeLine( VMCodeLine codeLine ) {
        /*dgb*/ Debug.getInstance().msg(Debug.COMPILE, " Adding "+codeLine ) ; /**/
        setCurrentFile( codeLine.getCoords().getFile() ) ;
        Assert.check( current.size()+1==codeLine.getCoords().getLineNumber() ) ;
        current.addElement( codeLine );
        if( codeLine.isSelected( currentSelection ) ) {
            /*dbg* Debug.getInstance().msg(Debug.COMPILE, "selected");/**/
            selectedLines.addElement( codeLine ) ; }
        else adjustGaps( current.size() ) ;
    }

    /** The code lines associated with coordinates. 
     * If the coords are unknown or do not correspond to a legitimate line, then null is returned.
     * @param coords The coords must not be SourceCoords.UNKNOWN.
     * @return Returns the line.
     */
    public VMCodeLine getCodeLine( SourceCoords coords ) {
        if( coords.equals( SourceCoords.UNKNOWN ) ) return null ;
        setCurrentFile( coords.getFile() ) ;
        int lineNum = coords.getLineNumber() ;
        if( lineNum < 0 || lineNum >= current.size() ) return null ;
        return current.elementAt( lineNum-1 ) ;
    }

    private void adjustGaps(int n) {
        // Preconditions.
        //   1 _< n _< current.size()
        //   Considering only the first n-1 elements of current (be they in
        //   current or in selectedLines:
        //     The invariant for selectedLines holds
        //   Changes: selectedLines
        //  Postcondition:
        //   Considering only the first n elements of current (be they in
        //   current or in selectedLines):
        //     The invariant for selectedLines holds
        if( ! allowGaps ) return ;
        VMCodeLine codeLine = current.elementAt(n-1) ;
        if( ! codeLine.isSelected( currentSelection ) ) {
            if( n==1 ) {
                // The first line of the file is not selected, so there is a gap
                // at the start.
                selectedLines.addElement( null ) ; }
            else if( current.elementAt( n-2 ).isSelected( currentSelection ) ) {
                // The previous line was visible, but this is not, so this is the start of a gap
                selectedLines.addElement( null ) ; } }
    }

    private void rebuildSelectedLines() {
        selectedLines = new Vector<VMCodeLine>() ;
        for( int i=0, sz=current.size() ; i < sz ; ++i ) {
            VMCodeLine codeLine = current.elementAt(i) ;
            if( codeLine.isSelected(currentSelection) ) selectedLines.addElement( codeLine );
            else adjustGaps(i+1);}
    }
    
    public void setSelection(SelectionInterface selection) {
        if( selection != currentSelection ) {
            currentSelection = selection ;
            rebuildSelectedLines() ; }
    }
    
    private void setAllowGaps( boolean newValue ) {
        if( newValue != allowGaps  ) {
            allowGaps = newValue ;
            rebuildSelectedLines() ; }
    }

    public SelectionInterface getSelection() {
        return currentSelection ;
    }

    public int getNumSelectedCodeLines(TMFile tmFile, boolean allowGaps) {
        setCurrentFile( tmFile ) ;
        setAllowGaps( allowGaps ) ;
        return selectedLines.size() ;
    }

    public CodeLine getSelectedCodeLine(TMFile tmFile, boolean allowGaps, int index) {
        setCurrentFile( tmFile ) ;
        setAllowGaps( allowGaps ) ;
        return selectedLines.elementAt(index) ;
    }

    public SourceCoords getCoordsOfLastLine( TMFile tmFile ) {
        setCurrentFile( tmFile ) ;
        if( current.size()==0 ) return SourceCoords.UNKNOWN ;
        VMCodeLine codeLine = current.elementAt(current.size() - 1) ;
        return codeLine.getCoords();
    }

    private void setCurrentFile( TMFile file ) {
        if( file ==  currentFile ) return ;
        current = fileToVector.get( file ) ;
        Assert.check( current != null ) ;
        currentFile = file ;
        rebuildSelectedLines() ;
    }

    public Enumeration<TMFile> getSourceFiles() {
        return fileToVector.keys() ;
    }
}