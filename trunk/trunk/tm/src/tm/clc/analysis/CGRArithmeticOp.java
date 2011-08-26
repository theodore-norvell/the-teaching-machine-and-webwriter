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

import tm.clc.ast.Arithmetic;
import tm.utilities.Assert;

/**
 * A CodeGenRule base class for generation of multiplicative, integral, shift, 
 * bitwise, and logical expressions involving one or two operands of 
 * arithmetic or pointer type.
 */
public abstract class CGRArithmeticOp extends CodeGenRule {
    private static final String INVALID_OPERATOR = 
        "Operator {0} cannot be used by {1}";

    private String [] opMapping;

    /**
     * Creates a new <code>CGRArithmeticOp</code> instance.
     *
     * @param applicableOps a list of the operators applicable to a given
     * subclass.
     */
    public CGRArithmeticOp (String [] applicableOps ) {
        opMapping = applicableOps;
    }

    /**
     * Provides an integral id representing the operation, if applicable to
     * the subclass. The id will match the corresponding id defined in 
     * <code>Clc.Ast.Arithmetic</code>. An apology will be generated if the
     * id was not found.
     * @return the index of the operator
     * @see tm.clc.ast.Arithmetic
     */
    protected int getOpId (String op) {
        for (int i=0; i < opMapping.length; i++) 
            if (op.equals (opMapping[i])) 
                return i % (Arithmetic.BOOLEAN_OR + 1);
        Assert.apology (INVALID_OPERATOR, 
                        new Object [] {op, this.getClass().getName ()});
        return -1;
    }
    
    protected boolean hasBoolResult( int operator_id ) {
        switch( operator_id ) {
        case Arithmetic.EQUAL :
        case Arithmetic.NOT_EQUAL :
        case Arithmetic.LESS :
        case Arithmetic.LESS_OR_EQUAL :
        case Arithmetic.GREATER :
        case Arithmetic.GREATER_OR_EQUAL :
            return true ; 
        default:
            return false ;
        }
    }
    /** gives a printable operator image - suitable for display. This 
     * will strip assignment from a shorthand assignement operator
     */
    protected String getOpImage (int opId) {
        return opMapping [opId % (Arithmetic.BOOLEAN_OR + 1)];
    }

}