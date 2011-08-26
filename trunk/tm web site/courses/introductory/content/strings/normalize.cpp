#include "normalize.h"

/** normalize *****************************************
* @params: name - a string reference containing a name
*
* @modified: name is modified so that there is only
*            a single space between each of its parts
*
* @return: nothing
********************************************************/

void normalize(string& name){
	eatSpacesAt(name, 0);

	int next = 0;
	do {
		next = name.find(" ", next);
		if (next != -1)
			eatSpacesAt(name, next+1);
	} while (next != -1);
	eatSpacesAt(name, name.length()-1);
}

/** eatSpacesAt *****************************************
* @params: str - a string reference
*              - position - in the string
*                     @pre: >=0 && < length()
*
* @modified: any spaces at position and immediately after
*             are removed from str
*
* @return: nothing
********************************************************/
void eatSpacesAt(string& str, int position){
	int numSpaces = 0; // Might not be any at position
	while (str.at(position+numSpaces) == ' ')
		numSpaces++;
	if (numSpaces > 0)
		str.erase(position, numSpaces);
}
