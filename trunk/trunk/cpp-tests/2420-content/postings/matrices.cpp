#include <cmath>
#include <fstream>
#include <iostream>
using namespace std;

const int MATRIX_SIZE = 100;
void fillMatrix(int M[], int rows, int cols, ifstream& inData);
void printMatrix(int M[], int rows, int cols);
void sumMatrices(int Result[],int A[],int B[],int rows,int cols);


int main(){

	int A[MATRIX_SIZE];		// A pair of matrices
	int B[MATRIX_SIZE];
	int Result[MATRIX_SIZE];
	int rows;
	int cols;

	ifstream inData;
//	ofstream outData;

	inData.open("arrays.txt");
	if(inData.fail()) {
		cout << "Unable to open array.txt" << endl;
		return -1;
	}

	inData >> rows;
	inData >> cols;

	if (rows * cols > MATRIX_SIZE) {
		cout << "The file matrices are larger than the program can handle!" << endl;
		return -1;
	}

	fillMatrix(A, rows, cols, inData);
	fillMatrix(B, rows, cols, inData);

	cout << "Matrix A contains ";
	printMatrix(A, rows, cols);

	cout << "\nAnd Matrix B contains ";
	printMatrix(B, rows, cols);

	sumMatrices(Result,A,B,rows,cols);
	cout <<"\nTheir sum is ";
	printMatrix(Result,rows,cols);

//	diffMatrices(Result,A,B,rows,cols);
	cout <<"\nTheir sum is ";
//	printMatrix(Result,rows,cols);

	return 0;
}

void fillMatrix(int M[], int rows, int cols, ifstream& inData){
	for(int r = 0; r < rows; r++)	// for each row
		for(int c = 0; c < cols; c++) {	// for each column
			inData >> M[c + r * cols];
		}
}

void printMatrix(int M[], int rows, int cols){
	cout << "{\n";
	for (int r = 0; r < rows; r++) {
		cout << '\t';
		for (int c = 0; c < cols - 1; c++) {
			cout << M[c + r * cols] << ", ";
		}
		cout << M[cols - 1 + r * cols];
		cout << endl;
	}
	cout << "}\n";

}

void sumMatrices(int Result[],int A[],int B[],int rows,int cols){
	int index;
	for (int r = 0; r < rows; r++)
		for (int c = 0; c < cols; c++) {
			index = c + r * cols;
			Result[index] = A[index] + B[index];
		}
			
}

