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

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/** New operator */
public class ExpNew extends ExpAbsNew
{
    /* package */ NodeList initializer_args ;

    public ExpNew( TypeNode t,
        boolean show_initializer,
        NodeList initializer_args,
        ExpressionNode initialization )
    {
        super(t, initialization) ;
        Assert.check( show_initializer || initializer_args.isEmpty());

        this.initializer_args = initializer_args ;

        // Syntax
        // 0 compute a string for the type id.
        Assert.check( t instanceof TyAbstractPointer ) ;
        TypeNode pointee = ((TyAbstractPointer)t).getPointeeType() ;
        Assert.check( pointee instanceof TyCpp ) ;
        String preSyntax = "new ("
            + ((TyCpp)pointee).typeId( "", false )
            + ")" ;

        // 1 compute the syntax array
        String [] syntax = new String[ initializer_args.length() + 1 ] ;
        if( show_initializer && initializer_args.isEmpty() ) {
            syntax[0] = preSyntax + "()" ; }
        else if( initializer_args.isEmpty() ) {
            syntax[0] = preSyntax ; }
        else {
            syntax[0] = preSyntax + "(" ;
            for(int i = 1 ; i < initializer_args.length() ; ++i ) {
                syntax[i] = "," ; }
            syntax[ initializer_args.length() ] = ")" ; }

        set_syntax( syntax ) ;
    }


    protected NodeList getChildrenForDump() {
        NodeList nl = new NodeList() ;
        for( int i=0 ; i<initializer_args.length() ; ++i )
            nl.addLastChild( initializer_args.get(i) ) ;
        if( initialization != null ) nl.addLastChild( initialization );
        return nl ;
    }

    public String toString(VMState vms) {
        return toString( vms, initializer_args ) ; }
}