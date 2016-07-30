package tm.gwt.test;

import java.util.ArrayList ;
import java.util.Set ;
import java.util.TreeSet ;
import java.util.Vector ;

import tm.gwt.state.MirrorCoords ;
import tm.gwt.state.MirrorState ;
import tm.gwt.state.MirrorStore ;
import tm.gwt.state.MirrorTMFile ;
import tm.gwt.state.StateCommander ;
import tm.interfaces.CodeLine ;
import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUp ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TMFileI ;
import tm.interfaces.TagSet ;
import tm.interfaces.TagSetInterface ;

public class TestController implements StateCommander {
    final MirrorState state ;
    int count = 0 ;
    TMFileI file = new MirrorTMFile( "fred.cpp" ) ;
    ArrayList<CodeLineI> lines = new ArrayList<CodeLineI>() ;
    ArrayList<SourceCoordsI> foci = new ArrayList<SourceCoordsI>() ;
    {
        StringBuffer b = new StringBuffer() ;
        Vector<MarkUp> markup = new Vector<MarkUp>() ;
        Set<TagSetInterface> tagSets = new TreeSet<TagSetInterface>()  ;
        

        SourceCoordsI coords = new MirrorCoords(file, 1)  ;
        b.append( "void main( ) {" ) ;
        CodeLineI line = new CodeLine(b, markup, coords, tagSets ) ;
        lines.add(  line  ) ;
        foci.add( coords ) ;

        b.setLength( 0 );
        b.append( "    int first, second;" ) ;
        coords = new MirrorCoords(file, 2)  ;
        line = new CodeLine(b, markup, coords, tagSets ) ;
        lines.add(  line  ) ;
        foci.add( coords ) ;

        b.setLength( 0 );
        b.append( "    cout << \"Input the first number: \";" ) ;
        coords = new MirrorCoords(file, 3)  ;
        line = new CodeLine(b, markup, coords, tagSets ) ;
        lines.add(  line  ) ;
        foci.add( coords ) ;

        b.setLength( 0 );
        b.append( "     cin >> first;" ) ;
        coords = new MirrorCoords(file, 4)  ;
        line = new CodeLine(b, markup, coords, tagSets ) ;
        lines.add(  line  ) ;
        foci.add( coords ) ;

        b.setLength( 0 );
        b.append( "}" ) ;
        coords = new MirrorCoords(file, 5)  ;
        line = new CodeLine(b, markup, coords, tagSets ) ;
        lines.add(  line  ) ;
        foci.add( coords ) ;
    }

    public TestController(MirrorState state) {
        this.state = state ;
        state.updateStore( makeStore0() ) ;
    }
    
    void next() {
        switch( count ) {
        case 0 : {
            state.setExpression( "\ufffetempF\ufffb = (tempC * 5 / 9) + 32" );
            state.putSelectedCodeLines( file, lines );
            state.setCodeFocus( foci.get(  count % foci.size() ) ) ;
            state.updateStore( makeStore1() ) ;
            count = 1 ;
        } break ;
        case 1 : {
            state.setExpression( "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore2() ) ;
            count = 2 ;
        } break ;
        case 2 : {
            state.setExpression( "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore3() ) ;
            count = 3 ;
        } break ;
        case 3 : {
            state.setExpression( "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore4() ) ;
            count = 4 ;
        } break ;
        case 4 : {
            state.setExpression( "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore5() ) ;
            count = 5 ;
        } break ;
        case 5 : {
            state.setExpression( "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore6() ) ;
            count = 6 ;
        } break ;
        case 6 : {
            state.setExpression( "\uffff50.0\ufffb" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            state.updateStore( makeStore0() ) ;
            count = 7 ;
        } break ;
        case 7 : {
            state.setExpression( "" );
            state.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 0 ;
        } break ;
        }
    }

    @Override
    public void goForward() {
        next() ;
    }

    @Override
    public void goBack() {
        int temp = ((count + 7) - 1) % 7;
        while( count != temp ) next() ;
    }

    @Override
    public void intoExp() {
        next() ;
    }

    @Override
    public void overAll() {
        next() ;
    }

    @Override
    public void intoSub() {
        next() ;
    }
    
    MirrorStore makeStore0() {
        return null ;
    }
    
    MirrorStore makeStore1() {
        return makeStore0() ;
    }
    
    MirrorStore makeStore2() {
        return makeStore0() ;
    }
    
    MirrorStore makeStore3() {
        return makeStore0() ;
    }
    
    MirrorStore makeStore4() {
        return makeStore0() ;
    }
    
    MirrorStore makeStore5() {
        return makeStore0() ;
    }
    
    MirrorStore makeStore6() {
        return makeStore0() ;
    }
    
    
}
