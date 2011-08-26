void main () {
	/*#B="qScope"*/ /*#b="qScope"*/int q;/*#/b*/
	for/*#B="iScope"*/(/*#b="iScope"*/int i=0;/*#/b*/i<10;i++) { // Not legal in C
		q++; //This is silly
		cout << i << q;
		/*#B="pScope"*/ /*#b="pScope"*/int p = 12;/*#/b*/ // Also useless, just illustrative
	}/*#/B*/ /*#/B*/
	cout << p; // The compiler will complain!
}/*#/B*/
