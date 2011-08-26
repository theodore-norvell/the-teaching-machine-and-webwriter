#include <iostream>
using namespace std;

void name();   // function declaration

int main(){
    cout << "Dear ";
    name();
    cout << ",\n";
    cout << "We're very happy, ";
    name();
    cout << ", that you've become a customer here at ABC Motors.  ";
    name();
    cout << ",\n we can assure you that you, ";
    name();
    cout << ", the customer always comes first with us.\n";
    cout << "Have a great day!\nThe ABC team\n";
    cout << "p.s. ";
    name();
    cout << ", the Team is always behind you!\n";
	return 0;
}

void name(){  // function definition (or implementation)
     cout << "Michael";
}
