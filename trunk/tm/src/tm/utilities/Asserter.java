package tm.utilities;

import tm.interfaces.AsserterI ;

public class Asserter implements AsserterI {

    @Override
    public void check(Throwable e) {
        Assert.check(  e );
    }

    @Override
    public void check(boolean proposition, Throwable e) {
        Assert.check( proposition, e ) ;
    }

    @Override
    public void check(boolean proposition) {
        Assert.check(  proposition ) ;
    }

    @Override
    public void check(boolean proposition, String message) {
        Assert.check(  proposition, message ) ;
    }

    @Override
    public void check(String message) {
        Assert.check( message ) ;
    }
    
    @Override
    public void toBeDone() {
        Assert.toBeDone() ; 
    }
}
