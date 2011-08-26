struct Node {
     char data ;
     Node *next ; } ;

 int main() {

    // Build a list
    Node *oldList = 0 ;
    for( int i = 0 ; i < 4 ; i = i+1 ) {
        Node *tmp = new Node ;
        tmp->next = oldList ;
        oldList = tmp ; }
    oldList->data = 'T' ;
    oldList->next->data = 'e' ;
    oldList->next->next->data = 's' ;
    oldList->next->next->next->data = 't' ;

    //Copy the list
    Node *p = oldList ; // Points to each node in oldList              
    Node *newList ;    // The head of the new list
    Node **pp = & newList ; // Points to successive links
    while( p != 0 ) {
        Node *tmp = new Node ;
        tmp->data = p->data ;
        *pp = tmp ;
        pp = & (tmp->next) ;
        p = p->next ; 
        0; }
    *pp = 0 ;
    
    return ;
 }
                                