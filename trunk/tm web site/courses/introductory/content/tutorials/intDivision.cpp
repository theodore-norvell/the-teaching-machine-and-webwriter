/*#H*/#include <iostream>
using namespace std;

// standard test functions
void constantClient();
void anxiousClient();
void independentClient();
void expressiveClient();
void confusedClient();

int foo(int a, int b);

/*#DA*/ int main(){
    constantClient();
    anxiousClient();
    independentClient();
    expressiveClient();
    confusedClient();
}/*#HA*/
/*#DB*/ // These are all tests of our service function
void constantClient(){
     cout << "foo(3,4) is " << foo(3, 4) << endl;
     cout << "foo(2,11) is " << foo(2, 11) << endl;
}

void anxiousClient(){
     int a = 4;
     int b = 13;
     cout << "foo(a,b) is " << foo(a, b) << endl;
}
void independentClient(){
     int fred = 7;
     int jarge = -2;
     cout << "foo(fred, jarge) is " <<  foo(fred, jarge) << endl;
}
void expressiveClient(){
     int x = 6;
     int y = 23;
     cout << "foo(x*x/12, y-x) is " << foo(x*x/12, y-x) << endl;
}
void confusedClient(){
     int a = 4;
     int b = 13;
     cout << "foo(b,a) is " << foo(b, a) << endl;
}/*#HB*/

/*#DC*/ // This is the service function we are tasked to create
int foo(int a, int b){
    return a + b/4;
}/*#HC*/


    
