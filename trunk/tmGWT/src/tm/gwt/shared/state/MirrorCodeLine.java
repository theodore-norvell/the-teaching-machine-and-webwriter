package tm.gwt.shared.state;

import java.util.TreeSet ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUpI;
import tm.interfaces.SourceCoordsI ;

import com.google.gwt.user.client.rpc.IsSerializable ;

// TODO. Consider deleting this class.
public class MirrorCodeLine implements IsSerializable, CodeLineI {
    private char[] chars ;
    private MirrorMarkUp[] markUp ;
    private MirrorCoords coords ;

    private MirrorCodeLine() {}

    public MirrorCodeLine(CodeLineI codeLineI, MirrorTMFile file){
        chars = codeLineI.getChars();
        MarkUpI[] codeLineMarkUp = codeLineI.markUp() ;
        markUp = new MirrorMarkUp[ codeLineMarkUp.length ] ;
        for( int i=0, sz=codeLineMarkUp.length ; i<sz ; ++i ) {
            markUp[i] = new MirrorMarkUp( codeLineMarkUp[i] ) ;
        }
        coords = new MirrorCoords(file, codeLineI.getCoords().getLineNumber());
    }

    public MirrorCodeLine( String str, MirrorMarkUp[] markUp, MirrorCoords sc) {
        chars = str.toCharArray() ;

        this.markUp = markUp ;

        coords = sc ;
    }

    @Override
    public char[] getChars() { return chars ; }

    @Override
    public SourceCoordsI getCoords() { return coords ; }

    @Override
    public MarkUpI[] markUp() { return markUp ; }

    @Override
    public String toString() {
        String ch = new String(chars) ;
        StringBuffer mu = new StringBuffer("[") ;
        for(int i=0 ; i < markUp.length ; ++i ) {
            if( i>0 ) mu.append(", ") ; mu.append( markUp[i] ) ; }
        mu.append("]") ;
        return "MirrorCodeLine "+coords+" <"+ch+">, "+mu ; }
}