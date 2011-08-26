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

package tm.clc.ast;

import tm.clc.datum.AbstractDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** Combine an operator and an assignment.
  * This is one of the only cases in the expression nodes where
  * we go beyond a tree. The left_operand and right_operands will
  * typically point into the other expression argument, which is a
  * representation of the calculation to be done. Consider the C++
  * expression
  *      i += 2.8
  * where "i" is an int variable. This can be compiled to the graph
  *      OpOpAssign( "+=", "=", p, q, r)
  * where
  *      p = OpId( i )
  * and
  *      q = ConstFloat( 2.8 )
  *      r = ConvertFloatToInt(OpFloat(ADD,ConvertIntToFloat(ExpFetch(p)),q)
  * Note that p recurs in q. The type should be as in OpAssign.
  */
public class OpOpAssign extends OpAssign
{
    String operator_image ;
    ExpressionNode right_operand ;

    private NodeList childrenForDump;

    public OpOpAssign( TypeNode t,
        String operator_image,
        String alternate_assignment_operator_image,
        ExpressionNode left_operand,
        ExpressionNode right_operand,
        ExpressionNode expression )
    {
        super("OpOpAssign", t, alternate_assignment_operator_image,
              left_operand, expression) ;
        this.operator_image = operator_image ;
        this.right_operand = right_operand ;
        childrenForDump = new NodeList() ;
        childrenForDump.addLastChild( left_operand );
        childrenForDump.addLastChild( right_operand );
        childrenForDump.addLastChild( expression );
    }

    /** Convert to a string starting with a given operand */
    public String toString(VMState vms) {
        if( vms.top().at( child_exp(0) ) == null ) {
            // Target is not yet evaluated.
            // Special case. Use the combined operator.
            String res  ;
            res = child_exp(0).toString( vms ) ;
            res += " "+operator_image+" " ;
            res += right_operand.toString( vms ) ;
            return res ; }
        else {
            return super.toString(vms) ; } }


    public String formatNodeData() {
       return super.formatNodeData() + "("+operator_image+")"; }

    protected NodeList getChildrenForDump() {
        return childrenForDump ;
    }
}