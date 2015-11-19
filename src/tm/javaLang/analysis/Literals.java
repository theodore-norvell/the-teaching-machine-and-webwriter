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

package tm.javaLang.analysis;

import java.util.Vector;

import tm.clc.analysis.ScopedName;
import tm.clc.ast.ConstFloat;
import tm.clc.ast.ConstInt;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.ConstBool;
import tm.javaLang.ast.ConstNull;
import tm.javaLang.ast.ConstStr;
import tm.javaLang.ast.TyClass;
import tm.javaLang.parser.JavaParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.VMState;

/**
 * Performs analysis of token images for conversion into AST
 * representation of literals.
 * <p>Follows section 2.13 of the ISO spec, covering integer, floating,
 * character, boolean, and string literals.
 * @author Derek Reilly
 * @created Nov 8, 2001
 */
public class Literals implements FundamentalTypeUser, JavaParserConstants {

    private static Debug dbg = Debug.getInstance ();

    /**
     * Creates an expression node for a Java literal.
     *
     * @param t a token kind as defined in JavaParserConstants
     * @param tokenImage the token as it appeared in the source file.
     * @param vms The virtual machine state
     * @return an <code>ExpressionNode</code> representing the litera.
     */
    public static ExpressionNode makeConstant (int t,
                                               String tokenImage,
                                               Java_CTSymbolTable symtab,
                                               VMState vms) {
        ExpressionNode constant = null;
        switch(t){
            case DECIMAL_LITERAL:
                constant = makeIntConst(tokenImage, 10);
                break;
            case HEX_LITERAL:
                constant = makeIntConst(tokenImage, 16);
                break;
            case OCTAL_LITERAL:
                constant = makeIntConst(tokenImage, 8);
                break;
            case FLOATING_POINT_LITERAL:
                constant = makeFloatConst(tokenImage);
                break;
            case CHARACTER_LITERAL:
                constant = makeCharConst(tokenImage);
                break;
            case STRING_LITERAL:
                {
                    TyClass objectClass = symtab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_OBJECT ) ;
                    TyClass stringClass = symtab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_STRING ) ;
                    
                    constant = makeStringConst( stringClass, objectClass, tokenImage, vms ) ;
    
                }
                break;
            case TRUE:
                constant = new ConstBool( true ) ;
                break;
            case FALSE:
                constant = new ConstBool( false );
                break;
            case NULL:  // Not sure how to handle this. Either ConstNull class or function call to TyClass
                constant = new ConstNull( ) ;
                break ;
            default :
                Assert.check(false) ;
        }
        return constant;
    }

    private static String parseCharsSequence( String str ) {
        // See JLS 3.10.4-6
        StringBuffer result = new StringBuffer() ;
        for( int i=0, len = str.length() ; i<len ; ) {
            char ch = str.charAt(i) ;

            // Set ch and increment i ;
            if( ch == '\\' )  {
                ch = str.charAt(i+1) ;
                switch( ch ) {
                    case 'b' : ch = '\b' ; i += 2 ; break ;
                    case 't' : ch = '\t' ; i += 2 ; break ;
                    case 'n' : ch = '\n' ; i += 2 ; break ;
                    case 'f' : ch = '\f' ; i += 2 ; break ;
                    case 'r' : ch = '\r' ; i += 2 ; break ;
                    case '"' : case '\'' : case '\\' : i+= 2 ; break ;
                    case '0' : case '1' : case '2' : case '3' :
                    case '4' : case '5' : case '6' : case '7' : {
                        // Octal escapes \a, \ab, \abc
                        int oct0 = (int)ch - (int)'0' ;
                        char ch1 = (char)0 ;
                        char ch2= (char)0 ;
                        if( i+2 < len ) {
                            ch1 = str.charAt(i+2) ;
                            if( i+3 < len ) {
                                ch2 = str.charAt(i+3) ; } }
                        if( '0' <= ch && ch <= '3'
                         && '0' <= ch1 && ch1 <= '7'
                         && '0' <= ch2 && ch2 <= '7' ) {
                            // Length is 4 \acb
                            int oct1 = (int)ch1 - (int)'0' ;
                            int oct2 = (int)ch2 - (int)'0' ;
                            ch = (char)( 64* oct0 + 8*oct1 + oct2 );
                            i += 4 ; }
                        else if( '0' <= ch1 && ch1 <= '7' ) {
                            // Length is 3 \ab
                            int oct1 = (int)ch1 - (int)'0' ;
                            ch = (char)( 8*oct0 + oct1 ) ;
                            i += 3 ;}
                        else  {
                            // Length is 2 \a
                            ch = (char)( oct0 ) ;
                            i += 2 ; } }
                    break ;
                    default: // The parser should have picked up illegal escapes already
                        Assert.check(false) ; } }
            else {
                i += 1 ; }
            result.append( ch ) ; }
        return result.toString() ;
    }


    /**
     * Creates a character constant expression for the constant represented
     * by the tokenImage.
     * @param tokenImage the char constant token as encountered by the parser
     * @return an <code>ExpressionNode</code> representing the char constant
     */
    private static ExpressionNode makeCharConst (String tokenImage) {
        // See JLS 3.10.4

        String stripped = tokenImage.substring (1, tokenImage.length () - 1);
        String parsed = parseCharsSequence( stripped ) ;
        Assert.check( parsed.length() == 1 ) ;
        char value = parsed.charAt(0) ;
        return new ConstInt (tyChar, tokenImage, value);
    }

    /**
     * Creates a string constant expression for the constant represented
     * by the tokenImage.
     * @param tokenImage the char constant token as encountered by the parser
     * @return an <code>ExpressionNode</code> representing the char constant
     */
    private static ExpressionNode makeStringConst
              ( TyClass stringClass,
                TyClass objectClass,
                String tokenImage,
                VMState vms ) {
        // See JLS 3.10.5

        String stripped = tokenImage.substring (1, tokenImage.length () - 1);
        String parsed = parseCharsSequence( stripped ) ;
        return new ConstStr (stringClass, tokenImage, parsed, vms);
    }

    /**
     * Creates a float constant expression given the provided token image.
     *
     * @param tokenImage the constant as encountered by the Parser
     * @return an <code>ExpressionNode</code> representing the float constant
     */
    private static ExpressionNode makeFloatConst (String tokenImage) {

        // the type is double unless suffix of (f/F) for float
        char suffix = Character.toUpperCase
            (tokenImage.charAt (tokenImage.length () - 1));

        TypeNode type;
        if (suffix == 'F') type = tyFloat;
        else type = tyDouble;

        if( suffix == 'F' || suffix == 'D' )
            tokenImage = tokenImage.substring(0, tokenImage.length () - 1) ;

        double value = 0.;
        try {
            value = Double.valueOf (tokenImage).doubleValue ();
        } catch (NumberFormatException nfe) {
            Assert.error ("Invailid floating constant");
        }

        return new ConstFloat (type, tokenImage, value);
    }

    /**
     * Creates an integral constant expression, given the token image and
     * the associated radix.
     *
     * @param tokenImage the raw token as encountered by the parser
     * @param radix the constant's radix (hex, decimal, or octal)
     * @return an <code>ExpressionNode</code> representing the int constant
     */
    private static ExpressionNode makeIntConst (String tokenImage, int radix) {
        TypeNode type = null;

        String rawVal = tokenImage;
        // strip prefixes
        switch (radix) {
        case 8:
            rawVal = rawVal.substring (1);
            break;
        case 16:
            rawVal = rawVal.substring (2);
            break;
        }

        // analyze and strip suffixes
        int rvl = rawVal.length ();
        if(Character.toUpperCase (rawVal.charAt (rvl-1)) == 'L'){
            type = tyLong;
            rawVal = rawVal.substring (0, rvl - 1);
        }
        else type = tyInt;

        // get the value
        long value ;

        try{
            if( radix == 10 ) {
                value = Long.parseLong (rawVal, radix); }
            else {
                // radix is 8 or 16.  Unfortunately Long.parseLong
                // doesn't parse negative long constants. So we
                // do it the hard way
                int logRadix = radix == 8 ? 3 : 4 ;
                long mask = type==tyInt ? 0x00000000FFFFFFFFL : 0xFFFFFFFFFFFFFFFFL ;
                value = 0 ;
                for( int i=0, len=rawVal.length() ; i<len ; ++i ) {
                    // Check that there is space available to shift in
                    // another digit.
                        if( (((value<<logRadix)&mask)>>>logRadix) != value )
                            throw new NumberFormatException() ;
                    char ch = rawVal.charAt(i) ;
                    int chVal ;
                    if( '0' <= ch && ch <= '9' ) chVal =  (int)ch - (int)'0' ;
                    else if( 'a' <= ch && ch <= 'f' ) chVal = 10 + (int)ch - (int)'a' ;
                    else if( 'A' <= ch && ch <= 'F' ) chVal = 10 + (int)ch - (int)'A' ;
                    else {Assert.check(false); chVal = 0 ;}
                    value = (value<<logRadix) | chVal ; } }

        } catch (NumberFormatException nfe) {
            Assert.error ("Invailid integer constant");
            value = 0 ; // Keep compiler happy
        }

        // build and return the constant expression

        ExpressionNode nd = new ConstInt (type, tokenImage, value);
        if( radix != 10 || type != tyInt ) {
            nd.setUninteresting( false ); }
        return nd ;

    }

}

