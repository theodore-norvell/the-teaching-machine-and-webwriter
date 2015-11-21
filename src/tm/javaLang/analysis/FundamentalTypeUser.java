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

import tm.javaLang.ast.TyBoolean;
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyDouble;
import tm.javaLang.ast.TyFloat;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyLong;
import tm.javaLang.ast.TyShort;
import tm.javaLang.ast.TyVoid;

/** 
 * Provides instance of each fundamental type, with no cv-qualification, 
 */
public interface FundamentalTypeUser {
    public static final TyBoolean tyBoolean = TyBoolean.get (); 
    public static final TyChar tyChar = TyChar.get (); 
    public static final TyByte tyByte = TyByte.get (); 
    public static final TyDouble tyDouble = TyDouble.get (); 
    public static final TyFloat tyFloat = TyFloat.get (); 
    public static final TyInt tyInt = TyInt.get (); 
    public static final TyLong tyLong = TyLong.get (); 
    public static final TyShort tyShort = TyShort.get (); 
    public static final TyVoid tyVoid = TyVoid.get ();
		
}
