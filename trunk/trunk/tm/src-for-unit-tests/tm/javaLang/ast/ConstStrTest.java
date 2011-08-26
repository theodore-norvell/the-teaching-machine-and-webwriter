package tm.javaLang.ast;

import tm.backtrack.BTTimeManager;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.ViewableST;
import tm.javaLang.datum.*;
import tm.virtualMachine.*;
import tm.virtualMachine.ExpressionEvaluation;

import java.util.Vector;

import tm.clc.analysis.ScopedName;
import tm.clc.ast.Java_AbstractAstTest;
import tm.clc.ast.VarNode;

/*
COPYRIGHT (C) 1997-2002 by Michael Bruce-Lockhart and Theodore S. Norvell.
The associated software is released to students for educational purposes only
and only for the duration of the course in which it is handed out. No other use
of the software, either commercial or non-commercial, may be made without the
express permission of the author.
*/

/*******************************************************************************
Class: ConstStrTest

Overview:
Tests Java string contants.
*******************************************************************************/

public class ConstStrTest extends Java_AbstractAstTest {
    ConstStr testCs;

    public ConstStrTest () { this ("ConstStrTest"); }

    public ConstStrTest (String name) {
        super (name);
        print=true;
    }

    public void setUp() { super.setUp(); }

    public void testCheckNodeLinks() {
        ExpressionEvaluation ee = evaluate();
        assertTrue(ee.isDone());
        assertTrue(ee.getRoot() != null);

        assertTrue(ee.at(testCs) instanceof PointerDatum);
        PointerDatum stringPtr = (PointerDatum) ee.at(testCs);

        assertTrue(stringPtr.deref() instanceof ObjectDatum);
        ObjectDatum stringDatum = (ObjectDatum) stringPtr.deref();

        assertTrue(stringDatum.getField(0) instanceof PointerDatum);
        PointerDatum arrayPtr = (PointerDatum) stringDatum.getField(0);

        assertTrue(arrayPtr.deref() instanceof ArrayDatum);
        ArrayDatum arrayDatum = (ArrayDatum) arrayPtr.deref();

        IntDatum length = arrayDatum.getLength() ;
        assertTrue( length.getValue() == 10 ) ;
        assertTrue(arrayDatum.getElement(0) instanceof CharDatum);
        CharDatum charDatum = (CharDatum) arrayDatum.getElement(0) ;
        assertEquals( (long)'t', charDatum.getValue() ) ;
        charDatum = (CharDatum) arrayDatum.getElement(9) ;
        assertEquals( (long)'g', charDatum.getValue() ) ;
    }

    public ExpressionEvaluation evaluate() {
        BTTimeManager btm = new BTTimeManager();
        ViewableST vst = new RT_Symbol_Table(btm);
        vms = new VMState(btm, 0, 1999, 2000, 3999, 4000, 5999, 6000, 7999, vst);
        JavaLangASTUtilities utilities = new JavaLangASTUtilities() ;
        TyClass objectClass = makeStubObjectClass() ;
        TyClass stringClass = makeStubStringClass(objectClass) ;
        utilities.setObjectClass( objectClass ) ;
        utilities.setStringClass( stringClass ) ;
        vms.setProperty("ASTUtilities", utilities);
        String testString = "testString" ;
        testCs = new ConstStr( stringClass, "testString", testString, vms);
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, testCs);
        ee.setSelected(testCs);
        vms.push(ee);

        advance(100);
        return ee;
    }

    private TyClass makeStubObjectClass() {
        ScopedName nm = new tm.javaLang.analysis.Java_ScopedName("Object") ;
        ScopedName fqn = new tm.javaLang.analysis.Java_ScopedName("java.lang.Object") ;
        TyClass tyClass = new TyClass(nm, fqn, null ) ;
        tyClass.setDefined() ;
        return tyClass ;
    }

    
    private TyClass makeStubStringClass(TyClass objectClass) {
        // We need a pointer to an array of characters.
        TyJavaArray tyArray = new TyJavaArray("", objectClass) ;
        tyArray.addToEnd( TyChar.get() ) ;
        TyPointer tyPAC = new TyPointer( tyArray ) ;
        
        // Var node for the field
        ScopedName fName = new tm.javaLang.analysis.Java_ScopedName("java.lang.String.myString") ;
        VarNode varNode = new VarNode(fName, tyPAC) ;
        
        // Make the class
        ScopedName nm = new tm.javaLang.analysis.Java_ScopedName("String") ;
        ScopedName fqn = new tm.javaLang.analysis.Java_ScopedName("java.lang.String") ;
        TyClass tyClass = new TyClass(nm, fqn, null ) ;
        
        // Add superclass
        tyClass.addSuperClass( objectClass ) ;
        
        // Add the field
        tyClass.addField( varNode ) ;
        
        // return
        tyClass.setDefined() ;
        return tyClass ;
    }
}