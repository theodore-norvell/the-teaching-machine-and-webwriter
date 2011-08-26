#include <iostream>
#include <fstream>
using namespace std;

int main(){
  ifstream inData;
  int x;

  inData.open("mydatafile.dat");
  inData >> x; // read from inData into the variable x
  // use file ...
  inData.close(); // finished with file.
}
