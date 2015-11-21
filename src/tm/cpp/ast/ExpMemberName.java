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

package tm.cpp.ast;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/**
 * Placeholder expression containing a class member id.
 * <p>Used when building a member access expression, upon encountering the 
 * member id but prior to resolving the class/object (lhs). 
 * <p><em>Note: this is not a functional expression, and should not
 * be part of the runtime representation of a program</em>
 */
public class ExpMemberName extends ExpressionNode {

	private static final String ILLEGAL_OP = 
		"TyMemberName is not a real type. This operation not permitted.";

	private Declaration match;

	public static TyMemberName tyMemberName = new TyMemberName ();
	
	private ScopedName mName;

    
    public ExpMemberName (ScopedName name) { 
		super (null);
		mName = name;
		set_type (tyMemberName);
	}

	public ScopedName getName () { return mName; }

	public void step (VMState a) { Assert.apology (ILLEGAL_OP); }
	public void select (VMState a) { Assert.apology (ILLEGAL_OP); }
	public String toString (VMState a) { 
		Assert.apology (ILLEGAL_OP);
		return null;
	}

	public void setMatch (Declaration d) { match = d; }
	public Declaration getMatch () { return match; }

	public static class TyMemberName extends TypeNode {

		public AbstractDatum makeMemberDatum (VMState a, int b, 
											  AbstractDatum c, String d) {
			Assert.apology (ILLEGAL_OP);
			return null;
		}

		public AbstractDatum makeDatum (VMState a, MemRegion b, String c) { 
			Assert.apology (ILLEGAL_OP);
			return null;
		} 

		public boolean equal_types (TypeInterface t) { 
			return (t instanceof TyMemberName); 
		}

		public int getNumBytes () { 
			Assert.apology (ILLEGAL_OP);
			return -1;
		}

        public String typeId() { return "" ; } 
        
		public String getTypeString () { return "Member Name - not a type"; }

	}
}

