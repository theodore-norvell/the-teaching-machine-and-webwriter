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

package tm.interfaces;

import java.util.Enumeration ;

import tm.backtrack.BTTimeManager;
import tm.displayEngine.OldConsoleDisplay;
import tm.utilities.TMFile;

public interface EvaluatorInterface extends StateInterface {
	
// Mutators
	
	/** Add more text to the input.
	 *  Lines are normally separated by newlines.
	 *  A 0 (nul) character anywhere in the string indicates the end of file.
	 */
	public void addInputString( String text ) ;
	
// Display callbacks
    /** The number of symbol table entries */
    public int getNumSTEntries() ;
    
    /** Get one symbol table entry
     * @precond 0 <= index && index < getNumSTEntries()
     */
    public STEntry getSymTabEntry(int index);

    /** The number of entries in the current stack frame */
    public int varsInCurrentFrame() ;

    /** The number of entries in the global frame.
     * The global frame includes such things as static variables.*/
    public int varsInGlobalFrame() ;

    /** What line is currently in focus?
     * <p>
     * When compilation stops because of a compiler error, the
     * offending line should be in focus.
     * <p>
     * When execution pauses, the line in focus should contain the
     * next operation to execute. */
  //TODO move into StateInterface by xiuhuali
//    public SourceCoords getCodeFocus() ;
    
    /** Get a list of all source files. */
    public Enumeration<TMFile> getSourceFiles() ;
    
    /** Set the current selection expression.
     * <p>The selection determines which code lines are visible and
     * which parts of selected lines are visible.
     */
    public void setSelection(SelectionInterface selection) ;
    
    /** A string representation of the line number of the code line currently in focus. */
    public String getPCLocation();
    
    /**
     * Gets a constant indicating the language being used by the code currently running
     * in the TM. Current valid language constants are CPP_LANG,
     * JAVA_LANG and UNKNOWN_LANG.
     * 
     * @return a language constant defined in TMBigApplet
     */
    public int getLanguage();

    /**
     * In autoStep mode, the TM is stepping itself rather than waiting on GUI events.
     * 
     * @return true if Teaching Machine is auto stepping
     */
    public boolean isInAuto();

    /**
     * The autostep rate indicates the speed of autostepping.
     * The rate is an integer between 0 and 100 inclusive.
     * 0 is slow, 100 is fast.
     * 
     * @return the current autostep rate
     */
    public int getAutoStepRate();
    

    /** Get the current time manager. 
     * 
     * @return the current time manager
     */
    BTTimeManager getTimeManager(); 

}