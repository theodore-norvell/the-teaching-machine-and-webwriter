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

import java.util.Hashtable;
import java.util.Vector;

import tm.clc.analysis.ConversionRules;
import tm.clc.ast.ExpFetch;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpAddressOf;
import tm.clc.ast.OpArithmeticConversion;
import tm.clc.ast.OpConvertToReference;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyArray;
import tm.cpp.ast.TyBool;
import tm.cpp.ast.TyChar;
import tm.cpp.ast.TyClass;
import tm.cpp.ast.TyDouble;
import tm.cpp.ast.TyFloat;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyFundamental;
import tm.cpp.ast.TyInt;
import tm.cpp.ast.TyIntegral;
import tm.cpp.ast.TyLong;
import tm.cpp.ast.TyLongDouble;
import tm.cpp.ast.TyPointer;
import tm.cpp.ast.TyRef;
import tm.cpp.ast.TyShortInt;
import tm.cpp.ast.TySignedChar;
import tm.cpp.ast.TyUnsignedChar;
import tm.cpp.ast.TyUnsignedInt;
import tm.cpp.ast.TyUnsignedLong;
import tm.cpp.ast.TyUnsignedShortInt;
import tm.cpp.ast.TyVoid;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Identifies standard conversions; i.e. implicit conversions defined for
 * built-in types. Can also build the AST representation of the conversion.
 * @author Derek Reilly
 * @created Oct 16, 2001
 */
public class StandardConversions implements ConversionRules {

    private static final String CONVERSION_NOT_SUPPORTED =
        "Sorry, conversion from {0} to {1} is not supported";
    private static final String IMPLICIT_CONVERSION_NOT_POSSIBLE =
        "Sorry, implicit conversion from {0} to {1} is not possible";
    private static final String CPC_OPERAND_MISMATCH =
        "Pointer types required for composite pointer conversion, have {0} and {1}";
    private static final String NPC_OPERAND_MISMATCH =
        "Null pointer constant conversion requires constant integral and pointer operands, have {0} and {1} respectively";
    private static final String NEED_ARITHMETIC_TYPES =
        "Arithmetic types (or enum, bit field) required to generate {0}" +
        ", have {1} and {2}";
    private static final String NON_ARITHMETIC_TYPE =
        "Cannot give arithmetic conversion ranking of non-arithmetic type {0}";

    private static final String ATP_ARRAY_TYPE_REQUIRED =
        "Array type required for array-to-pointer conversion, have {0}";

    private StandardConversionTable sctable = new StandardConversionTable ();
    private ArithmeticConversionTable actable = new ArithmeticConversionTable ();

    // the types of conversions
    /** Type conversion code : no conversion is <em>possible</em> (vs.
     * none required). */
    public static final int NONE = -2;

    /** Type conversion code : conversion sequence is ambiguous. */
    public static final int AMBIGUOUS = -1; // ambiguous std conversions?

    /** Type conversion code : lvalue to rvalue conversion. */
    public static final int LVAL_TO_RVAL = 0;

    /** Type conversion code : array to pointer conversion. */
    public static final int ARRAY_TO_POINTER = 1;

    /** Type conversion code : function to pointer conversion. */
    public static final int FUNCTION_TO_POINTER = 2;

    /** Type conversion code : cv-qualification conversion. */
    public static final int QUALIFICATION = 3;

    /** Type conversion code : integral promotion. */
    public static final int INT_PROMOTION = 4;

    /** Type conversion code : floating promotion. */
    public static final int FLOAT_PROMOTION = 5;

    /** Type conversion code : integral conversion. */
    public static final int INT_CONVERSION = 6;

    /** Type conversion code : floating conversion. */
    public static final int FLOAT_CONVERSION = 7;

    /** Type conversion code : floating <--> integral conversion. */
    public static final int FLOAT_INT_CONVERSION = 8;

    /** Type conversion code : pointer conversion. */
    public static final int PTR_CONVERSION = 9;

    /** Type conversion code : pointer-to-member conversion. */
    public static final int PTR_TO_MEMBER_CONVERSION = 10;

    /** Type conversion code : boolean conversion. */
    public static final int BOOL_CONVERSION = 11;

    /** Type conversion code : pointer to boolean conversion. */
    public static final int PTR_TO_BOOL = 12;

    /** Type conversion code : no conversion required - types are
     * equivalent. */
    public static final int EQUIVALENT_TYPES = 13;

    // non-standard conversions
    /** Type conversion code : user defined conversion - listed here in
     * relation to other conversions, but manifested in
     * <code>UserDefinedConversions</code> */
    public static final int USER_DEFINED = 100;

    /** Conversion code : ellipsis conversion - listed in relation to other
     * conversions */
    public static final int ELLIPSIS = 101;

    // additional flags for arithmetic conversions

    /** Type(s) is/are invalid for a particular operation. */
    public static final int INVALID_TYPES = -1;

    /** Convert type <code>a</code> to type <code>b</code>. */
    public static final int ATOB = 1;

    /** Convert type <code>b</code> to type <code>a</code>. */
    public static final int BTOA = 2;

    // additional flags for class conversions
    /** conversion to superclass */
    public static final int UP_CONVERSION = 100;

    /** conversion to subclass */
    public static final int DOWN_CONVERSION = 101;

    /** debugging statements this way */
    protected Debug d = Debug.getInstance ();

    private TypeNode tyBool = TyBool.get ();

    private static StandardConversions instance = new StandardConversions ();
    /** return the single instance of this class */
    public static StandardConversions getInstance () { return instance; }

