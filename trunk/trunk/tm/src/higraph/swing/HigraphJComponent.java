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
 * Created on 2009-10-09 by Theodore S. Norvell. 
 */
package higraph.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import higraph.view.HigraphView;

public class HigraphJComponent
extends JPanel {
    private static final long serialVersionUID = 4835442084275592940L;
    
    HigraphView<?,?,?,?,?,?,?> view ;
    
    Animator animator  ;
    
    int lengthIn_ms ;
   
    public HigraphJComponent( int lengthOfAnimationIn_ms ) {
    	lengthIn_ms = lengthOfAnimationIn_ms ; 
    }
    
    public HigraphJComponent( ) {
    	this( 500 ) ; // 1/2 a second animation time.
    }
    
    public void setSubgraphView( HigraphView<?,?,?,?,?,?,?> view )
    {
        this.view = view ;
        animator =  new Animator(view) ;
    }
    
    public void refresh() {
    	animator.start( lengthIn_ms ) ;
    }
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if( view != null ) view.drawArea( (Graphics2D) g ) ;
    }
}
