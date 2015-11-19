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

package tm.javaLang.parser;

import java.io.PrintStream;

import tm.utilities.Assert;
import tm.utilities.Debug;

public class SimpleVisitor implements JavaParserVisitor {

  Debug d = Debug.getInstance() ;
  
  public SimpleVisitor() {  }
  
  public Object visit(SimpleNode node, Object data) {
    Assert.apology("You can't use that visit()!");
    return data;
  }

  public void visit(SimpleNode node, int indentLevel) {
    dump(node, indentLevel);

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      visit((SimpleNode) node.jjtGetChild(i), indentLevel + 1);
    }
  }

  protected void dump(SimpleNode n, int in) {
    try {     
      String indentSpace = "";
      for(int i = 0; i < in; i++) { indentSpace += "  "; }
      
      d.msg(Debug.COMPILE,
        indentSpace + n.toString() + " " +
        n.jjtGetNumChildren() + " " +
        n.getBoolean() + " " +
        n.getInt() + " \"" +
        n.getString() + "\" " +
        n.getName() + " " +
        n.getSpecSet() + " " +
        n.getCoords()
      );
    } catch (Throwable e) {
      d.msg(d.COMPILE, "Oops... error in print()!");
      d.msg(d.COMPILE, e);
    }
  }
}