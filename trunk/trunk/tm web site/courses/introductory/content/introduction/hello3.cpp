#include <iostream>
using namespace std;

void hello();   // function declaration

int main(){
    hello();   // function call
    hello();   // function call
    hello();   // function call
    cout << "is anybody there?";
    return 0;
}

void hello(){  // function definition (or implementation)
     cout << "hello, ";
}
