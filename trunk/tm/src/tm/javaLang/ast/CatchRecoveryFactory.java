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

import tm.clc.ast.ClcRecoveryFactory;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.javaLang.datum.PointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.AbruptCompletionStatus;
import tm.virtualMachine.Recovery;
import tm.virtualMachine.VMState;

/**
 * <p>Title: The Teaching Machine</p>
 * <p>Description: </p>
 * <p>Company: Memorial University</p>
 * @author Theodore Norvell
 * @version 1.0
 */

public class CatchRecoveryFactory extends ClcRecoveryFactory {

    private TypeNode parameterType ;
    public CatchRecoveryFactory( TypeNode parameterType ) {
        this.parameterType = parameterType ;
    }


    public Recovery makeRecovery( VMState vms ) {
        return new CatchRecovery( vms ) ; }

    public String getDescription() {
        return "CatchRecoveryFactory "+parameterType.getTypeString() ; }

    private class CatchRecovery extends ClcRecovery {

        public CatchRecovery( VMState vms ) {
            super( vms ) ;}

        public boolean canHandle(AbruptCompletionStatus acs) {
            if( acs instanceof ThrowCompletionStatus ) {
                Datum thrownDatum = ((ThrowCompletionStatus)acs).thrownDatum ;
                Assert.check( thrownDatum instanceof PointerDatum ) ;
                PointerDatum ptr = (PointerDatum) thrownDatum ;
                Datum thrownObject = ptr.deref() ;
                return JavaLangASTUtilities.assignableReferenceType(
                                                (TyJava) ptr.getType(),
                                                (TyJava) parameterType ) ; }
            else {
               return false; }
        }

        public void handle(AbruptCompletionStatus acs) {
            super.handle( acs );
           /*Although there is no explicet pop of this argument list,
           when a catch-clause ends it will jump out to the finally
           part of the try which should pop the argument list. */
            vms.pushNewArgumentList();
            vms.addArgument( ((ThrowCompletionStatus)acs).thrownDatum );
        }
    }
}