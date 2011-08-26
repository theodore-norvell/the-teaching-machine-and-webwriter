#include <iostream>
using namespace std;



float dotProduct(float v1[], float v2[]);
void scale(float vector[], float factor);
void inputVector(float vector[]);
void outputVector(float vector[]);



int main(){
	float vector1[3];
	float vector2[3];
	float scaleFactor;

	inputVector(vector1);
	inputVector(vector2);

	cout << "Please input a scale factor: ";
	cin >> scaleFactor;


	outputVector(vector1);
	cout<< " dot ";
	outputVector(vector2);
	cout << " = " << dotProduct(vector1, vector2) << endl;

	outputVector(vector1);
	cout << " x " << scaleFactor << " = ";
	scale(vector1, scaleFactor);
	outputVector(vector1);
	cout << endl;

	outputVector(vector1);
	cout<< " dot ";
	outputVector(vector1);
	cout << " = " << dotProduct(vector1, vector1) << endl;



	return 0;
}


float dotProduct(float v1[], float v2[]){
	float product = 0.;
	for(int i = 0; i < 3; i++)
		product += v1[i]*v2[i];
	return product;
}

void scale(float vector[], float factor){
	for (int i =0; i < 3; i++)
		vector[i] *= factor;
}

void inputVector(float vector[]){
	cout << "Please input a vector (x y z): ";
	cin >> vector[0] >> vector[1] >> vector[2];
}

void outputVector(float vector[]){
	cout << '(' << vector[0] << ", " << vector[1] << ", " << vector[2] << ')';
}
