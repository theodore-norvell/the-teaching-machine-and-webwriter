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

package tm.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * Provides two level debugging control. Debugging is either globally active
 * or inactive. Default is active, but a sigle call to inactive() will
 * turn debugging off. This ensures debugging can be turned off in a shipped version.
 * 
 * @author mpbl and theo and derek
 */
public class Debug {
    
    private static Debug instance = new Debug (); // singleton instance
    
    /** A category that is on if any category is on. */
    public static final int ALWAYS =  1 ;
    public static final int COMPILE = 2 ;
    public static final int EXECUTE = 4 ;
    public static final int DISPLAY = 8 ;
    public static final int EDITOR = 16 ;
    public static final int CURRENT = 32 ;
    public static final int BACKTRACK = 64 ;
    
    // Invariant. If any category is active, then ALWAYS is active:
    //            activeCategories == 0 || (activeCategories & ALWAYS)!=0
    private int activeCategories  ;

   /**
     * Private constructor
     */
    private Debug () {
        turnOnAll() ; // Default is that debug is on.
    }
    
    /**
     * Use getInstance to use 'singleton' instance of class
     */
    public static Debug getInstance () {
        return instance;
    }

    /**
     * Is debugging active at all?
     */
    public boolean isOn () { return activeCategories != 0; }
    
    /** Is debugging active for this category? */
    public boolean isOn( int cat ) { return (cat & activeCategories) != 0 ; }

    /**
     * Turn debugging off.
     */
    public void deactivate () { activeCategories = 0 ; }
    
    /** Turn off all categories apart from ALWAYS */
    public void turnOffAll() {
        activeCategories = ALWAYS ;
    }
    
    /** Turn on all categories. */
    public void turnOnAll() {
        activeCategories = -1 ;
    }
    
    /** Turn on a specific category */
    public void turnOn(int cat) {
        activeCategories |= (cat|ALWAYS) ;
    }
    
    /** Turn on a specific category identified by a string */
    public void turnOn(String cat) {
        if(     cat.equalsIgnoreCase("COMPILE")) turnOn( COMPILE ) ;
        else if(cat.equalsIgnoreCase("EXECUTE")) turnOn(EXECUTE) ;
        else if(cat.equalsIgnoreCase("DISPLAY")) turnOn(DISPLAY) ;
        else if(cat.equalsIgnoreCase("EDITOR")) turnOn(EDITOR) ;
        else if(cat.equalsIgnoreCase("ALWAYS")) turnOn(ALWAYS) ;
        else if(cat.equalsIgnoreCase("CURRENT")) turnOn(CURRENT) ;
        else if(cat.equalsIgnoreCase("BACKTRACK")) turnOn(BACKTRACK) ;
        else msg(ALWAYS, "Unknown debug flag: '"+ cat + "'") ;
    }
    
    /**
     * Print debug statement with an newline after.
     * @param cat The category of message. The message is only printed if the category is on.
     * @param message The message to print.
     */
    public void msg (int cat, String message) {
    	if( (cat&activeCategories) != 0) System.out.println(message);
    }
    
    /** Print debug message with no newline at the end.
     *
     * @param cat The category of message. The message is only printed if the category is on.
     * @param message The message to print.
     */
    public void msgNoNL(int cat, String message) {
        if( (cat&activeCategories) != 0) System.out.print(message);
    }


    /** Print a stack trace if category is active 
     * @param cat The category of message. The message is only printed if the category is on.
     * @param e exception to print trace of.
     */
    public void msg(int cat, Throwable e ) {
        if( (cat&activeCategories) != 0) {
            StringWriter sw = new StringWriter() ;
            PrintWriter pw = new PrintWriter( sw ) ;
            e.printStackTrace(pw);
            pw.close();
            msg( cat, sw.toString() ) ;
        }
    }
}