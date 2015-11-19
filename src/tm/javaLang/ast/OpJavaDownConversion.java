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

import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.SelectorLeftToRight;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.Datum;
import tm.javaLang.datum.ObjectDatum;
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

public class OpJavaDownConversion extends DefaultExpressionNode {

    public OpJavaDownConversion( TypeNode t,
                   String operator_image,
                   ExpressionNode operand ) {
        super("Down cast", operand ) ;
        Assert.check( t instanceof TyPointer );
        set_type( t ) ;
        set_syntax( new String[]{operator_image, ""} ) ;
        set_selector( SelectorLeftToRight.construct() ) ;
        set_stepper( StepperOpJavaDownConversion.construct() ) ;
    }
}

class StepperOpJavaDownConversion extends StepperBasic {

    private static StepperOpJavaDownConversion instance ;
    
    static StepperOpJavaDownConversion construct() {
        if(instance == null) instance = new StepperOpJavaDownConversion();
        return instance;
    }
    
    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        TyJava toType = (TyJava) nd.get_type() ;
        AbstractDatum d = (AbstractDatum) vms.top().at( nd.child_exp(0) ) ;
        Assert.check( d instanceof PointerDatum );
        TyJava fromType = (TyJava) d.getType() ;
        JavaLangASTUtilities util
           = (JavaLangASTUtilities) vms.getProperty("ASTUtilities");
        if( util.assignableReferenceType( fromType, toType )  ) {
            return d ; }
        else {
            Assert.unsupported( "CastClassException") ;
            return /* pro forma */ null ; }
    }
}