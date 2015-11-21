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
import tm.clc.ast.DefaultExpressionNode;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.SelectorLeftToRight;
import tm.clc.ast.StepperBasic;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.Datum;
import tm.javaLang.datum.ArrayDatum;
import tm.javaLang.datum.PointerDatum;
import tm.languageInterface.NodeInterface;
import tm.utilities.Assert;
import tm.utilities.AssertException;
import tm.virtualMachine.VMState;


/*******************************************************************************
Class: ExpNewArray

Overview:
The new operator (for arrays).

*******************************************************************************/

public class ExpNewArray extends DefaultExpressionNode {

    Vector initializerTree ;
    /** Use this constructor for allocation expressions such as
     * <pre>
     *    new t[e0][e1][e2][][]
     * </pre> 
     * 
     * Preconditions.
     * <ul>
     *    <li> The dimensions parameter is not null and has a length of at least 1.
     * </ul>
     * 
     * @param t should be a TyPointer with a pointee type that is a TyJavaArray
     *        has an element type that is a TyPointer with a pointee type that is
     *        a TyJavaArray and so on until we get to the number of dimensions in
     *        in the expression. (E.g. 5 levels in the example  The element type of
     *        the last TyJavaArray is of course a TypeNode representing type T (if T is primitive)
     *        or a TyPointer to a TypeNode representing type T (if T is a "reference" type).
     * @param dimensions This NodeList has one expression per dimension expression
     *        given. In the example, the NodeList would consist of 3 nodes, one for e0,
     *        one for e1, and one for e2.
     */
    public ExpNewArray(TyPointer t, NodeList dimensions ) {
        super("ExpNewArray", dimensions);
        Assert.check( dimensions.length() > 0 );
        TypeNode pointee = t.getPointeeType();
        Assert.check(pointee instanceof TyJavaArray);
        
        this.initializerTree = null ;

        set_type(t);
        set_selector(SelectorLeftToRight.construct());
        set_stepper(StepperNewArray.construct());

        String[] syntax = new String[childCount() + 1];

        // Figure out how many dimensions there are (count)
        // and the name of the base type.
        int [] count = new int[] {0} ;
        String finalTypeString  = countDimensionsAndFindTypeString( t, count ) ;

        int filledDim = dimensions.length();
        Assert.check(filledDim <= count[0]);
        
        
        // Here we are dealing with an expression like
        //   new T [e0][e1][e2][][]
        syntax[0] = "new " + finalTypeString + "[";
        
        int i = 1;
        while(i < filledDim) {
            syntax[i] = "][";
            i++ ; }
        
        int lastElement = i;
        syntax[lastElement] = "]";
        
        while(i < count[0]) {
            syntax[lastElement] += "[]";
            i++ ; }
        set_syntax(syntax);
    }

