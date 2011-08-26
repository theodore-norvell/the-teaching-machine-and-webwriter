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

package tm.clc.analysis;

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TypeNode;

/**
 * Classes implementing this interface provide a range of type conversion
 * capability. Because type conversion logic is highly interrelated, and
 * often relies on the same data structures, it is provided by one or more
 * classes implementing this interface. Each such class should provide a
 * distinct set of type conversion rules, as appropriate for the language.
 * <p><code>CodeGenRule</code>s are used to provide access to the type conversion
 * logic for the expression building code. Such rules will delegate
 * the appropriate <code>ConversionRules</code> to perform the required task.
 */
public interface ConversionRules {

    /**
     * Provides an indication of the category of conversion required
     * to convert an object of type <code>from</code> to one of type
     * <code>to</code>.
     * <p><em>lvalue-&gt;rvalue transformations</em> and
     * <em>cv-qualification conversions</em> are considered implicit to this
     * routine and are not reflected in the categorization returned.
     * @param from the <code>TypeNode</code> value to convert from
     * @param to the <code>TypeNode</code> value to convert to
     * @return an <code>int</code> categorization of the conversion required.
     * This categorization is language-specific and defined by implementing
     * classes.
     */
    public int determine (TypeNode from, TypeNode to, boolean[] uninteresting) ;

    /**
     * Generates the AST representation of a type conversion, converting
     * an expression of one type to another type. This method should generate
     * the AST representation of <em>type-conversion</em>,
     * <em>cv-qualification conversion</em>, or both if necessary.
     * @param fromExp the expression to convert
     * @param to the type to convert to (including cv qualification)
     * @return an <code>ExpressionNode</code> value representing the
     * original expression with required conversion(s) applied.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to) ;

    /**
     * Generates the AST representation of a type conversion, converting
     * an expression of one type to another type. This method should generate
     * the AST representation of <em>type-conversion</em>,
     * <em>cv-qualification conversion</em>, or both if necessary.
     * @param fromExp the expression to convert
     * @param to the type to convert to (including cv qualification)
     * @param op_image a representation of the conversion operation for
     * display purposes
     * @return an <code>ExpressionNode</code> value representing the
     * original expression with required conversion(s) applied.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to, String op_image) ;

    /**
     * Generates the AST representation of a type conversion, converting
     * an expression of one type to another type. The type of
     * conversion operation to perform is explicitly specified.
     * @param fromExp the expression to convert
     * @param to the type to convert to (including cv qualification)
     * @param op_image a representation of the conversion operation for
     * display purposes
     * @param ccode language-specific value indicating the specific conversion
     * operation to perform
     * @return an <code>ExpressionNode</code> value representing the
     * original expression with required conversion(s) applied.
     */
    public ExpressionNode makeConversionExpression
        (ExpressionNode fromExp, TypeNode to, String op_image, int ccode, boolean uninteresting) ;

    // getConversionSequence
}
