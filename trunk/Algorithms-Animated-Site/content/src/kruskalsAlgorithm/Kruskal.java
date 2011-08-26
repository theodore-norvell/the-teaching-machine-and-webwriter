/* Created on 11 Aug, 2011 by Theodore S. Norvell. */
package kruskalsAlgorithm; /*#I
import tm.scripting.ScriptManager;
import tm.scripting.PDV; */
class Heap {
    int N ;
    // The heap invariant is that:
    //     0 < i && i < size implies priority[parent(i)] < priority[i]
    int size ;
    int [] edge ;
    int [] priority ;/*#I
    String [] name ; */
    // Location maps edges to their location in the heap.
    // If an edge is not in the heap, the location is -1.
    // Invariant: location[v] != -1 implies 
    //                0 <= location[v] && location[v] < size && edge[location[v]]==v
    int [] location ;
    
    public Heap(int N) { 
        this.N = N ;
        size = 0 ;
        edge = new int [N] ;
        priority = new int [N] ;
        location = new int [N] ;
        for( int e=0 ; e<N ; ++e ) location[e] = -1 ;
    }
    
    private int parent(int i) {
        return (i-1) / 2 ; }
    
    private int left(int i) {
        return (i+1)*2 - 1; }
    
    private int right(int i) {
        return (i+1)*2 ; }
    
    private void swap( int i, int j ) {
        int e = edge[i] ;
        int f = edge[j] ;
        location[e] = j ;
        location[f] = i ;
        edge[i] = f ;
        edge[j] = e ;
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
    
    public void enqueue(int e, int pi) {
        // require: location[e] == -1 || pi <= priority[location[i]]
        if( location[e] != -1) {
            // Node v is already in the heap. It's new priority may be lower.
            priority[location[e]] = pi ;
            bubbleUp( location[e] ) ; }
        else {
            edge[size] = e ;
            priority[size] = pi ;
            location[e] = size ; 
            ++size ;
            bubbleUp( size-1 ) ; }
    }
    
    /** Requires: ! empty() 
     * Ensures: result is the node with the smallest priority in the initial state of the heap
     * and the heap is altered by removing the result. I.e. the final set of nodes is the same
     * as the initial without result.  Priorities of all other items are unchanged.
     * @return result
     */
    public int dequeue( ) {
        int e = edge[0] ;
        swap( 0, size-1 ) ;
        --size ;
        location[e] = -1 ;
        sinkDown( 0 ) ;
        return e ;
    }
    
    public boolean isEmpty() {
        return size == 0 ;
    }
}

class Edge {
    int source, target, distance ;
    
    Edge( int source, int target, int distance ) {
        this.source = source ;
        this.target = target ;
        this.distance = distance ; }
    
    void output() {
        System.out.print( source ) ;
        System.out.print( " --") ;
        System.out.print( distance ) ;
        System.out.print( "-- ") ;
        System.out.println( target ) ;
    }
}

abstract class Graph {
    // Nodes are represented by integers from 0 to N
    int N ;
    Edge [] edges ;
    String [] name ;/*#I
    long [] x, y, namePosn, varPosn ; */
}
class SmallGraph extends Graph {
    SmallGraph() {
        N = 8 ;
        edges = new Edge[] {
                    new Edge(0, 1, 3),
                    new Edge(0, 2, 11),
                    new Edge(0, 5, 9),
                    new Edge(0, 6, 14),
                    new Edge(1, 3, 21),
                    new Edge(2, 3, 8),
                    new Edge(2, 4, 7),
                    new Edge(3, 5, 24),
                    new Edge(3, 7, 8),
                    new Edge(4, 5, 13),
                    new Edge(4, 6, 9),
                    new Edge(4, 7, 17),
                    new Edge(5, 6, 18),
                    new Edge(6, 7, 5) } ;  /*#I
        x = new long[] {  10, 160,  160,  250, 110,  60,  10, 250 } ;
        y = new long[] {  10,  10,   60,   90, 160,  90, 250, 250 } ;*/
    }
}

class Partition {
    int N ;
    int parent[] ;
    int size[] ;
    int numParts ; /*#I
    long color[] = new long[] {0xFF3333, 0x33FF33, 0x3333FF,
                               0xFFFF00, 0x00FFFF, 0xFF00FF,
                               0x999900, 0x009999, 0x990099, 
                               0xFF9999, 0x99FF99, 0x9999FF,
                               0x666600, 0x006666, 0x660066,
                               0x333300, 0x003333, 0x330033 } ;
    
    long getColor(int i) {
        while( parent[i] != i ) i = parent[i] ;
        return color[i % color.length ] ;
    } */
    
