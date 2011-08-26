package jsnoopy;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

import java.lang.reflect.*;
import java.util.Vector;

public class Format {

    static String formatString( String str ) {
        StringBuffer buf = new StringBuffer() ;
        buf.append('"') ;
        for( int i=0, sz = str.length() ; i < sz ; ++i ) {
            char ch = str.charAt(i) ;
            int ich = (int) ch ;
            switch( ch ) {
                case '\\' : buf.append( "\\\\" ) ;
                break;
                case '\"' : buf.append( "\\\"" ) ;
                break;
                case '\n' : buf.append( "\\n" ) ;
                break;
                case '\t' : buf.append( "\\t" ) ;
                break;
                default : {

                    if( 32 <= ich && ich <= 126 ) {
                        buf.append( ch ) ; }
                    else {
                        buf.append( "\\u" ) ;
                        int h0 = ich & 0xF ;
                        ich >>= 4 ;
                        int h1 = ich & 0xF ;
                        ich >>= 4 ;
                        int h2 = ich & 0xF ;
                        ich >>= 4 ;
                        int h3 = ich & 0xF ;
                        buf.append( toHex( h3 ) ) ;
                        buf.append( toHex( h2 ) ) ;
                        buf.append( toHex( h1 ) ) ;
                        buf.append( toHex( h0 ) ) ; } } } }

        buf.append('"') ;
        return buf.toString() ;
    }

    static String methodToString( Method method ) {
        StringBuffer buf = new StringBuffer() ;
        buf.append( formatString(method.getName()) ) ;
        buf.append( "<" ) ;
        Class[] classes = method.getParameterTypes() ;
        for( int i=0, len=classes.length ; i<len ; ++i ) {
            buf.append( classes[i].getName() ) ;
            if( i != len-1 ) buf.append( "," ) ; }
        buf.append( ">" ) ;
        return buf.toString() ;
    }

    static String objectToStringShort( Object obj ) {
        if( obj == null ) {
            return "null" ; }
        else if( obj instanceof Boolean ) {
            return ((Boolean) obj).toString() ; }
        else if( obj instanceof Byte ) {
            return ((Byte) obj).toString() ; }
        else if( obj instanceof Character ) {
            return ((Character) obj).toString() ; }
        else if( obj instanceof Double ) {
            return ((Double)obj).toString() ; }
        else if( obj instanceof Float ) {
            return ((Float)obj).toString() ; }
        else if( obj instanceof Integer ) {
            return ((Integer)obj).toString() ; }
        else if( obj instanceof Long ) {
            return ((Long)obj).toString() ; }
        else if( obj instanceof Short ) {
            return ((Short)obj).toString() ; }
        else if( obj instanceof String ) {
            String str = (String) obj ;
            return format_and_shorten( str ) ; }
        else if( obj.getClass().isArray() ) {
            try {
                int len = Array.getLength( obj ) ;
                StringBuffer buf = new StringBuffer() ;
                buf.append("{" ) ;
                for( int i=0 ; i < Math.min(3, len) ; ++i ) {
                    Object item = Array.get( obj, i ) ;
                    buf.append( objectToStringShort( item ) ) ;
                    if( i != len-1 ) { buf.append( "," ) ; } }
                if( len > 3 ) { buf.append( ".." ) ; }
                buf.append( "}" ) ;
                return buf.toString() ; }
            catch( IllegalArgumentException e ) {
                Assert.check( false, "Internal error" ) ;
                return null ; }
            catch( ArrayIndexOutOfBoundsException e ) {
                Assert.check( false, "Internal error" ) ;
                return null ; } }
        else {
            String str = obj.toString() ;
            return "Obj"+format_and_shorten( str ) ; }
    }

    static String format_and_shorten( String str ) {
        int length = str.length() ;
        if( length > 30 ) {
            int hashVal = stringHash( str, length ) ;
            String firstPart = str.substring(0, 15 ) ;
            String lastPart = str.substring(length-15,length ) ;
            return formatString( firstPart )
                 +"+..+"
                 + formatString(lastPart)
                 + Integer.toHexString(hashVal) ; }
        else {
                return formatString( str ) ; }
    }

    static int stringHash( String str, int length ) {
        int hashVal = 0 ;
        for( int i = 0 ; i<length ; ++i ) {
            short b = (short) str.charAt( i ) ;
            byte msbit = (byte) (hashVal>>>31) ;
            hashVal = (hashVal<<1) ^ b ^ msbit ; }
        return hashVal ; }

    static String objectToStringFull( Object obj )
    throws JSnoopyException {
        if( obj == null ) {
            return "null" ; }
        else if( obj instanceof Boolean ) {
            return ((Boolean) obj).toString() ; }
        else if( obj instanceof Byte ) {
            return "byte "+((Byte) obj).toString() ; }
        else if( obj instanceof Character ) {
            return "char "+formatString( ((Character) obj).toString() ); }
        else if( obj instanceof Double ) {
            return "double "+((Double)obj).toString() ; }
        else if( obj instanceof Float ) {
            return "float "+((Float)obj).toString() ; }
        else if( obj instanceof Integer ) {
            return ((Integer)obj).toString() ; }
        else if( obj instanceof Long ) {
            return "long "+((Long)obj).toString() ; }
        else if( obj instanceof Short ) {
            return "short "+((Short)obj).toString() ; }
        else if( obj instanceof String ) {
            return formatString( (String)obj ) ; }
        else if( obj.getClass().isArray() ) {
            try {
                int len = Array.getLength( obj ) ;
                StringBuffer buf = new StringBuffer() ;
                buf.append("{" ) ;
                for( int i=0 ; i < len ; ++i ) {
                    Object item = Array.get( obj, i ) ;
                    buf.append( objectToStringFull( item ) ) ;
                    if( i != len-1 ) { buf.append( "," ) ; } }
                buf.append( "}" ) ;
                buf.append( obj.getClass().getComponentType().getName() );
                return buf.toString() ; }
            catch( IllegalArgumentException e ) {
                Assert.check( false, "Internal error" ) ;
                return null ; }
            catch( ArrayIndexOutOfBoundsException e ) {
                Assert.check( false, "Internal error" ) ;
                return null ; } }
        else {
            throw new JSnoopyException( "Sorry objects of type "+
                obj.getClass().getName() +
                " can not (yet) appear in primary calls" ) ;
            }
    }

    static private char toHex( int i ) {
        if( 0 <= i && i <= 9 )  return (char)( (int)'0' + i ) ;
        else                    return (char)( (int)'a' + i - 10 ) ;
    }

    public static String unformatString( String str ) {
        StringBuffer buf = new StringBuffer() ;

        int i = 1 ;
        int sz = str.length() - 1 ;
        while( i < sz ) {
            char[] charResult = new char[1] ;
            int[]  lenResult = new int[1] ;
            charToAscii( str, i, charResult, lenResult ) ;
            buf.append( charResult[0] ) ;
            i += lenResult[0] ; }
        return buf.toString() ;
    }

    static private void charToAscii( String image, int start, char[] charResult, int[] lenResult ) {
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
            case '\\' :
                ascii = 92 ;
                len = 2 ;
                break ;
            case '"' :
                ascii = 34 ;
                len = 2 ;
                break ;
            case 'u' : case 'U' :
                // Unicode
                ascii = 0 ;
                len = 2 ;
                for( int i=0 ; i < 4 && start+len < image.length(); ++i ) {
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
                default :
                        // Anything else is a lexical error.
                        // and should already have been caught.
                        len = ascii = 0 ; /*Make compiler happy*/} }

        charResult[0] = (char) ascii ;
        lenResult[0] = len ; }
}