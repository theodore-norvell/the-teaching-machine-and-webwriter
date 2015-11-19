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

import java.util.Vector;

import tm.clc.ast.ConstFloat;
import tm.clc.ast.ConstInt;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ConstStr;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyChar;
import tm.cpp.datum.ArrayDatum;
import tm.cpp.datum.CharDatum;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/**
 * Performs analysis of token images for conversion into AST
 * representation of literals.
 * <p>Follows section 2.13 of the ISO spec, covering integer, floating,
 * character, boolean, and string literals.
 * @author Derek Reilly
 * @created Nov 8, 2001
 */
public class Literals implements FundamentalTypeUser {
    private static final String INVALID_CHARACTER_LITERAL =
        "{0} is an invalid character literal.";
    private static final String INVALID_INT_LITERAL =
        "{0} is an invalid integer literal.";
    private static final String INVALID_FLOAT_LITERAL =
        "{0} is an invalid floating literal.";
    private static final String MULTICHARACTER_LITERALS =
        "multicharacter literals";
    private static final String MULTICHARACTER_WIDE_LITERALS =
        "wide-character literals containing multiple c-chars";
    private static final String NOT_IMPLEMENTED =
        "Sorry, {0} not implemented";

    private static Debug dbg = Debug.getInstance ();
    /**
     * Creates a boolean constant expression representing a raw boolean value.
     *
     * @param b a <code>boolean</code> value
     * @return an <code>ExpressionNode</code> representing the boolean value
     */
    public static ExpressionNode make_bool_const (boolean b) {
        return new ConstInt (ctyBool, new Boolean (b).toString (), (b) ? 1 : 0);
    }

    /**
     * Creates a <em>string literal</em> (constant char array expression)
     * representing the <code>String</code> provided.
     * <br><em>wide string literals currently not supported</em>
     * @param str a <code>String</code> value
     * @return an <code>ExpressionNode</code> representing the string literal
     */
    public static ExpressionNode make_string_const (String str, VMState vms) {

        int sz = str.length() ;
        Assert.check( str.charAt(0) == '"'
                    && str.charAt( sz - 1 ) == '"' ) ;

        // First obtain a vector representing the byte values
        Vector byteVector = new Vector() ;
        Vector imageVector = new Vector() ;
        int i = 1 ;
        while( i < sz ) {
            byte len ;
            byte charToAdd ;
            String imageToAdd ;
            if( i==sz-1 ) {
                charToAdd = 0 ;
                imageToAdd = "\\0" ;
                len = 1 ; }
            else {
                byte[] result = new byte[2] ;
                charToAscii( str, i, result ) ;
                len = result[0] ;
                charToAdd = result[1] ;
                imageToAdd = str.substring(i, i+len) ; }

            byteVector.addElement( new Integer( charToAdd ) ) ;
            imageVector.addElement( imageToAdd ) ;
            i += len ; }

        int size = byteVector.size() ;
        Assert.check( size > 0 ) ;
        dbg.msg (Debug.COMPILE, "array size for string const is " + size);
        Store theStore = vms.getStore() ;

        // Make the type of the array:
            TyArray tyArrayOfChar = new TyArray() ;
            tyArrayOfChar.setNumberOfElements( size ) ;
            tyArrayOfChar.addToEnd( TyChar.get( Cpp_SpecifierSet.CVQ_CONST ) ) ;

        // Make an array datum
            ArrayDatum array = (ArrayDatum) tyArrayOfChar.makeDatum( vms, theStore.getStatic(), str ) ;

        /* Now we have a vector of byte values. Let's put them in the memory. */
        for(int j = 0 ; j < size ; ++j ) {
            // Make a datum
            CharDatum charDatum = (CharDatum) array.getElement(j) ;
            Integer integerValue = (Integer) byteVector.elementAt( j ) ;
            charDatum.putValue( integerValue.intValue() ) ; }


        return new ConstStr (array, imageVector, str);
    }

