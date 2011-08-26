/* Created on Jun 24, 2011 by Theodore S. Norvell. */
package dijk; /*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV; */
class Heap {
    int N ;
    // The heap invariant is that:
    //     0 < i && i < size implies priority[parent(i)] < priority[i]
    int size ;
    int [] node ;
    int [] priority ;/*#I
    String [] name ; */
    // Location maps nodes to their location in the heap.
    // If a node is not in the heap, the location is -1.
    // Invariant: location[v] != -1 implies 
    //                0 <= location[v] && location[v] < size && node[location[v]]==v
    int [] location ; /*#I
    
      void repositionHeapNodes() {
         int x = 250 ;
         int y = 25;
         int DELTA_Y = 40 ;
         boolean[] marked = new boolean[N] ;
         for( int i=0 ; i < size ; ++i ) {
             int v = -1 ;
             for( int j = 0 ; j < N ; ++j ) {
                 if( location[j] != -1 && !marked[j] && (v == -1 || priority[location[j]] < priority[location[v]] ) )
                     v = j ; }
             marked[v] = true ;
             ScriptManager.relay("HigraphManager", "placeNode", location[v], x, y) ;
             y = y + DELTA_Y ; } }
       
    void setName( String[] name ) {
        this.name = name ; }
*/
    public Heap(int N) { /*#I
        ScriptManager.relay("HigraphManager", "createString", "q" ) ;
        ScriptManager.relay("HigraphManager", "placeString", "q", 250, 0 ) ;
        ScriptManager.relay("HigraphManager", "addToString", "q", "queue" ) ; */
        this.N = N ;
        size = 0 ;
        node = new int [N] ;
        priority = new int [N] ;
        location = new int [N] ;
        for( int v=0 ; v<N ; ++v ) location[v] = -1 ;
    }
    
    private int parent(int i) {
        return (i-1) / 2 ; }
    
    private int left(int i) {
        return (i+1)*2 - 1; }
    
    private int right(int i) {
        return (i+1)*2 ; }
    
    private void swap( int i, int j ) {
        int u = node[i] ;
        int v = node[j] ;
        location[u] = j ;
        location[v] = i ;
        node[i] = v ;
        node[j] = u ;
        int t = priority[i] ;
        priority[i] = priority[j] ;
        priority[j] = t ;
    }
    
    /** Requires: that the heap invariant is obeyed everywhere except that item i's
     * priority may be smaller than its parent's.
     *  Ensures: the heap invariant is restored without changing the set
     * @param i
     */
    private void bubbleUp(int i) {
        // We bubble the item at i up the heap until it reaches the top or a point where it is
        // its priority is less or equal to its parent's.
        while( i != 0 && priority[i] < priority[parent(i)]) {
            swap(i, parent(i) ) ;
            i = parent(i) ; }
    }
    
    /** Requires: that the heap invariant holds everywhere except that item i's
     * priority may be bigger than one or both of its children.
     * Ensures: the heap  invariant is restored without changing the set.
     * @param i
     */
    private void sinkDown( int i ) {
        while( left(i) < size && priority[left(i)] < priority[i] 
               || right(i) < size && priority[right(i)] < priority[i] ) {
            // Swap with the smaller child
            if( right(i) < size && priority[right(i)] < priority[left(i)] ) {
                swap( i, right(i) ) ;
                i = right(i) ; }
            else {
                swap( i, left(i) ) ;
                i = left(i) ; }
        }

    } 
    
    public void enqueue(int v, int pi) {
        // require: location[v] == -1 || pi <= priority[location[i]]
        if( location[v] != -1) {
            // Node v is already in the heap. It's new priority may be lower.
            priority[location[v]] = pi ; /*#I
            String p = Integer.toString(pi) ;
            ScriptManager.relay("HigraphManager", "setNodeExtraLabel", location[v], "priorityLabel", p) ; */
            bubbleUp( location[v] ) ; }
        else {
            node[size] = v ;
            priority[size] = pi ;
            location[v] = size ; /*#I
            ScriptManager.relay("HigraphManager", "makeNode", location[v] ) ;
            ScriptManager.relay("HigraphManager", "setNodeFillColor", location[v], PDV.GREY) ; 
            ScriptManager.relay("HigraphManager", "setNodeNameLabel", location[v], name[v] ) ;
            ScriptManager.relay("HigraphManager", "setNodeNamePosition", location[v], PDV.EAST ) ;
            ScriptManager.relay("HigraphManager", "setNodeValueShow", location[v], false ) ;
            ScriptManager.relay("HigraphManager", "createNodeExtraLabel", location[v], "priorityLabel", PDV.CENTER ) ;
            String p = Integer.toString(pi) ;
            ScriptManager.relay("HigraphManager", "setNodeExtraLabel", location[v], "priorityLabel", p) ; */
            ++size ;
            bubbleUp( size-1 ) ; } /*#I
        repositionHeapNodes() ; */
    }
    
