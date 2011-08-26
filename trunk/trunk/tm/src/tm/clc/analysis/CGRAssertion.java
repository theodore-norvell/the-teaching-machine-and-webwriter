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

import tm.utilities.Assert;

/**
 * <code>CGRAssertion</code> applies a test and generates an apology if
 * it doesn't pass.
 */
public class CGRAssertion extends CodeGenRule {
	private CGRTest test; // the test to apply
	private String failMessage; // message to provide if the assertion fails
	// will use message in CGRTest alone if unset

	public CGRAssertion (CGRTest test) { this.test = test; }
	public CGRAssertion (CGRTest test, String failMessage) { 
		this.test = test;
		this.failMessage = failMessage;
	}

	/**
	 * Applies the related test, doing nothing if the test passes, but
	 * generating an apology if it doesn't.
	 */
	public void apply (ExpressionPtr exp) {
		if (!test.applies (exp)) 
			apologize (test.getMessageOnFalse (exp));
	}

	/**
	 * Generates the "assertion failed" message.
	 */
	public void apologize (String msg) {
		Assert.apology ((failMessage == null) 
						? msg 
						: failMessage + " (" + msg + ")"); 
	}

}

