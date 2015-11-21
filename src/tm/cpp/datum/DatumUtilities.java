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

package tm.cpp.datum;

import tm.clc.datum.AbstractFloatDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.ClcDatumUtilities;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class DatumUtilities extends ClcDatumUtilities
{
    static String asciiToString( byte ascii ) {
        switch( ascii ) {
        case (int)'\n'  : return "\\n" ;
        case (int)'\t'  : return "\\t" ;
        case 11         : return "\\v" ;
        case  7         : return "\\a" ;
        case  8         : return "\\b" ;
        case 13         : return "\\r" ;
        case 12         : return "\\f" ;
        case (int)'\\'  : return "\\\\" ;
        case (int)'\''  : return "\\\'" ;
        case (int)'\"'  : return "\\\"" ;
        case  0 : return "\\0" ;
        default :
            if( 32 <= ascii && ascii <=126 ) {
                // Printable ascii character.
                return (new Character( (char) ascii )).toString() ; }
            else {
                int high = (ascii & 0xF0) >>> 4 ;
                int low = ascii & 0xF ;
                char highchar = (char) (high < 10 ? high + (int)'0' : high - 10 + (int)'A' ) ;
                char lowchar =  (char) (low  < 10 ? low + (int)'0' : low  - 10 + (int)'A' ) ;
                char[] temp = {'\\', 'x', highchar, lowchar } ;
                return new String( temp ) ; } } }

    static public boolean inputString( VMState vms, PointerDatum ptr ) {
        Console console = vms.getConsole() ;
        boolean success = ClcDatumUtilities.skipWhiteSpace( console ) ;
        if( ! success ) {
            // Had to ask for more input.
            return false ; }
        String str = "" ;
        int len=0 ;
        while(true) {
            char inputChar = console.peekChar(len) ;
            if( inputChar == '\uffff' ) return false ;
            if( ClcDatumUtilities.isWhiteSpace(inputChar) ) { break ; }
            str += inputChar ;
            len += 1 ; }
        
        // Having gotten this far, there are len characters that
        // can be read.
        console.consumeChars( len ) ;
        
        // This is not a very safe way to 
        // do it, as there is no check that actual
        // characters are being set.
        int addr = ptr.getValue() ;
        Memory mem = vms.getMemory() ;
        for( int i=0 ; i<len ; ++i ) {
            mem.putByte( addr+i, (byte) str.charAt(i)); }
        // And the nul byte.
        mem.putByte( addr+len, (byte)0 ) ;
        
        return true ;
    }

    
    static public boolean inputInt( VMState vms, AbstractIntDatum datum, int sizeInBytes, boolean unsigned ) {
        /*  After whitespace is skipped, we read the number according to the
            following right recursive grammar
                0 --> (+ | - | epsilon) 1
                1 --> D 2 
                2 --> D 2 |  epsilon
            where D is any digit and epsilon is the empty string.
            
            The resulting value must fit in the space provided and
            must not be negative if the datum is unsigned.
        */
        Console console = vms.getConsole() ;
        boolean success = ClcDatumUtilities.skipWhiteSpace( console ) ;
        if( ! success ) {
            // Had to ask for more input.
            return false ; }
        else {
            long value = 0 ; // We actually build the value as a negative number regardless of sign.
            boolean negative = false ;
            boolean overflow = false ;
            int i = 0 ;
            int state = 0 ;
            char inputChar = console.peekChar(0) ;
            loop: while( true ) {
                switch( state ) {
                    case 0 :
                        if( inputChar == '+' ) {
                            state = 1 ;
                            ; ++i ; inputChar = console.peekChar(i) ;}
                        else if( inputChar == '-' ) {
                            negative = true ;
                            state = 1 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else {
                            state = 1 ; }
                     break ;
                     case 1 :
                        if( Character.isDigit( inputChar ) ) {
                            value = - Character.digit( inputChar, 10 ) ;
                            state = 2 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else break loop ;
                    break ;
                    case 2 :
                        if( Character.isDigit( inputChar ) ) {
                            value = 10*value - Character.digit( inputChar, 10 ) ;
                            // If that makes value flip to positive, then something is not right.
                            if( value > 0 ) overflow = true ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ; } }
            
            // If we had to quit the loop because of an absence of input,
            // then return immediately
            if( inputChar == '\uffff' ) {
                return false ; }
                
            if( ! negative ) {
                value = - value ;
                if( value < 0 ) overflow = true ; }
                
            if( state == 2 && unsigned ) {
                // Unisigned datum, state 2
                // Does it fit.
                if( sizeInBytes == 8 ) 
                    // This is not quite right.
                    // success should be set to false if
                    // bits were lost.
                    success = true ;
                else {
                    long max = 1L << (8*sizeInBytes-1) ;
                    success = 0 <= value && value <= max ; } }
            else if( state == 2 && !overflow ) {
                // So far so good.  We have a legitimate long.
                // But does it fit in the space provided?
                if( sizeInBytes == 8 )
                    success = true ;
                else {
                    long max = (1L << (8*sizeInBytes-1) ) - 1 ;
                    long min = (-max) - 1  ;
                    success = min <= value && value <= max ; } }
            else {
                success = false ; }
            
            console.consumeChars( i ) ;
            if( success ) {
                // Success
                datum.putValue( value ) ;
                return true ; } 
            else {
                // Bad syntax or overflow of some kind.
                console.setFailBit() ;
                return true ; } } }
    
    static public boolean inputFloat( VMState vms, AbstractFloatDatum datum ) {
        /*  After whitespace is skipped, we read the number according to the
            following right recursive grammar
                0 --> (+ | - | epsilon) 1
                1 --> D 2 | . 3
                2 --> D 2 | . 4 | (e | E) 5 | epsilon
                3 --> D 4
                4 --> D 4 | (e | E) 5 | epsilon
                5 --> (+ | - | epsilon) 6
                6 --> D 7
                7 --> D 7 | epsilon
            where D is any digit and epsilon is the empty string.
            
            I am not checking for overflow. For proper C++, values that
            overflow should be rejected.  I'm not going to bother.
        */
        Console console = vms.getConsole() ;
        boolean success = ClcDatumUtilities.skipWhiteSpace( console ) ;
        if( ! success ) {
            // Had to ask for more input.
            return false ; }
        else {
            double mantissa = 0.0 ;
            long exponent = 0 ;
            double decimalMultiplier = 1.0 ; // Place of each decimal
            boolean negativeMantissa = false ;
            boolean negativeExponent = false ;
            
            int i = 0 ; // Number of charcters considered thus far.
            int state = 0 ;
            char inputChar = console.peekChar(0) ;
            
            loop: while( true ) {
                switch( state ) {
                    case 0 :
                        if( inputChar == '+' ) {
                            state = 1 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else if( inputChar == '-' ) {
                            negativeMantissa = true ;
                            state = 1 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else {
                            state = 1 ; }
                     break ;
                     case 1 :
                        if( Character.isDigit( inputChar ) ) {
                            mantissa = Character.digit( inputChar, 10 ) ;
                            state = 2 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else if( inputChar == '.' ) {
                            state = 3 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else break loop ;
                    break ;
                    case 2 :
                        if( Character.isDigit( inputChar ) ) {
                            mantissa = 10*mantissa + Character.digit( inputChar, 10 ) ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else if( inputChar == '.' ) {
                            state = 4 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else if( inputChar == 'e' || inputChar == 'E' ) {
                            state = 5 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ;
                    break ;
                    case 3 :
                        if( Character.isDigit( inputChar ) ) {
                            decimalMultiplier /= 10.0 ;
                            mantissa += decimalMultiplier * Character.digit( inputChar, 10 ) ;
                            state = 4 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ;
                    break ;
                    case 4 :
                        if( Character.isDigit( inputChar ) ) {
                            decimalMultiplier /= 10.0 ;
                            mantissa += decimalMultiplier * Character.digit( inputChar, 10 ) ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else if( inputChar == 'e' || inputChar == 'E' ) {
                            state = 5 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ;
                    break ;
                    case 5 :
                        if( inputChar == '+' ) {
                            state = 6 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else if( inputChar == '-' ) {
                            negativeExponent = true ;
                            state = 6 ;
                            ++i ; inputChar = console.peekChar(i) ;}
                        else {
                            state = 6 ; }
                     break ;
                     case 6 :
                        if( Character.isDigit( inputChar ) ) {
                            exponent = Character.digit( inputChar, 10 ) ;
                            state = 7 ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ;
                    case 7 :
                        if( Character.isDigit( inputChar ) ) {
                            exponent = exponent*10 + Character.digit( inputChar, 10 ) ;
                            ++i ; inputChar = console.peekChar(i) ; }
                        else break loop ; } }
            
            // If we had to quit the loop because of an absence of input,
            // then return immediately
            if( inputChar == '\uffff' ) {
                return false ; }
            
            console.consumeChars( i ) ;
            if( state == 2 || state == 4 || state == 7 ) {
                // Success
                if( negativeMantissa ) mantissa = - mantissa ;
                if( negativeExponent ) exponent = - exponent ;
                double value ;
                try{ value = mantissa * Math.pow(10.0, (double)exponent) ; }
                catch( ArithmeticException e ) { value = 0.0 ; }
                datum.putValue( value ) ;
                return true ; } 
            else {
                // Bad syntax.
                console.setFailBit() ;
                return true ; } } }
}