    /**
     * Provide the implicit, standard conversion sequence required given the
     * parameters. Standard conversion sequences do not involve user-defined
     * conversion functions.
     * <br>An apology will be generated if no standard conversion sequence
     * exists for the two types
     * @param from the type to convert from
     * @param to the type to convert to
     * @param flags constraining the conversions that can be performed in
     * the context of the call
     * @return the implicit conversion sequence to apply
     */
    public ConversionSequence getStandardConversionSequence
        (TypeNode from, TypeNode to, int flags) {
        boolean suppressed = false; // is this conversion suppressed by flags ?
        ConversionSequence seq =
            new ConversionSequence (ConversionSequence.STANDARD_CS);


        // 1. 0..1 of lvalue->rvalue, array->pointer, function->pointer
        // ** lvalue->rvalue is always taken care of externally
        // ** Function conversions not supported
        if (from instanceof TyFun) { // apologize
            Assert.apology (CONVERSION_NOT_SUPPORTED,
                            new Object [] { from.getTypeString (),
                                            to.getTypeString () });
        }
        // array->pointer
        if (from instanceof TyArray) {
            if ((flags & ARRAY_TO_POINTER) != 0) {
                // array->pointer conversions are suppressed for this
                // conversion. Since it is required for any standard conversion
                // involving an array, no conversion sequence is possible
                suppressed = true;
            } else {
                TypeNode pconv = new TyPointer ();
                pconv.setAttributes (from.getAttributes ());
                pconv.addToEnd (((TyArray) from).getElementType ());
                seq.addConversion (ARRAY_TO_POINTER, pconv, false);
                from = pconv;
            }
        }

        // 2. 0..1 of any other standard conversion but qualification
        if (!suppressed) {
            boolean[] uninteresting = new boolean[1] ;
            int convType = determine (from, to, uninteresting);
            switch (convType) {
            case EQUIVALENT_TYPES:
                // do nothing
                break;
            case AMBIGUOUS:
            case NONE:
                // complain
                Assert.apology (IMPLICIT_CONVERSION_NOT_POSSIBLE,
                                new Object [] { from.getTypeString (),
                                                to.getTypeString () });
                break;
            case ARRAY_TO_POINTER:
                // already taken care of
                break;
            default:
                if ((flags & convType) != 0) {
                    // this conversion is suppressed - no conversion sequence
                    suppressed = true;
                }
                seq.addConversion (convType, to, uninteresting[0]);
                break;
            }
        }

        // 3. 0..1 qualification conversion
        if (!suppressed &&
            rankQualifiers (from.getAttributes (), to.getAttributes ()) != 0) {
            if ((flags & QUALIFICATION) != 0) {
                // this conversion is suppressed - no conversion sequence
                // ?? is this correct for suppressed qual conversion ?
                suppressed = true;
            }
            seq.addConversion (QUALIFICATION, to, true);
        }

        return (suppressed) ? null : seq;
    }

    /**
     * Determines the conversion required to convert type <code>from</code>
     * to type <code>to</code>.
     * @param from the <code>TypeNode</code> value to convert from
     * @param to the <code>TypeNode</code> value to convert to
     * @return the conversion code representing the required conversion
     */
    public int determine (TypeNode from, TypeNode to, boolean[] uninteresting) {
        // ?? missing:
        // - 4.4 qualification conversion (require cv qualifiers) - implicit ?
        // - 4.3 function-to-pointer conversion -- determine will be passed
        //   return type, not fn
        // - 4.1 Lvalue-to-rvalue conversion - implicit ?
        // - 4.2-2 string literal --> char pointer -- how will strings be represented?
        //   If as arrays of char, this is covered.

        int conversion = NONE; // Default
        uninteresting[0] = true ; // Default

        if (to.equal_types (from)) { /*CHECK*/
            conversion = EQUIVALENT_TYPES;

        } else if (to instanceof TyFundamental) {
            conversion = sctable.getConversion (from, to);
            uninteresting[0] = sctable.getUninteresting( from, to ) ;

        } else if (to instanceof TyPointer) {
            // 4.10 - pointer conversions

            if (from instanceof TyIntegral) {
                // 1. null pointer constant --> null pointer value of the pointer type, incl cv qual
                conversion = PTR_CONVERSION;

            } else if (((TyPointer)to).getPointeeType() instanceof TyVoid) {
                // 2. pointer to object type T --> pointer to void
                conversion = PTR_CONVERSION;

            } else if (from instanceof TyArray) {
                // 4.3-1 array-to-pointer conversion
                // follow conversion sequence
                TypeNode pType = ((TyPointer) to).getPointeeType ();
                TypeNode aType = ((TyArray) from).getElementType ();
                boolean[] junk = new boolean[1] ;
                if( determine (aType, pType, junk) != NONE ) {
                    conversion = ARRAY_TO_POINTER ;
                    uninteresting[0] = false ;
                }
            } else if (from instanceof TyPointer) {
                // 3. pointer to object type A --> pointer to base class B
                TypeNode aType = ((TyPointer) from).getPointeeType ();
                TypeNode bType = ((TyPointer) to).getPointeeType ();
                if (aType instanceof TyAbstractClass &&
                    bType instanceof TyAbstractClass) {
                    int conversionType = getClassConversion
                        ((TyAbstractClass)aType, (TyAbstractClass)bType);
                    if (conversionType == UP_CONVERSION)
                        conversion = PTR_CONVERSION;
                } else {
                    boolean[] junk = new boolean[1] ;
                    conversion = (determine (aType, bType, junk));
                    if (conversion != EQUIVALENT_TYPES &&
                        conversion != NONE)
                        conversion = PTR_CONVERSION;
                }
            }
            // 4.11 - pointer to member conversions
            // 1. null pointer constant --> as above
            // 2. pointer to member of B of type T --> pointer to member of D of type T, where
            //    D is derived class of B. More details.

        } else if (to instanceof TyArray) {
            if (from instanceof TyPointer) {
                // ??? where is the rule here ???
                // follow conversion sequence
                TypeNode pType = ((TyPointer) from).getPointeeType ();
                TypeNode aType = ((TyArray) to).getElementType ();
                boolean[] junk = new boolean[1] ;
                // I don't know what this is about.  Conversion from pointer to array?
                // Is it possible?  TSN
                conversion = determine (pType, aType, junk);
            }

        } else if (to instanceof TyClass) {
            if (from instanceof TyClass) { // up conversion ?
                int cConv = getClassConversion ((TyClass) from, (TyClass) to);
                conversion = (cConv == UP_CONVERSION) ? UP_CONVERSION : NONE;
            }
        }


        return conversion;
    }


