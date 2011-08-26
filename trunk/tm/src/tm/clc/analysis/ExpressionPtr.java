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

import java.util.Vector;

import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;

/**
 * Wraps around an expression so that the expression can be modified as
 * rules are applied in sequence. This model is used so that different kinds of 
 * modifications can implement <code>CodeGenRule</code>. 
 */
public class ExpressionPtr {
	/** 
	 * when dealing with operands, you sometimes want to deal with the 
	 * expression being built. This is a pretend index into the operands
	 * list that will cause the expression to be accessed instead.
	 */
	protected static final int _EXP = -1;
	private static final String INVALID_REQUEST = 
		"cannot get {0} from {1}, instance of {2}";
	private static final String NO_SUCH_ELEMENT = 
		"asked for element {0} from set of size {1}";
	private ExpressionNode expression; // resulting expression
	private Vector operands; // operands

	/** operator or function id, used in expression display */
	public ScopedName opid; 
	/** operation category, used to determine  which rules to apply. Often
	 *  the same as opid
	 */
	public ScopedName opcat; 

	/**
	 * Creates a new <code>ExpressionPtr</code> instance with no
	 * initial references.
	 */
	public ExpressionPtr () { operands = new Vector (); }

	/**
	 * Creates a new <code>ExpressionPtr</code> instance with an initial 
	 * reference to the provided <code>ExpressionNode</code>
	 * @param exp the <code>ExpressionNode</code> to refer to 
	 */
	public ExpressionPtr (ExpressionNode exp, ScopedName op, 
						   Object [] operands) { 
		this (op, operands);
		expression = exp; 
	}

	/**
	 * Creates a new <code>ExpressionPtr</code> instance with an initial 
	 * reference to the provided <code>ExpressionNode</code>
	 * @param exp the <code>ExpressionNode</code> to refer to 
	 */
	public ExpressionPtr (ExpressionNode exp) {
		expression = exp; 
	}

	/**
	 * Creates a new <code>ExpressionPtr</code> instance with the  
	 * provided raw materials
	 * @param op operator or function id
	 * @param operands list of operands
	 */
	public ExpressionPtr (ScopedName op, Object [] operands) { 
		opid = op;
		opcat = op;
		int opcount = (operands == null) ? 0 : operands.length;
		this.operands = new Vector (opcount);
		for (int i = 0; i < opcount; i++) 
			if (operands [i] != null)
				this.operands.addElement (operands [i]);
	}

	/**
	 * "assignment"
	 * <br><em>Note:</em>
	 * Only expression node references can be set via this method.
	 * Type node references are established once using the corresponding 
	 * constructor; node list references are the same, but their elements 
	 * can be changed via <code>NodeList</code> methods. 
	 * @param exp the <code>ExpressionNode</code> to reference
	 */
	public void set (ExpressionNode e) { expression = e; }

	/**
	 * "assignment"
	 * @param e the entity to add
	 * @param idx the position in which to place it (replacing what was
	 * there). Functions like <code>addOperand</code> if 
	 * <code>idx == operandCount</code>. 
	 */
	public void set (Object e, int idx) { 
		if (idx == _EXP) {
			Assert.check (e instanceof ExpressionNode); 
			expression = (ExpressionNode) e;
		} else { // functions as an addOperand if idx is just right
			if (idx == operands.size ()) operands.addElement (e);
			else operands.setElementAt (e, idx);
		} 
	}

	/**
	 * Adds an operand to the end of the list
	 * @param o the operand to add
	 */
	public void addOperand (Object o) { operands.addElement (o); }
	/**
	 * Inserts an operand at the specified index
	 * @param o the operand to add
	 * @param idx the position in which to insert it
	 */
	public void addOperand (Object o, int idx) { 
		operands.insertElementAt (o, idx);
	}
	/**
	 * Removes the operand at the specified index, shrinking 
	 * the operands list.
	 * @param idx the index of the operand to remove
	 */
	public void removeOperand (int idx) { 
		operands.removeElementAt (idx);
	}

	/**
	 * Removes all operands, leaving the expression if set
	 */
	public void removeAllOperands () {
		operands.removeAllElements ();
	}

	/**
	 * "dereference as expression"
	 * @return the underlying <code>ExpressionNode</code>, or 
	 * <code>null</code> if this points to nothing
	 */
	public ExpressionNode get () { 
		return expression; 
	}

