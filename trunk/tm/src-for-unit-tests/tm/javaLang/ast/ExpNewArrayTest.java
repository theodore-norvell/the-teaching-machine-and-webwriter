package tm.javaLang.ast;

import java.util.Vector;

import tm.clc.analysis.ScopedName;
import tm.clc.ast.Java_AbstractAstTest;
import tm.clc.ast.*;
import tm.clc.datum.*;
import tm.interfaces.Datum;
import tm.javaLang.ast.*;
import tm.javaLang.datum.*;
import tm.virtualMachine.*;

/*
COPYRIGHT (C) 1997-2002 by Michael Bruce-Lockhart and Theodore S. Norvell.
The associated software is released to students for educational purposes only
and only for the duration of the course in which it is handed out. No other use
of the software, either commercial or non-commercial, may be made without the
express permission of the author.
*/

/*******************************************************************************
Class: ExpNewArrayTest

Overview:
Tests the Java new operator (for arrays).
*******************************************************************************/

public class ExpNewArrayTest extends Java_AbstractAstTest{
    public ExpNewArrayTest () { this ("ExpNewTest"); }

    public ExpNewArrayTest (String name) {
        super(name);
        print = true;
    }

    public void setUp() { super.setUp(); }

    public void testSingleDimensionalArray() {
        // Set up array type
        TyClass objectClass = makeStubObjectClass() ;
        TyJavaArray arrayType = new TyJavaArray("", objectClass);
        TyChar charType = TyChar.get();
        arrayType.addToEnd(charType);

        // Set up NodeList
        ConstInt dim = new ConstInt(TyInt.get(), "3", 3);
        NodeList dimensions = new NodeList();
        dimensions.addLastChild(dim);

        TyPointer tp = new TyPointer();
        tp.addToEnd(arrayType);
        ExpNewArray exp = new ExpNewArray(tp, dimensions);
        PointerDatum thePointer = runTest(exp);

        assertTrue(thePointer.deref() != null);
        assertTrue(thePointer.deref() instanceof ArrayDatum);

        ArrayDatum theArray = (ArrayDatum) thePointer.deref();

        assertTrue(theArray.getLength().getValue() == 3);

        for(int i = 0; i < 3; i++) {
            assertTrue(theArray.getElement(i) instanceof CharDatum);
        }
    }

    public void testMultiDimensionalArray() {
        // Set up array type
        TyClass objectClass = makeStubObjectClass() ;
        TyJavaArray tax = new TyJavaArray("", objectClass);
        TyJavaArray tay = new TyJavaArray("", objectClass);
        TyJavaArray taz = new TyJavaArray("", objectClass);

        TyPointer tpx = new TyPointer();
        TyPointer tpy = new TyPointer();

        TyInt intType = TyInt.get();

        taz.addToEnd(intType);
        tpy.addToEnd(taz);
        tay.addToEnd(tpy);
        tpx.addToEnd(tay);
        tax.addToEnd(tpx);

        // Set up NodeList
        NodeList dimensions = new NodeList();

        ConstInt x = new ConstInt(TyInt.get(), "3", 3);
        ConstInt y = new ConstInt(TyInt.get(), "4", 4);
        ConstInt z = new ConstInt(TyInt.get(), "5", 5);
        dimensions.addLastChild(x);
        dimensions.addLastChild(y);
        dimensions.addLastChild(z);

        TyPointer tp = new TyPointer();
        tp.addToEnd(tax);
        ExpNewArray exp = new ExpNewArray(tp, dimensions);
        PointerDatum thePointer = runTest(exp);

        assertTrue(thePointer.deref() != null);
        assertTrue(thePointer.deref() instanceof ArrayDatum);

        ArrayDatum ax = (ArrayDatum) thePointer.deref();
        assertEquals( 3,  ((IntDatum)ax.getLength()).getValue() ) ;
        for(int i = 0; i < 3; i++) {
            assertTrue(ax.getElement(i) instanceof PointerDatum);
            PointerDatum px = (PointerDatum) ax.getElement(i);
            assertTrue(px.deref() instanceof ArrayDatum);
            ArrayDatum ay = (ArrayDatum) px.deref();
            assertEquals( 4, ((IntDatum)ay.getLength()).getValue() ) ;
            for (int j = 0; j < 4; j++) {
                assertTrue(ay.getElement(j) instanceof PointerDatum);
                PointerDatum py = (PointerDatum) ay.getElement(j);
                assertTrue(py.deref() instanceof ArrayDatum);
                ArrayDatum az = (ArrayDatum) py.deref();
                assertEquals( 5, ((IntDatum)az.getLength()).getValue() ) ;
                for (int k = 0; k < 5; k++) {
                    assertTrue(az.getElement(k) instanceof IntDatum);
                    IntDatum id = (IntDatum) az.getElement(k) ;
                    assertTrue( id.getValue() == 0 ) ;
                }
            }
        }
    }
    