    /**
     * Builds an <code>ExpressionNode</code> representing a conversion of
     * the provided expression's type to the type indicated.
     *
     * @param fromExp the <code>ExpressionNode</code> requiring the conversion
     * @param to a <code>TypeNode</code> representing the type to convert to
     * @return an <code>ExpressionNode</code> comprised of the original
     * expression, converted to the indicated type, the original expression
     * if no conversion is required, or <code>null</code> if no conversion
     * is possible.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to) {
        return makeConversionExpression (fromExp, to, "");
    }

    /**
     * Builds an <code>ExpressionNode</code> representing a conversion of
     * the provided expression's type to the type indicated.
     *
     * @param fromExp the <code>ExpressionNode</code> requiring the conversion
     * @param to a <code>TypeNode</code> representing the type to convert to
     * @param op_image the image (if any) of the operator under which this
     * conversion is taking place.
     * @return an <code>ExpressionNode</code> comprised of the original
     * expression, converted to the indicated type, the original expression
     * if no conversion is required, or <code>null</code> if no conversion
     * is possible.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to, String op_image) {
        // better params may be the expression to convert plus the conversion type id
        TypeNode from = fromExp.get_type ();

        // we don't care about ref
        if (from instanceof TyRef) from = ((TyRef) from).getPointeeType ();
        if (to instanceof TyRef) to = ((TyRef) to).getPointeeType ();

        d.msg (Debug.COMPILE, "converting " + from.getTypeString () + " to "
               + to.getTypeString ());

        boolean[] uninteresting = new boolean[1] ;
        int convType = determine (from, to, uninteresting);

        return makeConversionExpression (fromExp, to, op_image, convType, uninteresting[0]);
    }

    /**
     * Builds an <code>ExpressionNode</code> representing a conversion of
     * the provided expression's type to the type indicated.
     *
     * @param fromExp the <code>ExpressionNode</code> requiring the conversion
     * @param to a <code>TypeNode</code> representing the type to convert to
     * @param op_image the image (if any) of the operator under which this
     * conversion is taking place.
     * @param convType a flag specifying the type of conversion to perform
     * @return an <code>ExpressionNode</code> comprised of the original
     * expression, converted to the indicated type, the original expression
     * if no conversion is required, or <code>null</code> if no conversion
     * is possible.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to, String op_image, int convType, boolean uninteresting) {

        // Watch out for references that need a fetch
        if( fromExp.get_type() instanceof TyRef ) {
            TypeNode base_type = ((TyRef)fromExp.get_type()).getPointeeType() ;
            fromExp = new ExpFetch( base_type, fromExp ) ; }

        ExpressionNode convExp = fromExp;
        switch (convType) {
        case PTR_TO_BOOL:
        case BOOL_CONVERSION:
            convExp =  new OpArithmeticConversion	(tyBool, "", fromExp);
            convExp.setUninteresting( uninteresting );
            break;
        case INT_PROMOTION:
        case FLOAT_PROMOTION:
        case INT_CONVERSION:
        case FLOAT_CONVERSION:
        case PTR_CONVERSION:
        case PTR_TO_MEMBER_CONVERSION:
        case QUALIFICATION:
            convExp = new OpArithmeticConversion (to, op_image, fromExp);
            convExp.setUninteresting( uninteresting );
            break;
        case FLOAT_INT_CONVERSION:
            convExp = new OpArithmeticConversion (to, op_image, fromExp);
            convExp.setUninteresting( uninteresting );
            break;
        case ARRAY_TO_POINTER:
            convExp = makeArrayToPointerConversion (fromExp, op_image);
            convExp.setUninteresting( uninteresting );
            break;
        case NONE:
        case EQUIVALENT_TYPES:
            convExp = fromExp;
            break;
        default:
            Assert.apology (CONVERSION_NOT_SUPPORTED, new Object []
                { fromExp.get_type().getTypeString (), to.getTypeString () });
            break;
        }
        return convExp;
    }

    /**
     * Performs standard arithmetic conversions given two operands, as per
     * section 4.5 of the ARM.
     * @param opa an operand
     * @param opb an operand
     * @return a pair of <code>ExpressionNodes</code>, consisting of the
     * original operands with any standard arithmetic conversions applied,
     * will throw an apology if types are not appropriate for arithmetic
     * converions; previous type analysis required.
     */
    public Vector makeArithmeticConversionExpressions
        (ExpressionNode opa, ExpressionNode opb) {
        ExpressionNode conva = opa, convb = opb;
        TypeNode ta = opa.get_type ();
        TypeNode tb = opb.get_type ();

        switch (actable.get (ta, tb)) {
        case INVALID_TYPES:
            // standard arithmetic conversions apply only to arithmetic types,
            // or those objects which are candidates for integral promotions
            // (enumerator, object of enumeration type, int bit field)
            // ** NOTE : currently this second group are not recognized
            Assert.apology (NEED_ARITHMETIC_TYPES, new Object []
                {"standard arithmetic conversions", ta.getTypeString (),
                 tb.getTypeString ()});
            break;
        case NONE: // no conversion required
            break;
        case ATOB: // ta converted to tb
            conva = new OpArithmeticConversion (tb, "", opa);
            conva.setUninteresting( sctable.getUninteresting( ta, tb ) );
            break;
        case BTOA: // tb converted to ta
            convb = new OpArithmeticConversion (ta, "", opb);
            convb.setUninteresting( sctable.getUninteresting( tb, ta ) );
            break;
        case INT_PROMOTION: // integral promotion performed first
            conva = makeIntegralPromotionExpression (opa);
            convb = makeIntegralPromotionExpression (opb);
            // now, compare the two again
            Vector res = makeArithmeticConversionExpressions (conva, convb);
            conva = (ExpressionNode) res.elementAt (0);
            convb = (ExpressionNode) res.elementAt (1);
            break;
        default:
            break;
        }
        Vector results = new Vector ();
        results.addElement (conva);
        results.addElement (convb);
        return results;
    }

