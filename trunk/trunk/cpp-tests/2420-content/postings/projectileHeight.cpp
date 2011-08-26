#include <iostream>
#include <cmath>
using namespace std;

const double GRAV_ACCEL = 9.81; // Acceleration due to Gravity
const double PI = acos(-1.0);   // Pi

void outputDescription();       // Output program description
double radFromDeg(double deg);  // Convert degrees to radians
double maxProjectileHeight(double v0, double theta); // Compute max height

int main() {
  double launchAngDeg; // Input launch angle (degrees)
  double launchSpeed;  // Input launch speed (m/s)
  double launchAngRad; // Radians equivalent of launchAngDeg

  outputDescription();
  cout << "Please enter launch angle above the horizontal (degrees): ";
  cin >> launchAngDeg;
  if (0 <= launchAngDeg && launchAngDeg <= 90) {
    cout << "Please enter the launch speed (m/s): ";
    cin >> launchSpeed;
    if (launchSpeed >= 0) {
      launchAngRad = radFromDeg(launchAngDeg);
      cout << "The maximum height is: ";
      cout << maxProjectileHeight(launchSpeed, launchAngRad) << " m." << endl;
    } else {
      cout << launchSpeed << " is an invalid launch speed." << endl;
    }
  } else {
    cout << launchAngDeg << " is an invalid launch angle." << endl;
  }

  return 0;
}

void outputDescription() {
  cout << "This program calculates the maximum height achieved by\n";
  cout << "a projectile launched at a given angle above the horizontal\n";
  cout << "with a given initial speed." << endl;
}

double radFromDeg(double deg) {
  return PI/180.0 * deg;
}

double maxProjectileHeight(double v0, double theta) {
  double v0y = v0 * sin(theta); // Y (up) component of v0
  return 0.5 * v0y * v0y / GRAV_ACCEL;
}
