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

/*
 * Created on Apr 1, 2005.  No kidding.
 *
 */
package tm.interfaces;

/**
 * @author theo
 *
 */
public interface StatusProducer {
    

    /** Return a status code indicating the current status of the TM.
     * @see tm.interfaces.TMStatusCode */
    public int getStatusCode() ;
    
    /** Return a user friendly message explaining why that status is what it is.
     * In the case of a compiler error or an execution error, it should be the
     * text of the message.
     * @see tm.interfaces.TMStatusCode */
    public String getStatusMessage() ;

}
