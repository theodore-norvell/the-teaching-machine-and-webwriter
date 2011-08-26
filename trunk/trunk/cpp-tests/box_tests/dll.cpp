struct Node {
	int data ;
	Node *flink ;
	Node *blink ;
} ;

void insertAfter(Node *curr, int newData)
{
	Node *tmp = new(nothrow) Node ;
	tmp->data = newData ;
	tmp->flink = curr->flink ;
	tmp->blink = curr ;
	if( curr->flink ) {
		curr->flink->blink = tmp ;
	}
	curr->flink = tmp ;
}
	
void main () {
	Node *head = new(nothrow) Node ;
	head->data = 0 ;
	head->flink = 0 ;
	head->blink = 0 ;
	
	insertAfter(head, 1) ;
	insertAfter(head, 2) ;
	
	return ;
}