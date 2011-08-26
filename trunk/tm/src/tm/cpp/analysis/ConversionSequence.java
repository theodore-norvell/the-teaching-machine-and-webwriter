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

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.ConversionRules;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.utilities.Assert;

/**
 * Represents the conversion sequence required from type a to type b.
 * <p><em>Standard</em> conversion sequences consist of zero to three
 * conversions, in the following order:
 * <ol><li>lvalue to rvalue conversion (a fetch)</li>
 * <li>conversion or promotion (base type conversion)
 * <li>cv-qualification conversion </ol>
 * <p><em>User-defined</em> conversion sequences involve a user-defined
 * conversion, possibly preceded and/or followed by a standard conversion
 * sequence.
 * <p><em>Ellipsis</em> conversion sequences represent the covering of
 * arguments past a point by ellipsis in a function's parameter list.
 * @author Derek Reilly
 * @version 1.0
 */
public class ConversionSequence {
    private static final String CONVERSION_LIMIT_EXCEEDED =
        "This sequence cannot exceed {0} conversions";

    /** an identity conversion sequence consists of no conversions */
    public static final int IDENTITY_CS = 0;

    /** a standard conversion sequence has at most three conversions, in
     * order: lvalue->rvalue, promotion/conversion, qualification conversion
     */
    public static final int STANDARD_CS = 3;

    /** a user-defined conversion sequence has at most 7 conversions:
     * standard, user-defined conversion, standard
     */
    public static final int USER_DEFINED_CS = 7;

    /** maximum number of conversions in any sequence */
    public static final int MAX_CONVERSIONS = USER_DEFINED_CS;

    private static ConversionRules sc = StandardConversions.getInstance ();
    // TBD The arguments must not be null!!
    private static CodeGenRule r_function_call = new CGRFunctionCall ( null, null, null);

    private Element [] sequence;
    private int lastElement = -1;
    /**
     * Creates a new <code>ConversionSequence</code> instance.
     * Enough room is made for the maximum number of conversions.
     */
    public ConversionSequence () {
        sequence = new ConversionSequence.Element [MAX_CONVERSIONS];
    }

    /**
     * Creates a new <code>ConversionSequence</code> instance.
     *
     * @param numConversions the number of (or max number of) conversions
     * in this sequence
     */
    public ConversionSequence (int numConversions) {
        sequence = new ConversionSequence.Element [numConversions];
    }

    /**
     * Adds the conversion represented by the conversion code, as defined
     * in <code>StandardConversions</code>.
     *
     * @param code the conversion code.
     */
    //public void addConversion (int code) {
    //    this.addConversion (code, (TypeNode) null);
    //}

    /**
     * Adds the conversions in the conversion sequence
     *
     * @param seq the conversion sequence.
     */
    public void addConversions (ConversionSequence seq) {
        for (int i = 0; i < seq.length (); i++) {
            ConversionSequence.Element el = seq.sequence [i];
            if (el.udfn () != null)
                this.addConversion (el.code (), el.udfn ());
            else
                this.addConversion (el.code (), el.type (), el.uninteresting ());
        }
    }

    /**
     * Adds the conversion represented by the conversion code, as defined
     * in <code>StandardConversions</code>. The target type is also provided.
     *
     * @param code the conversion code.
     * @param target the target type.
     */
    public void addConversion (int code, TypeNode target, boolean uninteresting) {
        if (lastElement < sequence.length - 1) {
            sequence [++lastElement] =
                new ConversionSequence.Element (code, target, uninteresting);
        } else {
            Assert.apology (CONVERSION_LIMIT_EXCEEDED,
                            Integer.toString (sequence.length));
        }
    }


    /**
     * Adds the conversion represented by the conversion code, as defined
     * in <code>StandardConversions</code>. The target type and user-defined
     * function is also provided.
     *
     * @param code the conversion code.
     * @param udfn the user-defined conversion function / constructor.
     */
    public void addConversion (int code, FunctionDeclaration udfn) {
        if (lastElement < sequence.length - 1) {
            sequence [++lastElement] =
                new ConversionSequence.Element (code, udfn);
        } else {
            Assert.apology (CONVERSION_LIMIT_EXCEEDED,
                            Integer.toString (sequence.length));
        }
    }