    public void testMultiDimensionalArrayWithUnfilledDimensions() {
        // Set up array type
        TyClass objectClass = makeStubObjectClass() ;
        TyJavaArray tax = new TyJavaArray("", objectClass);
        TyJavaArray tay = new TyJavaArray("", objectClass);
        TyJavaArray taz = new TyJavaArray("", objectClass);

        TyPointer tpx = new TyPointer();
        TyPointer tpy = new TyPointer();

        TyInt intType = TyInt.get();

        taz.addToEnd(intType);
        tpy.addToEnd(taz);
        tay.addToEnd(tpy);
        tpx.addToEnd(tay);
        tax.addToEnd(tpx);

        // Set up NodeList
        NodeList dimensions = new NodeList();

        ConstInt x = new ConstInt(TyInt.get(), "3", 3);
        ConstInt y = new ConstInt(TyInt.get(), "4", 4);
        dimensions.addLastChild(x);
        dimensions.addLastChild(y);

        TyPointer tp = new TyPointer();
        tp.addToEnd(tax);
        ExpNewArray exp = new ExpNewArray(tp, dimensions);
        PointerDatum thePointer = runTest(exp);

        assertTrue(thePointer.deref() != null);
        assertTrue(thePointer.deref() instanceof ArrayDatum);

        ArrayDatum ax = (ArrayDatum) thePointer.deref();
        assertEquals( 3,  ((IntDatum)ax.getLength()).getValue() ) ;
        for(int i = 0; i < 3; i++) {
            assertTrue(ax.getElement(i) instanceof PointerDatum);
            PointerDatum px = (PointerDatum) ax.getElement(i);
            assertTrue(px.deref() instanceof ArrayDatum);
            ArrayDatum ay = (ArrayDatum) px.deref();
            assertEquals( 4, ((IntDatum)ay.getLength()).getValue() ) ;
            for (int j = 0; j < 4; j++) {
                assertTrue(ay.getElement(j) instanceof PointerDatum);
                PointerDatum py = (PointerDatum) ay.getElement(j);
                assertTrue(py.isNull());
            }
        }
    }
    
    public void testInitializerRectangular() {
        // Set up array type
        TyClass objectClass = makeStubObjectClass() ;
        TyJavaArray tax = new TyJavaArray("", objectClass);
        TyJavaArray tay = new TyJavaArray("", objectClass);
        TyJavaArray taz = new TyJavaArray("", objectClass);
        
        TyPointer tpx = new TyPointer();
        TyPointer tpy = new TyPointer();
        
        TyInt intType = TyInt.get();
        
        taz.addToEnd(intType);
        tpy.addToEnd(taz);
        tay.addToEnd(tpy);
        tpx.addToEnd(tay);
        tax.addToEnd(tpx);
        
        // Set up Vector representing the array initializer
        // This one is a classic 3D rectangular.
        Vector init0 = new Vector() ;
        int count = 0 ;
        for( int i=0 ; i<3 ; ++i ) {
            Vector init1 = new Vector() ;
            init0.addElement( init1 ) ;
            for( int j=0 ; j<4 ; ++j  ) {
                Vector init2 = new Vector() ;
                init1.addElement( init2 ) ;
                for( int k = 0 ; k < 5  ; ++k ) {
                    ConstInt x = new ConstInt( TyInt.get(), ""+count, count) ;
                    count++ ;
                    init2.addElement( x ) ;
                }
            }
        }
        
        TyPointer tp = new TyPointer();
        tp.addToEnd(tax);
        ExpNewArray exp = new ExpNewArray(tp, init0);
        PointerDatum thePointer = runTest(exp);
        
        count = 0 ;
        ArrayDatum ax = (ArrayDatum) thePointer.deref();
        assertEquals( 3,  ((IntDatum)ax.getLength()).getValue() ) ;
        for(int i = 0; i < 3; i++) {
            assertTrue(ax.getElement(i) instanceof PointerDatum);
            PointerDatum px = (PointerDatum) ax.getElement(i);
            assertTrue(px.deref() instanceof ArrayDatum);
            ArrayDatum ay = (ArrayDatum) px.deref();
            assertEquals( 4, ((IntDatum)ay.getLength()).getValue() ) ;
            for (int j = 0; j < 4; j++) {
                assertTrue(ay.getElement(j) instanceof PointerDatum);
                PointerDatum py = (PointerDatum) ay.getElement(j);
                assertTrue(py.deref() instanceof ArrayDatum);
                ArrayDatum az = (ArrayDatum) py.deref();
                assertEquals( 5, ((IntDatum)az.getLength()).getValue() ) ;
                for (int k = 0; k < 5; k++) {
                    assertTrue(az.getElement(k) instanceof IntDatum);
                    IntDatum id = (IntDatum) az.getElement(k) ;
                    assertTrue( id.getValue() == count ) ;
                    count++ ;
                }
            }
        }
    }

    private PointerDatum runTest(ExpNewArray exp) {

        ExpressionEvaluation ee = new ExpressionEvaluation(vms, exp);

        vms.push(ee);
        advance(200);

        PointerDatum thePointer = (PointerDatum) ee.at(exp);

        assertTrue(ee.isDone());
        assertTrue(ee.getRoot() != null);

        assertTrue(ee.at(exp) instanceof PointerDatum);

        return thePointer;
    }
    
    private TyClass makeStubObjectClass() {
        ScopedName nm = new tm.javaLang.analysis.Java_ScopedName("Object") ;
        ScopedName fqn = new tm.javaLang.analysis.Java_ScopedName("java.lang.Object") ;
        TyClass tyClass = new TyClass(nm, fqn, null ) ;
        tyClass.setDefined() ;
        return tyClass ;
    }

}