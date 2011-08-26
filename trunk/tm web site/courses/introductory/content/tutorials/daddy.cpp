/** daddy ***************************************************************
*
* @params: longer - the length of the longer stick @pre: > 0
*          shorter - the length of the shorter stick @pre: >= 0, < longer
*
* @returns: the percentage of the longer stick that should be broken off
*              in order to make the two the same.
*************************************************************************/

double daddy(double longer, double shorter) {
       double breakOff = (longer - shorter) / 2.0;
       return 100 * breakOff /longer;
}