    /** Requires: ! empty() 
     * Ensures: result is the node with the smallest priority in the initial state of the heap
     * and the heap is altered by removing the result. I.e. the final set of nodes is the same
     * as the initial without result.  Priorities of all other items are unchanged.
     * @return result
     */
    public int dequeue( ) {
        int u = node[0] ;
        swap( 0, size-1 ) ;
        --size ;
        location[u] = -1 ;
        sinkDown( 0 ) ;  /*#I
        ScriptManager.relay("HigraphManager", "deleteNode", location[u] ) ;
        repositionHeapNodes() ; */
        return u ;
    }
    
    public boolean isEmpty() {
        return size == 0 ;
    }
}
/*#T tm*/
class Edge {
    int target, distance ;
    Edge( int target, int distance ) {
        this.target = target ; this.distance = distance ;
    }
}
abstract class Graph {
    // Nodes are represented by integers from 0 to N
    int N ;
    Edge [][] edges ;
    String [] name ;/*#I
    long [] x, y, namePosn, varPosn ; */
}
class SmallGraph extends Graph {
    SmallGraph() {
        N = 6 ;
        edges = new Edge[][] {
                /*0*/ new Edge[] { new Edge(1, 1), new Edge( 3, 2), new Edge(4, 3) },
                /*1*/ new Edge[] { new Edge(2, 1) },
                /*2*/ new Edge[] { new Edge(4, 2), new Edge(5, 4) },
                /*3*/ new Edge[] { new Edge(4, 2) },
                /*4*/ new Edge[] { new Edge(5, 2) },
                /*5*/ new Edge[] {} } ; 
        name = new String[] {"Tor", "Mnt", "QC", "NY", "Bos", "Hfx" } ;  /*#I
        x = new long[] { 20, 120, 220, 20, 120, 220 } ;
        y = new long[] { 20, 20, 20, 200, 200, 200 } ;
        namePosn = new long[] { PDV.NORTH, PDV.NORTH, PDV.NORTH, PDV.SOUTH, PDV.SOUTH, PDV.SOUTH } ;
        varPosn = new long[] { PDV.SOUTHEAST, PDV.SOUTH, PDV.SOUTHWEST, PDV.NORTHEAST, PDV.NORTH, PDV.NORTHWEST } ;
 */    }
}

public class Dijkstra {
    Graph g;
    // d[i] represents the shortest distance from the source to node i.
    int[] d ;
    // p[i] represents the predecessor of i on the path back to the source.
    int[] p ;
    
