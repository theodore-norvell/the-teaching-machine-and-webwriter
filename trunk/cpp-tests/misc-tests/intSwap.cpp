void swap1(int a1, int a2);
void swap2(int& a1, int& a2);

void main () {
    int a = 3;
    int b = 4;

    swap1(a,b);
    cout << a << b;
    swap2(a,b);
    cout << a << b;
}

void swap1(int arg1, int arg2){
    int temp;

    temp = arg1;
    arg1 = arg2;
    arg2 = temp;
}
void swap2(int& arg1, int& arg2){
    int temp;

    temp = arg1;
    arg1 = arg2;
    arg2 = temp;
}

