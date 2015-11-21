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

package tm.clc.analysis;

import tm.utilities.Assert;

/**
 * <code>LFConst</code>
 * A constant <code>LFlag</code>
 */	
public class LFConst extends LFlags {
		
	/**
	 * Creates a new <code>LFConst</code> instance with the flags set
	 * according to <code>rawVal</code>.
	 * @param rawVal an <code>int</code> value
	 */
	public LFConst (int rawVal) { super (rawVal); }
	
	/**
	 * Message provided when an attempt is made to modify an <code>LFConst</code>
	 */
	public static final String READONLY = "cannot modify constant LFlag";
	
	/**
	 * Overrides <code>LFlags.setRawVal</code> to complain.
	 *
	 * @param rawVal an <code>int</code> value
	 */
	public void classify (int rawVal) { Assert.apology (READONLY); }
	
	/**
	 * Overrides <code>LFlags.set</code> to complain.
	 *
	 * @param rv an <code>int</code> value
	 */
	public void set (int rv) { Assert.apology (READONLY); }
	
	/**
	 * Overrides <code>LFlags.unset</code> to complain.
	 *
	 * @param rv an <code>int</code> value
	 */
	public void unset (int rv) { Assert.apology (READONLY); }
	
	/**
	 * Returns an editable clone of this object
	 * @return an editable <code>LFlags</code> instance with the same flags 
	 * as this <code>LFConst</code>.
	 */
	public LFlags writable () { return new LFlags (val); }
	
}
