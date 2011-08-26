/***** boolean variables & expressions ********

  This is not a program in the accepted sense.
  Rather it is a series of isolated examples
  strung together as if they were a program.

**********************************************/



int main(){
    int n = 3;

    bool isOn = true;
    bool flag;
	bool toggle = false;
    
    flag = !isOn;
	toggle = !toggle;

    flag = n;
	toggle = !toggle;

	flag = (n != 0);
	toggle = !toggle;
    
    flag = n < 3;
	toggle = !toggle;

    flag = n >= 3 && n < 6;
	toggle = !toggle;

	return 0;
}