    /**
     * Generates an arithmetic conversion expression from a smaller integral
     * type to a TyInt.
     */
    public ExpressionNode makeIntegralPromotionExpression (ExpressionNode e) {
        ExpressionNode result = null;
        int rank = actable.getRanking (e.get_type ()) ;
        if (rank < actable.INTEGER) {
            result = new OpArithmeticConversion (TyInt.get (), "", e);
            result.setUninteresting( sctable.getUninteresting( e.get_type(), TyInt.get() ) ); }
        else
            result = e;
        return result;
    }

    /**
     * Generates a boolean conversion expression (assumes valid convertee
     * type)
     */
    public ExpressionNode makeBooleanConversionExpression (ExpressionNode e) {
        ExpressionNode bce = e;
        ConversionSequence ctb =
            getStandardConversionSequence (e.get_type (), tyBool, 0);

        if (ctb == null)
            Assert.apology (IMPLICIT_CONVERSION_NOT_POSSIBLE, new String []
                {e.get_type().getTypeString (), tyBool.getTypeString ()});

        for (int i = 0; i < ctb.length (); i++) {
            ConversionSequence.Element cel = ctb.element (i);
            bce = makeConversionExpression (bce, cel.type (), "");
        }
        return bce;
    }

    /** Generates the AST representation of an <em>array-to-pointer</em>
     * conversion.
     * @param fromExp the expression requiring the conversion
     * @param op_image the image of the operator (if any) under which the
     * conversion is taking place
     */
    public ExpressionNode makeArrayToPointerConversion
        (ExpressionNode fromExp, String op_image) {
        ExpressionNode converted = null;
        TypeNode t = fromExp.get_type ();

        ExpressionNode refExp ;
        TyArray tyArray ;
        if( t instanceof TyArray ) {
            TyRef tyRefToArray = new TyRef( t ) ;
            refExp = new OpConvertToReference( tyRefToArray, fromExp ) ;
            refExp.setUninteresting(true);
            tyArray = (TyArray) t ; }
        else if( t instanceof TyRef ) {
            TypeNode temp = ((TyRef)t).getPointeeType() ;
            Assert.apology ( temp instanceof TyArray,
                            ATP_ARRAY_TYPE_REQUIRED,
                            new String [] {t.getTypeString ()});
            refExp = fromExp ;
            tyArray = (TyArray) temp ; }
        else {
            Assert.apology ( ATP_ARRAY_TYPE_REQUIRED,
                             new String [] {t.getTypeString ()});
            tyArray = null ; refExp = null ; }


        TypeNode elementType = tyArray.getElementType () ;
        TyPointer p = new TyPointer ();
        p.addToEnd (elementType);
        converted = new OpAddressOf (p, op_image, refExp ) ;
        // converted.set_uninteresting(true);
        return converted;
    }

    /**
     * Gives AST representation of composite pointer conversion,
     * whereby the cv-qualification of <code>orig</code>'s type is unioned
     * with that of <code>other</code>.
     * <ul>
     * In addition:
     * <li>a null pointer constant is converted to the type of the
     *     <code>other</code>operand - HANDLED EXTERNALLY by
     *     nullPointerConstantConversion
     * <li>if <code>other</code> is a pointer to void, <code>left</code> will
     *     get that type
     * </ul>
     *
     * @param orig the pointer expression to modify
     * @param other the pointer expression with cv-qualifiers to union
     * @return the AST representation of the composite qualification conversion
     */
    public ExpressionNode makeCompositePointerConversion
        (ExpressionNode orig, ExpressionNode other) {
        ExpressionNode converted = orig;

        // we need two pointer arguments
        TypeNode origType = orig.get_type() ;
        TypeNode otherType = other.get_type() ;
        Assert.apology ( origType instanceof TyPointer && otherType instanceof TyPointer,
               CPC_OPERAND_MISMATCH, new String []
                {origType.getTypeString (), otherType.getTypeString ()});
        TyPointer orp = (TyPointer) origType ;
        TyPointer otp = (TyPointer) otherType;

        // ptr to anything --> ptr to void
        if (otp.getPointeeType () instanceof TyVoid) {
            TypeNode orpt = orp.getPointeeType ();
            if (!(orpt instanceof TyVoid)) {
                TyVoid vt = TyVoid.get (orpt.getAttributes ());
                converted = new OpArithmeticConversion (vt, "", orig);
                converted.setUninteresting(true);
            }
        }


        int orpq = orp.getAttributes ();
        int otpq = otp.getAttributes ();

        if (orpq != otpq) {
            int compositeCVQ = orpq | otpq;
            // make a copy of ptr type with composite cvq
            TyPointer compositePtr =
                copyType (((TyPointer) converted.get_type ()), compositeCVQ);

            converted = new OpArithmeticConversion (compositePtr, "", converted);
            converted.setUninteresting(true);
        }

        return converted;
    }

