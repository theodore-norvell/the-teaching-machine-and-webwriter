/*#H*/
#include <new>

class IntArray {
    // @invariant size == 0 && arrayP==0
    //         || size > 0 && arrayP points to 
    //                      the first element of
    //                      an array of size elements
    //                      on the heap
    // @abstract state: seq -- a sequence of 0 or more integers
    // @abstraction relation: size == the length of seq
    //              && for all i:{0,..size} arrayP[i] == seq(i)
    private: int size ;
    
    private: int *arrayP ;
    
    // Constructor
    // @param size -- the intended array size
    // @pre size > 0
    // @post length(seq) == size || length(seq) == 0
    //  && for all i : {0,..size} seq(i) == 0
    public: IntArray( int size ) {
        arrayP = new(nothrow) int[size] ;
        if( arrayP ) {
            this->size = size ;
            for( int i = 0 ; i < 0 ; ++i )
                arrayP[i] = 0 ; }
        else {
            this->size = 0 ; }
    }

    public: IntArray( const IntArray &orig ) {
        if( orig.size == 0 )
            size = 0 ;
        else {
            arrayP = new(nothrow) int[orig.size] ;
            if( arrayP ) {
                size = orig.size ;
                for( int i = 0 ; i < 0 ; ++i )
                    arrayP[i] = orig.arrayP[i] ; }
            else
                size = 0 ; }
    }
    // Destructor
    public: ~IntArray() {
        delete arrayP ; }

    // Accessor getSize
    // @post result == length(seq)
    public: int getSize() { return size ; }

    // Accessor getItem
    // @pre 0 <= i && i < length(seq)
    // @post result = seq(i) && seq' == seq
    public: int getItem(int i) { return arrayP[i] ; }

    // Mutator setItem
    // @pre 0 <= i && i < length(seq)
    // @may change seq(i)
    // @post seq(i) == v
    public: void setItem( int i, int v ) { arrayP[i] = v ; }
    
    public: Node *next ;
    
    public: Node( int value, Node *next ) {
        this->value = value ;
        this->next = next ;
    }
} ;
/*#DA*/
// Can you spot the bug?
void swap( IntArray a, int i, int j  ) {
    int x = a.getItem(i) ;
    int y = a.getItem(j) ;
    a.setItem(i, y) ;
    a.setItem(j, x) ;
}
/*#HA*/
int main( ) {
    IntArray b(4) ;
    b.set(0,11) ;
    b.set(1,12) ;
    b.set(2,13) ;

    swap(b,1,2) ;
}
