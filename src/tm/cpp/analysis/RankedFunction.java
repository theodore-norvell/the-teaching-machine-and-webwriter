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

import tm.clc.analysis.FunctionDeclaration;

/**
 * Wraps around a <code>FunctionDeclaration</code> that is to be 
 * / has been ranked
 * for conversion of arguments. Rankings are per parameter, and 
 * represent the ranks of the conversions required to use this function
 * with a given set of arguments.
 */
public class RankedFunction { 
    /** the ranked function */
    public FunctionDeclaration declaration;
    /** the rankings by parameter */
    public int [] rankings;
    /** the conversion sequences by parameter */
    //public ConversionSequence [] sequences;
    
    /**
     * Creates a <code>RankedFunction</code> with no rankings yet 
     * assigned.
     * @param fd the function to rank
     * @param paramsToCompare the number of parameters that will be
     * compared against function arguments, in left to right order.
     */
    public RankedFunction (FunctionDeclaration fd, int paramsToCompare) { 
        declaration = fd;
        rankings = new int [paramsToCompare];
        //sequences = new ConversionSequence [paramsToCompare];
    }
    
    /**
     * Adds a ranking.
     *
     * @param idx the (left to right) position of the parameter
     * @param rank the ranking of the parameter
     */
    public void addRanking (int idx, int rank) { rankings [idx] = rank; }
    
    /**
     * Adds a conversion sequence.
     * 
     * @param idx the (left to right) position of the parameter
     * @sequence the conversion sequence required to convert the argument
     * to the parameter type
     */
    /*public void addConversionSequence (int idx, ConversionSequence sequence){
        sequences [idx] = sequence;
    }*/

    /** 
     * Indicates whether or not the conversion sequences for each parameter 
     * have been identified
     */
    /*public boolean conversionSequencesIdentified () {
        boolean defined = true;
        for (int i = 0; i < sequences.length; i++)
            if (sequences [i] == null) defined = false;
        return defined;
    }
    */
    public String toString() {
        String res =  "RankedFunction("+declaration.getRuntimeId() + ", [";
        for( int i = 0, len=rankings.length; i < len ; ++i) {
            res += rankings[i] + (i==len-1 ? "" : ",") ; }
        res += "])" ;
        return res ; }
        
}