    /**
     * Makes a copy of a <code>TypeNode</code> instance, with modifications.
     * <br>This implementation generates a new <code>TyPointer</code> that is
     * a copy of the first, save for cv-qualification which is determined by
     * the value in <code>cvq</code>.
     */
    public TyPointer copyType (TyPointer t,	int cvq) {
            TyPointer ptcopy = new TyPointer ();
            ptcopy.addToEnd (t.getPointeeType ());
            ptcopy.setAttributes (cvq);
            return ptcopy;
    }

    /**
     * Gives AST representation of null pointer conversion
     *
     * @param from the null pointer constant expression
     * @param to the pointer type to convert to
     * @return the AST representation of the conversion
     */
    public ExpressionNode makeNullPointerConstantConversion
        (ExpressionNode from, TypeNode to) {

        if (!((from.is_integral_constant ()) &&
              (from.get_integral_constant_value () == 0) &&
              (to instanceof TyPointer))) {
            Assert.apology (NPC_OPERAND_MISMATCH, new String []
                {from.name (), to.getTypeString ()});
        }

        ExpressionNode result = new OpArithmeticConversion (to, "", from);
        result.setUninteresting(true);
        return result ;
    }

    /**
     * Provides a comparison of 2 <em>cv-qualification</em> identifiers.
     * Qualifiers are interpreted as follows:
     * <pre>
     * 0 : automatic (no cv qualification)
     * 1 : const
     * 2 : volatile
     * 3 : const volatile
     * </pre>
     * A value of 3 is more cv-qualified than all other values. A
     * value of 0 is less cv-qualified than all other values.
     * @param cv1 first qualifier identifier
     * @param cv2 second qualifier identifier
     * @return -1 if cv1 is less qualified than cv2, 0 if equally qualified,
     *  and 1 if more qualified.
     */
    public int rankQualifiers (int cv1, int cv2) {
        int ranking = 0; // assume equivalent, or both in {CONST, VOLATILE}
        if (cv1 != cv2) {
            if (cv1 == 3 || cv2 == 0) ranking = 1;
            else if (cv2 == 3 || cv1 == 0) ranking = -1;
        }
        return ranking;
    }

    /**
     * Determines if two types have are equivalent as parameter types.
     * It is assumed that array types have already been converted to
     * pointer types and that function types have already been
     * converted to pointers to functions. It therefore remains only
     * to check if the two types are the same disregarging cv
     * qualification. Reference ISO 8.3.5 para 3.
     */
    public boolean equivalentParameterTypes (TypeNode t1, TypeNode t2) {
        boolean equiv = t1.equal_types (t2) ; /*CHECK*/
        return equiv;
    }

    /**
     * Determines the conversion required to convert type <code>from</code>
     * to type <code>to</code>.
     * <ul>Possible values:
     * <li><code>NONE</code> (no conversion possible)
     * <li><code>AMBIGUOUS</code> (ambiguity in hierarchy, no conversion)
     * <li><code>DOWN_CONVERSION</code> (<code>from</code> is a superclass
     * of <code>to</code>)
     * <code>UP_CONVERSION</code> (<code>from</code> is a subclass of
     * <code>to</code>)
     * <li><code>EQUIVALENT_TYPES</code> (no conversion required)
     * </ul>
     * This method uses the inner class <code>ClassConversionRelation</code>,
     * which may be instantiated directly to accomplish the same thing.
     * @see this#ClassConversionRelation
     * @param from the fqn value to convert from
     * @param to the fqn value to convert to
     * @return the conversion code representing the required conversion
     */

    public int getClassConversion (TyAbstractClass from, TyAbstractClass to) {
        return new ClassConversionRelation (from, to).conversionType;
    }

    /**
     * Provides the path between the two class types indicating the
     * complete and unambiguous relationship between them which permits
     * the relevant up/down conversion
     * @param from the class converting from
     * @param to the class converting to
     * @return an int array representing the class hierarchy path
     */
    public int [] getClassConversionPath (TyAbstractClass from,
                                          TyAbstractClass to) {
        return new ClassConversionRelation (from, to).getPath ();
    }

    /**
     * Identifies the complete and unambiguous relationship between the
     * two classes which permits the relevant up/down conversion
     * @param from the class converting from
     * @param to the class converting to
     * @return a representation of the relationship between the two classes,
     * including the conversion path and the type of conversion required.
     */
    public ClassConversionRelation getClassConversionRelation
        (TyAbstractClass from, TyAbstractClass to) {
        return new ClassConversionRelation (from, to);
    }