	/**
	 * "dereference as expression"
	 * @return the underlying <code>ExpressionNode</code>, or 
	 * apologize if the wrong type of entity exists at the specified 
	 * position
	 * @param idx the index of the operand to access
	 */
	public ExpressionNode get (int idx) {
		return (idx == _EXP) 
			? expression
			: (ExpressionNode) operandAtAs (idx, ExpressionNode.class);
	}

	/*
	 * "dereference as set of expressions"
	 * @param idx the index of the operand to access
	 * @return <code>NodeList</code> if operand at idx is a 
	 * group of expressions, or apologize if the wrong type of entity 
	 * exists at the specified position
	 */
	public NodeList get_list (int idx) {
		Assert.apology (idx != _EXP, INVALID_REQUEST, new Object []
			{"node list", "final expression", "expression"});
		return (NodeList) operandAtAs (idx, NodeList.class);
	}

	/**
	 * Provides the type of the expression as built so far
	 * @return the <code>TypeNode</code> representing the type of the 
	 * expression 
	 */
	public TypeNode get_type () { return get_type (_EXP); }
	/**
	 * Provides the type of the expression at the specified position (or
	 * the entity at that position if it is in fact a <code>TypeNode</code>)
	 * @param idx the index of the operand
	 * @return the corresponding <code>TypeNode</code>
	 */
	public TypeNode get_type (int idx) {
		TypeNode t = null;
		if (idx == _EXP) { 
			Assert.check (expression != null);
			t = expression.get_type ();
		} else {
			Object operand = operands.elementAt (idx);
			if (operand instanceof ExpressionNode) {
				t = ((ExpressionNode) operand).get_type ();
			} else {
				t = (TypeNode) operandAtAs (idx, TypeNode.class);
			}
		}
		return t;
	}
	
	/* get the operand at idx, ensuring it's a c */
	protected Object operandAtAs (int idx, Class c) {
		Object operand = operands.elementAt (idx);
		Class oc = operand.getClass ();
		Assert.apology (c.isAssignableFrom (oc), 
						INVALID_REQUEST, new Object [] 
			{c.getName (), "operand " + idx, oc.getName ()});
		return operand;
	}

	/**
	 * Provides the "rvalue type" of the expression. This accesses the 
	 * underlying type if an lvalue expression, or the actual type if an 
	 * rvalue expression.
	 * @return the <code>TypeNode</code> representing the rvalue type
	 */
	public TypeNode get_base_type () { return get_base_type (_EXP); }
	/**
	 * Provides the "rvalue type" of the expression or type at the 
	 * specified position. This accesses the underlying type if an lvalue 
	 * expression or reference type, or the actual type if an rvalue 
	 * expression or other type.
	 * @idx the index of the operand
	 * @return the <code>TypeNode</code> representing the rvalue type
	 */
	public TypeNode get_base_type (int idx) {
		TypeNode t = get_type (idx);
		if (t instanceof TyAbstractRef) 
			t = ((TyAbstractRef) t).getPointeeType();
		return t;
	}

	/** 
	 * Indicates whether the expression being built is an instance of 
	 * the provided class (including instance of derived class).
	 * @param c the class to test against
	 * @return <code>true</code> if the expression is assignable to a 
	 * reference of type <code>c</code>, <code>false</code> otherwise.
	 */
	public boolean is (Class c) { return is (c, _EXP); }

	/** 
	 * Indicates whether the operand at the specified location is an instance
	 * of the provided class (including instance of derived class).
	 * @param c the class to test against
	 * @param idx the index of the operand
	 * @return <code>true</code> if the operand is assignable to a 
	 * reference of type <code>c</code>, <code>false</code> otherwise.
	 */
	public boolean is (Class c, int idx) {
		return (idx == _EXP) 
			? c.isAssignableFrom (expression.getClass ())
			: c.isAssignableFrom (operands.elementAt(idx).getClass ());
	}

	/**
	 * Gives the number of operands
	 * @return the number of operands
	 */
	public int operandCount () { 
		return (operands == null) ? 0 : operands.size (); 
	}

	/**
	 * Gives the display version of the operator or function id
	 */
	public ScopedName op () { return opid; }
	/**
	 * Gives the display version of the operator or function id
	 */
	public ScopedName id () { return opid; }

}
