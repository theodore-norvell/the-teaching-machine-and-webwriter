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

package tm.portableDisplays;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
//import java.util.*;

/*
    An iterator class for DatumDisplay objects. D3 stands for Datum-DatumDisplay
    and recognizes that Datums and DatumDisplays form tightly coupled pairs.
    Since compound D3's are effectively a tree it is useful to have an iterator
    for them.
    
    The D3 Iterator only walks a single datum. It was designed to walk top level
    compound datums, visiting their descendents, although an iterator can be built
    for any D3 pair.
    
    The tree structure information is actually lodged in the datums. Thus, in practice,
    an iterator walks the datum tree then recovers the associated datum display object.
    
    For display purposes, a d3 pair can be classified as Compound or Scalar. In turn,
    a compound d3 may either be expanded or unexpanded. Leaves hold either a scalar datum
    or an unexpanded compound datum. Branch nodes hold an expanded compound datum. The D3
    iterator walks from leaf to leaf.
    
*/

public class D3Iterator1 extends Object {
    
    protected tm.portableDisplays.DatumDisplay myRoot;    // The first Datum Display in the generator
    protected tm.portableDisplays.DatumDisplay current;
    protected DataDisplayView theView;

// Public Interface
    public D3Iterator1(tm.portableDisplays.DatumDisplay root, DataDisplayView view){
        myRoot = root;
        current = myRoot;
        theView = view;
    }
    
 
    public void reset(){
        current = myRoot;
    }
    
    public void increment(){
        //Assert.check(current != null);//TODO reinstate
        Datum next = oneStep(current.getDatum());  // one step from current
   // Keep stepping until a scalar datum is found or we run out
        while (next != null && !isLeaf(next)) {
            next = oneStep(next);
        }
        current = (next == null) ? null :
        	tm.portableDisplays.DatumDisplay.getAssociated(next, theView);
//       System.out.println(current == null ? "null" : current.toString()); // Tree trace
 
    }
    
    // Takes one step to next D3 regardless of whether it is a leaf or branch
    private Datum oneStep(Datum from){
        if (!isLeaf(from))  // If I am compound, drill down
            return from.getChildAt(0);
        Datum parent = from.getParent();
   // Otherwise get younger sib, or younger sib of a previous generation
        while (parent != null) { 
            int nextSib = from.getBirthOrder() + 1;
            if (parent.getNumChildren() > nextSib)
                return parent.getChildAt(nextSib);
            from = parent;
            parent = from.getParent();
        }
        return null;  // run out of datums
    }
    
// Leaves are scalars and unexpanded compounds
    private boolean isLeaf(Datum datum){
        if (datum.getNumChildren()==0) return true;
        DatumDisplay dd = DatumDisplay.getAssociated(datum, theView);
        return (!dd.getExpander().getExpanded());
    }

    public tm.portableDisplays.DatumDisplay getNode(){
        //Assert.check (current != null);//TODO reinstate
        return current;
    }
    
    public boolean atEnd(){
        return (current == null);
    }
    
    public boolean atStart(){
        return (current == myRoot);
    }
    
    public String toString(){
        return "D3Iterator for " + myRoot.toString();
    }
    
} 
