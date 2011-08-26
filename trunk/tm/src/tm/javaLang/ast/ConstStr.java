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

import java.util.Vector;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.SelectorAlways;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.javaLang.datum.ObjectDatum;
import tm.javaLang.datum.PointerDatum;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: ConstStr

Overview:
This class represents string constants.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class ConstStr extends DefaultExpressionNode {
    // Use a BTVar to hold on to the string object datum in the heap.
    // This way, when the node is executed a second, third and so on time,
    // we don't have to keep adding new strings to the heap.
    private BTVar<ObjectDatum> stringDatumHolder ;
    private String value ;

    public ConstStr (TyClass stringClass,
                     String image,
                     String value,
                     VMState vms)  {
        super("ConstStr");
        this.value = value ;
        BTTimeManager tm = vms.getTimeManager() ;
        this.stringDatumHolder = new BTVar<ObjectDatum>(tm) ;
        set_syntax(new String[] { image });
        set_selector(SelectorAlways.construct());
        set_stepper(new StepperConstStr());
        setUninteresting(true);

        // Construct the value (stringDatum) and type
            TyPointer tp = new TyPointer();
            tp.addToEnd(stringClass);
            set_type(tp);
    }

    ObjectDatum getDatum() { return stringDatumHolder.get() ; }
    
    void putDatum( ObjectDatum theStringDatum ) { stringDatumHolder.set( theStringDatum ); }
    
    String getValue() { return value ; }
}

class StepperConstStr extends StepperBasic {
    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        // The first time this node is evaluated the datum is built.
            ConstStr nd1 = (ConstStr) nd;
            ObjectDatum stringDatum = nd1.getDatum();
            if( stringDatum == null ) {
                JavaLangASTUtilities util
                    = (JavaLangASTUtilities) vms.getProperty("ASTUtilities");
                stringDatum = util.makeStringObject( nd1.getValue(), vms ) ;
                nd1.putDatum( stringDatum ) ;
            }

        JavaLangASTUtilities util
         = (JavaLangASTUtilities) vms.getProperty("ASTUtilities");
        TypeNode tp = nd1.get_type();
        PointerDatum ptr = (PointerDatum) util.scratchDatum(tp, vms);
        ptr.putValue(stringDatum);
        return ptr;
    }
}