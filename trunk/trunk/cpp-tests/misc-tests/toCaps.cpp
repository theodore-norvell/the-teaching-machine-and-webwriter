int main(){
    char* p = "T-m";

    while(*p) {
        if (*p >='a' && *p<='z')
            *p += 'A'-'a';
        p++;
   }
   cout << *p << '\n';

// A separate example

int f;
int c=21;
f = 9.*c/5+32.5;
cout << "Room temperature in fahrenheit is " << f << '\n';
return 0;
}
    
