/*#HA*/ #include <iostream>
using namespace std;

void runTest();
int main(){
    runTest();
    return 0;
}

int getMiddle(int A, int B, int C);
void report(int a, int b, int c);

void runTest(){
     int a = 1;
     int b = 2;
     int c = 3;
     // Test most combinations
     report(a,b,c);
     report(a,c,b);
     report(b,a,c);
     report(b,c,a);
     report(c,a,b);
     report(c,b,a);
     report(a,a,b);
     report(a,b,b);
     report(a,b,a);
     report(a,a,a);
     }

void report(int a, int b, int c){
     cout << "The middle of (" << a << ", " << b;
     cout << ", " << c << ") is " << getMiddle(a,b,c);
     cout << endl;
}
/*#DA*/
/** getMiddle ******************************************
*
* @params: A, B, C - any three integers
*
* @ returns: the middle value 
*********************************************************/     
int getMiddle(int A, int B, int C){
    int middle;
    if (A < B) {
          if (B < C)
             middle = B;
          else if (C < A)
               middle = A;
          else
              middle = C;
    } else {  // A > =B
           if (A < C)
              middle = A;
           else if (B > C)
              middle = B;
           else
              middle = C;
    }
    return middle; 
}/*#HA*/
    
