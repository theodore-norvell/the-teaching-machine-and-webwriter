/***** factorialTable function ******************************
    @descr: A function to print a table of factorials from 1
	            to n

	@param: n - the last number in the table @pre: n > 1

***********************************************************/
void factorialTable(int n){
	long fact = 1;
// Output a table heading
    cout << "Table of Factorials\n  i\t  i!\n\n";

    for (int i = 1; i <= n; i++)  {
        fact *= i;
        cout << i << "\t" << fact << endl;
    }
     
}
