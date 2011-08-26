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

package tm.displayEngine;

import java.awt.Dimension;

import tm.displayEngine.generators.AbstractGenerator;
  
    
  public class LinkedLayoutManager {
    
	  // gap between indirection levels  
	     private static final int HSPACING = 20;
	  // gap between top level datums  
	     private static final int VSPACING = 15;
	     
	     private LinkedDisplay owner;
//	     private Dimension mySize = new Dimension(0,0);
	     
	     public LinkedLayoutManager(LinkedDisplay o){
	         owner = o;
	     }

	 /*  Simple linked layout algorithm whereby increasing levels of indirection result in
	     displacement to the right. The first node is placed top left (at (0,0)). Thereafter
	     as the displayTree is iterated through each new node is examined for indirection level.
	     If the level has increased, it is stepped off a standard distance to the right of the
	     last node.
	         Otherwise it is stepped down and located at the x point that corresponds
	     to its level of indirection. If that is the same as the last node, it will be
	     immediately below it. If it is less, it will be moved back as appropriate (but will
	     still appear stepped down).
	 */
	     public void layoutDisplay(AbstractGenerator generator){

	         int x[] = new int[20];  // twenty levels of indirection!
	         int y[] = new int[20];
	         x[0] = 2;
	         y[0] = 4;
	         int width = 0;
	         int height = 0;
	         
	         int indirectionLevel = 0;
	         int level = 0;
	         
	         LinkedDatumDisplay current = null;
	         LinkedD3Iterator myIterator = generator.getIterator(owner);
//	         myIterator.reset(); reverted to non-singleton iterator of Version 95
	         
	         while (!myIterator.atEnd()){
	  /*          Expander expander = current.getExpander();
	             if (expander == null || !expander.getExpanded())
	                     y[level] += LinkedDatumDisplay.BASE_HEIGHT;*/
	             
	             current = myIterator.getNode();
//	             System.out.println(myIterator.toString());
	             level = current.getIndirection();
	  //           if (level != indirectionLevel) { 
	                 x[level]= x[0] + level * (LinkedDatumDisplay.MY_WIDTH + HSPACING);
	                 y[level] += VSPACING;
	   //          }
	             current.resize(LinkedDatumDisplay.NAME_W,0,LinkedDatumDisplay.VALUE_W,0);    // height will be sized automatically
	             current.move(x[level],y[level]);
	             y[level] += current.getSize().height + VSPACING;
	             if (width < x[level] + LinkedDatumDisplay.MY_WIDTH + HSPACING)
	                 width = x[level] + LinkedDatumDisplay.MY_WIDTH + HSPACING;
	             if (height < y[level] )
	                 height = y[level];
	             indirectionLevel = level;
	             myIterator.increment();
	         }
	         owner.setPreferredSize(new Dimension(width + HSPACING, height + VSPACING));
	     }
  }
