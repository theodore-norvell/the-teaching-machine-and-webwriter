/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * vector.cpp -- Demonstrate using Arrays as vectors.
 *
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
using namespace std;

void vectPrint(const double vec[], int len);
void vectScale(double vec[], int len, double scale);
double dotProduct(const double l[], const double r[], int len);

/******************************************************************
 * main -- Test some vector operations.
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int
main()
{
  double left[3];
  double right[3];
  double scalar;

  cout << "Please enter the left operand (x, y, z): ";
  cin >> left[0] >> left[1] >> left[2];
  cout << "Please enter the right operand (x, y, z): ";
  cin >> right[0] >> right[1] >> right[2];

  cout << "left = ";
  vectPrint(left, 3);
  cout << endl;

  cout << "Please enter a scalar: ";
  cin >> scalar;
  vectPrint(right, 3);
  cout << " * " << scalar << " = ";
  vectScale(right, 3, scalar);
  vectPrint(right, 3);
  cout << endl;

  vectPrint(left, 3);
  cout << " dot ";
  vectPrint(right, 3);
  cout << " = " << dotProduct(left, right, 3) << endl;

  return 0;
}

/******************************************************************
 * dotProduct -- calculate the dot product of two vectors
 *
 * Parameters: 
 *   l -- left operand vector
 *   r -- right operand vector
 *   len -- dimensionality of both vectors
 *
 * Returns: the vector dot product
 *******************************************************************/
double dotProduct(const double l[], const double r[], int len) {
	double result = 0.0;
	for (int i = 0; i < len; i++) {
		result += l[i] * r[i];
	}
	return result;
}

/******************************************************************
 * vectPrint -- output a vector
 *
 * Parameters:
 *   vec -- the vector to output
 *   len -- the dimensionality of the vector
 *
 * Modifies:
 *  cout -- the vector is output
 *
 * Returns: nothing
 *******************************************************************/
void vectPrint(const double vec[], int len) {
	cout << "(";
	for (int i = 0; i < len-1; i++) {
		cout << vec[i] << ", ";
	}
	if (len > 0) {
		cout << vec[i];
	}
	cout << ")";

	return;
}

/******************************************************************
 * vectScale -- multiply a vector by a scalar
 *
 * Parameters:
 *   vec -- the vector to be scaled (in/out)
 *   len -- the dimensionality of the vector
 *   scale -- the scaler to multiply by
 *
 * Returns: nothing
 *******************************************************************/
void vectScale(double vec[], int len, double scale) {
	for (int i = 0; i < len; i++) {
		vec[i] *= scale;
	}
	return;
}