    /** Use this constructor for allocation expressions such as
     * <pre>
     *    new T[][][] {{a,b}, c, {{d}}} 
     * </pre>
     * 
     * Preconditions.
     * <ul>
     *    <li> The initializer tree is not null and is made up of
     *         Vectors and ExpressionNodes of the appropriate types.
     * </ul>
     * 
     * @param t should be a TyPointer with a pointee type that is a TyJavaArray
     *        has an element type that is a TyPointer with a pointee type that is
     *        a TyJavaArray and so on until we get to the number of dimensions in
     *        in the expression. (E.g. 3 in the example.)  The element type of
     *        the last TyJavaArray is of course a TypeNode representing type T (if T is primitive)
     *        or a TyPointer to a TypeNode representing type T (if T is a "reference" type).
     *@param initializerTree. This should be as long as the array initializer (3 in the example).
     *        Each element of the Vector must either be an ExpressionNode or a Vector
     *        respecting the same restriction (recursively on down). Any expressions in
     *        the initializer tree must match (exactly) the type appropriate to that
     *        level in the tree. So in the example, the a and b Expression nodes
     *        must evaluate to datums of type TyPointer representing "int[]";
     *        the c node is similar, but must represent an "int[][]"; finally the d
     *        node must evaluate to an IntDatum.
     */
    public ExpNewArray(TyPointer t, Vector initializerTree ) {
        super("ExpNewArray");
        TypeNode pointee = t.getPointeeType();
        Assert.check(pointee instanceof TyJavaArray);
        
        this.initializerTree = initializerTree ;
        addInitializerTreeNodesAsChildren( initializerTree ) ;

        set_type(t);
        set_selector(SelectorLeftToRight.construct());
        set_stepper(StepperNewArray.construct());

        String[] syntax = new String[childCount() + 1];

        // Figure out how many dimensions there are (count)
        // and the name of the base type.
        int [] count = new int[] {0} ;
        String finalTypeString  = countDimensionsAndFindTypeString( t, count ) ;
        
        // In this case we are dealing with an expression like this
        //    new[][][] {{a,b},c,{{d}}}
        syntax[0] = "new " + finalTypeString ;
        for( int i=0 ; i<count[0] ; ++i ) syntax[0] += "[]" ;
        
        fillInSyntax( new int[] {0}, syntax, initializerTree ) ;
 
        set_syntax(syntax);
    }
    
    private String countDimensionsAndFindTypeString( TyPointer t, int[] count ) {
            TypeNode tPrime = t;
            count[0]  = 0 ;
            while(true) {
                if(tPrime instanceof TyJavaArray) {
                    tPrime = ((TyJavaArray) tPrime).getElementType();
                    count[0]++;
                } else if(tPrime instanceof TyAbstractPointer) {
                    tPrime = ((TyPointer) tPrime).getPointeeType();
                } else { break; }
            }
            return tPrime.getTypeString() ;
    }

    /** Add the nodes from the initializer tree as children so they'll be 
     * evaluated.
     * @param initializerTree
     */
    private void addInitializerTreeNodesAsChildren(Vector v) {
        for( int i=0, sz=v.size() ; i<sz ; ++i ) {
            Object elem = v.elementAt(i) ;
            if( elem instanceof ExpressionNode ) {
                addLastChild( (ExpressionNode) elem ) ; }
            else {
                Assert.check( elem instanceof Vector ) ;
                addInitializerTreeNodesAsChildren( (Vector)elem ) ; } }
    }

    /** Fill in the syntax array based on the initializer tree.
     * 
     * @param c A counter that says which part of the syntax array to work on.
     * @param syntax The syntax array
     * @param tree The tree to process: either an ExpressionNode or Vector.
     */
    private void fillInSyntax(int[] c, String[] syntax, Object tree) {
        if( tree instanceof ExpressionNode ) {
            c[0] += 1 ;
            syntax[ c[0 ] ] = "" ; }
        else {
            Vector v = (Vector) tree ;
            int sz = v.size() ;
            if( sz == 0 ) syntax[ c[0] ] += "{}" ;
            else {
                for( int i=0 ; i<sz ; ++i ) {
                    syntax[ c[0] ] += i==0 ? "{" : ", " ;
                    fillInSyntax( c, syntax, v.elementAt(i) ) ; }
                syntax[ c[0 ] ] += "}" ; } }
    }
}

class StepperNewArray extends StepperBasic {
    
    private static StepperNewArray instance;

    static StepperNewArray construct(){
        if(instance == null) instance = new StepperNewArray();
        return instance;
    }

