package tm.gwt.test;

import java.util.ArrayList ;
import java.util.Set ;
import java.util.TreeSet ;
import java.util.Vector ;

import tm.gwt.state.MirrorCoords ;
import tm.gwt.state.MirrorState ;
import tm.gwt.state.MirrorTMFile ;
import tm.gwt.state.StateCommander ;
import tm.interfaces.CodeLine ;
import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUp ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TMFileI ;
import tm.interfaces.TagSet ;
import tm.interfaces.TagSetInterface ;

public class TestState extends MirrorState implements StateCommander {
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

    public TestState() {
        
    }
    
    void next() {
        switch( count ) {
        case 0 : {
            this.setExpression( "\ufffetempF\ufffb = (tempC * 5 / 9) + 32" );
            this.putSelectedCodeLines( file, lines );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 1 ;
        } break ;
        case 1 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 2 ;
        } break ;
        case 2 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 3 ;
        } break ;
        case 3 : {
            this.setExpression( "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 4 ;
        } break ;
        case 4 : {
            this.setExpression( "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 5 ;
        } break ;
        case 5 : {
            this.setExpression( "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 6 ;
        } break ;
        case 6 : {
            this.setExpression( "\uffff50.0\ufffb" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 7 ;
        } break ;
        case 7 : {
            this.setExpression( "" );
            this.setCodeFocus( foci.get(  count % foci.size() ) );
            count = 0 ;
        } break ;
        }
    }

    @Override
    public void goForward() {
        next() ;
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
}
