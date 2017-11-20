package tm.gwt.state;

import java.util.ArrayList ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.evaluator.Evaluator;
import tm.interfaces.CodeLineI ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.Selection ;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.StateInterface ;
import tm.interfaces.StoreInterface ;
import tm.interfaces.TMFileI ;
import tm.utilities.TMFile;

public class MirrorState implements StateInterface, IsSerializable {

    private String expression = "" ;
    private ArrayList<MirrorCodeLine> codeLines = new ArrayList<MirrorCodeLine>() ;
    private Selection selection = new Selection(Selection.TokenType.TRUE) ;
    private MirrorCoords codeFocus = new MirrorCoords(new MirrorTMFile( "" ), 1) ;
    private MirrorStore store = new MirrorStore() ;
    private ArrayList<String> consoleLines = new ArrayList<String>() ;

    public MirrorState() {
    }

    public void update( StateInterface newState ) {

        expression = newState.getExpression();

        TMFileI tmFile = newState.getCodeFocus().getFile();
        MirrorTMFile mirrorTMFile = new MirrorTMFile(tmFile.getFileName());
        codeLines.clear();
        int size = newState.getNumSelectedCodeLines(tmFile, true);
        for(int i = 0; i < size; i++){
            MirrorCodeLine line = new MirrorCodeLine(newState.getSelectedCodeLine(tmFile, true, i), mirrorTMFile);
            codeLines.add(line);
        }

        codeFocus = new MirrorCoords(mirrorTMFile, newState.getCodeFocus().getLineNumber());

        store.update( newState.getStore() ); 

        for(int i = 0; i < consoleLines.size(); i++){
            consoleLines.set(i, newState.getConsoleLine(i));
        }
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

    public void setSelection( Selection selection ) {
        this.selection = selection ;
    }

    @Override
    public SelectionInterface getSelection() {
        return selection ;
    }

    public void setCodeFocus(MirrorCoords coords) {
        this.codeFocus = coords ;
    }

    @Override
    public SourceCoordsI getCodeFocus() {
        return this.codeFocus ;
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

    @Override
    public MirrorStore getStore() {
        return store ;
    }




}
