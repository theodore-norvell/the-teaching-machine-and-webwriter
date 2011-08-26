/*
 * Created on Jun 16, 2011 by Theodore S. Norvell. 
 */
package demo;

import java.util.Iterator;
import java.awt.geom.Rectangle2D;

import demo.model.*;
import higraph.model.interfaces.*;
import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.layout.HigraphLayoutManager;
import higraph.view.layout.NodeLayoutManager;

public class DemoBoxesInBoxesLayout
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends NodeLayoutManager<NP,EP,HG,WG,SG,N,E>
{
    final int LEFT_MARGIN = 5 ;
    final int RIGHT_MARGIN = 5 ;
    final int TOP_MARGIN = 5 ;
    final int BOTTOM_MARGIN = 5 ;
    final int VERTICAL_GAP = 5 ;
    final int HORIZONTAL_GAP = 5 ;
    final int LEAF_WIDTH = 10 ;
    final int LEAF_HEIGHT = 10 ;
    
    public DemoBoxesInBoxesLayout<NP,EP,HG,WG,SG,N,E> getThis() {
        return this ;
    }

    @Override
    public void layoutLocal(HigraphView<NP,EP,HG,WG,SG,N,E> view) {
        /* Do a layout local on the top nodes */{
            HigraphView<NP,EP,HG,WG,SG,N,E>.TopIterator it = view.getTops() ;
            while( it.hasNext() ) {
                NodeView<NP,EP,HG,WG,SG,N,E> nv = it.next() ;
                NodeLayoutManager<NP,EP,HG,WG,SG,N,E> lm = nv.getLayoutManager() ;
                if( lm == null ) lm = getThis() ;
                lm.layoutLocal(nv) ; }
        }
        
        double h = 0 ;
        double w = 0 ;
        {   HigraphView<NP,EP,HG,WG,SG,N,E>.TopIterator it = view.getTops() ;
            while( it.hasNext() ) {
                DemoNodeView nv = (DemoNodeView) (it.next()) ;
                Rectangle2D extent = nv.getNextExtent() ;
                nv.translateNextHierarchy(w, 0 ) ;
                if( h < extent.getHeight() ) h = extent.getHeight() ;
                w = w + extent.getWidth() + HORIZONTAL_GAP ; }
        }
    }

    @Override
    public void layoutLocal(NodeView<NP,EP,HG,WG,SG,N,E> view) {
        /*dbg*/System.out.println(">> layoutlocal( "+view+")"); /**/
        final int N = view.getNumChildren() ;
        double w, h ;
        if( N == 0 ) {
            w = LEAF_WIDTH ;
            h = LEAF_HEIGHT ;
        } else {
            // Recurse on the children
            for( int i=0 ; i < N ; ++i ) {
                NodeView<NP,EP,HG,WG,SG,N,E> nv = view.getChild(i) ;
                NodeLayoutManager<NP,EP,HG,WG,SG,N,E> lm = nv.getLayoutManager() ;
                if( lm == null ) lm = getThis() ;
                lm.layoutLocal(nv) ;
            }

            h = TOP_MARGIN ;
            w = 0 ;
            for( int i=0 ; i < N ; ++i ) {
                DemoNodeView nv = (DemoNodeView)( view.getChild(i) );
                Rectangle2D extent = nv.getNextExtent() ;
                /*dbg*/System.out.println("   moving "+nv+" from ("+extent.getMinX()+", "+extent.getMinY()+", "+extent.getWidth()+", "+extent.getHeight()+")"); /**/
                /*dbg*/System.out.println("   moving "+nv+"   to ("+5+", "+h+", "+extent.getWidth()+", "+extent.getHeight()+")"); /**/
                nv.translateNextHierarchy(LEFT_MARGIN, h ) ;
                if( w < extent.getWidth() ) w = extent.getWidth() ;
                h = h + extent.getHeight() + VERTICAL_GAP ;
            }
            w = LEFT_MARGIN + w + RIGHT_MARGIN ;
            h = h - VERTICAL_GAP + BOTTOM_MARGIN ;
        }
        /*dbg*/System.out.println("   placing "+view+" at (0, 0, "+w+", "+h+")"); /**/
        view.placeNext(0, 0) ;
        view.setNextShape( new Rectangle2D.Double(0, 0, w, h) ) ;
        /*dbg*/System.out.println("<< layoutlocal( "+view+")"); /**/
    }

}
