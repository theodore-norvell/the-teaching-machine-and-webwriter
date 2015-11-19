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

import tm.clc.ast.TypeNode;
import tm.cpp.ast.TyBool;
import tm.cpp.ast.TyChar;
import tm.cpp.ast.TyDouble;
import tm.cpp.ast.TyFloat;
import tm.cpp.ast.TyInt;
import tm.cpp.ast.TyLong;
import tm.cpp.ast.TyLongDouble;
import tm.cpp.ast.TyNone;
import tm.cpp.ast.TyShortInt;
import tm.cpp.ast.TySignedChar;
import tm.cpp.ast.TyUnsignedChar;
import tm.cpp.ast.TyUnsignedInt;
import tm.cpp.ast.TyUnsignedLong;
import tm.cpp.ast.TyUnsignedShortInt;
import tm.cpp.ast.TyVoid;

/** 
 * Provides instance of each fundamental type, with no cv-qualification, and
 * const versions of those fundamental types needed for representation of 
 * literal values.
 */
public interface FundamentalTypeUser {
    public static final TyBool tyBool = TyBool.get (); 
    public static final TyChar tyChar = TyChar.get (); 
    public static final TyDouble tyDouble = TyDouble.get (); 
    public static final TyFloat tyFloat = TyFloat.get (); 
    public static final TyInt tyInt = TyInt.get (); 
    public static final TyLong tyLong = TyLong.get (); 
    public static final TyLongDouble tyLongDouble = TyLongDouble.get (); 
    public static final TyShortInt tyShortInt = TyShortInt.get (); 
    public static final TySignedChar tySignedChar = TySignedChar.get (); 
    public static final TyUnsignedChar tyUnsignedChar = TyUnsignedChar.get (); 
    public static final TyUnsignedInt tyUnsignedInt = TyUnsignedInt.get (); 
    public static final TyUnsignedLong tyUnsignedLong = TyUnsignedLong.get (); 
    public static final TyUnsignedShortInt tyUnsignedShortInt = TyUnsignedShortInt.get (); 
    public static final TyVoid tyVoid = TyVoid.get ();
 
	public static final TyNone tyNone = TyNone.get ();

	// need const versions of certain fundamental types (literals)
	public static final TypeNode  ctyInt = TyInt.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyUnsignedInt = TyUnsignedInt.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyChar = TyChar.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyBool = TyBool.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyLong = TyLong.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyUnsignedLong = TyUnsignedLong.get (Cpp_Specifiers.CVQ_CONST); 
	public static final TypeNode  ctyFloat = TyFloat.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyDouble = TyDouble.get (Cpp_Specifiers.CVQ_CONST);
	public static final TypeNode  ctyLongDouble = TyLongDouble.get (Cpp_Specifiers.CVQ_CONST);


		
}