    /**
     * Ranks arithmetic types according to standard arithmetic conversion
     * rules.
     */
    public class ArithmeticConversionTable {
        protected final int BOOLEAN = 0, CHAR = 1, S_CHAR = 1,
            U_CHAR = 2, SHORT = 3, U_SHORT = 4, INTEGER = 5,
            U_INTEGER = 6, LONG = 7, U_LONG = 8, FLOAT = 9, DOUBLE = 10,
            L_DOUBLE = 11;

        private Hashtable typeRanking; // ranking of arithmetic types
        // wrt arithmetic conversions

        private void buildRankTable () {
            typeRanking = new Hashtable ();
            typeRanking.put (TyBool.class, new Integer (BOOLEAN));
            typeRanking.put (TyChar.class, new Integer (CHAR));
            typeRanking.put (TySignedChar.class, new Integer (S_CHAR));
            typeRanking.put (TyUnsignedChar.class, new Integer (U_CHAR));
            typeRanking.put (TyShortInt.class, new Integer (SHORT));
            typeRanking.put (TyUnsignedShortInt.class, new Integer (U_SHORT));
            typeRanking.put (TyInt.class, new Integer (INTEGER));
            typeRanking.put (TyUnsignedInt.class, new Integer (U_INTEGER));
            typeRanking.put (TyLong.class, new Integer (LONG));
            typeRanking.put (TyUnsignedLong.class, new Integer (U_LONG));
            typeRanking.put (TyFloat.class, new Integer (FLOAT));
            typeRanking.put (TyDouble.class, new Integer (DOUBLE));
            typeRanking.put (TyLongDouble.class, new Integer (L_DOUBLE));
        }

        /**
         * Creates a new <code>ArithmeticConversionTable</code> instance.
         *
         */
        public ArithmeticConversionTable () {
            buildRankTable ();
        }

        /**
         * Provides an indication of relative ranking such that conversion(s)
         * can be performed.
         * <ul>Possible values:
         * <li><code>NONE</code> : no conversion required (equivalent types)
         * <li><code>ATOB</code> : conversion from <code>a</code> to
         * <code>b</code> required
         * <li><code>BTOA</code> : conversion from <code>b</code> to
         * <code>a</code> required
         * <li><code>INT_PROMOTION</code> : integral promotion required for
         * either or both operands
         * <li><code>INVALID_TYPES</code> : either or both operands have non-
         * arithmetic types.
         *</ul>
         * @param a an operand
         * @param b an operand
         * @return an indication of ranking, as above
         */
        public int get (TypeNode a, TypeNode b) {
            int conversionCode = NONE;
            Object amatch = typeRanking.get (a.getClass ());
            Object bmatch = typeRanking.get (b.getClass ());
            if (amatch == null || bmatch == null) {
                conversionCode = INVALID_TYPES;
            } else {
                int arank = ((Integer) amatch).intValue ();
                int brank = ((Integer) bmatch).intValue ();
                if (arank < INTEGER || brank < INTEGER) {
                    conversionCode = INT_PROMOTION;
                } else {
                    conversionCode =
                        (arank < brank) ? ATOB
                        : (arank > brank) ? BTOA
                        : NONE ;
                }
            }
            return conversionCode;
        }

        public int getRanking (TypeNode t) {
            Integer match = (Integer) typeRanking.get (t.getClass ());
            if (match == null)
                Assert.apology (NON_ARITHMETIC_TYPE, t.getTypeString ());
            return match.intValue ();
        }
    }


    /**
     * A table indicating the category of conversion required to convert
     * one type to another. The table is comprised of standard conversions
     * <em>only</em>. It does not contain knowledge of user-defined conversions
     * or class hierarchies.
     */
    public class StandardConversionTable {
        private Hashtable table = new Hashtable ();
        private Hashtable uninterestingTable = new Hashtable ();

        private final Integer _ARRAY_TO_POINTER = new Integer (ARRAY_TO_POINTER);
        private final Integer _INT_PROMOTION = new Integer (INT_PROMOTION);
        private final Integer _FLOAT_PROMOTION = new Integer (FLOAT_PROMOTION);
        private final Integer _INT_CONVERSION = new Integer (INT_CONVERSION);
        private final Integer _FLOAT_CONVERSION = new Integer (FLOAT_CONVERSION);
        private final Integer _FLOAT_INT_CONVERSION = new Integer (FLOAT_INT_CONVERSION);
        private final Integer _BOOL_CONVERSION = new Integer (BOOL_CONVERSION);
        private final Integer _PTR_TO_BOOL = new Integer (PTR_TO_BOOL);
        private final Integer _EQUIVALENT_TYPES = new Integer (EQUIVALENT_TYPES);


