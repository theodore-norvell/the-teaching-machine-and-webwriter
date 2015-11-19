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

import tm.virtualMachine.VMState;

public class SelectorLeftToRight implements Selector
{
    private static SelectorLeftToRight singleton ;
    
    public static SelectorLeftToRight construct() {
        if( singleton == null ) singleton = new SelectorLeftToRight() ;
        return singleton ; }
    
    public void select(ExpressionNode nd, VMState vms) {
        // Try to find an unmapped child.
            int f = -1 ;
            for(int i = 0 ; i < nd.childCount() ; ++ i ) {
                if( vms.top().at(nd.child_exp(i)) == null ) {
                    f = i ; break ; } }
                    
        if( f == -1 ) {
            // None found, all children ar mapped. Select this one
                vms.top().setSelected( nd ) ; }
        else {
            // Recurse on first unmapped child
                nd.child_exp(f).select( vms ) ; } }
}