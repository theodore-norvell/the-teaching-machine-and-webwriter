package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.cpp.ast.*;
import tm.cpp.parser.ParserConstants ;
import tm.clc.datum.* ;
import tm.cpp.datum.* ;
import tm.utilities.Debug;
import tm.utilities.ApologyException;
import tm.virtualMachine.VMState;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.backtrack.BTTimeManager;
import junit.framework.*;
import java.util.*;

import tm.clc.analysis.AnalysisTestCase;

/**
 * base class for expression builder tests
 */
public abstract class ExpressionBuilderTest extends AnalysisTestCase
        implements tm.cpp.analysis.TestConstantUser, ParserConstants, FundamentalTypeUser, Cpp_Specifiers, Cpp_LFlags {

        TypeNode tyPointer, tyClass, tyFun, tyRefInt, ctyRefInt, tyPointerClass;
        TyArray tyArray, tyArrayChar;
        ExpressionNode five, one, x, ffive, fone, y, p, a, c, b, _true, foo,
                foox, ac, rint, crint, bar, pc;
        ScopedName x_sn, y_sn, r_sn, c_sn, sn_C, foo_sn, bar_sn, pc_sn;
        ConstStr str_const;
        ExpressionNode exp, operand;

        ExpressionBuilder eb;

    protected static final int boStatic = 0 ;
    protected static final int toStatic = 999 ;
        protected static final int boHeap = toStatic+1 ;
        protected static final int toHeap = boHeap+1000-1 ;
        protected static final int boStack = toHeap+1 ;
        protected static final int toStack = boStack+1000-1 ;
        protected static final int boScratch = toStack+1 ;
        protected static final int toScratch = boScratch+1000-1 ;


        VMState vms;
    protected RT_Symbol_Table rt_symtab ;

        static Cpp_CTSymbolTable st = new Cpp_CTSymbolTable ();
        static { st.enterFileScope (); }
        static Cpp_ExpressionManager cem = new Cpp_ExpressionManager (st);
        Cpp_DeclarationManager dm;


        public ExpressionBuilderTest (String name) {
                super (name);
        }

        protected void setUp () {
//                d.on ();


        // Create a time manager to synchronize back tracking
                BTTimeManager timeMan = new BTTimeManager() ;

        // Create the symbol table
                rt_symtab = new RT_Symbol_Table( timeMan ) ;

                // Create a virtual machine state .
                vms = new VMState(  timeMan,
                                    boStatic, toStatic, boHeap, toHeap,
                                    boStack, toStack, boScratch, toScratch,
                                    rt_symtab ) ;

                Cpp_StatementManager sm = new Cpp_StatementManager() ;
                if (dm == null) dm = new Cpp_DeclarationManager (cem, sm, vms);

                // Prepare the VMState
                vms.setProperty("ASTUtilities", new Cpp_ASTUtilities() ) ;
                vms.setProperty("DatumUtilities", new DatumUtilities() ) ;



                // create the expression for 5
                five = new ConstInt (ctyInt, "5", 5);

                // create the expression for 1
                one = new ConstInt (ctyInt, "1", 1);

                // create the expression for x
                String xid = "x";
                x_sn = new Cpp_ScopedName (xid);
                x = new ExpId (new TyRef (tyInt), xid, x_sn);

                // create the expression for 5.0
                ffive = new ConstFloat (ctyFloat, "5.0", 5.0);

                // create the expression for 1.0
                fone = new ConstFloat (ctyFloat, "1.0", 1.0);

                // create the expression for y
                String yid = "y";
                y_sn = new Cpp_ScopedName (yid);
                y = new ExpId (new TyRef (tyFloat), yid, y_sn);

                tyPointer = new TyPointer (); tyPointer.addToEnd (tyInt);
                // create the expression for p
                String pid = "p";
                p = new ExpId (new TyRef (tyPointer), pid, new Cpp_ScopedName (pid));

                tyArray = new TyArray (); tyArray.addToEnd (tyInt);
                // create the expression for a
                String aid = "a";
                a = new ExpId (new TyRef (tyArray), aid, new Cpp_ScopedName (aid));

                String idC = "C";
                sn_C = new Cpp_ScopedName (idC);
                tyClass = new TyClass (idC, sn_C, null);
                // create the expression for c
                String cid = "c";
                c_sn = new Cpp_ScopedName (cid);
                c = new ExpId (new TyRef (tyClass), cid, c_sn);

                String bid = "b";
                b = new ExpId (new TyRef (tyBool), bid, new Cpp_ScopedName (bid));

                _true = new ConstInt (tyBool, "true", 1);

                // create a string constant
                str_const = (ConstStr) Literals.make_string_const ("\"blah\"", vms);
                // array of char
                tyArrayChar = new TyArray (); tyArrayChar.addToEnd (tyChar);
                // create the expression for a
                String acid = "ac";
                ac = new ExpId (new TyRef (tyArrayChar), acid, new Cpp_ScopedName (acid));

                // reference to int
                tyRefInt = new TyRef (tyInt);

                // create the id expression for rint
                String riid = "ri";
                r_sn = new Cpp_ScopedName (riid);
                rint = new ExpId (new TyRef (tyRefInt), riid, r_sn);

                // constant reference to int
                ctyRefInt = new TyRef (ctyInt);
                ctyRefInt.setAttributes (Cpp_Specifiers.CVQ_CONST);

                // create the id expression for rint
                String criid = "cri";
                crint = new ExpId (new TyRef (ctyRefInt), criid, new Cpp_ScopedName (criid));

                // function taking no parameters and returning int
                tyFun = new TyFun (new Vector (), false);
                tyFun.addToEnd (tyInt);

                // create the following scope and declaration space
                // int x;
                // int &r;
                // int foo ();
                // class C {
                // 	float y;
                // 	int bar ();
                // };
                // C c;
                // C *pc;

                Cpp_SpecifierSet sps = new Cpp_SpecifierSet ();
                NodeList nl = new NodeList ();
                Declaration decl;

                // int x
                decl = new Declaration (VARIABLE_LF, x_sn, null, sps, tyInt);
                decl.setDefinition (decl);
                decl.getCategory ().set
                        (dm.type_extractor.categorizeType (tyInt));
                st.addDeclaration (decl);

                // int &r
                decl = new Declaration (VARIABLE_LF, r_sn, null, sps, tyRefInt);
                decl.setDefinition (decl);
                decl.getCategory ().set
                        (dm.type_extractor.categorizeType (tyRefInt));
                st.addDeclaration (decl);

                // int foo ()
                foo_sn = new Cpp_ScopedName ("foo");
                st.addDefiningDeclaration (foo_sn, REG_FN_LF, tyFun);

                // class C
                ClassHead chc =
                        new ClassHead (ClassHead.CLASS, sn_C, new Vector ());
                Declaration c_decl = st.addDefiningDeclaration (chc);

                st.enterScope (c_decl);

                // float y
                decl = new Declaration (VARIABLE_LF, y_sn, null, sps, tyFloat);
                decl.setDefinition (decl);
                decl.getCategory ().set
                        (dm.type_extractor.categorizeType (tyFloat));
                st.addDeclaration (decl);

                // int bar ()
                bar_sn = new Cpp_ScopedName ("bar");
                st.addDefiningDeclaration (bar_sn, MEM_FN_LF, tyFun);


                st.exitScope ();

                // C c
                decl = new Declaration (VARIABLE_LF, c_sn, null, sps, tyClass);
                decl.setDefinition (c_decl);
                decl.getCategory ().set
                        (dm.type_extractor.categorizeClass (decl));
                st.addDeclaration (decl);

                tyPointerClass = new TyPointer (); tyPointerClass.addToEnd (tyClass);
                // C *pc
                String pcid = "pc";
                pc_sn = new Cpp_ScopedName (pcid);
                pc = new ExpId (new TyRef (tyPointer), pcid, pc_sn);
                decl = new Declaration (VARIABLE_LF, pc_sn, null, sps, tyPointerClass);
                decl.setDefinition (decl);
                decl.getCategory ().set
                        (dm.type_extractor.categorizeType (tyPointerClass));
                st.addDeclaration (decl);

        }

        protected ExpressionNode applyExp (ScopedName op,
                                                                                Object l,
                                                                                Object r) {
                return this.applyExp (op, new Object [] {l, r});
        }
        protected ExpressionNode applyExp (ScopedName op,
                                                                                Object e) {
                return this.applyExp (op, new Object [] {e});
        }
        protected ExpressionNode applyExp (ScopedName op,
                                                                                Object [] operands) {
                ExpressionPtr exp = new ExpressionPtr (op, operands);
                eb.apply (exp);
                return exp.get ();
        }

        protected void applyBadExp (ScopedName op, Object l, Object r) {
                this.applyBadExp (op, new Object [] {l, r});
        }
        protected void applyBadExp (ScopedName op, Object e) {
                this.applyBadExp (op, new Object [] {e});
        }
        protected void applyBadExp (ScopedName op, Object [] operands) {
                ExpressionPtr exp = new ExpressionPtr (op, operands);
                try {
                        eb.apply (exp);
                        assertTrue (false);
                } catch (ApologyException ae) {
                        d.msg (Debug.COMPILE, EX_AP + ae.getMessage ());
                }
        }

        protected void validate (ExpressionNode e, Object expC, boolean types) {
                validate (e, expC, new Object [][] {}, types);
        }
        protected void validate (ExpressionNode e, Object expC,
                                                   Object opC, boolean types) {
                validate (e, expC, opC, (Object) null, types);
        }
        protected void validate (ExpressionNode e, Object expC,
                                                   Object op1C, Object op2C, boolean types) {
                validate (e, expC, new Object [] {op1C}, new Object [] {op2C},
                                  types);
        }
        protected void validate (ExpressionNode e, Object expC,
                                                   Object [] opC, boolean types) {
                validate (e, expC, opC, new Object [] {}, types);
        }
        protected void validate (ExpressionNode e, Object expC,
                                                   Object [] op1C, Object [] op2C,
                                                   boolean types) {
                validate (e, expC, new Object [][] {op1C, op2C}, types);
        }
        protected void validate (ExpressionNode e, Object expC,
                                                   Object [][] opCs, boolean types) {
                Object toCompare; // comparing types or expressions
                if (types) toCompare = e.get_type ();
                else toCompare = e.getClass ();

                // top level exp/type must match expC
                if (types) {
                        assertTrue (((TypeNode) toCompare).equal_types
                                                ((TypeNode) (expC)));
                } else assertTrue (toCompare.equals (expC));

                // look at operands
                for (int i = 0; i < opCs.length; i++) {
                        // looking at operand [i]
                        ExpressionNode currExp = e.child_exp (i);
                        for (int j = 0; j < opCs[i].length; j++) {

                                toCompare = (types)
                                        ? (Object) currExp.get_type ()
                                        : (Object) currExp.getClass ();

                                try {
                                        if (types) {
                                                assertTrue (((TypeNode) toCompare).equal_types
                                                                        ((TypeNode) (opCs [i][j])));
                                        } else assertTrue (toCompare.equals (opCs [i][j]));

                                } catch (AssertionFailedError afe) {
                                        String actual, expected;
                                        if (types) {
                                                actual = ((TypeNode) toCompare).typeId ();
                                                expected = ((TypeNode) opCs[i][j]).typeId ();
                                        } else {
                                                actual = ((Class) toCompare).getName ();
                                                expected = ((Class) opCs[i][j]).getName ();
                                        }
                                        d.msg (Debug.COMPILE, "Expecting " + expected + ", got " + actual);
                                        throw afe;
                                }

                                if (currExp.childCount () > 0)
                                        currExp = currExp.child_exp (0);
                                else break;
                        }
                }
        }

        protected void doValidate (ExpressionNode e, Expect et,
                                                           boolean types) {
                Object toCompare; // comparing types or expressions
                if (types) toCompare = e.get_type ();
                else toCompare = e.getClass ();

                boolean matched = false;
                try {
                        // top level exp/type must match expected
                        if (types) {
                                assertTrue (((TypeNode) toCompare).equal_types
                                                        ((TypeNode) (et.expected)));
                        } else assertTrue (toCompare.equals (et.expected));
                        matched = true;
                        // look at sub expressions
                        // subexpression counts must match OR we are testing less than is
                        // there
                        assertTrue (et.subCount () <= e.childCount ());
                } catch (AssertionFailedError afe) {
                        if (!matched) {
                                String actual, expected;
                                if (types) {
                                        actual = ((TypeNode) toCompare).typeId ();
                                        expected = ((TypeNode) et.expected).typeId ();
                                } else {
                                        actual = ((Class) toCompare).getName ();
                                        expected = ((Class) et.expected).getName ();
                                }
                                d.msg (Debug.COMPILE, "Expecting " + expected + ", got " + actual);
                        } else {
                                d.msg (Debug.COMPILE, "Have " + e.childCount () +
                                           " subexpressions, expected " + et.subCount ());
                        }
                        throw afe;
                }

                for (int i = 0; i < et.subCount (); i++) {
                        ExpressionNode currExp = e.child_exp (i);
                        Expect currC = et.sub (i);
                        doValidate (currExp, currC, types);
                }


        }

        public class Expect {
                private Vector subs;
                public Object expected;
                public Expect (Object topE, Expect [] subE) {
                        expected = topE;
                        subs = new Vector ();
                        for (int i = 0; i < subE.length; i++)
                                subs.addElement (subE [i]);
                }
                public Expect (Object topE, Object subE) {
                        expected = topE;
                        subs = new Vector ();
                        subs.addElement ((subE instanceof Expect)
                                                         ? subE
                                                         : (new Expect (subE)));
                }
                public Expect (Object topE) {
                        expected = topE;
                        subs = new Vector ();
                }
                public int subCount () { return subs.size (); }
                public Expect sub (int i) {
                        return (Expect) subs.elementAt (i);
                }

        }

}

