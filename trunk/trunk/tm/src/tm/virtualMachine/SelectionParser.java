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
 * Created on 2009-06-03 by Theodore S. Norvell. 
 */
package tm.virtualMachine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tm.expressionParser.*;
import tm.utilities.Assert;
import tm.virtualMachine.Selection.TokenType;

public class SelectionParser
    extends  AbstractExpressionParser< Selection, SelectionParser.Token>
{
    
    private Matcher matcher ;
    private String str ;
    
    public static Selection parse( String str) {
        return (new SelectionParser( str)).parseExpression() ;
    }
    
    private SelectionParser( String str) {
        this.str = str ;
        this.matcher = spaces.matcher( str )  ;
    }

    static class Token extends AbstractExpressionParser.BasicToken {
        public Selection.TokenType tokenType ;
        
        Token( String str, Category cat, Selection.TokenType type) {
            this.image = str ;
            this.category = cat ;
            this.tokenType = type ;
        }
        
        Token( String str, Category cat) {
            this.image = str ;
            this.category = cat ;
        }
    }

    @Override
    public void error() {
        Assert.apology("Can not parse selection expression") ;
    }
    
    class Pair {
        Pattern pattern ;
        Action action ;
        Pair( Pattern p, Action a ) { pattern = p ;  action = a ; }
    }
    
    abstract class Action {
        abstract Token run( String str ) ;
    }
        
       

    private Pattern spaces = Pattern.compile( "\\s*" ) ;
    private Pair[] pairs = {
            new Pair( Pattern.compile("\\&|\\."),
                      new Action() {
                        @Override Token run(String str) {
                            return new Token( str, Category.OP, Selection.TokenType.AND ) ;
                      }} ),
            new Pair( Pattern.compile("\\||\\+"),
                      new Action() {
                        @Override Token run(String str) {
                            return new Token( str, Category.OP, Selection.TokenType.OR ) ;
                      }} ),
            new Pair( Pattern.compile("\\!|\\~"),
                      new Action() {
                        @Override Token run(String str) {
                            return new Token( str, Category.OP, Selection.TokenType.NOT ) ;
                      }} ),
            new Pair( Pattern.compile("\\("),
                       new Action() {
                            @Override Token run(String str) {
                                return new Token( str, Category.LPAR ) ;
                       }} ),
            new Pair( Pattern.compile("\\)"),
                      new Action() {
                           @Override Token run(String str) {
                               return new Token( str, Category.RPAR ) ;
                       }} ),
            new Pair( Pattern.compile("([a-z,A-Z,0-9])+"),
                       new Action() {
                            @Override Token run(String str) {
                                str = str.toLowerCase() ;
                                if( str.equals("true") ) 
                                    return new Token( str, Category.ATOM, Selection.TokenType.TRUE ) ;
                                else if( str.equals("false") )
                                    return new Token( str, Category.ATOM, Selection.TokenType.FALSE ) ;
                                else
                                    return new Token( str, Category.ATOM, Selection.TokenType.TAG ) ;
                       }} ),
            new Pair( Pattern.compile("$"),
                       new Action() {
                            @Override Token run(String str) {
                                return new Token( str, Category.EOF ) ;
                        }} )
    } ;
    
    @Override
    public Token getNextToken() {
        matcher.usePattern( spaces ) ;
        boolean ok = matcher.lookingAt() ;
        int start = matcher.start() ;
        int end = matcher.end();
        Assert.check( ok && start == 0 ) ;
        str = str.substring( end ) ;
        matcher.reset( str ) ;
        for( Pair pair : pairs ) {
            matcher.usePattern( pair.pattern ) ;
            if( matcher.lookingAt() ) {
                start = matcher.start();
                end = matcher.end() ;
                Assert.check(  start == 0 ) ;
                String image = matcher.group(0) ;
                str = str.substring( end ) ;
                matcher.reset( str ) ;
                return pair.action.run( image ) ;
            }
        }
        error() ;
        return null ;
    }
    
    @Override
    public int binaryPrecedence(Token op) {
        switch( op.tokenType ) {
        case AND: return 2 ;
        case OR: return 1 ;
        default: Assert.check(false) ; return 0 ;
        }
    }

    @Override
    public boolean isBinaryOp(Token op) {
        if( op.category != Category.OP ) return false ;
        switch( op.tokenType ) {
        case AND: case OR: return true ;
        default: return false; }
    }

    @Override
    public boolean isRightAssociative(Token op) {
        return false;
    }

    @Override
    public int unaryPrecedence(Token op) {
        Assert.check( op.tokenType == Selection.TokenType.NOT ) ;
        return 3;
    }

    @Override
    public boolean isUnaryOp(Token op) {
        if( op.category != Category.OP ) return false ;
        switch( op.tokenType ) {
        case NOT: return true ;
        default: return false; }
    }

    @Override
    public Selection mkNode(Token tok) {
        if( tok.tokenType == TokenType.TAG ) 
            return new Selection( tok.image ) ;
        else return new Selection( tok.tokenType ) ;
    }

    @Override
    public Selection mkNode(Token tok, Selection operand) {
       return new Selection( tok.tokenType, operand ) ;
    }

    @Override
    public Selection mkNode(Token tok, Selection left, Selection right) {
        return new Selection( tok.tokenType, left, right ) ;
    }
}
