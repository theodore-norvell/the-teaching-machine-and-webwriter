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

/*
 * Created on 2009-06-02 by Theodore S. Norvell. 
 * See http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm
 * for an explanation of the algorithm.
 */
package tm.expressionParser;

public abstract class AbstractExpressionParser
   < Result,
     Token extends AbstractExpressionParser.BasicToken
   >
{
    
    /** BasicToken categories: */
    public enum Category { EOF, LPAR, RPAR, OP, ATOM }
    
    /** BasicToken: Go ahead and subclass if you want more fields. */
    public static class BasicToken {
        public Category category ;
        public String image ;
    }
    
    private Token nextToken = null ;
    
    Token next() {
        if( nextToken == null ) nextToken = getNextToken() ;
        return nextToken ;
    }
    
    void consume() {
        nextToken = null ;
    }
    
    void expect( Category category ) {
        if( next().category == category ) consume() ;
        else error() ;
    }
    
    public Result parseExpression( ) {
        Result t = parseExpression( 0 ) ;
        expect( Category.EOF ) ;
        return t ;
    }
    

    
    protected Result parseExpression( int p ) {
        Result t = parsePrimary() ;
        while( isBinaryOp( next() ) && binaryPrecedence( next()) >= p) {
            Token op = next() ;
            consume() ;
            int q = isRightAssociative( op ) ? binaryPrecedence( op )
                                             : 1 + binaryPrecedence(  op ) ;
            Result t1 = parseExpression( q ) ;
            t = mkNode( op, t, t1 ) ;
        }
        return t ;
    }
    
    protected Result parsePrimary() {
        Token tok = next() ;
        if( isUnaryOp( tok  )) {
            consume() ;
            int q = unaryPrecedence( tok ) ;
            Result t = parseExpression( q ) ;
            return mkNode( tok, t ) ; }
        else if( tok.category == Category.LPAR ) {
            consume() ;
            Result t = parseExpression( 0 ) ; 
            expect( Category.RPAR ) ;
            return t ;
        } else if( tok.category == Category.ATOM ) {
            consume() ;
            return mkNode( tok ) ;
        } else {
            error() ;
            return null ;
        }
    }
    
    public abstract Result mkNode( Token tok ) ;
    
    public abstract Result mkNode( Token tok, Result operand ) ;
    
    public abstract Result mkNode( Token tok, Result left, Result right ) ;
    
    public abstract boolean isBinaryOp( Token op ) ;
    
    public abstract int binaryPrecedence( Token op ) ;
    
    public abstract boolean isUnaryOp( Token op ) ;
    
    public abstract int unaryPrecedence( Token op ) ;
    
    public abstract boolean isRightAssociative( Token op ) ;
    
    /** Deliver the next token. */
    public abstract Token getNextToken() ;
    
    public abstract void error() ;
}
