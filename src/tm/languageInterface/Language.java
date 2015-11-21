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

package tm.languageInterface;


import tm.backtrack.BTTimeManager;
import tm.interfaces.ViewableST;
import tm.utilities.TMFile;
import tm.virtualMachine.VMState;


/** An interface for objects representing languages.

    <P> Language dependent resources are held in such objects
*/

public interface Language {

    public ViewableST makeSymTab( BTTimeManager tm ) ;

    public void compile( TMFile tmFile, VMState vms) ;
    
    /** Add a program argument.  This adds a new argument to main.
     * <p>It must be called before initializeTheState. */
    public void addProgramArgument( String argument ) ;

    public void initializeTheState( VMState vms ) ;
    
    public void callSubroutine( VMState vms, Object key, Object[] args ) ;

    public String getName() ;

    public int getLanguage();
}