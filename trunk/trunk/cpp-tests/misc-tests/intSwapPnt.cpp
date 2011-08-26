void intSwap(int* a1, int* a2);

void main () {
    int a = 3;
    int b = 4;

    int*p1 = &a;
    int* p2 = &b;

    intSwap(p1,p2);
    cout << a << b;
}

void intSwap(int* pnt1, int* pnt2){
    int temp;

    temp = *pnt1;
    *pnt1 = *pnt2;
    *pnt2 = temp;
}
