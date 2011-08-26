/*#HA*/ #include <iostream>
using namespace std;

double triangleArea(double base, double height);
bool isEven(int number);
char toUpper(char letter);

int main(){
	triangleArea(4.23, 5.17);
	isEven(3);
	isEven(-22);
	toUpper('a');
	toUpper('b');
	toUpper('z');
	toUpper('3');
	return 0; 
}
/*#DA*/
/** triangleArea *******************************************
*  
* @params: base - the base of a triangle @pre: non-negative
*          height - of the same triangle @pre: non-negative
*
* @returns: the area of the triangle
***********************************************************/
double triangleArea(double base, double height){
	double area;
	area = (base * height)/2;
	return area;
}


/** isEven ***************************************************
*  
* @params: number- any integer
*
* @returns: true if number is even, false otherwise
*************************************************************/
bool isEven(int number){
	return (number%2 == 0);
}

/** toUpper **************************************************
*
* @params: letter @pre must be lower case
*
* @returns: the uppercase version of the letter
**************************************************************/
char toUpper(char letter){
	return letter + 'A'-'a';
}/*#H*/

        
