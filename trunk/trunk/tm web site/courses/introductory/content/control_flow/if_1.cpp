/*#H*/ #include <iostream>
using namespace std;

char letterGrade(int mark);

int main(){
    int mark;
    char myGrade;

	cout<< "Enter your mark: ";
	cin >> mark;

	myGrade = letterGrade(mark);
	cout << "\nThis is a";
	if (myGrade == 'A' || myGrade == 'F')
		cout << 'n';
	cout << ' ' << myGrade << '.' << endl;
	return 0;
}
/*#DA*/ /** letterGrade **************************
*
* @params: mark - a valid mark @pre: must be on [0, 100]
*
* @returns: letter grade equivalent
***************************************************/

char letterGrade(int mark){
	char grade;
	if (mark >= 80) grade = 'A';
	if (mark >= 65 && mark <80) grade = 'B';
	if (mark >= 55 && mark < 65) grade = 'C';
	if (mark >=50 && mark < 55) grade = 'D';
	if (mark < 50) grade = 'F';
	return grade;
}/*#HA*/
