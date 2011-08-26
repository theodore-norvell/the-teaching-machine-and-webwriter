package tm.cpp.analysis;

import tm.clc.analysis.*;
import tm.clc.ast.NodeList;
import tm.cpp.ast.*;


import junit.framework.*;

import java.util.Vector;

/**
 * tests C++ implementation of function overload resolution
 */

public class OverloadResolverTest extends TestCase
    implements tm.cpp.analysis.TestConstantUser {

        static OverloadResolver or = OverloadResolver.getInstance ();
        static ScopedName fnid = new Cpp_ScopedName (FNID);

        static TyBool tyBool = TyBool.get ();
        static TyChar tyChar = TyChar.get ();
        static TyInt tyInt = TyInt.get ();
        static TyLong tyLong = TyLong.get ();
        static TyFloat tyFloat = TyFloat.get ();
        static TyDouble tyDouble = TyDouble.get ();
        static TyArray tyArrayInt = new TyArray ();
        static TyArray tyArrayFloat = new TyArray ();
        static TyArray tyArray2d = new TyArray ();
        static TyPointer tyPointerInt = new TyPointer ();
        static TyPointer tyPointerPtr = new TyPointer ();
        static {
            tyArrayInt.setNumberOfElements( 1 ) ;
                tyArrayInt.addToEnd (tyInt);
            tyArrayFloat.setNumberOfElements( 1 ) ;
                tyArrayFloat.addToEnd (tyFloat);
            tyArray2d.setNumberOfElements( 1 ) ;
                tyArray2d.addToEnd (tyArrayInt);
                tyPointerInt.addToEnd (tyInt);
                tyPointerPtr.addToEnd (tyPointerInt);
        }

        static FunctionDeclaration fnoarg, fbool, fchar, fint, flong, ffloat,
                fdouble, farrayInt, farrayFloat, farray2d, fpointerInt, fpointerPtr,
                f2boolbool, fboolellipsis, f3boolboolbooldefault, f3boolboolbool, f2boolint,
                f2intint, f2intfloat, f2intdouble, f2chardouble, f2charfloat, f2longfloat;
        static {
                // no parameters
                fnoarg = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                // single parameter
                fbool = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fbool.addParameter (tyBool, false);
                fchar = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fchar.addParameter (tyChar, false);
                fint = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fint.addParameter (tyInt, false);
                flong = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                flong.addParameter (tyLong, false);
                ffloat = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                ffloat.addParameter (tyFloat, false);
                fdouble = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fdouble.addParameter (tyDouble, false);
                farrayInt = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                farrayInt.addParameter (tyArrayInt, false);
                farrayFloat = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                farrayFloat.addParameter (tyArrayFloat, false);
                farray2d = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                farray2d.addParameter (tyArray2d, false);
                fpointerInt = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fpointerInt.addParameter (tyPointerInt, false);
                fpointerPtr = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fpointerPtr.addParameter (tyPointerPtr, false);
                // 2 parameters
                f2boolbool = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2boolbool.addParameter (tyBool, false);
                f2boolbool.addParameter (tyBool, false);
                f2boolint = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2boolint.addParameter (tyBool, false);
                f2boolint.addParameter (tyInt, false);
                fboolellipsis = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                fboolellipsis.addParameter (tyBool, false);
                fboolellipsis.ellipsisInParamList ();
                f2intfloat = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2intfloat.addParameter (tyInt, false);
                f2intfloat.addParameter (tyFloat, false);
                f2intint = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2intint.addParameter (tyInt, false);
                f2intint.addParameter (tyInt, false);
                f2intdouble = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2intdouble.addParameter (tyInt, false);
                f2intdouble.addParameter (tyDouble, false);
                f2charfloat = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2charfloat.addParameter (tyChar, false);
                f2charfloat.addParameter (tyFloat, false);
                f2chardouble = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2chardouble.addParameter (tyChar, false);
                f2chardouble.addParameter (tyDouble, false);
                f2longfloat = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f2longfloat.addParameter (tyLong, false);
                f2longfloat.addParameter (tyFloat, false);
                // 3 parameters
                f3boolboolbool = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f3boolboolbool.addParameter (tyBool, false);
                f3boolboolbool.addParameter (tyBool, false);
                f3boolboolbool.addParameter (tyBool, false);
                f3boolboolbooldefault = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                f3boolboolbooldefault.addParameter (tyBool, false);
                f3boolboolbooldefault.addParameter (tyBool, false);
                f3boolboolbooldefault.addParameter (tyBool, true);
        }

    public OverloadResolverTest () { super ("OverloadResolverTest"); }
    public OverloadResolverTest (String name) { super (name); }

    protected void setUp () { }

        /**
         * Candidate functions:
         * <pre>
         * void foo () ;
         * </pre>
         * Function calls:
         * <pre>
         * foo ();
         * foo (long);
         * </pre>
         */
        public void testNoParam () {
                // create function declarations
                Declaration foo = new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);

                // create argument list for foo ()
                Vector args = new Vector ();

                // do overload resolution
                RankedFunction rf = or.disambiguate (foo, args);
                Declaration match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == foo);

                // do same for foo (long)
                args.addElement (tyLong);
                rf = or.disambiguate (foo, args);
                match = null;
                match = null;
                if (rf != null) match = rf.declaration;
                assertTrue (match == null);
        }


        /**
         * Candidate functions:
         * <pre>
         * void foo (bool) ;
         * </pre>
         * Function calls:
         * <pre>
         * foo ();
         * foo (long);
         * foo (bool);
         * foo (long, long);
         * foo (long []);
         * </pre>
         */
        public void testOneParam () {
                // create function declarations
                FunctionDeclaration foo =
                        new FunctionDeclaration (Cpp_LFlags.REG_FN_LF, fnid);
                foo.addParameter (tyBool, false);

                // create argument list for foo ()
                Vector args = new Vector ();

                // do overload resolution
                RankedFunction rf = or.disambiguate (foo, args);
                Declaration match = null;
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for foo (long)
                args.addElement (tyLong);
                rf = or.disambiguate (foo, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == foo);

                // do same for foo (bool)
                args.removeAllElements ();
                args.addElement (tyBool);
                rf = or.disambiguate (foo, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == foo);

                // do same for foo (long, long)
                args.removeAllElements ();
                args.addElement (tyLong); args.addElement (tyLong);
                rf = or.disambiguate (foo, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for foo (int [])
                args.removeAllElements ();
                args.addElement (tyArrayInt);
                rf = or.disambiguate (foo, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == foo);

        }

        /**
         * Function call: one arg (bool)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo ();
         * foo (bool, bool);
         * foo (bool);
         * foo (int);
         * foo (int []);
         * </pre>
         */
        public void testBoolArg () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyBool);

                // create candidate function set : foo (), foo (bool, bool)
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (fnoarg);
                candidates.addElement (f2boolbool);

                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (bool), foo (int)}
                candidates.removeAllElements ();
                candidates.addElement (fbool);
                candidates.addElement (fint);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;
                assertTrue (match == fbool);

                // do same for {foo (int [])}
                rf = or.disambiguate (farrayInt, args);
                match = null;
                if (rf != null) match = rf.declaration;;
                assertTrue (match == null);

                // do same for {foo (int), foo (int [])}
                candidates.removeAllElements ();
                candidates.addElement (fint);
                candidates.addElement (farrayInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fint);

        }


        /**
         * Function call: one arg (int)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (bool);
         * foo (int);
         * foo (long);
         * foo (float);
         * foo (int []);
         * foo (int *);
         * </pre>
         */
        public void testIntArg () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyInt);

                // create candidate function set: { foo (bool), foo (int), foo (long }
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (fbool);
                candidates.addElement (fint);
                candidates.addElement (flong);

                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;


                assertTrue (match == fint);

                // do same for {foo (bool), foo (long)}
                candidates.removeAllElements ();
                candidates.addElement (fbool);
                candidates.addElement (flong);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (bool), foo (float)}
                candidates.removeAllElements ();
                candidates.addElement (fbool);
                candidates.addElement (ffloat);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (long), foo (float)}
                candidates.removeAllElements ();
                candidates.addElement (flong);
                candidates.addElement (ffloat);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (int []), foo (int *)}
                candidates.removeAllElements ();
                //		candidates.addElement (farrayInt);
                candidates.addElement (fpointerInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fpointerInt); // assumes 0 value here
        }


        /**
         * Function call: one arg (float)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (bool);
         * foo (int);
         * foo (double);
         * foo (float);
         * foo (int []);
         * foo (int *);
         * </pre>
         */
        public void testFloatArg () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyFloat);

                // create candidate function set: { foo (bool), foo (int) }
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (fbool);
                candidates.addElement (fint);

                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;


                assertTrue (match == null);

                // do same for {foo (int []), foo (int *)}
                candidates.removeAllElements ();
                //		candidates.addElement (farrayInt);
                candidates.addElement (fpointerInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (int), foo (double)}
                candidates.removeAllElements ();
                candidates.addElement (fint);
                candidates.addElement (fdouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fdouble);

                // do same for {foo (int), foo (float), foo (double)}
                candidates.removeAllElements ();
                candidates.addElement (fint);
                candidates.addElement (ffloat);
                candidates.addElement (fdouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == ffloat);
        }

        /**
         * Function call: one arg (double)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (int);
         * foo (double);
         * foo (float);
         * </pre>
         */
        public void testDoubleArg () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyDouble);

                // create candidate function set: { foo (int), foo (float) }
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (fint);
                candidates.addElement (ffloat);

                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;


                assertTrue (match == null);

                // do same for {foo (float), foo (double)}
                candidates.removeAllElements ();
                candidates.addElement (ffloat);
                candidates.addElement (fdouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fdouble);
        }

        /**
         * Function call: one arg (int [])<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (bool);
         * foo (char);
         * foo (int);
         * foo (long);
         * foo (double);
         * foo (float);
         * foo (int []);
         * foo (int [][]);
         * foo (float []);
         * foo (int *);
         * </pre>
         */
        public void testIntArrayArg () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyArrayInt);

                // candidate function set: { foo (int []) }
                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (farrayInt, args);
                if (rf != null) match = rf.declaration;;

                assertTrue (match == farrayInt);

                // do same for {foo (int []), foo (int *)}
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (farrayInt);
                candidates.addElement (fpointerInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (int [][]), foo (int [])}
                candidates.removeAllElements ();
                candidates.addElement (farray2d);
                candidates.addElement (farrayInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == farrayInt);

                // do same for {foo (float [])}
                rf = or.disambiguate (farrayFloat, args);
                match = null;
                if (rf != null) match = rf.declaration;;
                assertTrue (match == null);

                // do same for {foo (float []), foo (int *)}
                candidates.removeAllElements ();
                candidates.addElement (farrayFloat);
                candidates.addElement (fpointerInt);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fpointerInt);

                // do same for {foo (bool), foo (float [])}
                candidates.removeAllElements ();
                candidates.addElement (fbool);
                candidates.addElement (farrayFloat);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fbool);

                // do same for {foo (int), foo (double), foo (long), foo (char), foo (float)}
                candidates.removeAllElements ();
                candidates.addElement (fchar);
                candidates.addElement (fint);
                candidates.addElement (flong);
                candidates.addElement (ffloat);
                candidates.addElement (fdouble);


                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);
        }

        /**
         * Function call: two args (bool, bool)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (bool, bool);
         * foo (bool, ...);
         * foo (bool, bool, bool=true);
         * foo (bool, bool, bool);
         * foo (bool);
         * foo (bool, int);
         * </pre>
         */
        public void testTwoBoolArgs () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyBool);
                args.addElement (tyBool);

                // candidate function set: { foo (bool, bool) }
                Declaration match = null;
                RankedFunction rf = or.disambiguate (f2boolbool, args);
                if (rf != null) match = rf.declaration;
                assertTrue (match == f2boolbool);

                // candidate function set: { foo (bool, bool, bool=true) }
                rf = or.disambiguate (f3boolboolbooldefault, args);
                match = null;
                if (rf != null) match = rf.declaration;;
                assertTrue (match == f3boolboolbooldefault);

                // candidate function set: { foo (bool, ...) }
                rf = or.disambiguate (fboolellipsis, args);
                match = null;
                if (rf != null) match = rf.declaration;;
                assertTrue (match == fboolellipsis);

                // {foo (bool, bool), foo (bool, ...), foo (bool, bool, bool=true)}
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (f2boolbool);
                candidates.addElement (fboolellipsis);
                candidates.addElement (f3boolboolbooldefault);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // {foo (bool, ...), foo (bool, bool, bool=true)}
                candidates.removeAllElements ();
                candidates.addElement (fboolellipsis);
                candidates.addElement (f3boolboolbooldefault);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f3boolboolbooldefault);

                // {foo (bool, ...), foo (bool)}
                candidates.removeAllElements ();
                candidates.addElement (fboolellipsis);
                candidates.addElement (fbool);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == fboolellipsis);

                // {foo (bool, bool, bool=true), foo (bool, bool, bool)}
                candidates.removeAllElements ();
                candidates.addElement (f3boolboolbooldefault);
                candidates.addElement (f3boolboolbool);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f3boolboolbooldefault);

                // {foo (bool, ...), foo (bool, int)}
                candidates.removeAllElements ();
                candidates.addElement (fboolellipsis);
                candidates.addElement (f2boolint);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f2boolint);

                // {foo (bool, bool, bool=true), foo (bool, int)}
                candidates.removeAllElements ();
                candidates.addElement (f3boolboolbooldefault);
                candidates.addElement (f2boolint);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f3boolboolbooldefault);

        }

        /**
         * Function call: two args (int, float)<br>
         * Candidate functions (various subsets tested):
         * <pre>
         * foo (int, float);
         * foo (int, double);
         * foo (char, double);
         * foo (char, float);
         * foo (long, float);
         * foo (int, int);
         * foo (bool, bool);
         * </pre>
         */
        public void testIntFloatArgs () {
                // create argument list for function call
                Vector args = new Vector ();
                args.addElement (tyInt);
                args.addElement (tyFloat);

                // create candidate function set: { foo (int, float), foo (int, double) }
                DeclarationSetMulti candidates = new DeclarationSetMulti ();
                candidates.addElement (f2intfloat);
                candidates.addElement (f2intdouble);

                // do overload resolution
                Declaration match = null;
                RankedFunction rf = or.disambiguate (candidates, args);
                if (rf != null) match = rf.declaration;


                assertTrue (match == f2intfloat);

                // do same for {foo (char, float), foo (int, double)}
                candidates.removeAllElements ();
                candidates.addElement (f2charfloat);
                candidates.addElement (f2intdouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (long, float), foo (int, double)}
                candidates.removeAllElements ();
                candidates.addElement (f2longfloat);
                candidates.addElement (f2intdouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == null);

                // do same for {foo (bool, bool), foo (int, int)}
                candidates.removeAllElements ();
                candidates.addElement (f2boolbool);
                candidates.addElement (f2intint);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f2intint);

                // do same for {foo (bool, bool), foo (char, double)}
                candidates.removeAllElements ();
                candidates.addElement (f2boolbool);
                candidates.addElement (f2chardouble);

                rf = or.disambiguate (candidates, args);
                match = null;
                if (rf != null) match = rf.declaration;

                assertTrue (match == f2chardouble);
        }

}

