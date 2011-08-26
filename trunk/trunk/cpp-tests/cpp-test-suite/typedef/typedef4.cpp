//! Run.
class Node {
	public: int data ;
	public: Node *next ;
}

typedef Node *List ;

int main() {
	List p = new Node ;
	p->data = 42 ;
	p->next = 0 ;
	return 0 ;
}