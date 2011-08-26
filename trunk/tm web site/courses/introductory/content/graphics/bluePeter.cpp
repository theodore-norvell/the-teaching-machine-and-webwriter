/******************************************************************
 * Memorial University of Newfoundland
 * Engineering 1020 Structured Programming
 * bluePeter -- Create an image of a blue peter flag
 *
 *
 * Author: Michael Bruce-Lockhart
 *
 *******************************************************************/
#include <iostream>
using namespace std;
#include <ScriptManager>

const int LEFT = 1;
const int RIGHT = 2;
const int MAX_WIDTH = 128;
const int MAX_HEIGHT = 128;
const int RED = 0xff0000;
const int GREEN = 0x00ff00;
const int BLUE = 0x0000ff;
const int WHITE = RED + GREEN + BLUE;
const int YELLOW = GREEN + BLUE;
const int SCALE_DOWN = 8;

// Standard image interface routines
void loadPicture(int p);
void savePicture(int p);
void updatePicture(int p);
void createPicture(int width, int height, int p);
void scaleImageUp(int p);

int getWidth(int p);
int getHeight(int p);
int getPixel(int x, int y, int p);
void setPixel(int x, int y, int pix, int p);


void makeBluePeter(int p);

/******************************************************************
 * main 
 *
 * Arguments: none
 *
 * Returns: 0
 *******************************************************************/
int main() {
	makeBluePeter(LEFT);

  return 0;
}
/*#T S */
void loadPicture(int p){
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
}

void updatePicture(int p){
	ScriptManager::relay("imagePlugIn", "updateImage", p);
}/*#/T S */

/*#T A */ void makeBluePeter(int p){
     int width = MAX_WIDTH/SCALE_DOWN;
     int height = MAX_HEIGHT/SCALE_DOWN;
	createPicture(width, height, p);
	for (int x = 0; x < width; x++){
    	for (int y = 0; y < height; y++){
            if (x >= width/4 && x < 3*width/4 && y >= height/4 && y < 3*height/4)
               setPixel(x,y,WHITE,p);
            else
                setPixel(x,y,BLUE,p);
        }
        updatePicture(p);
    }
    updatePicture(p);
}/*#/T A */



    