        private void buildTable () {
            Hashtable cvt = new Hashtable ();

            // bool
            table.put (TyBool.class, cvt);
            cvt.put (TyBool.class, _EQUIVALENT_TYPES);
            cvt.put (TyChar.class, _BOOL_CONVERSION);
            cvt.put (TySignedChar.class, _BOOL_CONVERSION);
            cvt.put (TyUnsignedChar.class, _BOOL_CONVERSION);
            cvt.put (TyShortInt.class, _BOOL_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _BOOL_CONVERSION);
            cvt.put (TyInt.class, _BOOL_CONVERSION);
            cvt.put (TyUnsignedInt.class, _BOOL_CONVERSION);
            cvt.put (TyLong.class, _BOOL_CONVERSION);
            cvt.put (TyUnsignedLong.class, _BOOL_CONVERSION);
            cvt.put (TyFloat.class, _BOOL_CONVERSION);
            cvt.put (TyDouble.class, _BOOL_CONVERSION);
            cvt.put (TyLongDouble.class, _BOOL_CONVERSION);
            cvt.put (TyArray.class, _PTR_TO_BOOL);
            cvt.put (TyPointer.class, _PTR_TO_BOOL);

            // char
            cvt = new Hashtable ();
            table.put (TyChar.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _EQUIVALENT_TYPES);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // signed char
            cvt = new Hashtable ();
            table.put (TySignedChar.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _EQUIVALENT_TYPES);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // unsigned char
            cvt = new Hashtable ();
            table.put (TyUnsignedChar.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _EQUIVALENT_TYPES);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // short int
            cvt = new Hashtable ();
            table.put (TyShortInt.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _EQUIVALENT_TYPES);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // unsigned short int
            cvt = new Hashtable ();
            table.put (TyUnsignedShortInt.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _EQUIVALENT_TYPES);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // int
            cvt = new Hashtable ();
            table.put (TyInt.class, cvt);
            cvt.put (TyBool.class, _INT_PROMOTION);
            cvt.put (TySignedChar.class, _INT_PROMOTION);
            cvt.put (TyChar.class, _INT_PROMOTION);
            cvt.put (TyUnsignedChar.class, _INT_PROMOTION);
            cvt.put (TyShortInt.class, _INT_PROMOTION);
            cvt.put (TyUnsignedShortInt.class, _INT_PROMOTION);
            cvt.put (TyInt.class, _EQUIVALENT_TYPES);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // unsigned int
            cvt = new Hashtable ();
            table.put (TyUnsignedInt.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _EQUIVALENT_TYPES);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // long
            cvt = new Hashtable ();
            table.put (TyLong.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _EQUIVALENT_TYPES);
            cvt.put (TyUnsignedLong.class, _INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // unsigned long
            cvt = new Hashtable ();
            table.put (TyUnsignedLong.class, cvt);
            cvt.put (TyBool.class, _INT_CONVERSION);
            cvt.put (TySignedChar.class, _INT_CONVERSION);
            cvt.put (TyChar.class, _INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _INT_CONVERSION);
            cvt.put (TyShortInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _INT_CONVERSION);
            cvt.put (TyInt.class, _INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _INT_CONVERSION);
            cvt.put (TyLong.class, _INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _EQUIVALENT_TYPES);
            cvt.put (TyFloat.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_INT_CONVERSION);

            // float
            cvt = new Hashtable ();
            table.put (TyFloat.class, cvt);
            cvt.put (TyBool.class, _FLOAT_INT_CONVERSION);
            cvt.put (TySignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyFloat.class, _EQUIVALENT_TYPES);
            cvt.put (TyDouble.class, _FLOAT_CONVERSION);
            cvt.put (TyLongDouble.class, _FLOAT_CONVERSION);

            // double
            cvt = new Hashtable ();
            table.put (TyDouble.class, cvt);
            cvt.put (TyBool.class, _FLOAT_INT_CONVERSION);
            cvt.put (TySignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_PROMOTION);
            cvt.put (TyDouble.class, _EQUIVALENT_TYPES);
            cvt.put (TyLongDouble.class, _FLOAT_CONVERSION);

            // long double
            cvt = new Hashtable ();
            table.put (TyLongDouble.class, cvt);
            cvt.put (TyBool.class, _FLOAT_INT_CONVERSION);
            cvt.put (TySignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedChar.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedShortInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedInt.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyUnsignedLong.class, _FLOAT_INT_CONVERSION);
            cvt.put (TyFloat.class, _FLOAT_CONVERSION);
            cvt.put (TyDouble.class, _FLOAT_CONVERSION);
            cvt.put (TyLongDouble.class, _EQUIVALENT_TYPES);

            // uninteresting conversions to bool

            Hashtable set = new Hashtable() ;
            uninterestingTable.put( TyBool.class, set ) ;
            set.put (TyBool.class, Boolean.TRUE ) ;

            // uninteresting conversions to char, signed char and unsigned char

            set = new Hashtable () ;
            uninterestingTable.put( TyChar.class, set ) ;
            uninterestingTable.put( TySignedChar.class, set ) ;
            uninterestingTable.put( TyUnsignedChar.class, set ) ;
            set.put (TyChar.class, Boolean.TRUE);
            set.put (TySignedChar.class, Boolean.TRUE);
            set.put (TyUnsignedChar.class, Boolean.TRUE);

            // uninteresting conversions to short

            set = new Hashtable ();
            uninterestingTable.put (TyShortInt.class, set);
            set.put (TyShortInt.class, Boolean.TRUE);

            // uninteresting conversions to unsigned short

            set = new Hashtable ();
            uninterestingTable.put (TyUnsignedShortInt.class, set);
            set.put (TyUnsignedShortInt.class, Boolean.TRUE);

            // uninteresting conversions to int

            set = new Hashtable ();
            uninterestingTable.put (TyInt.class, set);
            set.put (TyShortInt.class, Boolean.TRUE);
            set.put (TyUnsignedShortInt.class, Boolean.TRUE);
            set.put (TyInt.class, Boolean.TRUE);

            // uninteresting conversions to unsigned int

            set = new Hashtable ();
            uninterestingTable.put (TyUnsignedInt.class, set);
            set.put (TyUnsignedShortInt.class, Boolean.TRUE);
            set.put (TyUnsignedInt.class, Boolean.TRUE);

            // uninteresting conversions to long int

            set = new Hashtable ();
            uninterestingTable.put (TyLong.class, set);
            set.put (TyShortInt.class, Boolean.TRUE);
            set.put (TyUnsignedShortInt.class, Boolean.TRUE);
            set.put (TyInt.class, Boolean.TRUE);
            set.put (TyUnsignedInt.class, Boolean.TRUE);
            set.put (TyLong.class, Boolean.TRUE);

            // uninteresting conversions to unsigned long int

            set = new Hashtable ();
            uninterestingTable.put (TyUnsignedLong.class, set);
            set.put (TyUnsignedShortInt.class, Boolean.TRUE);
            set.put (TyUnsignedInt.class, Boolean.TRUE);
            set.put (TyUnsignedLong.class, Boolean.TRUE);

            // uninteresting conversions to float

            set = new Hashtable ();
            uninterestingTable.put (TyFloat.class, set);
            set.put (TyFloat.class, Boolean.TRUE);

            // uninteresting conversions to double

            set = new Hashtable ();
            uninterestingTable.put (TyDouble.class, set);
            set.put (TyDouble.class, Boolean.TRUE);

            // uninteresting conversions to long double

            set = new Hashtable ();
            uninterestingTable.put (TyLongDouble.class, set);
            set.put (TyLongDouble.class, Boolean.TRUE);
        }

        /**
         * Creates a new <code>StandardConversionTable</code> instance,
         * building the table.
         */
        public StandardConversionTable () { buildTable (); }

        /**
         * Gives the conversion code corresponding to a conversion from
         * type <code>from</code> to type <code>to</code>
         * <br> See constants defined in <code>StandardConversions</code>
         * for recognized conversion codes.
         * @param from the <code>TypeNode</code> value to convert from
         * @param to the <code>TypeNode</code> value to convert to
         * @param uninteresting an array of one boolean. To be set to whether this is a boring conversion.
         * @return the conversion code
         */
        public int getConversion (TypeNode from, TypeNode to) {
            int ctype = NONE;
            Hashtable possible = (Hashtable) table.get (to.getClass ());
            if (possible != null) {
                Integer t = (Integer) possible.get (from.getClass ());
                if (t != null) {
                    ctype = t.intValue() ;
                }
            }
            return ctype;
        }

        public boolean getUninteresting( TypeNode from, TypeNode to ) {
            boolean result = false ; // default is interesting
            Hashtable set = (Hashtable) uninterestingTable.get (to.getClass ());
            if( set != null && set.get (from.getClass ()) != null )
                result = true ;
            return result ;
        }
    }


