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

import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyBoolean;
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyDouble;
import tm.javaLang.ast.TyFloat;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyLong;
import tm.javaLang.ast.TyShort;
import tm.javaLang.ast.TyVoid;

/*******************************************************************************
Interface: Java_Specifiers

Overview:
This interface represents the Java Specifiers.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public interface Java_Specifiers {
    public static final int SP_BOOLEAN = 0;
    public static final int SP_CHAR = 1;
    public static final int SP_BYTE = 2;
    public static final int SP_SHORT = 3;
    public static final int SP_INT = 4;
    public static final int SP_LONG = 5;
    public static final int SP_FLOAT = 6;
    public static final int SP_DOUBLE = 7;
    public static final int SP_VOID = 8;
    
    public static final int SP_ABSTRACT = 9;
    public static final int SP_FINAL = 10;
    public static final int SP_NATIVE = 11;
    public static final int SP_PUBLIC = 12;
    public static final int SP_PRIVATE = 13;
    public static final int SP_PROTECTED = 14;
    public static final int SP_STATIC = 15;
    public static final int SP_STRICTFP = 16;
    public static final int SP_SYNCHRONIZED = 17;
    public static final int SP_TRANSIENT = 18;
    public static final int SP_VOLATILE = 19;
    
    public static final int SP_NUM_SPECIFIERS = 20;

    public static final String [] spec_strings = new String [] {
        "boolean", "char", "byte", "short", "int", "long", "float",
        "double", "void",
        
        "abstract", "final", "native", "public", "private", "protected",
        "static", "strictfp", "synchronized", "transient", "volatile"
    };
    
    public static final TypeNode [] typeMapping = new TypeNode[]{
        TyBoolean.get(), TyChar.get(), TyByte.get(), TyShort.get(), TyInt.get(),
        TyLong.get(), TyFloat.get(), TyDouble.get(), TyVoid.get()};
}