    Dijkstra(Graph g) {
        this.g = g ;
        d = new int[g.N];
        p = new int[g.N] ; /*#I
        makeInvisibleNode(g);
        for( int i = 0 ; i < g.N ; ++i ) {
            ScriptManager.relay("HigraphManager", "makeNode", d[i] ) ;
            ScriptManager.relay("HigraphManager", "addChild", g, true, d[i] ) ;
            ScriptManager.relay("HigraphManager", "setNodeNameLabel", d[i], g.name[i] ) ;
            ScriptManager.relay("HigraphManager", "setNodeNamePosition", d[i], g.namePosn[i] ) ;
            ScriptManager.relay("HigraphManager", "createNodeExtraLabel", d[i], "var", g.varPosn[i]  ) ;
            ScriptManager.relay("HigraphManager", "placeNode", d[i], g.x[i], g.y[i]) ; }
        for(int i=0 ; i < g.N ; ++i ) {
            Edge [] outgoing = g.edges[i] ;
            for( int j=0 ; j < outgoing.length ; ++j ) {
                Edge e = outgoing[j] ;
                ScriptManager.relay("HigraphManager", "makeEdge", d[i], d[e.target] ) ;
                ScriptManager.relay("HigraphManager", "createEdgeLabel", d[i], d[e.target], "distance", PDV.CENTER ) ;
                ScriptManager.relay("HigraphManager", "setEdgeLabel", d[i], d[e.target], "distance", e.distance ) ;
                ScriptManager.relay("HigraphManager", "setEdgeLabelFill", d[i], d[e.target], "distance", PDV.WHITE ) ; } }*/
    }
    /*#I private void makeInvisibleNode(Graph g){
        ScriptManager.relay("HigraphManager", "makeNode", g, true) ; 
            ScriptManager.relay("HigraphManager", "setNodeNameShow", g, true, false ) ;
            ScriptManager.relay("HigraphManager", "setNodeValueShow", g, true, false ) ;
            ScriptManager.relay("HigraphManager", "setNodeFillColor", g, true, PDV.TRANSPARENT ) ;
            ScriptManager.relay("HigraphManager", "setNodeColor", g, true, PDV.TRANSPARENT ) ;   	
    }*/
/*#T ww*/
    private void findShortestPathsFrom(int s) {
        Heap queue = new Heap(g.N) ;/*#I
        queue.setName( g.name ) ; */
        // Initially all distance are unknown.
        for(int i=0 ; i < g.N ; ++i ) {
            // We use -1 to represent an unknown distance
            d[i] = -1 ;
            // In the p array, we use -1 to represent an
            // unknown predecessor.
            p[i] = -1 ; }
        // Initially we know the distance from node s
        // to node s is 0.
        d[s] = 0 ; 
        // Add s to the queue with priority of 0. (Make s grey.)
        queue.enqueue(s, 0) ; /*#I
        ScriptManager.relay("HigraphManager", "setNodeFillColor", d[0], PDV.GREY);
         */
        while( ! queue.isEmpty() ) {
            // The grey node with the lowest label is suitable
            // to be the next node visited. (It becomes black.)
            int v = queue.dequeue() ; /*#I
            ScriptManager.relay("HigraphManager", "setNodeExtraLabel", d[v], "var", "v" ) ;
            ScriptManager.relay("HigraphManager", "setNodeFillColor", d[v], PDV.BLACK);
            ScriptManager.relay("HigraphManager", "setNodeValueColor", d[v], PDV.WHITE);*/
            // Loop through all edges leaving v
            Edge [] outgoing = g.edges[v] ;
            for( int i = 0 ; i < outgoing.length ; ++i ) {
                Edge e = outgoing[i] ; /*#I
                ScriptManager.relay("HigraphManager", "setEdgeColor", d[v], d[e.target], PDV.GREEN);*/
                int u = e.target ;/*#I
                ScriptManager.relay("HigraphManager", "setNodeExtraLabel", d[u], "var", "u" ) ;*/
                int distance = e.distance ;
                if( d[u] == -1 || d[u] > d[v] + distance ) {
                    d[u] = d[v] + distance ;  /*#I
                    if( p[u] != -1 ) ScriptManager.relay("HigraphManager", "setEdgeColor", d[p[u]], d[u], PDV.BLACK);*/
                    p[u] = v ;
                    // Add u to the queue. If it is already on
                    // the queue, it's priority is revised.
                    // (It becomes a grey.)
                    queue.enqueue(u, d[u]) ;  /*#I
                    ScriptManager.relay("HigraphManager", "setEdgeColor", d[v], d[u], PDV.RED);
                    ScriptManager.relay("HigraphManager", "setNodeFillColor", d[u], PDV.GREY);*/
                } /*#I else {
                    ScriptManager.relay("HigraphManager", "setEdgeColor", d[v], d[u], PDV.BLACK); }
                ScriptManager.relay("HigraphManager", "setNodeExtraLabel", d[u], "var", "" ) ;*/
            } /*#I
            ScriptManager.relay("HigraphManager", "setNodeExtraLabel", d[v], "var", "" ) ;*/
        }
    } /*#/T ww*/

    private void printPath( int u, int s ) {
        if( u==s ) {
            System.out.print(u) ; }
        else {
            Edge[] es = g.edges[p[u]] ;
            int i=0 ; while( es[i].target != u ) i++ ;
            printPath( p[u], s ) ;
            System.out.print( "  --");
            System.out.print( es[i].distance ) ;
            System.out.print( "-->  " ) ;
            System.out.print( u ) ; }
    }

    public void printPaths(int s) {
        for( int u = 0 ; u < g.N ; ++u ) {
            if( d[u] == -1 ) {
                System.out.print( u ) ;
                System.out.print(" is unreachable from " ) ;
                System.out.println( s ) ; }
            else {
                System.out.print( "d[" ) ;
                System.out.print( u ) ;
                System.out.print( "] = " ) ;
                System.out.println( d[u] ) ;
                printPath( u, s ) ;
                System.out.println(); } }
    }
    
    /*#I
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.NORTH);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.BLUE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.ELLIPSE);
        ScriptManager.relay("HigraphManager", "setDefaultEdgeColor", PDV.BLACK);
        ScriptManager.relay("HigraphManager", "setDefaultTargetDecorator", PDV.ARROWHEAD);
        ScriptManager.relay("HigraphManager", "setDefaultBranchColor", PDV.TRANSPARENT);
    }
     */

    public static void main(String [] args) { /*#I setup(); */
       Graph g1 = new SmallGraph() ;
       Graph g2 = new SmallGraph() ;
       Dijkstra d1 = new Dijkstra(g1) ;
       Dijkstra d2 = new Dijkstra(g2) ;
       ScriptManager.relay("HigraphManager", "placeNode", g2, true, 400, 100) ;
        
       d1.findShortestPathsFrom( 0 ) ;
       d1.printPaths( 0 ) ;
    }
}
