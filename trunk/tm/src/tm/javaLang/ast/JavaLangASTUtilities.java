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

import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.clc.analysis.ScopedName;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.datum.*;
import tm.javaLang.analysis.Java_ScopedName;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: JavaLangASTUtilities

Overview:
A bunch of functions useful to AST components.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class JavaLangASTUtilities extends Clc_ASTUtilities {

    public final TyBoolean boolType = TyBoolean.get ();
    public final TyChar charType = TyChar.get ();
    public final TyDouble doubleType = TyDouble.get ();
    public final TyInt intType = TyInt.get ();
    public final TyLong longType = TyLong.get ();
    private TyClass stringClass ;
    private TyClass objectClass ;
    
    public JavaLangASTUtilities( ) {
    }
    

    /**
     * @return Returns a TyClass representing java.lang.Object
     */
    public TyClass getObjectClass() {
        Assert.check( objectClass != null ) ;
        return objectClass; }

    /**
     * @param objectClass The objectClass to set.
     */
    public void setObjectClass(TyClass objectClass) {
        this.objectClass = objectClass;
    }


    /**
     * @return Returns a TyClass representing the class java.lang.String
     */
    public TyClass getStringClass() {
        return stringClass;
    }


    /**
     * @param stringClass The stringClass to set.
     */
    public void setStringClass(TyClass stringClass) {
        Assert.check( stringClass != null ) ;
        this.stringClass = stringClass; }


    public TypeNode getIntType() { return intType;  }
    public TypeNode getBooleanType() { return boolType; }
    public TypeNode getFloatingType() { return doubleType; }
    public TyAbstractRef getRefType(TypeNode target_type) {
        return new TyRef(target_type);
    }
    public TypeNode getVoidType() { return null; }

    /** Do this one later too.
    public void convertRef(VMState vms, ExpressionNode nd, RefDatum ref) {
        AbstractDatum varDatum = (AbstractDatum) ref.deref();

        varDatum.putHighlight(Datum.PLAIN);
        vms.top().map(nd, varDatum);
    }
 ***/

    public ObjectDatum makeStringObject(String value, VMState vms ) {
        // Set up the relevant types
            TyPointer tp = new TyPointer(); // Pointer to an array of chars
            TyJavaArray ta = new TyJavaArray("", getObjectClass() ) ;
            tp.addToEnd(ta);
            ta.addToEnd(TyChar.get());

        // Make an image for display purposes
            String image = unparse( value ) ;
        // Make the datums
            ArrayDatum theArray = ta.makeArrayDatum(vms, value.length());
            ObjectDatum stringObject = (ObjectDatum) getStringClass().makeDatum(vms, vms.getStore().getHeap(), image);
            // Note! The name on the next field should match the declaration
            // in the string class in the library
            ScopedName pointerFieldName = new Java_ScopedName( "java.lang.String.myString" ) ;
            PointerDatum field = (PointerDatum) stringObject.getFieldByName( pointerFieldName ) ;
            field.putValue( theArray );


        // Fill the array with characters.
            for(int i = 0; i < value.length(); i++) {
                CharDatum cd = (CharDatum) theArray.getElement(i) ;
                char ch = value.charAt(i) ;
                cd.putValue( (long) ch );
        }

        return stringObject ;
    }

    /** Refence type is assignable to another reference type.
     * Used for run time checks in
     * <ul> <li> Checking a cast: (toType) fromExp
     *      <li> Catching exceptions: catch( toType param ) { ... }
     *      <li> instaceof: fromExp instanceof toType
     * </ul>
     */

    static boolean assignableReferenceType( TyJava fromType, TyJava toType ) {
        return fromType.equal_types( toType ) || fromType.isReachableByWideningFrom( toType ) ; }

    private static String unparse( String str ) {
        final int CUTOFF = 5 ;
        StringBuffer buf = new StringBuffer() ;
        int len = str.length() ;
        if( len > CUTOFF ) len =  CUTOFF-2 ;
        buf.append('"') ;
        for( int i = 0 ; i < len ; ++i ) {
            char ch = str.charAt(i) ;
            if( ch == '\\' || ch == '"' ) {
                buf.append( '\\' ) ; buf.append( ch ) ; }
            else if( 32 <= ' ' && ch <= '~' ) {
                // printable ASCII other than " or \
                buf.append( ch ) ; }
            else {
                switch( ch ) {
                case '\n' : buf.append( "\\n") ;
                break ;
                case '\r' :  buf.append( "\\r") ;
                break ;
                case '\t' :  buf.append( "\\t") ;
                break ;
                default: {
                    int chi = ch ;
                    int h0 = chi % 16 ;
                    chi = chi / 16 ;
                    int h1 = chi % 16 ;
                    chi = chi / 16 ;
                    int h2 = chi % 16 ;
                    chi = chi / 16 ;
                    int h3 = chi ;
                    buf.append( "\\u") ;
                    buf.append( (char)('A'+h3)) ;
                    buf.append( (char)('A'+h2)) ;
                    buf.append( (char)('A'+h1)) ;
                    buf.append( (char)('A'+h0)) ; }
                }
            }
        }
        if( len < str.length() ) buf.append("..") ;
        buf.append( '"' ) ;
        return buf.toString() ;
    }

    public boolean isString( AbstractDatum d ) {
        if( d instanceof PointerDatum ) {
            PointerDatum ptr = (PointerDatum) d ;
            TypeInterface pointeeType = ptr.getPointeeType() ;
            if( pointeeType instanceof TyAbstractClassDeclared ) {
                TyAbstractClassDeclared tac = (TyAbstractClassDeclared) pointeeType;
                    return tac.getFullyQualifiedName().equals( new Java_ScopedName("java.lang.String")) ; }
            else
                return false; }
        else return false ;
    }

    @Override
    public Class getNativeClass( AbstractDatum d ) {
        if( isString(d) ) {
            return "".getClass() ; }
        else {
            return d.getNativeClass() ; } }


    @Override
    public Object getNativeValue( AbstractDatum d, VMState vms) {
        if( isString(d) ) {
            PointerDatum ptr = (PointerDatum) d ;
            if( ptr.isNull() ) {
                return null ; }
            
            else {
                ObjectDatum pointee = (ObjectDatum) ptr.deref() ;
                Assert.check( pointee != null, "java pointer has bad value") ;
                
                ArrayDatum StringArrayDatum = pointee.getStringArrayDatum();
                int size = (int) StringArrayDatum.getNumberOfElements();
                char[] stringData = new char[size];
                for (int i = 0; i < size; i++)
                    stringData[i] = (char) ((CharDatum) StringArrayDatum.getElement(i))
                            .getValue();
                return new java.lang.String(stringData); } }
        else {
            return d.getNativeValue() ; }
    }


    @Override
    public void putNativeValue( AbstractDatum d, Object nativeResult, VMState vms ) {
        // Give it a value
        if( isString( d ) ) {
            PointerDatum ptr = (PointerDatum) d ;
            if( nativeResult == null ) {
                ptr.putValue(0) ; }
            else {
                Assert.check( nativeResult instanceof String ) ;
                JavaLangASTUtilities util
                    = (JavaLangASTUtilities) vms.getProperty("ASTUtilities") ;
                ObjectDatum stringDatum = util.makeStringObject( (String) nativeResult, vms ) ; 
                ptr.putValue( stringDatum ) ; } }
        else { 
            d.putNativeValue( nativeResult) ; }
    }
}