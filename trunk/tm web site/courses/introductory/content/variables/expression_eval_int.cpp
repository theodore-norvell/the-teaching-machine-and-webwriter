/*#H*/ /******************************************
 * Memorial University of Newfoundland
 * Engineering 2420 Structured Programming
 * expression_eval_int.cpp 
 * Demonstrate integer arithmetic.
 *
 * Author: Michael Bruce-Lockhart
 * Date: 2006.04.28
 *
 *************************************************/
/*#DM*/int main() {
    int i = 11;
    int j = 7;
    int k = 8;
    int result;
    int remainder;
    result = i * j + k;   // normal precedence
    result = i*(j + k); // altered precedence
    result = i/ j;      // integer division
    remainder = i%j;    // mod operator is remainder
	
	return 0;
}/*#HM*/
