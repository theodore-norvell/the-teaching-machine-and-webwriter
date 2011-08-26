int main(){/*#TA*/
		int	x = 1, y = 2;
		int* ip;	// ip is a pointer-to-int
		ip = &x;	// set the value of ip to the address-of-x
		y = *ip;	// y is now 1 
		*ip = 0;	// x is now 0 
		++*ip;		// increment x 
		(*ip)++;	// increment it again - unary ops right to left 
		*++ip;		// increments the pointer, then derefs it
	/*#/TA*/	return 0;
	}
	