    /** Represents the unambiguous and complete relationship between two
     * classes that permits up/down conversion from one to the other
     */
    public class ClassConversionRelation {
        public TyAbstractClass from, to;
        public int conversionType;
        public Vector path = new Vector ();

        public ClassConversionRelation (TyAbstractClass from,
                                        TyAbstractClass to) {
            this.from = from;
            this.to = to;
            determineRelation ();
        }

        public int [] getPath () {
            int [] path_array = new int [path.size ()];

            for (int i = 0; i < path.size (); i++)
                path_array [i] = ((Integer) (path.elementAt (i))).intValue ();

            return path_array;
        }

        private void determineRelation () {
            if (from.equal_types (to)) conversionType = EQUIVALENT_TYPES; /*CHECK*/
            else {
                // test for "from" as superclass of "to"
                int numMatches = buildClassRelation (0, to, from);
                switch (numMatches) {
                case 0: // test for "to" as a superclass of "from"
                    numMatches = buildClassRelation (0, from, to);
                    switch (numMatches) {
                    case 0: // no path found in either direction
                        conversionType = NONE;
                        break;
                    case 1: // "to" is an unambiguous base class of "from"
                        conversionType = UP_CONVERSION;
                        break;
                    default: // ambiguous derivation path
                        conversionType = AMBIGUOUS;
                        path.removeAllElements ();
                        break;
                    }
                    break;
                case 1: // "from" is an unambiguous base class of "to"
                    conversionType = DOWN_CONVERSION;
                    break;
                default: // ambiguous derivation path
                    conversionType = AMBIGUOUS;
                    path.removeAllElements ();
                    break;
                }
            }
        }

        /* build class relation information assuming "test" is a superclass
           of "in" */
        private int buildClassRelation (int depth,
                                        TyAbstractClass in,
                                        TyAbstractClass test)
        {
            // how many times does "test" appear as a superclass (direct or
            // indirect) from this point in the lattice?
            int count = 0;

            // put a placeholder in the path for this depth (changed in the
            // loop
            path.addElement (new Integer (-1));

            for (int i = 0; i < in.superClassCount (); i++) {
                // get the immediate superclass at this position
                TyAbstractClass superClass = in.getSuperClass (i);

                // if a match hasn't already been found from this branch of
                // the lattice, set the path to include the position of
                // this superclass (replacing the value currently stored for
                // this depth)
                if (count == 0)	path.insertElementAt (new Integer (i), depth);

                if (superClass.equal_types (test)) { // a match /*CHECK*/
                    count ++; // increment # matches found

                } else { // keep looking up the class hierarchy for a match
                    count += buildClassRelation (depth + 1, superClass, test);
                }
            }

            // if no matches were found from this point in the lattice, remove
            // the last element in the path (this will be the element for
            // this depth)
            if (count == 0) path.removeElementAt (depth);

            return count;
        }

    }

}
