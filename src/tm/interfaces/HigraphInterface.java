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


/**
 * The HigraphInterface is actually for the HigraphManager which mediates
 * all commands to any Higraph displays 
 *
 * @author mpbl
 */
public interface HigraphInterface extends PlugIn, Configurable{
    //TODO I don't think this interface is ever used. Why to we have it? TSN 2011 May 21.
    // TODO If we do use it I suggest renaming as HigraphManagerInterface
    public void refresh();
}
