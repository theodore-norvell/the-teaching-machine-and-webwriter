package tm.gwt.test;

import tm.gwt.state.MirrorState ;

public class TestState extends MirrorState {
    int count = 0 ;

    public TestState() {
        
    }
    
    void next() {
        switch( count ) {
        case 0 : {
            this.setExpression( "\ufffetempF\ufffb = (tempC * 5 / 9) + 32" );
            count = 1 ;
        } break ;
        case 1 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32" );
            count = 2 ;
        } break ;
        case 2 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32" );
            count = 3 ;
        } break ;
        case 3 : {
            this.setExpression( "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32" );
            count = 4 ;
        } break ;
        case 4 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32" );
            count = 5 ;
        } break ;
        case 5 : {
            this.setExpression( "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb" );
            count = 6 ;
        } break ;
        case 6 : {
            this.setExpression( "\uffff50.0\ufffb" );
            count = 7 ;
        } break ;
        case 7 : {
            this.setExpression( "" );
            count = 0 ;
        } break ;
        }
    }
}
