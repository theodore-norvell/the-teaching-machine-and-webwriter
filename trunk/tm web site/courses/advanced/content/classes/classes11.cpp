enum status {UNDERFLOW,EMPTY,PENDING,FULL,OVERFLOW};

class Queue {
public:
	void putItem(int);
	int getItem(void);
	status getState(void);
private:
	int q[100];		// The queue array itself
	int* pHead;
	int* pTail;
	status current;
}
