package tm.gwt.state;

import java.util.ArrayList ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.StateInterface ;
import tm.interfaces.TMFileI ;

public class MirrorState implements StateInterface, IsSerializable {

    private String expression ;
    private ArrayList<CodeLineI> codeLines = new ArrayList<CodeLineI>() ;
    private SelectionInterface selection ;
    private SourceCoordsI codeFocus ;
    private MirrorRegion staticRegion ;
    private MirrorRegion stackRegion ;
    private MirrorRegion heapRegion ;
    private MirrorRegion scratchRegion ;

    public void putExpression( String exp ) {
        this.expression = exp ;
    }
    
    @Override
    public String getExpression() {
        return expression ;
    }
    
    public void putSelectedCodeLines( TMFileI tmFile, ArrayList<CodeLineI> lines ){
        this.codeLines = lines ;
    }

    @Override
    public CodeLineI getSelectedCodeLine(TMFileI tmFile, boolean allowGaps, int n) {
        if( allowGaps ) 
            return codeLines.get( n ) ;
        else {
            int c = 0 ;
            for( int i = 0, sz = codeLines.size() ; i < sz ; ++i ) {
                if( codeLines.get( i ) != null ) {
                    if( c == n ) return codeLines.get( i ) ;
                    else c++ ; }  }
            throw new AssertionError( "Line no found" ) ; }
    }

    @Override
    public int getNumSelectedCodeLines(TMFileI tmFile, boolean allowGaps) {
        if( allowGaps ) 
            return codeLines.size() ;
        else {
            int c = 0 ;
            for( int i = 0, sz = codeLines.size() ; i < sz ; ++i )
                if( codeLines.get( i ) != null ) c++ ;
            return c ; }
    }
    
    public void putSelection( SelectionInterface selection ) {
        this.selection = selection ;
    }

    @Override
    public SelectionInterface getSelection() {
        return selection ;
    }

    public void setCodeFocus(SourceCoordsI coords) {
        this.codeFocus = coords ;
    }

    @Override
    public SourceCoordsI getCodeFocus() {
        return this.codeFocus ;
    }

    @Override
    public RegionInterface getStaticRegion() {
        return this.staticRegion ;
    }

    @Override
    public RegionInterface getStackRegion() {
        return this.stackRegion ;
    }

    @Override
    public RegionInterface getHeapRegion() {
        return this.heapRegion ;
    }

    @Override
    public RegionInterface getScratchRegion() {
        return this.scratchRegion ;
    }

}
