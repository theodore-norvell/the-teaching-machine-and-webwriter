package tm.gwt.state;

import java.util.ArrayList ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.Selection ;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.StateInterface ;
import tm.interfaces.StoreInterface ;
import tm.interfaces.TMFileI ;

public class MirrorState implements StateInterface, IsSerializable {

    private String expression = "" ;
    private ArrayList<MirrorCodeLine> codeLines = new ArrayList<MirrorCodeLine>() ;
    private SelectionInterface selection = new Selection(Selection.TokenType.TRUE) ;
    private SourceCoordsI codeFocus = new MirrorCoords(new MirrorTMFile( "" ), 1) ;
    private MirrorStore store = new MirrorStore() ;
    private ArrayList<String> consoleLines = new ArrayList<String>() ;

    public MirrorState() {
    }
    
    public void update( StateInterface newState ) {
        // TODO
    }

    public void setExpression( String exp ) {
        this.expression = exp ;
    }
    
    @Override
    public String getExpression() {
        return expression ;
    }
    
    public void putSelectedCodeLines( TMFileI tmFile, ArrayList<MirrorCodeLine> lines ){
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
    
    public void setSelection( SelectionInterface selection ) {
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
    
    @Deprecated // Should only be needed for testing.
    public void updateStore( StoreInterface store ) {
        this.store.update(  store ) ;
    }

    @Override
    public RegionInterface getStaticRegion() {
        return this.store.staticRegion ;
    }

    @Override
    public RegionInterface getStackRegion() {
        return this.store.stackRegion ;
    }

    @Override
    public RegionInterface getHeapRegion() {
        return this.store.heapRegion ;
    }

    @Override
    public RegionInterface getScratchRegion() {
        return this.store.scratchRegion ;
    }

    public void addConsoleLine( String line ) {
        consoleLines.add(line);
    }

	@Override
	public int getNumConsoleLines() {
		return consoleLines.size();
	}

	@Override
	public String getConsoleLine(int l) {
		return consoleLines.get(l);
	}




}