    int numberOfParts() { return numParts ; }
    
    /** Initially there are N sets, one for each item */
    public Partition( int N ) {
        this.N = N ;
        this.numParts = N ;
        this.parent = new int[N] ;
        this.size = new int[N] ;
        for( int i=0 ; i < N ; ++i ) {
            parent[i] = i ;
            size[i] = 1 ; }}
    
    /** Are two items in the same set */
    public boolean find( int i, int j ) {
        while( parent[i] != i ) i = parent[i] ;
        while( parent[j] != j ) j = parent[j] ;
        return i==j ; }
    
    /** Combine two sets into one. */
    public void union( int i, int j ) {
        while( parent[i] != i ) i = parent[i] ;
        while( parent[j] != j ) j = parent[j] ;
        if( i != j ) {
            if( size[i] < size[j] ) {
                parent[i] = j ;
                size[j] = size[j] + size[i] ; }
            else {
                parent[j] = i ;
                size[i] = size[i] + size[j] ; } }
        --numParts ;}
}
    

/*#T tm*/
public class Kruskal {
    Graph g ;
    
    Kruskal(Graph g) {
        this.g = g ; /*#I 
        for( int i = 0 ; i < g.N ; ++i ) {
            ScriptManager.relay("HigraphManager", "makeNode", g.x[i] ) ;
            ScriptManager.relay("HigraphManager", "placeNode", g.x[i], g.x[i], g.y[i]) ; }
        for(int i = 0 ; i < (g.edges).length ; ++i ) {
            Edge e = g.edges[i] ;
            ScriptManager.relay("HigraphManager", "makeEdge", g.x[e.source], g.x[e.target] ) ;
            ScriptManager.relay("HigraphManager", "createEdgeLabel", g.x[e.source], g.x[e.target], "distance", PDV.CENTER ) ;
            ScriptManager.relay("HigraphManager", "setEdgeLabel", g.x[e.source], g.x[e.target], "distance", e.distance ) ;
            ScriptManager.relay("HigraphManager", "setEdgeLabelFill", g.x[e.source], g.x[e.target], "distance", PDV.WHITE ) ; }*/
    }
/*#I
    void recolor(Partition s) {
        for( int i=0 ; i < g.N ; ++i ) {
            ScriptManager.relay("HigraphManager", "setNodeFillColor", g.x[i], s.getColor(i) ) ; } }
*/
    
/*#T ww*/    
    void computeMinimumSpanningTree() {
        Heap priorityQueue = new Heap( (g.edges).length ) ; 
        for( int i = 0 ; i < (g.edges).length; ++i ) {
            priorityQueue.enqueue(i, g.edges[i].distance) ; }
        Partition s = new Partition(g.N) ;/*#I
        recolor(s) ; */
        while( ! priorityQueue.isEmpty() && s.numberOfParts() > 1 ) {
            int i = priorityQueue.dequeue() ;
            Edge e = g.edges[i] ; /*#I
            ScriptManager.relay("HigraphManager", "setEdgeColor", g.x[e.source], g.x[e.target], PDV.GREEN ) ; */
            if( ! s.find(e.source, e.target) ) {
                e.output() ; /*#I
                ScriptManager.relay("HigraphManager", "setEdgeColor", g.x[e.source], g.x[e.target], PDV.RED ) ; */
                s.union( e.source, e.target ) ;/*#I
                recolor(s) ; */
            } /*#I else {
                ScriptManager.relay("HigraphManager", "setEdgeColor", g.x[e.source], g.x[e.target], PDV.BLACK ) ; } */
        }
    } /*#/T ww*/

    
    public static void main(String [] args) { /*#I 
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", false, PDV.NORTH);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", true, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.ELLIPSE);
        ScriptManager.relay("HigraphManager", "setDefaultEdgeColor", PDV.BLACK);*/
        Graph g = new SmallGraph() ;
        Kruskal kruskal = new Kruskal(g) ;
        kruskal.computeMinimumSpanningTree() ;
    }
}/*#/T tm*/
