/*#HA*/ #include <iostream>
using namespace std;

double triangleArea(double base, double height );

int main(){
    cout << "The area of Pythagorus' famous 3-4-5 triangle is ";
    cout << triangleArea(3,4);
    cout << "\n while that of his 5-12-13 triangle is ";
    cout << triangleArea(5, 12);
	return 0; 
}
/*#DA*/ /** triangleArea ******************************
*
* @params: base - length of the base @pre >=0
*          height - length of height
*             @pre >= 0, same units as base
*
* @returns: triangle area in base units squared
**********************************************/
 
double triangleArea(double base, double height ){
	return (base * height)/ 2;
}/*#H*/
