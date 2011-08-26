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

import tm.clc.ast.TyAbstractFun;


/*******************************************************************************
Class: TyFun

Overview:
This class represents the Java function type.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class TyFun extends TyAbstractFun implements TyJava {
    public TyFun(Vector param_types) { super(param_types); }

    public String getTypeString() {
        return "fcn returning " + returnType().getTypeString();
    }

    public String typeId() {
        String result = ((TyJava)returnType()).typeId() ;

        int count=getParamCount() ;
        result += "(" ;
        if( count != 0 ) {
            for( int i=0 ; i<count ; ++i ) {
                result += ((TyJava)getParamType(i)).typeId() ;
                if( i+1 < count ) result += ", " ; } }
        result += ")" ;

        return result ; }

    public String elementId(){ return typeId(); }

    public boolean isReachableByWideningFrom(TyJava fromType) {
        return false;
    }
}
