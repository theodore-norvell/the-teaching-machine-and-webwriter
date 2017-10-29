package tm.gwt.state;

import java.util.TreeSet ;
import java.util.Vector ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUp ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TagSetInterface ;

import com.google.gwt.user.client.rpc.IsSerializable ;

public class MirrorCodeLine implements IsSerializable, CodeLineI {
    private char[] chars ;
    private MarkUp[] markUp ;
    private MirrorCoords coords ;
    /** A list of all tag sets that apply anywhere on this line */
    private TreeSet<TagSetInterface> tagSets ;

    private MirrorCodeLine() {}
    
    public MirrorCodeLine( StringBuffer buff, Vector<MarkUp> markUpVector, MirrorCoords sc, TreeSet<TagSetInterface> tagSets) {
        chars = new char[ buff.length() ] ;
        if( buff.length() > 0 ) // Work around jview bug!
            buff.getChars(0, buff.length(), chars, 0);

        markUp = new MarkUp[ markUpVector.size() ] ;
        for( int i = 0, sz = markUpVector.size() ; i < sz ; ++i ) {
            markUp[i] = markUpVector.elementAt(i) ; }

        coords = sc ;
        this.tagSets = tagSets ;
    }

    @Override
    public char[] getChars() { return chars ; }

    @Override
    public SourceCoordsI getCoords() { return coords ; }

    @Override
    public MarkUp[] markUp() { return markUp ; }

    @Override
    public String toString() {
        String ch = new String(chars) ;
        StringBuffer mu = new StringBuffer("[") ;
        for(int i=0 ; i < markUp.length ; ++i ) {
            if( i>0 ) mu.append(", ") ; mu.append( markUp[i] ) ; }
        mu.append("]") ;
        return "ClcCodeLine "+coords+" <"+ch+">, "+tagSets+", "+mu ; }
}