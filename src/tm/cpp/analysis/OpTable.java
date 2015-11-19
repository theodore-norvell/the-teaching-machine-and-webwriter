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

import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;

/**
 * Provides mapping from opcode to String representation, following
 * values established in <code>Cpp.Parser.ParserConstants</code>.
 */

public class OpTable implements ParserConstants {
    private static final String NOT_AN_OPERATOR = "{0} is not an operator.";

    private static final String [] t =
    { "[]", null, "()", null, "::", null, null, ",", null, null, "=", "*=", "/=",
      "%=", "+=", "-=", "<<=", ">>=", "&=", "^=", "|=", "||", "&&", "|", "^", "&", "==",
      "!=", "<", ">", "<=", ">=", "<<", ">>", "+", "-", "*", "/", "%", "++", "--",
      "~", "!", ".", "->", ".*", "->*" };

    private static final String NEW_S = "new";
    private static final String DELETE_S = "delete";
    private static final String SIZEOF_S = "sizeof";
    private static final String TYPEID_S = "typeid";
    private static final String DYNAMIC_CAST_S = "dynamic_cast";
    private static final String CONST_CAST_S = "const_cast";
    private static final String STATIC_CAST_S = "static_cast";
    private static final String REINTERPRET_CAST_S = "reinterpret_cast";
    public static final String OPERATOR_S = "operator";
    private static final int OCSTART = OPEN_BRACKET;

    private static Hashtable opcodes = new Hashtable ();
    static {
        // right now only alpha operators that can be overridden are here
        // - it would be OK and probably good to put all operators in here
        opcodes.put (NEW_S, new Integer (NEW));
        opcodes.put (DELETE_S, new Integer (DELETE));
        opcodes.put (SIZEOF_S, new Integer (SIZEOF));
    }
    public static int get (String opname) {
        Integer opcode = (Integer) opcodes.get (opname);
        return (opcode == null) ? -1 : opcode.intValue ();
    }

    public static String get (int opcode) {
        switch (opcode) {
        case NEW: return NEW_S;
        case DELETE: return DELETE_S;
        case SIZEOF: return SIZEOF_S;
        case TYPEID: return TYPEID_S;
        case DYNAMIC_CAST: return DYNAMIC_CAST_S;
        case CONST_CAST: return CONST_CAST_S;
        case STATIC_CAST: return STATIC_CAST_S;
        case REINTERPRET_CAST: return REINTERPRET_CAST_S;
        case OPERATOR: return OPERATOR_S;
        default:
            int idx = opcode - OCSTART;
            if (idx < 0 || idx > t.length - 1) {
                String image = (opcode >= 0 && opcode < tokenImage.length)
                    ? tokenImage [opcode]
                    : "non-existent token " + opcode;
                Assert.apology (NOT_AN_OPERATOR, image);
            }
            return t [idx];
        }
    }

}