    public AbstractDatum inner_step(ExpressionNode nd, VMState vms) {
        ExpNewArray nd1 = (ExpNewArray) nd;

        Clc_ASTUtilities util
         = (Clc_ASTUtilities) vms.getProperty("ASTUtilities");

        Assert.check(nd1.get_type() instanceof TyAbstractPointer);
        TyPointer ptrType = (TyPointer) nd1.get_type();
        Assert.check(ptrType.getPointeeType() instanceof TyJavaArray);
        TyJavaArray arrayType = (TyJavaArray) ptrType.getPointeeType();

        PointerDatum ptr = (PointerDatum) util.scratchDatum(ptrType, vms);

        if(nd1.initializerTree == null ) {
            // We allocate the array(s) according to the dimensions
            // and default initialize.
            allocateArray(ptr, nd1, 0, arrayType, vms); }
        else {
            // We allocate the array(s) according to the initializer tree
            // and initialize using the expressions
            traverseInitializerTree( ptr, nd1.initializerTree, arrayType, vms ) ; }

        return ptr;
    }

    /** Allocate and initialize arrays based on an array initializer.
     * @param arrayPtr
     * @param tree
     * @param arrayType
     * @param vms
     */
    private void traverseInitializerTree(
            PointerDatum arrayPtr,
            Vector tree,
            TyJavaArray arrayType,
            VMState vms) {
        
        // Make array on the heap & store address in the pointer 
        int length = tree.size() ;
        ArrayDatum newArray = arrayType.makeArrayDatum(vms, length);
        arrayPtr.putValue(newArray);
        
        // Now we initialize the array
        for( int i=0 ; i < length ; ++i ) {
            AbstractDatum targ = newArray.getElement( i ) ;
            Object obj = tree.elementAt(i) ;
            if( obj instanceof ExpressionNode ) {
                // In this case the expression is used to initialize the
                // element.
                ExpressionNode expNd = (ExpressionNode) obj ;
                // expNd will also be child and will have been mapped
                // to a datum already
                AbstractDatum src = (AbstractDatum) vms.top().at( expNd ) ;
                // Copy the bytes with 0 fill if we run out.
                int source_size = src.getNumBytes() ;
                for( int j=0, sz = targ.getNumBytes() ; j<sz ; ++j ) {
                    if( j < source_size ) {
                        targ.putByte(j, src.getByte(j)) ; }
                    else {
                        targ.putByte(j, 0 ) ; } }
                
            }
            else {
                // Here we have an array initializer.
                Vector v = (Vector) obj ;
                

                // We must be initializing a pointer to an array
                TypeNode elementType = arrayType.getElementType() ;
                Assert.check( elementType instanceof TyPointer ) ;
                TypeNode subArrayType = ((TyPointer)elementType).getPointeeType() ;
                Assert.check( subArrayType instanceof TyJavaArray ) ;
                
                // So the target is a PointerDatum.
                // We initialize the pointer datum with by allocating an array.
                traverseInitializerTree( (PointerDatum) targ, v, (TyJavaArray) subArrayType, vms ) ;
            }
        }        
    }

    private void allocateArray(PointerDatum arrayPtr, ExpNewArray nd,
                               int child, TyJavaArray t, VMState vms) {
        NodeInterface theChild = (NodeInterface) nd.child(child);
        
        Object temp = vms.top().at(theChild);
        Assert.check(temp instanceof AbstractIntDatum, "array allocation requires integer size");
        AbstractIntDatum dimDatum = (AbstractIntDatum) temp;
        long dim = dimDatum.getValue();
        
        Assert.unsupported(dim >= 0, "NegativeArraySizeException", true) ;

        int elements = (int) dim;
        ArrayDatum newArray = t.makeArrayDatum(vms, elements);
        arrayPtr.putValue(newArray);

        if(nd.childCount() > (child + 1)) { // use subroutine in nodes
            for(int i = 0; i < elements; i++) {
                AbstractDatum test = newArray.getElement(i);
                Assert.check(newArray.getElement(i) instanceof PointerDatum,
                		 "non-pointer element");
                PointerDatum ptr = (PointerDatum) newArray.getElement(i);
                TyJavaArray type = (TyJavaArray) ((TyPointer) t.getElementType()).getPointeeType();
                allocateArray(ptr, nd, child + 1, type, vms);
            }
        }
    }
}