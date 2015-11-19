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

import java.util.Vector;

import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.SelectorAlways;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.cpp.datum.ArrayDatum;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** ConstStr -- represents C++ string constants .*/
public class ConstStr extends DefaultExpressionNode
{

	ArrayDatum value ;
	Vector imageVector ;

	public ConstStr (ArrayDatum value, Vector imageVector, String str)  {
        super("ConstStr") ;
        Assert.check( value.getNumChildren() == imageVector.size() ) ;
        set_type( (TypeNode) value.getType() ) ;
        set_syntax( new String[]{ str } ) ;
        set_selector( SelectorAlways.construct() ) ;
        set_stepper( new StepperConstStr( value ) ) ;
        setUninteresting( true ) ;
        this.value = value ;
        this.imageVector = imageVector ; }
    
    public int length() {return value.getNumChildren() ; }
    
    public ArrayDatum getValue() { return value ; }
    
    public String getImageOfCharacter(int i) {
        Assert.check( 0 <= i && i < imageVector.size() ) ;
        return (String) imageVector.elementAt(i) ; }
}
        
class StepperConstStr extends StepperBasic {

        private ArrayDatum value ;
        
        StepperConstStr( ArrayDatum value ) { this.value = value ; }
        
        public AbstractDatum inner_step( ExpressionNode nd, VMState vms ) {
		    
		     return value ; }
}
