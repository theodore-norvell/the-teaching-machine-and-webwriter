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
	char grade;/*#DB*/
    if (mark >= 80)
		grade = 'A';
	else if (mark >= 65 )
		grade = 'B';
	else if (mark >= 55 )
		grade = 'C';
	else if (mark >=50 )
		grade = 'D';
	else
		grade = 'F';
/*#HB*/	return grade;
}