    /** THIS METHOD TO CHANGE -- integrate with existing code in this class
     */
    static private void charToAscii( String image, int start, byte[] result ) {
        int ascii ;
        int len ;
        if( image.charAt(start) != '\\' ) {
            ascii = (int) image.charAt( start ) ;
            len = 1 ; }
        else {
            switch( image.charAt(start+1) ) {
            case 'n' :
                ascii = 10 ;
                len = 2 ;
                break ;
            case 't' :
                ascii = 9 ;
                len = 2 ;
                break ;
            case 'v' :
                ascii = 11 ;
                len = 2 ;
                break ;
            case 'b' :
                ascii = 8 ;
                len = 2 ;
                break ;
            case 'r' :
                ascii = 13 ;
                len = 2 ;
                break ;
            case 'f' :
                ascii = 12 ;
                len = 2 ;
                break ;
            case 'a' :
                ascii = 7 ;
                len = 2 ;
                break ;
            case '\\' :
                ascii = 92 ;
                len = 2 ;
                break ;
            case '?' :
                ascii = 63 ;
                len = 2 ;
                break ;
            case '\'' :
                ascii = 39 ;
                len = 2 ;
                break ;
            case '"' :
                ascii = 34 ;
                len = 2 ;
                break ;
            case 'x' : case 'X' :
                // Hex
                ascii = 0 ;
                len = 2 ;
                while( true ) {
                    int ch = (int) image.charAt( start+len ) ;
                    if( (int)'0' <= ch && ch <= (int) '9' ) {
                        ascii *= 16 ;
                        ascii += ch - (int)'0' ;
                        len += 1 ;}
                    else if( (int) 'A' <= ch && ch <= (int) 'F' ) {
                        ascii *= 16 ;
                        ascii += ch - (int)'A' + 10 ;
                        len += 1 ; }
                    else if( (int) 'a' <= ch && ch <= (int) 'f' ) {
                        ascii *= 16 ;
                        ascii += ch - (int)'a' + 10 ;
                        len += 1 ; }
                    else break ; }
                break ;
            case '0' :
                ascii = 0 ;
                len = 2 ;
                while( true ) {
                    if( len == 5 ) break ;
                    int ch = (int) image.charAt( start+len ) ;
                    if( (int)'0' <= ch && ch <= (int) '7' ) {
                        ascii *= 8 ;
                        ascii += ch - (int)'0' ;
                        len += 1 ;}
                    else break ; }
                break ;
            default :
                // Anything else is a lexical error.
                Assert.check( false ) ;
                len = ascii = 0 ; /*Make compiler happy*/} }

        // Pack both results in one result
        Assert.apology( 0 <= ascii && ascii <= 255,
            "Character in char const or string is not ascii." ) ;
        result[0] = (byte) len ;
        result[1] = (byte) ascii ; }


    /**
     * Creates a character constant expression for the constant represented
     * by the token_image.
     * <ul>The following char constant forms are recognized but not currently
     * supported by the system (i.e. an apology will be thrown):
     * <li>wide character literals (wchar_t)
     * <li>universal character names (Unicode escapes)
     * <li>multicharacter literals (this is different from simple, octal, and
     * hex escaped char literals)
     * <li>hex char literals whose value exceeds that which can be represented
     * by a <code>long</code> in java
     * </ul>
     * @param token_image the char constant token as encountered by the parser
     * @return an <code>ExpressionNode</code> representing the char constant
     */
    public static ExpressionNode make_char_const (String token_image) {
        // ** it occurred to me *after* writing this method that the
        // parser could easily distinguish between the various cases and
        // pass indicators. At the moment parsing takes place in this method

        boolean wide = Character.toUpperCase (token_image.charAt (0)) == 'L';
        String charC = token_image.substring ((wide)? 2 : 1,
                                              token_image.length () - 1);

        char esc = (charC.length () > 1) ? charC.charAt (1) : 0;

        // 2.1.3.2
        long value = -1;

        switch (charC.length ()) {
        case 0:
            Assert.apology (INVALID_CHARACTER_LITERAL, token_image);
            break;
        case 1:
            value = charC.charAt (0);
            break;
        case 2:
            switch (charC.charAt (0)) {
            case '\\':
                value = (Character.isDigit (esc)) ? octalEscapeSequence (charC)
                    : simpleEscapeSequence (esc);
                break;
            default:
                Assert.apology (NOT_IMPLEMENTED, (wide)
                                ? MULTICHARACTER_WIDE_LITERALS
                                : MULTICHARACTER_LITERALS);
                break;
            }
            break;
        case 3:	case 4:
            if (Character.isDigit (esc)) {
                value = octalEscapeSequence (charC);
                break;
            }
            // Fall through to next case
        case 5:	case 6:
            if (esc == 'u' || esc == 'U') {
                value = universalCharacterName (charC);
                break;
            }
            // Fall through to next case
        default:
            if (esc == 'x' ) {
                value = hexEscapeSequence (charC);
            } else if (charC.charAt (0) == '\\') {
                Assert.apology (INVALID_CHARACTER_LITERAL, token_image);
            } else {
                Assert.apology (NOT_IMPLEMENTED, (wide)
                                ? MULTICHARACTER_WIDE_LITERALS
                                : MULTICHARACTER_LITERALS);
            }
            break;
        }

        if (wide || value > 255) {
            Assert.apology (NOT_IMPLEMENTED, "wchar_t"); }

        return new ConstInt (ctyChar, token_image, value);
    }

    /**
     * Creates a float constant expression given the provided token image.
     *
     * @param token_image the constant as encountered by the Parser
     * @return an <code>ExpressionNode</code> representing the float constant
     */
    public static ExpressionNode make_float_const (String token_image) {

        // 2.13.3.1
        // the type is double unless suffix of (f/F) for float or
        // (l/L) for long double
        char suffix = Character.toUpperCase
            (token_image.charAt (token_image.length () - 1));

        TypeNode type = (suffix == 'L') ? ctyLongDouble
            : (suffix == 'F') ? ctyFloat
            : ctyDouble;

        double value = 0;
        try {
            value = Double.valueOf ((Character.isDigit (suffix)) ? token_image :
                                    token_image.substring
                                    (0, token_image.length () - 1))
                .doubleValue ();

        } catch (NumberFormatException nfe) {
            Assert.apology (INVALID_FLOAT_LITERAL, token_image);
        }

        return new ConstFloat (type, token_image, value);
    }

