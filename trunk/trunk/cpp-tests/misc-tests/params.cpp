void foo( int j, int &k, int m ) {
    return ;
}

void main(){
    int i = 997 ;
	foo( i, i, ++i ) ;
}
