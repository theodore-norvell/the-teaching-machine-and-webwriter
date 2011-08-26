/*#TA*/
/** Class to represent natural numbers of any number of
 * digits.
 * @author Theodore S. Norvell
 */

public class BigNat {
    /* Class Invariant:
     *    (FORALL i in {0, 1, ..., digits.length-1} :
     *       0 <= digits[i] && digits[i] < 1000000000)
     *       && (sign == 1 || sign == -1)
     * Representation: The value represented is
     *           (SUM i in {0, 1, ..., digits.length-1} :
     *                digit[i] * 1,000,000,000 ^ i )
     */
    private int[] digits ;
    private static final int BASE = 1000000000 ; 
    
    /** Construct a BigNat from a nonnegative int.*/
    public BigNat( int value )/*#/TA*/ {
        assert value >= 0 ;
        digits = new int[2] ;
        digits[ 0 ] = value % BASE ;
        digits[ 1 ] = value / BASE ; 
    }
/*#/TB*/    
    private BigNat(int[] digits) {
        this.digits = digits ;
    }/*#TA*/
    
    /** Add to another BigNat. */
    public BigNat add( BigNat other )/*#/TA*/ {
        
        int thisLength = this.digits.length ;
        int otherLength = other.digits.length ;
        int newLength = 1+ Math.max( thisLength, otherLength );
        int[] newDigits = new int[ newLength ] ;
        int carry = 0 ;
        // Invariant: newDigits represents the
        // sum of the of the first i digits minus carry * 10^(9*i).
        // The carry is always 0 or 1.
        for( int i = 0 ; i < newLength ; ++i ) {
            int thisDigit ;
            if( i < thisLength ) thisDigit = this.digits[i] ;
            else thisDigit = 0 ;
            int otherDigit  ;
            if( i < otherLength ) otherDigit = other.digits[i] ;
            else otherDigit = 0 ;
            int sum = thisDigit + otherDigit + carry ;
            newDigits[i] = sum % BASE ;
            carry = sum / BASE ;
            assert carry == 0 || carry == 1 ; }
        assert carry == 0 ;
        return new BigNat( newDigits ) ; }/*#TA*/
/*#/TB*/
    /** Multiply by another BigNat. */
    public BigNat multiply( BigNat other )/*#/TA*/ {
        int thisLength = this.digits.length ;
        BigNat product = new BigNat( 0 ) ;
        for( int i=0 ; i < thisLength ; ++i ) {
            BigNat partialProduct = other.mult1( digits[i],i ) ;
            product = product.add( partialProduct ) ; }
        return product ; }

    /** Multiply by a single digit while shifting.
     * @param multiplier The digit to multiply by.
     * It should be such that <code>0 <= multiplier && multiplier < BASE</code>
     * @param shift The amount to shift by
     * @return A new BigNat whose first <code>shift<code>
     * digits are 0 and whose remaining digits represent
     * the value of this object multiplied by the multiplier.
     */
    private BigNat mult1(int multiplier, int shift) {
        int thisLength = this.digits.length ;
        int newLength = this.digits.length + 1 + shift ;
        int[] newDigits = new int[ newLength ] ;
        // Note when an array of int is allocated, the
        // elements are all set to 0. Thus the first "shift"
        // elements are already 0.
        int carry = 0 ;
        for( int i=0 ; i < thisLength ; ++i ) {
            int thisDigit = this.digits[i] ;
            long prod = (long)thisDigit * (long)multiplier
                      + carry ;
            assert prod < (long)BASE * (long)BASE ;
            newDigits[i+shift] = (int)( prod % BASE );
            carry = (int)( prod / BASE ); }
        newDigits[ newLength - 1 ] = carry ;
        return new BigNat( newDigits ) ; }/*#TA*/

    /** Represent as a String in decimal notation. */
    public String toString( )/*#/TA*/ {
        int i = this.digits.length-1 ;
        // Find the first nonzero digit working from
        // most significant digit.
        for( ; i >= 0 && digits[i]==0 ; --i ) ;
        // If no nonzero digits, result is "0"
        if( i < 0 ) return "0" ;
        // First nonzero digit is a bit special
        // as it requires no padding.
        String str = String.valueOf( digits[i] ) ;
        for(  --i ; i >= 0 ; --i ) {
            String oneDigit = String.valueOf( digits[i] ) ;
            // Pad the digit to a legth of 9.
            while( oneDigit.length() < 9 ) {
                oneDigit = "0".concat( oneDigit ) ; }
            str = str.concat( oneDigit ) ; }
        return str ; }/*#TA*/
}

