package tm.gwt.server;

import java.util.Set ;
import java.util.TreeSet ;
import java.util.Vector ;
import java.lang.reflect.Method ;

import org.junit.Test ;

import com.google.gwt.user.client.rpc.SerializationException ;
import com.google.gwt.user.server.rpc.RPC ;
import com.google.gwt.user.server.rpc.RemoteServiceServlet ;
import com.google.gwt.user.server.rpc.SerializationPolicy ;

import tm.gwt.shared.state.MirrorCodeLine ;
import tm.gwt.shared.state.MirrorCoords ;
import tm.gwt.shared.state.MirrorMarkUp ;
import tm.gwt.shared.state.MirrorTMFile ;
import tm.gwt.shared.state.MirrorTagSet ;
import tm.interfaces.CodeLine ;
import tm.interfaces.MarkUp ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.Selection ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.TagSet ;
import tm.interfaces.TagSetInterface ;

public class TestForSerializability {
    static class DummyService extends RemoteServiceServlet {

        public MirrorTMFile mirrorFile() {
            MirrorTMFile mfile = new MirrorTMFile("foo.cpp" );
            return mfile ; }

        public MirrorCoords mirrorCoords() {
            MirrorTMFile mfile = new MirrorTMFile("foo.cpp" );
            MirrorCoords mcoords = new MirrorCoords(mfile, 1) ;
            return mcoords ; }

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

        public Selection selection() {
            Selection sel0 = new Selection("abc") ;
            Selection sel1 = new Selection("def") ;
            Selection sel2 = new Selection(Selection.TokenType.OR, sel0, sel1 ) ;
            return sel2 ;
        }
    }
    
    static void serialize( Object ob, Method m, RemoteServiceServlet servelet ) throws SerializationException {

        //SerializationPolicy serializationPolicy = servelet.getSerializationPolicy( "tmGWT", "" ) ;
        String encoded = RPC.encodeResponseForSuccess( m, ob ) ;
    }
    
    @Test
    public void testMirrorCodeLineFor() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorCodeLine mcl = service.mirrorCodeLine() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorCodeLine" ) ;
        serialize( mcl, m, service) ;
    }
    
    @Test
    public void testMirrorFile() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorTMFile mfile = service.mirrorFile() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorFile" ) ;
        serialize( mfile, m, service ) ;
    }
    
    @Test
    public void testMirrorCoords() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorCoords mcoords = service.mirrorCoords() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorCoords" ) ;
        serialize( mcoords, m, service ) ;
    }
    
    @Test
    public void testSelection() throws Throwable {
        DummyService service = new DummyService() ;
        Selection sel = service.selection() ;
        Method m = DummyService.class.getDeclaredMethod( "selection" ) ;
        serialize( sel, m, service ) ;
    }
    
    @Test
    public void testTagSet() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorTagSet mts = service.mirrorTagSet() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorTagSet" ) ;
        serialize( mts, m, service ) ;
    }
    
    @Test
    public void testMarkUp0() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp0() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp0" ) ;
        serialize( mmu, m, service ) ;
    }
    
    @Test
    public void testMarkUp1() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp1() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp1" ) ;
        serialize( mmu, m, service ) ;
    }
    
    @Test
    public void testMarkUp2() throws Throwable {
        DummyService service = new DummyService() ;
        MirrorMarkUp mmu = service.mirrorMarkUp2() ;
        Method m = DummyService.class.getDeclaredMethod( "mirrorMarkUp2" ) ;
        serialize( mmu, m, service ) ;
    }
}
