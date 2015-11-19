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

import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.cpp.analysis.ClassSH;
import tm.cpp.analysis.RankedFunction;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/**
 * Placeholder expression containing a function id.
 * <p>Used when building a function call, upon encountering the 
 * function id but prior to dealing with the arguments. 
 * <p><em>Note: this is not a functional expression, and should not
 * be part of the runtime representation of a program</em>
 */
public class ExpFunctionName extends ExpressionNode {

    private static final String ILLEGAL_OP = 
        "TyFunctionName is not a real type. This operation not permitted.";

    private RankedFunction match;

    private ExpressionNode classObj;
    private ClassSH classScope;
    private String memAccessOp;
    private boolean recipientIsImplicitlyThis ;

    public static TyFunctionName tyFunctionName = new TyFunctionName ();
    
    private ScopedName fName;

    
    public ExpFunctionName (ScopedName name) { 
        super (null);
        fName = name;
        set_type (tyFunctionName);
    }

    public ScopedName getName () { return fName; }

    public void step (VMState a) { Assert.apology (ILLEGAL_OP); }
    public void select (VMState a) { Assert.apology (ILLEGAL_OP); }
    public String toString (VMState a) { 
        Assert.apology (ILLEGAL_OP);
        return null;
    }

    public void setMatch (RankedFunction fd) {
        Debug d = Debug.getInstance() ;
        d.msg(Debug.COMPILE,  "Match set to " + fd.toString() );
        match = fd; }
    public RankedFunction getMatch () { return match; }

    /** 
     * Set the expression referring to the class or class object used to 
     * access the function in a member access expression, if being 
     * accessed in such a context
     * @param co the AST representation of the relevant class or object
     * on the lhs of the access expression.
     */
    public void setClassObject (ExpressionNode co) { classObj = co; }

    /** 
     * Get the expression referring to the class or class object used to 
     * access the function in a member access expression, if being 
     * accessed in such a context
     * @return the AST representation of the relevant class or object
     * on the lhs of the access expression.
     */
    public ExpressionNode getClassObject () { return classObj; }

    /** 
     * Set the scope of the class used to 
     * access the function in a member access expression, if being 
     * accessed in such a context
     * @param cs the class scope
     */
    public void setClassScope (ClassSH co) { classScope = co; }

    /** 
     * Get the scope of the class used to 
     * access the function in a member access expression, if being 
     * accessed in such a context
     * @return the class scope
     */
    public ClassSH getClassScope () { return classScope; }

    public void setMemberAccessOperator (String image) {
        memAccessOp = image;
    }

    public String getMemberAccessOperator () { return memAccessOp; }
    
    public void setRecipientIsImplicitlyThis( boolean v ) {
        recipientIsImplicitlyThis = v ; }
    
    public boolean getRecipientImplicitlyThis() {
        return recipientIsImplicitlyThis ; }

    public static class TyFunctionName extends TypeNode {

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
            return (t instanceof TyFunctionName); 
        }

        public int getNumBytes () { 
            Assert.apology (ILLEGAL_OP);
            return -1;
        }

    public String typeId() { return "" ; }
    
    public String getTypeString () { return "Function Name - not a type"; }

    }
}

