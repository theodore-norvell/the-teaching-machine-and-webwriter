package tm.gwt.server;

import java.util.Set ;
import java.util.TreeSet ;
import java.util.Vector ;
import java.lang.reflect.Method ;

import org.junit.Test ;

import com.google.gwt.user.client.rpc.SerializationException ;
import com.google.gwt.user.server.rpc.RPC ;
import com.google.gwt.user.server.rpc.RemoteServiceServlet ;

import tm.gwt.shared.state.MirrorCodeLine ;
import tm.gwt.shared.state.MirrorCoords ;
import tm.gwt.shared.state.MirrorMarkUp ;
import tm.gwt.shared.state.MirrorTMFile ;
import tm.gwt.shared.state.MirrorTagSet ;
import tm.interfaces.CodeLine ;
import tm.interfaces.MarkUp ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.SourceCoords ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TagSet ;
import tm.interfaces.TagSetInterface ;
import tm.utilities.StringFileSource ;
import tm.utilities.TMFile ;

public class TestForSerializability {
    static class DummyService extends RemoteServiceServlet {

        public MirrorTMFile mirrorFile() {
            MirrorTMFile mfile = new MirrorTMFile("foo.cpp" );
            return mfile ; }

        public MirrorTagSet mirrorTagSet() {
            TagSet ts = new TagSet("abc") ;
            MirrorTagSet mts = new MirrorTagSet( ts ) ;
            return mts ;
        }

        public MirrorMarkUp mirrorMarkUp0() {
            MarkUp mu = new MarkUp( 0, MarkUpI.COMMENT ) ;
            MirrorMarkUp mmu = new MirrorMarkUp( mu ) ;
            return mmu ;
        }

        public MirrorMarkUp mirrorMarkUp1() {
            TagSet ts = new TagSet("abc") ;
            MarkUp mu = new MarkUp( 0, MarkUpI.CHANGE_TAG_SET, ts ) ;
            MirrorMarkUp mmu = new MirrorMarkUp( mu ) ;
            return mmu ;
        }

        public MirrorMarkUp mirrorMarkUp2() {
            TagSet ts = TagSet.getEmpty() ;
            MarkUp mu = new MarkUp( 0, MarkUpI.CHANGE_TAG_SET, ts ) ;
            MirrorMarkUp mmu = new MirrorMarkUp( mu ) ;
            return mmu ;
        }
        
        public MirrorCodeLine mirrorCodeLine() {
            StringBuffer buff = new StringBuffer() ;
            MirrorTMFile file = new MirrorTMFile("foo.cpp");
            Vector<MarkUp> markUpVector = new Vector<MarkUp>() ;
            SourceCoordsI sc = new MirrorCoords( file, 1 ) ;
            Set<TagSetInterface> tagSets = new TreeSet<TagSetInterface>();
            CodeLine codeLine = new CodeLine( buff, markUpVector, sc, tagSets ) ;
            return new MirrorCodeLine( codeLine, file ) ; }
    }
    
    static void serialize( Object ob, Method m ) throws SerializationException {
        String encoded = RPC.encodeResponseForSuccess( m, ob ) ;
    }
    
    @Test
    public void testMirrorCodeLineFor() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorCodeLine mcl = service.mirrorCodeLine() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorCodeLine" ) ;
        serialize( mcl, m) ;
    }
    
    @Test
    public void testMirrorFile() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorTMFile mfile = service.mirrorFile() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorFile" ) ;
        serialize( mfile, m) ;
    }
    
    @Test
    public void testTagSet() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorTagSet mts = service.mirrorTagSet() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorTagSet" ) ;
        serialize( mts, m) ;
    }
    
    @Test
    public void testMarkUp0() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp0() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp0" ) ;
        serialize( mmu, m) ;
    }
    
    @Test
    public void testMarkUp1() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp1() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp1" ) ;
        serialize( mmu, m) ;
    }
    
    @Test
    public void testMarkUp2() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp2() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp2" ) ;
        serialize( mmu, m) ;
    }
}
