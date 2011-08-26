#include <iostream>
#include <new>

using namespace std ;

struct SLNode {
    int data ;
    SLNode *next ; } ;


/* TM doesn't support delete yet,
*  so we will be very wasteful and
*  not recycle nodes
*
*static void deleteList( SLNode *head ) {
*    while( head != 0 ) {
*       SLNode *temp = head ;
*       head = head->next ;
*       delete temp ; }
*   return ;
*}
*/

static SLNode *makeSquaresList(int n) {
    SLNode *head ;
    head = 0 ;
    for( int i=n-1 ; i >= 0 ; --i ) {
        SLNode *newNode = new(nothrow) SLNode ;
        // Null check omitted to simplify example.
        newNode->data = i*i ;
        newNode->next = head ;
        head = newNode ; }
    return head ;
}

static void makeList0() {
    SLNode *head = makeSquaresList(3) ;
    //deleteList( head ) ;
    return ;
}

// This makes more sense if the data member "data" is char.
static SLNode *stringToReversedList( char *str ) {
    SLNode *head ;
    head = 0 ;
    while( *str != 0 ) {
        SLNode *newNode = new(nothrow) SLNode ;
        // Null check omitted to simplify example.
        newNode->data = *str ;
        newNode->next = head ;
        head = newNode ;
        str++ ; }
    return head ;
}
    
static void makeList1() {
    // Note I'm avoiding string constants here
    // because I don't think the TM treats them
    // quite right. A string constant should be
    // an array in static store, but instead the
    // TM treats is as a series of characters that
    // happen to be sequential in memory.
    char str[4] ;
    str[0] = 'a'; str[1] = 'b'; str[2] = 'c'; str[3] = 0;
    SLNode *head = stringToReversedList( str ) ;
    //deleteList( head ) ;
    return ;
}
    
static void traverseList() {
    SLNode *head = makeSquaresList(3) ;
    SLNode *ptr = head ;
    while( ptr != 0 ) {
        cout << ptr->data << '\n' ;
        ptr = ptr->next ; }
    return ;
}

static void reverseCopyList() {
    SLNode *origHead = makeSquaresList(3) ;

    // Now the copy algorihtm begins
    SLNode *cursor = origHead ;
    SLNode *copyHead = 0 ;
    while( cursor != 0 ) {
        SLNode *newNode = new(nothrow) SLNode ;
        // Null check omitted to simplify example.
        newNode->data = cursor->data ;
        newNode->next = copyHead ;
        copyHead = newNode ;
        cursor = cursor->next ; }
    // Done copy

    //deleteList( origHead ) ;
    //deleteList( copyHead ) ; 
    return ;
}

static void ppCopyList() {
    SLNode *origHead = makeSquaresList(3) ;

    // Now the copy algorihtm begins
    SLNode *cursor = origHead ;
    SLNode *copyHead ;
    SLNode **ptrToNewestLink = &copyHead ;
    while( cursor != 0 ) {
        SLNode *newNode = new(nothrow) SLNode ;
        // Null check omitted to simplify example.
        newNode->data = cursor->data ;
        *ptrToNewestLink = newNode ;
        ptrToNewestLink = &(newNode->next) ;
        cursor = cursor->next ; }
    *ptrToNewestLink = 0 ;
    // Done copy

    //deleteList( origHead ) ;
    //deleteList( copyHead ) ; 
    return ;
}

static void ppInsert() {
    SLNode *head = makeSquaresList(3) ;
    SLNode *nodeToInsert = new(nothrow) SLNode ;
    // Null check omitted to simplify example.
    nodeToInsert->data = 3 ;

    // The insertion algorithm follows:
    // (A) linkPtr starts by pointing to the
    // head link.
    SLNode **linkPtr = & head ;
    // (B) Now we let linkPtr point to each
    // successive link until the link where
    // insertion is required is found.
    while( *linkPtr != 0 &&
           (*linkPtr)->data < nodeToInsert->data ) {
        linkPtr = & (*linkPtr)->next ; }
    // (C) Insert the new node.
    nodeToInsert->next = *linkPtr ;
    *linkPtr = nodeToInsert ;
    
    // deleteList( head ) ;
    return ;
}

static void copy( SLNode *source, SLNode *&target ) {
    if( source == 0 ) {
        target = 0 ; }
    else {
        target = new(nothrow) SLNode ;
        // Null check omitted.
        target->data = source->data ;
        copy( source->next, target->next ) ; }
    
    return ;
}

// Frankly: I'm not sure how this should look in the
// linked view.  It could end up a tangled mess,
// regardless of layout.
static void recursiveCopyList() {
    SLNode *origHead = makeSquaresList(3) ;
    SLNode *copyHead ;
    copy( origHead, copyHead ) ;
    //deleteList( origHead ) ;
    //deleteList( copyHead ) ; 
    return ;
}

int main() {
    makeList0() ;
    makeList1() ;
    traverseList() ;
    reverseCopyList() ;
    ppCopyList() ;
    ppInsert() ;
    recursiveCopyList() ;
    return 0 ;
} 
