package tm.gwt.shared.state;

import java.util.TreeSet ;
import java.util.Vector ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUp ;
import tm.interfaces.MarkUpI;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TagSetInterface ;

import com.google.gwt.user.client.rpc.IsSerializable ;

public class MirrorCodeLine implements IsSerializable, CodeLineI {
    private char[] chars ;
    private MirrorMarkUp[] markUp ;
    private MirrorCoords coords ;
    /** A list of all tag sets that apply anywhere on this line */
    private TreeSet<MirrorTagSet> tagSets ;

    private MirrorCodeLine() {}

    public MirrorCodeLine(CodeLineI codeLineI, MirrorTMFile file){
        chars = codeLineI.getChars();
        MarkUpI[] codeLineMarkUp = codeLineI.markUp() ;
        markUp = new MirrorMarkUp[ codeLineMarkUp.length ] ;
        for( int i=0, sz=codeLineMarkUp.length ; i<sz ; ++i ) {
            markUp[i] = new MirrorMarkUp( codeLineMarkUp[i] ) ;
        }
        coords = new MirrorCoords(file, codeLineI.getCoords().getLineNumber());
        tagSets = new TreeSet<MirrorTagSet>(); // TODO. Copy the tag sets.
    }

    public MirrorCodeLine( String str, MirrorMarkUp[] markUp, MirrorCoords sc, TreeSet<MirrorTagSet> tagSets) {
        chars = str.toCharArray() ;

        this.markUp = markUp ;

        coords = sc ;
        this.tagSets = tagSets ;
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
        return "ClcCodeLine "+coords+" <"+ch+">, "+tagSets+", "+mu ; }
}