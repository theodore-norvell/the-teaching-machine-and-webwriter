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

package tm.cpp.analysis;

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

public class Initializer {

    private NodeList arg_list;
    private int kind ;
    
    public static final int COPY = 1, DIRECT = 2, NONE = 3 ;
    
    public Initializer(int kind ) {
        Assert.check( kind == NONE ) ;
        this.kind = kind ;
        this.arg_list = new NodeList() ;
    }

    public Initializer (int kind, ExpressionNode exp) {
        Assert.check( kind == COPY ) ;
        arg_list = new NodeList( exp ) ;
        this.kind = kind ;
    }

    // multiple initialization arguments
    public Initializer (int kind, NodeList nodes) {
        Assert.check( kind == DIRECT ) ;
        arg_list = nodes ;
        this.kind = kind ;
    }

    /** Gives the kind of the initialization expression
     *  @return COPY, DIRECT, or NONE
     */
    public int initializationKind() { return kind ; }
    
    /**
     * Gives the type of the evaluated initialization expression.
     * @return a TypeNode representing the type of the expression.
     */
    public TypeNode getType () { 
        return getExp().get_type() ;
    }

    /**
     * Gives the expression rvalue to be assigned in initialization
     */
    public ExpressionNode getExp () { 
        Assert.check( length() == 1 ) ;
        return (ExpressionNode) arg_list.get(0) ;
    }
    
    public ExpressionNode getExp( int i ) {
        Assert.check( i < length() ) ;
        return (ExpressionNode) arg_list.get(i) ;
    }

    /**
     * Gives inialization arguments
     */
    public NodeList getArgs () { 
        return arg_list;
    }
    
    /** Gives number of initialization arguments
     */
    public int length() {
        return arg_list.length() ;
    }
}