    /**
     * Identifies whether this sequence is for an identity (i.e. no)
     * conversion.
     *
     * @return true if this is an identity conversion sequence, false otherwise
     */
    public boolean isIdentity () { return lastElement == -1; }

    /**
     * Identifies whether this sequence is for an ellipsis conversion.
     *
     * @return true if this is an ellipsis conversion sequence, false otherwise
     */
    public boolean isEllipsis () {
        return (lastElement == 0 &&
                sequence[0].code () == StandardConversions.ELLIPSIS);
    }

    /**
     * Identifies whether this sequence is for a user-defined conversion.
     *
     * @return true if this is a user-defined conversion sequence, false
     * otherwise
     */
    public boolean isUserDefined () {
        int i = 0;
        for ( ; i <= lastElement; i++)
            if (sequence[i].code () == StandardConversions.USER_DEFINED) break;
        return i <= lastElement;
    }

    /**
     * Identifies whether this is a standard conversion sequence.
     *
     * @return true if this is a standard conversion sequence, false otherwise
     */
    public boolean isStandard () {
        return (isIdentity () || !(isEllipsis () || isUserDefined ()));
    }

    /**
     * Gives the number of conversions in the sequence
     */
    public int length () {
        return lastElement + 1;
    }

    /**
     * Applies the conversion sequence against the provided expression
     */
    public ExpressionNode apply (ExpressionNode fromExp) {
        ExpressionNode result = fromExp;
        for (int i = 0; i < lastElement; i++) {
            Element e = sequence [i];
            switch (e.code ()) {
            case StandardConversions.USER_DEFINED: // function call
                FunctionDeclaration fd = e.userDefinedConversionFunction;
                ExpFunctionName efn = new ExpFunctionName (fd.getName ());
                efn.setMatch (new RankedFunction (fd, 0));
                ExpressionPtr ep =
                    new ExpressionPtr (efn, new Cpp_ScopedName (),
                                        new Object [] {result});
                r_function_call.apply (ep);
                result = ep.get ();
                break;
            case StandardConversions.ELLIPSIS:
                Assert.apology ("ellipsis support not yet implemented");
                break;
            default:
                result = sc.makeConversionExpression (fromExp, e.type (),
                                                      "", e.code (), e.uninteresting() );
                break;
            }

        }
        return result;
    }

    /**
     * Gives the element at the specified index, or <code>null</code> if
     * none exists.
     */
    public ConversionSequence.Element element (int idx) {
        ConversionSequence.Element element = null;
        if (idx >= 0 && idx <= lastElement) element = sequence [idx];
        return element;
    }

    /**
     * Represents a single step in the conversion sequence.
     */
    public class Element {
        private int ccode;
        private boolean uninteresting ;
        private TypeNode targetType = null;
        private FunctionDeclaration userDefinedConversionFunction = null;

        /**
         * Creates a new <code>Element</code> instance.
         *
         * @param ccode the conversion code
         * @param target the target type
         * @param uninteresting Is the conversion too boring to show the user?
         */
        Element (int ccode, TypeNode target, boolean uninteresting) {
            this.ccode = ccode;
            this.targetType = target;
            this.uninteresting = uninteresting ;
        }

        /**
         * Creates a new <code>Element</code> instance.
         *
         * @param ccode the conversion code
         * @param udfn the user-defined conversion
         */
        Element (int ccode, FunctionDeclaration udfn) {
            this.ccode = ccode;
            this.userDefinedConversionFunction = udfn;
            this.targetType = udfn.getType ();
            this.uninteresting = false ;
        }

        /**
         * Returns the conversion code for this element.
         *
         * @return the conversion code
         */
        public int code () { return ccode; }

        /**
         * Returns the target type for this element.
         *
         * @return the target type, or <code>null</code> if not applicable.
         */
        public TypeNode type () { return targetType; }

        /**
         * Returns whether the conversion not worth showing to the user.
         *
         * @return whether the conversion is uninteresinting.
         */
        public boolean uninteresting () { return uninteresting; }

        /**
         * Returns the user defined conversion function for this element.
         *
         * @return the conversion fn, or <code>null</code> if not applicable.
         */
        public FunctionDeclaration udfn () {
            return userDefinedConversionFunction;
        }

    }
}

