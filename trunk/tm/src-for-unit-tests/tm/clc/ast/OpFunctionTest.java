package tm.clc.ast;

import java.util.Vector ;

import tm.clc.analysis.* ;
import tm.clc.ast.*;
import tm.clc.datum.* ;
import tm.cpp.analysis.* ;
import tm.cpp.ast.* ;
import tm.cpp.datum.* ;
import tm.interfaces.SourceCoords;
import tm.virtualMachine.* ;

public class OpFunctionTest extends Cpp_AbstractAstTest {

    public OpFunctionTest() { this ("OpFunctionTest"); }
    public OpFunctionTest (String name) { super (name); print=true;}

    FunctionDefnCompiled defn ;
    ScopedName fred, i, j ;
    TypeNode tyInt ;
    TyAbstractRef tyRefToInt ;
    TypeNode fredsType ;
    ExpressionNode call ;

    protected void makeDefn() {
        /* Definition of:
             int fred(int i, int j) {
                return i-j ; }
        */
        // Some types
        tyInt = TyInt.get() ;
        tyRefToInt = new TyRef( tyInt ) ;
        // Some names
        fred = new Cpp_ScopedName("fred");
        i = new Cpp_ScopedName("fred") ; i.set_absolute() ; i.append("i") ;
        j = new Cpp_ScopedName("fred") ; j.set_absolute() ; j.append("j") ;
        // Some var nodes
        VarNode i_vn = new VarNode( i, tyInt ) ;
        VarNode j_vn = new VarNode( j, tyInt ) ;
        // Fred's type.
        Vector params = new Vector() ;
        params.addElement( tyInt ) ;
        params.addElement( tyInt ) ;
        fredsType = new TyFun( params, false ) ;
        fredsType.addToEnd( tyInt ) ;
        // DECL for i
        StatDecl i_decl = new StatDecl( SourceCoords.UNKNOWN, 0, i_vn ) ;
        // Init of i
        ExpressionNode i_assign =
            new OpAssign( tyRefToInt,
                           "=",
                           new ExpId( tyRefToInt,
                                       "i",
                                       i ),
                           new ExpArgument( tyInt,
                                             0 ) ) ;

        StatDo i_init = new StatDo(SourceCoords.UNKNOWN, 1, i_assign ) ;
        // DECL for j
        StatDecl j_decl = new StatDecl(SourceCoords.UNKNOWN, 1, j_vn ) ;
        // Init of j
        ExpressionNode j_assign =
            new OpAssign( tyRefToInt,
                           "=",
                           new ExpId( tyRefToInt,
                                       "j",
                                       j ),
                           new ExpArgument( tyInt,
                                             1 ) ) ;

        StatDo j_init = new StatDo(SourceCoords.UNKNOWN, 2, j_assign ) ;
        // result <- i - j
        ExpressionNode result_assign =
            new OpAssign( tyRefToInt,
                           "=",
                           new ExpResult( tyRefToInt ),
                           new OpInt( tyInt,
                                       Arithmetic.SUBTRACT,
                                       "+",
                                       new ExpFetch( tyInt,
                                                      new ExpId( tyRefToInt,
                                                                 "i",
                                                                 i ) ),
                                       new ExpFetch( tyInt,
                                                      new ExpId( tyRefToInt,
                                                                 "j",
                                                                 j ) ) ) ) ;
       StatDo result = new StatDo( SourceCoords.UNKNOWN, 2, result_assign ) ;
       // RETURN
       StatReturn rtn = new StatReturn(SourceCoords.UNKNOWN, 2 ) ;
       // Put the body together.
       i_decl.next().set( i_init ) ;
       i_init.next().set( j_decl ) ;
       j_decl.next().set( j_init ) ;
       j_init.next().set( result ) ;
       result.next().set( rtn ) ;
       // Build the definition
       Object fredsKey = OverloadResolver.munge (fred, fredsType) ;
       defn = new FunctionDefnCompiled( fredsKey ) ;
       defn.getBodyLink().set( i_decl ) ;

       // Put it in the symbol table
       this.symtab.addFunctionDefinition( defn ) ;
    }

    protected void makeCall() {
        // fred( 42, 39 )
        NodeList args = new NodeList() ;
        args.addLastChild( new ConstInt(tyInt, "42", 42 ) ) ;
        args.addLastChild( new ConstInt(tyInt, "39", 39 ) ) ;
        Object key = defn.getKey() ;
        call = new OpFuncCall( tyInt, "fred", false, key, args ) ; }

    public void testFunctionCall() {
        makeDefn() ;
        makeCall() ;

        ExpressionEvaluation ee = new ExpressionEvaluation( this.vms, call ) ;
        this.vms.push( ee ) ;

        advance( 1000 ) ;

        Object result0 = ee.at( call ) ;
        assertTrue( result0 instanceof AbstractIntDatum ) ;
        AbstractIntDatum result1 = (AbstractIntDatum) result0 ;
        long v = result1.getValue() ;
        assertTrue( v == 3 ) ;
    }


}
