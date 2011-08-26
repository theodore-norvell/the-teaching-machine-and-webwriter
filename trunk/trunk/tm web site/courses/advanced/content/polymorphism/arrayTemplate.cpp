/* Here we also get type checking over arrays of different types. */

template <int n, class T>
struct Array{
	T a[n];
};

void main(){
    Array<10,double> x,y;  // x & y are arrays of 10 doubles
    for (int i=0;i<10;i++)
    x.a[i] = 1./(i+1);
    y = x;                 // Type safe. Compiler will flag problems
    for (i=0;i<10;i++)
        cout << x.a[i] <<", "<< y.a[i] << "\n";
}
