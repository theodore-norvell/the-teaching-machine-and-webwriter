//     Copyright 1998--2011 Michael Bruce-Lockhart and Theodore S. Norvell
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

/*
 * Created on Aug 20, 2011 by Theodore S. Norvell. 
 */
package tm.utilities;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class ConcurUtilities {
    
    static public void doOnSwingThread( Runnable thunk ) throws InvocationTargetException {
        if( SwingUtilities.isEventDispatchThread() ) {
            thunk.run();
        } else {
            try {
                SwingUtilities.invokeAndWait( thunk ) ;
            } catch (InterruptedException e) {
                // Shouldn't happen.  (Famous last words.)
                e.printStackTrace(); } }
    }
    
    private static class Var<V> {
        V value ;
    }
    
    static public <V> V doOnSwingThread( final ResultThunk<V> thunk )  throws InvocationTargetException {
        if( SwingUtilities.isEventDispatchThread() ) {
            return thunk.run();
        } else {
            final Var<V> var = new Var<V>() ;
            try {
                SwingUtilities.invokeAndWait( new Runnable() {
                    @Override public void run() {
                        var.value = thunk.run() ;
                    } }) ;
                return var.value ;
            } catch (InterruptedException e) {
                // Shouldn't happen.  (Famous last words.)
                e.printStackTrace();
                return null ; }  }
    }
}
