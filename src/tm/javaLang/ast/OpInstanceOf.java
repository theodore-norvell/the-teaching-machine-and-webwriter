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

package tm.javaLang.ast;

import tm.clc.analysis.ScopedName;
import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.SelectorLeftToRight;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.javaLang.datum.BooleanDatum;
import tm.javaLang.datum.PointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class OpInstanceOf extends DefaultExpressionNode {
    TyJava comparisonType ;

    public OpInstanceOf( ExpressionNode operand,
                          TyJava theType,
                          ScopedName nameOfType )
    {
        super("OpInstanceOf", operand) ;
        this.comparisonType = theType ;

        set_type( TyBoolean.get() ) ;
        String [] syntax = new String[ ] { "", "instanceof "+nameOfType.getName() } ;
        set_syntax( syntax ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpInstanceOf.construct() ) ;
    }
}

class StepperOpInstanceOf extends StepperBasic {

    private static StepperOpInstanceOf singleton ;

    static StepperOpInstanceOf construct() {
        if( singleton == null ) singleton = new StepperOpInstanceOf() ;
        return singleton ; }

    public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
        // Get value of operand
            Object xd = vms.top().at( nd.child_exp(0) ) ;
            Assert.check( xd instanceof PointerDatum ) ;
            PointerDatum ptr = (PointerDatum) xd ;

        boolean value ;
        // Check for null
            if( ptr.isNull() ) {
                value = false ; }
        else {
            // Find the item
                int address = ptr.getValue() ;
                TypeNode type = (TypeNode) ptr.getPointeeType() ;
                AbstractDatum item = (AbstractDatum) vms.getStore().chasePointer( address, type ) ;
                if( item==null ) {
                    Assert.check("Pointer dereference fails." ) ; }
                TyJava datumType = (TyJava) item.getType() ;
                TyJava comparisonType = ((OpInstanceOf)nd).comparisonType ;
                value = JavaLangASTUtilities.assignableReferenceType( datumType, comparisonType ) ; }

        // Construct a scratch bool
            Clc_ASTUtilities util
                = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            BooleanDatum result
                = (BooleanDatum) util.scratchDatum(nd.get_type(), vms) ;
            result.putValue( value?1:0 );

        // Done
            return result ; }
}