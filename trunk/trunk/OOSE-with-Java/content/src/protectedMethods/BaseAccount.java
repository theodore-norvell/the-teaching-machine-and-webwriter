package protectedMethods;

abstract class BaseAccount {
    private double interestRate ;
    // Invariant: interestRate >= 0

    //... other data ...  

    protected BaseAccount( double interestRate ) {
        assert interestRate >= 0 ;
        this.interestRate = interestRate ; }

    protected double getInterestRate() {
        return interestRate ; }

    //... other methods not mentioning interestRate ... 
}
