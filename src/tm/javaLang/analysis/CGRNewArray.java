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

package tm.javaLang.analysis;

import java.util.Vector;
import tm.utilities.Assert;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.CGRFetch;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.ExpNewArray;

/**
 * Generates the AST representation for a new array
 * 
 * @author mpbl
 */
public class CGRNewArray extends CodeGenRule {
	private TypeNode elementType;
	private TyClass theObject;
	private Vector initTree = null;
	private int dimensions;
	private JavaExpressionBuilder eBuilder;

	/**
	 *  Constructor.
	 * @param type the type of the elements of the array
	 * @param objectClass the Java Object class
	 * @param iTree initialization vector, may be null
	 * @param dims total no of dimensions of the array
	 * @param eb the JavaExpressionBuilder
	 */
    public CGRNewArray (TypeNode type, TyClass objectClass, Vector iTree, int dims, JavaExpressionBuilder eb) {
    	elementType = type;
    	theObject = objectClass;
    	initTree = iTree;
    	dimensions = dims;
    	eBuilder = eb;
    }

    /**
     * Applies the <code>CGRNewArray</code> rule to the expression
     * The operands of the <code>expPtr</code> contain the expression nodes for each dimension
	 *<P><strong>Precondition:</strong>
	 * <ul><li><code>exp.operandCount()</code> will return the number of dimensions that have
	 * expression nodes. Should not exceed the dimensionality of the array. Must
	 * be zero if there are initializers.</li>
	 * <li><code>exp.get(i)</code> returns the expression node defining the size of the i'th
	 * dimension</li>
	 * <li><code>initTree</code> will contain initializers if there are any. Must be <code>null</code>
	 * if <code>exp.operandCount() > 0</code></li>
	 * </ul>
	 * <p><strong>Postcondition:</strong>
	 * <ul>
	 *     <li><code>exp</code> will be set to an <code>ExpNewArray</code> node</li>
	 * </ul>
     */
   public void apply (ExpressionPtr exp) {
   		Assert.check(exp.operandCount() == 0 || initTree == null);
	    ExpressionNode en = null;
	    NodeList dimList = new NodeList();
	    TyPointer tyPointer = new TyPointer();
	    TyJavaArray tyArray = new TyJavaArray("", theObject);
	    tyPointer.addToEnd(tyArray);
	    for (int i = 0; i < dimensions; i++){
	    	if (i < exp.operandCount())
	    		dimList.addLastChild(exp.get(i));
        	if (i > 0) {
        		TyPointer innerPointer = new TyPointer();
        		tyArray.addToEnd(innerPointer);
        		tyArray = new TyJavaArray("", theObject);
        		innerPointer.addToEnd(tyArray);
        	}
	    }
        tyArray.addToEnd(elementType);
        if (initTree == null)
        	en = new ExpNewArray(tyPointer, dimList);
	 	else {
			typeCheckandConvert(initTree,(TyJavaArray)tyPointer.getPointeeType());
			en = new ExpNewArray(tyPointer, initTree);
   		}
        exp.set(en);
    }
   /**
    * Recursive method to check that the initializer tree against the array and
    * type convert its leaf nodes as necessary
    * <P><strong>Precondition:</strong>
     * <ul><li>the dimensions of the initializer should not exceed those of the
     * array type</li>
     * <li>the initializers are of, or can be
    * converted to, the correct type</li>
     * </ul>
    * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li>the iTree nodes have fetches and conversions inserted as necessary
     *     </li>
     * </ul>
    * @param iTree the initializer expression tree
    * @param tyArray the array (or subArray) to be checked
    */
   private void typeCheckandConvert(Vector iTree, TyJavaArray tyArray){
	  for (int i = 0; i < iTree.size(); i++){
   		TypeNode leafType = tyArray.getElementType();
		if (iTree.elementAt(i)instanceof Vector){
			Assert.check(leafType instanceof TyPointer,"mismatched dimensions in array initializer");			
			TypeNode subArray = ((TyPointer)leafType).getPointeeType();
		   	Assert.check(subArray instanceof TyJavaArray,
		   			"mismatched dimensions in array initializer");
		   	typeCheckandConvert( (Vector)iTree.elementAt(i), (TyJavaArray)subArray);
	   	} else {
			ExpressionNode initNode = (ExpressionNode)iTree.elementAt(i);
			if (!initNode.get_type().equal_types(leafType)){
				ExpressionPtr leafInit = new ExpressionPtr(new Java_ScopedName("arrayElementInitializer"), new Object [] {});
				leafInit.addOperand(leafType);
				leafInit.addOperand(initNode);
				CodeGenRule fetch = new CGRFetch(1);
				fetch.apply(leafInit);
    			CodeGenRule conversion = leafType instanceof TyPointer ?
    					eBuilder.rb.get(JavaExpressionBuilder.R_REF_CONVERSION) :
						eBuilder.rb.get(JavaExpressionBuilder.R_PRIM_ASSIGN_CONVERSION);
				conversion.apply(leafInit);
				iTree.setElementAt(leafInit.get(1),i);
				
			}
		}
	  }
   	
   }
}

