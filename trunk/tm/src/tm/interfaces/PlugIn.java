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
 * PlugIns are components which can be plugged in to the Teaching Machine without being
 * recompiled. To accompish this piece of magic every PlugIn has to have its own {@link
 * tm.plugins.PlugInFactory Plugin Factory}. Thus PlugIns and PlugInFactories are developed in
 * pairs.
 * <p>
 * The PlugInFactory for this PlugIn should have a createPlugIn method which returns an object
 * that implements this (currently empty) interface.
 * 
 * @author mpbl
 * @see tm.plugins.PlugInFactory
 */ 
public interface PlugIn {

}
