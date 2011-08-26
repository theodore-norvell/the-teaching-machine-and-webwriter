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

/*
 * CGRInvocation.java
 *
 * Created on August 13, 2003, 8:26 AM
 */

package tm.javaLang.analysis;

/**
 *
 * @author  mpbl
 */
import java.util.Enumeration;

import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.TyJava;
import tm.utilities.Assert;


public abstract class CGRInvocation extends CodeGenRule {
    
    static final int EQUAL = 0;
    static final int MORE = 1;
    static final int LESS = 2;
    static final int AMBIGUOUS = 3;

    Java_CTSymbolTable symbolTable;
    /**
     * Creates a new rule instance
     * @param st the compile-time symbol table for id resolution
     */
    public CGRInvocation (Java_CTSymbolTable st) {
        symbolTable = st;
    }
    
    
    DeclarationSet findClosestMethod(DeclarationSet candidates, NodeList args){
        // look for the match among the candidates
        // First (Section 15.12.2.1) Find Methods that are Applicable and Accessible
        DeclarationSetMulti possibles = new DeclarationSetMulti();
        Enumeration matches = candidates.elements();
        while (matches.hasMoreElements()) {
            Declaration possible = (Declaration)matches.nextElement();
            TyFun tyFun = (TyFun)possible.getType();
            if (symbolTable.isAccessible(possible) &&
                tyFun.getParamCount() == args.length() ) {
                    boolean keep = true;
                    for (int i = 0; i < args.length(); i++){
                        TyJava paramType = getBaseType(tyFun.getParamType(i));
                        TyJava callingType = getBaseType(((ExpressionNode)args.get(i)).get_type());
                        if (!(paramType.equal_types(callingType) || paramType.isReachableByWideningFrom(callingType))) {
                            keep = false;
                            break;
                        }
                    }
                    if (keep) possibles.append(possible);
            }
        }
        if (!possibles.isEmpty() && possibles.getSingleMember()==null)
            disambiguate((DeclarationSetMulti)possibles);
        return possibles;
    }
    
    private void disambiguate(DeclarationSetMulti set){
        while (doPass(set))
            if (set.getSingleMember() != null) return;
    }
    
//    Compare the first element in the set to others, removing any others encountered that are
//    less specific, and removing first element (and stopping) if it is less specific than
//    any other. Returns true if a removal made.
//    If false is returned all members of the set are equivalent to the first
//    Assertion. Set has more than one member
    private boolean doPass(DeclarationSetMulti set){
        FunctionDeclaration first = (FunctionDeclaration) set.elementAt(0);
        boolean removal = false;
        for (int i = set.size() - 1; i > 0 ; i--) {
            FunctionDeclaration other = (FunctionDeclaration)set.elementAt(i);
            switch ( compare(first, other)) { 
                case MORE:{
                set.removeElementAt(i);
                removal = true;
                }
                break;
                case LESS: {
                    set.removeElementAt(0);
                    return true;
                }
                case EQUAL: {
                    Assert.error("Found identical methods or constructors " + first.getName().getName() +
                        " and " + other.getName().getName());
                }
                break;
                
                case AMBIGUOUS:
                    break;
            }
        }
        return removal;
    }

    
    // returns MORE if first's paramater set is more specific, LESS if other's is,
    // AMBIGUOUS if ambiguous and EQUAL if all paramaters are same
    private int compare(FunctionDeclaration first, FunctionDeclaration other) {
        // First check the classes or interfaces in which the two functions appear
        TyJava firstType = (TyJava)((SHCommon)first.getDefinition()).getClassDeclaration().getType();
        TyJava otherType = (TyJava)((SHCommon)other.getDefinition()).getClassDeclaration().getType();
        boolean firstMore = otherType.isReachableByWideningFrom(firstType);
        boolean otherMore = false;
        if (!firstMore)otherMore = firstType.isReachableByWideningFrom(otherType);
//        TyJava firstType = getBaseType( first.getParameter(0));
//        TyJava otherType = getBaseType( other.getParameter(0));
//        firstMore = otherType.isWidening(firstType);
//        otherMore = false;
//        if (!firstMore)otherMore = firstType.isWidening(otherType);
        // Continue the check, parameter by parameter
        for (int i = 0; i < first.getParameterCount(); i++) {
            firstType = getBaseType( first.getParameter(i));
            otherType = getBaseType( other.getParameter(i));
            if (firstMore) {
                if (firstType.isReachableByWideningFrom(otherType)) return AMBIGUOUS;    // ambiguous
            }
            else if (otherMore) {
                if (otherType.isReachableByWideningFrom(firstType)) return AMBIGUOUS;    // ambiguous
            }
            else { // neither set yet because arguments so far are exact type match
                firstMore = otherType.isReachableByWideningFrom(firstType);
                if (!firstMore)otherMore = firstType.isReachableByWideningFrom(otherType);
            }
        }
        if (firstMore) return MORE;
        if (otherMore) return LESS;
        return EQUAL; // Actually, identical, which is ambiguous of a sort
    }
    
    private TyJava getBaseType(TypeNode type){
        if (type instanceof TyAbstractPointer)
            return (TyJava)((TyAbstractPointer)type).getPointeeType();
        return (TyJava) type;
    }
                 
    
    
}
