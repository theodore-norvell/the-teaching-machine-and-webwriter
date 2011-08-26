package tm.javaLang.ast;

import tm.clc.ast.Java_AbstractAstTest;
import junit.framework.*;

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.clc.datum.*;
import tm.interfaces.ViewableST;
import tm.javaLang.datum.*;
import tm.languageInterface.Language;
import tm.languageInterface.NodeInterface;
import tm.virtualMachine.*;

/*
COPYRIGHT (C) 1997-2002 by Michael Bruce-Lockhart and Theodore S. Norvell.
The associated software is released to students for educational purposes only
and only for the duration of the course in which it is handed out. No other use
of the software, either commercial or non-commercial, may be made without the
express permission of the author.
*/

/*******************************************************************************
Class: ExpNewTest

Overview:
Tests the Java new operator.
*******************************************************************************/

public class ExpNewTest extends Java_AbstractAstTest {
    ScopedName name_q;
    TyInt tyInt;
    TyPointer tyPtr;
    TyRef tyRefPtr;

    public ExpNewTest () { this ("ExpNewTest"); }

    public ExpNewTest (String name) {
        super(name);
        print = true;
    }

    public void setUp() { super.setUp(); }

    public void testNoInitializers() {
        // As in "new testClass()"
        NodeList args = new NodeList();
        runTest(args);
    }

    public void testWithIntitArgs() {
        // As in "new testClass(1,2)"
        NodeList args = new NodeList();
        TypeNode tyInt = TyInt.get() ;
        Node oneNode = new ConstInt(tyInt, "1", 1) ;
        Node twoNode = new ConstInt(tyInt, "2", 2) ;
        args.addFirstChild(oneNode);
        args.addFirstChild(twoNode);

        runTest(args);
    }

    public void runTest(NodeList args) {
        ScopedName fqn = new tm.javaLang.analysis.Java_ScopedName("testClass");
        TyClass type = new TyClass(fqn, fqn, null) ;
        type.setDefined() ;
        TyPointer tp = new TyPointer();
        TyRef tyRef = new TyRef( type ) ;
        tp.addToEnd(type);

        ExpArgument init = new ExpArgument( tyRef, 0 ) ;
        ExpressionNode exp = new ExpNew(tp, args, init);
        // exp = ExpNew(pointer to testClass)[args, ExpArg(0)]
        ExpressionEvaluation ee = new ExpressionEvaluation(vms, exp);

        vms.push(ee);
        advance(100);

        assertTrue(ee.isDone());
        assertTrue(ee.getRoot() != null);

        assertTrue(ee.at(exp) instanceof PointerDatum);
        PointerDatum thePointer = (PointerDatum) ee.at(exp);

        assertTrue(thePointer.deref() != null);
        assertTrue(thePointer.deref() instanceof ObjectDatum);
    }
}