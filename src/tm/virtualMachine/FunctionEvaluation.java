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

package tm.virtualMachine;

import tm.interfaces.Datum;
import tm.languageInterface.StatementInterface;

public class FunctionEvaluation extends Evaluation {
    
    // The recipient object is null for functions that are not methods and for static initialization chains.
    private final Datum recipientObject ;
    
    // The resultDatum is null for static and dynamic initialization chains in Java.
    // Currently (earlier 2011) it is not null for void functions, but that could change.
    // Furthermore it is not null for static initialization chains in C++, but that could also change.
    private final Datum resultDatum ;
    
    

    public FunctionEvaluation(VMState vms, StatementInterface stmt, Datum recipientObject, Datum resultDatum ) {
        super( vms, stmt ) ;
        setSelected( stmt ) ;
        this.recipientObject = recipientObject ;
        this.resultDatum = resultDatum ;
    }
    
    Datum getResultDatum() { return resultDatum ; }

    public boolean isDone() {
        return false ; 
    }
}