    /**
     * Creates an integral constant expression, given the token image and
     * the associated radix.
     *
     * @param token_image the raw token as encountered by the parser
     * @param radix the constant's radix (hex, decimal, or octal)
     * @return an <code>ExpressionNode</code> representing the int constant
     */
    public static ExpressionNode make_int_const (String token_image, int radix) {
        TypeNode type = null;

        String rawVal = token_image;
        // 2.13.1
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
        boolean unsigneds = false;
        boolean longs = false;
        int rvl = rawVal.length ();
        int sufCount = 0 ;
        for( int i = rvl-1; i>=0 && i>=rvl-2; --i ) {
            char suf = Character.toUpperCase (rawVal.charAt (i));
            switch (suf) {
            case 'L' : longs = true;
            break ;
            case 'U' : unsigneds = true;
                sufCount++;
            break; } }
        rawVal = rawVal.substring (0, rvl - sufCount);

        // get the value
        long value ;
        try {
            if( rawVal.length()==0 ) {
                value = 0 ; }
            else if( radix == 10 ) {
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
            Assert.apology (INVALID_INT_LITERAL, token_image);
            value = 0 ; // Keep compiler happy
        }

        // determine the type (2.13.1-2)
        if (radix == 10) { // signed decimal
            // behaviour undefined if value can't be held in a long int -
            // we are just assigning it a long int type regardless
            boolean fitsInInt = 0 <= value && value <= 0x8FFFFFFFL ;
            type = (!longs && fitsInInt)
                ? (unsigneds) ? ctyUnsignedInt : ctyInt
                : (unsigneds) ? ctyUnsignedLong : ctyLong;

        } else { // signed octal or hexidecimal
            boolean fitsInUnsignedInt =  0 <= value && value < 0xFFFFFFFFL; ;
            boolean fitsInInt = 0 <= value && value <= 0x8FFFFFFFL ;
            boolean fitsInLong = value > 0 ;
            if (longs || !fitsInUnsignedInt) {
                type = (unsigneds || !fitsInLong)
                    ? ctyUnsignedLong : ctyLong;
            } else {
                type = (unsigneds || !fitsInInt)
                    ? ctyUnsignedInt : ctyInt;
            }
        }

        // build and return the constant expression

        ExpressionNode nd = new ConstInt (type, token_image, value);
        if( radix != 10 || longs || unsigneds ) {
            nd.setUninteresting( false ); }
        return nd ;

    }

    /**
     * Converts an octal escape sequence into the integer value of
     * the character represented.
     */
    private static long octalEscapeSequence (String seq) {
        // octal escape sequences consist of a backslash followed by 1 to 3
        // octal digits - size limit tested in calling method.
        long value = 0;
        try {
            value = Long.parseLong (seq.substring (1), 8);
        } catch (NumberFormatException nfe) {
            Assert.apology (INVALID_CHARACTER_LITERAL, seq);
        }
        return value;
    }

    /**
     * Converts a hexidecimal escape sequence into the integer value of
     * the character represented.
     */
    private static long hexEscapeSequence (String seq) {
        // hex escape sequences consist of a backslash followed by an 'x'
        // and one or more hex digits. No limit to size, but interpretation
        // of high values is implementation specific.
        long value = 0;
        try {
            value = Long.parseLong (seq.substring (1), 16);
        } catch (NumberFormatException nfe) {
            Assert.apology (INVALID_CHARACTER_LITERAL, seq);
        }
        return value;
    }

    /**
     * Converts a simple escape sequence into the integer value of the
     * character represented.
     */
    private static long simpleEscapeSequence (char c) {
        long value = -1;
        switch (c) {
        case 'n': // newline
            value = '\n';
            break;
        case 't': // horizontal tab
            value = '\t';
            break;
        case 'v': // vertical tab
            value = '\u000B';
            break;
        case 'b': // backspace
            value = '\b';
            break;
        case 'r': // carriage return
            value = '\r';
            break;
        case 'f': // form feed
            value = '\f';
            break;
        case 'a': // alert (bell)
            value = '\u0007';
            break;
        case '\\': // backslash
        case '?': // question mark
        case '\'': // single quote
        case '"': // double quote
            value = c;
            break;
        default:
            Assert.apology (INVALID_CHARACTER_LITERAL, "\\" + c);
            break;
        }

        return value;
    }

    /**
     * Converts the universal character identifier into an integer representing
     * its value.
     * <br><em>Not yet implemented</em>
     */
    private static long universalCharacterName (String nm) {
        Assert.apology (NOT_IMPLEMENTED, "universal character names");
        return -1;
    }
}

