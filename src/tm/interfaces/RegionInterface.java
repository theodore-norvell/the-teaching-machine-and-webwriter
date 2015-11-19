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

/* (C) Theodore Norvell 1999 */
package tm.interfaces;
public interface RegionInterface extends tm.interfaces.Datum
{
    /** The frame boundary is the number of datums in the
     * region that should be displayed in a way that
     * shows they are not currently accessible. In
     * particular for the stack region this is the
     * number of datums the region that are not
     * in the current top frame.
     */
    public int getFrameBoundary();
}