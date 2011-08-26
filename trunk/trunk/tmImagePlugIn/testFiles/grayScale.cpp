/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 1020 Structured Programming
 * grayScale -- Turn a picture to black and white
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/
#include <iostream>
using namespace std;
/*#TS*/#include <ScriptManager>
/*#/TS*/
const int LEFT = 1;
const int RIGHT = 2;

/*#T PI*//**************************************
 Picture interface. In each case p
 refers to either the LEFT or RIGHT
 picture.
****************************************/
// Load a .jpg from file into picture p
void loadPicture(int p);
 
// Save picture p to a .jpg file
void savePicture(int p);

// set up a canvas of size w x h pixels to draw picture p 
void createPicture(int width, int height, int p);

// get the width of picture p in pixels
int getWidth(int p);
// get the height of picture p in pixels
int getHeight(int p);

// retreive the pixel at (x,y) from picture p
int getPixel(int x, int y, int p);
// set the pixel at (x,y) in picture p
void setPixel(int x, int y, int pix, int p);
/*#/T PI*/

/*#TA*/ void makeGrayScalePicture();
int getGray(int pixel);

/******************************************************************
 * main 
 *
 * Arguments: none
 *
 * Returns: 0
 *******************************************************************/
int main() {
	loadPicture(LEFT);
	makeGrayScalePicture();
  return 0;
}/*#/TA*/

/*#TS*/void loadPicture(int p){
      ScriptManager::relay("imagePlugIn", "loadImage", p);
}

void savePicture(int p){
      ScriptManager::relay("imagePlugIn", "saveImage", p);
}

void createPicture(int width, int height, int p){
      ScriptManager::relay("imagePlugIn", "createImage", width, height, p);
}


int getWidth(int p){
	return ScriptManager::relayRtnInt("imagePlugIn", "getImageWidth", p);
}

int getHeight(int p){
	return ScriptManager::relayRtnInt("imagePlugIn", "getImageHeight", p);
}

int getPixel(int x, int y, int p){
	return ScriptManager::relayRtnInt("imagePlugIn", "getPixelAt", x, y, p);
}

void setPixel(int x, int y, int pix, int p){
	ScriptManager::relay("imagePlugIn", "setPixelAt", x, y, pix, p);
}/*#/TS*/

/*#TB*/void makeGrayScalePicture(){
// Make the new picture the same size as the original
    int width = getWidth(LEFT);
    int height = getHeight(LEFT);
    createPicture(width, height, RIGHT);

// Draw the new picture using gray pixels    
    for (int x = 0; x < width; x++){
       for (int y = 0; y < height; y++) {
          int pixel = getPixel(x,y, LEFT);
          setPixel(x,y,getGray(pixel), RIGHT);
       }
    }
}

// Create a gray version of the coloured pixel
int getGray(int pixel){
    int red = (pixel & 0xff0000)/ (256 * 256);
    int green = (pixel & 0x00ff00)/ (256);
    int blue = pixel & 0x0000ff;
    int gray = (red + blue + green)/3;
    return gray + 256 * gray + 256 * 256 * gray;
}/*#/TB*/
    


