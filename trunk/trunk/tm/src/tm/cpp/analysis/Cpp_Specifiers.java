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

/** 
 * C++ - specific declaration specifiers/modifiers
 */
public interface Cpp_Specifiers {
    public static final int SP_TYPEDEF = 0;
    public static final int SP_AUTO = 1;
    public static final int SP_REGISTER = 2;
    public static final int SP_STATIC = 3;
    public static final int SP_EXTERN = 4;
    public static final int SP_MUTABLE = 5;
    public static final int SP_INLINE = 6;
    public static final int SP_VIRTUAL = 7;
    public static final int SP_EXPLICIT = 8;
    public static final int SP_FRIEND = 9;
    public static final int SP_PRIVATE = 10;
    public static final int SP_PUBLIC = 11;
    public static final int SP_PROTECTED = 12;

    public static final int SP_CHAR = 13;
    public static final int SP_WCHAR_T = 14;
    public static final int SP_BOOL = 15;
    public static final int SP_SHORT = 16;
    public static final int SP_INT = 17;
    public static final int SP_LONG = 18;
    public static final int SP_SIGNED = 19;
    public static final int SP_UNSIGNED = 20;
    public static final int SP_FLOAT = 21;
    public static final int SP_DOUBLE = 22;
    public static final int SP_VOID = 23;

    public static final int SP_CONST = 24;
    public static final int SP_VOLATILE = 25;
    public static final int SP_CLASS = 26;
    public static final int SP_STRUCT = 27;
    public static final int SP_UNION = 28;
    public static final int SP_ENUM = 29;
    public static final int SP_NUM_SPECIFIERS = 30;

	public static final String [] spec_strings = new String []
	{"typedef", "auto", "register", "static", "extern", "mutable", "inline", 
	 "virtual", "explicit", "friend", "private", "public", "protected", "char",
	 "wchar_t", "bool", "short", "int", "long", "signed", "unsigned", "float",
	 "double", "void", "const", "volatile", "class", "struct", "union", "enum"};
	 

	/** cv-qualification flag for CONST designation */
	public static final int CVQ_CONST    = 0x1;
	/** cv-qualification flag for VOLATILE designation */
	public static final int CVQ_VOLATILE = 0x2;

}
