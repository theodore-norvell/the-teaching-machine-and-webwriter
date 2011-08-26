/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * washer.cpp -- Program to compute the weight of a number of washers.
 *
 * Input: Washer outer diameter
 *        Washer hole diameter
 *        Washer thickness
 *        Number of washers in a batch
 *        Material density
 * Output: Weight of the batch.
 *
 * Author: Dennis Peters
 *
 *******************************************************************/
#include <iostream>
#include <cmath>
using namespace std;

void outputDescription();     // Output the program description
float batchMass();            // Compute mass of batch of washers
void outResults(float mass);  // Output the mass of the batch
float washerVolume(float dia, float hole, float thick);
                              // Compute volume of a washer
float circleArea(float r);    // Compute the area of a circle

const float PI = acos(-1.0);  


/******************************************************************
 * main --
 *
 * Parameters: none
 * Modifies: nothing
 *
 * Returns: 0
 *******************************************************************/
int
main()
{
  float mass = 0.0; // Computed mass of batch.

  outputDescription();
  mass = batchMass();
  outResults(mass);

  return 0;
}

/******************************************************************
 * outputDescription -- output the description of the program
 *
 * Parameters: none
 * Modifies: cout -- outputs the description
 *
 * Returns: nothing
 *******************************************************************/
void
outputDescription()
{
  cout << "This program calculates the mass of a batch of washers\n";
  cout << "given the washer dimenstions and density and the number of\n";
  cout << "washers in the batch." << endl;
}

/******************************************************************
 * batchMass -- compute the mass of the batch of washers
 *
 * Parameters: none
 * Modifies: cin, cout -- Prompts the user and inputs the following:
 *                - Washer outer diameter
 *                - Washer hole diameter
 *                - Washer thickness
 *                - Material density
 *                - Number of washers in the batch
 *
 * Precondition: none
 *
 * Returns: mass of the batch of washers
 *******************************************************************/
float
batchMass()
{
  float mass = 0.0; // calculated mass of the batch.
  float dOut = 0.0; // Outer diameter of the washer
  float dHole = 0.0; // Diameter of the washer hole
  float th = 0.0; // Washer thicknes
  float dens = 0.0; // Density of the material.
  int num = 0; // Number of washers in the batch.
  float washerMass = 0.0; // Mass of one washer

  cout << "Please enter the washer dimensions, in mm, as follows: " << endl;
  cout << "  Diameter: ";
  cin >> dOut;
  cout << "  Hole diameter: ";
  cin >> dHole;
  cout << "  Thickness: ";
  cin >> th;
  cout << "What is the density of the washer material, in kg/m^3? ";
  cin >> dens;

  washerMass = washerVolume(dOut, dHole, th) * 1e-9 * dens;

  cout << "How many washers are in the batch? ";
  cin >> num;

  mass = washerMass * num;

  return mass;
}

/******************************************************************
 * outResults -- output the computed mass of the batch.
 *
 * Parameters: mass -- the mass of the batch.
 * Modifies: cout -- Outputs the results.
 *
 * Precondition: none
 *
 * Returns: nothing
 *******************************************************************/
void
outResults(float mass)
{
  cout << "The batch weighs " << mass << " kg." << endl;
}

/******************************************************************
 * washerVolume -- compute the volume of a washer.
 *
 * Parameters: dia  -- outer diameter of the washer
 *             hole -- hole diameter
 *             thick -- thickness of washer
 * Modifies: nothing
 *
 * Precondition: dia > 0 and hole > 0 and thick > 0
 *
 * Returns: volume of the washer
 *******************************************************************/
float
washerVolume(float dia, float hole, float thick)
{
  float vol = 0.0; // Calculated volume of the washer

  vol = (circleArea(dia/2.0) - circleArea(hole/2.0)) * thick;

  return vol;
}

/******************************************************************
 * circleArea -- compute the area of a circle.
 *
 * Parameters: r -- radius of the circle. 
 * Modifies: nothing
 *
 * Precondition: r > 0
 *
 * Returns: area of a circle of radius r.
 *******************************************************************/
float
circleArea(float r)
{
  float area = 0.0; // Calculated circle area.

  area = PI * r * r;

  return area;
}

/******************************************************************
 * $RCSfile$   $Revision: 53 $
 * $Date: 2008-01-12 08:40:20 -0330 (Sat, 12 Jan 2008) $
 * $State$ 
 *
 *                     REVISION HISTORY
 *
 * $Log$
 *
 ******************************************************************/
