package tm;
import tm.plugins.TestPluginManagerDialog;
import junit.framework.*;
import junit.runner.BaseTestRunner;

public class AllTests
{
        public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite= new TestSuite("Teaching Machine Tests");

        // Clc.Ast
        suite.addTestSuite( tm.clc.ast.ConstIntTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ConstFloatTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ConstPtrTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ExpFetchTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ExpUnimplementedTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpAssignTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpIntTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpFloatTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpPointerTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpArithmeticConversionTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpParenthesesTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpMemberTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ExpThisMemberTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpUpConversionTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpDownConversionTest.class ) ;
        suite.addTestSuite( tm.clc.ast.ExpThisTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpFunctionTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpMemberCallTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpThisMemberCallTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpLogicalTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpIfThenElseTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpPointerTest2.class ) ;
        suite.addTestSuite( tm.clc.ast.OpOpAssignTest.class ) ;
        suite.addTestSuite( tm.clc.ast.OpIncrementTest.class ) ;

        // Clc.Analysis
        suite.addTestSuite( tm.clc.analysis.ScopedNameTest.class ) ;
        suite.addTestSuite( tm.clc.analysis.LFlagsTest.class ) ;
        suite.addTestSuite( tm.clc.analysis.DeclarationTest.class ) ;
        suite.addTestSuite( tm.clc.analysis.DeclarationSetTest.class ) ;
        suite.addTestSuite( tm.clc.analysis.ScopeTest.class ) ;
        suite.addTestSuite( tm.clc.analysis.ScopeHolderTest.class ) ;

        // Clc.RT_Sym_Tab
        suite.addTestSuite( tm.clc.rtSymTab.InitializationChainTest.class ) ;

        // Cpp.Ast
        suite.addTestSuite( tm.cpp.ast.ExpNewTest.class ) ;
        suite.addTestSuite( tm.cpp.ast.ExpNewArrayTest.class ) ;

        // Cpp.Analysis
        suite.addTestSuite( tm.cpp.analysis.Cpp_LFlagsTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.CommonSHTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.ClassSHTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.FunctionSHTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.CTSymbolTableTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.ClassLookupTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.ClassQualLookupTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.FnLookupTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.NsLookupTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.StandardConversionTest.class ) ;
        /*
        suite.addTestSuite( Cpp.Analysis.OverloadResolverTest.class ) ;
        */
        suite.addTestSuite( tm.cpp.analysis.RuntimeIdTest.class ) ;
        suite.addTestSuite( tm.cpp.analysis.LineMapTest.class ) ;
        /*
        suite.addTestSuite( Cpp.Analysis.Eb_OperatorTest.class ) ;
        */

        // JavaLang.Ast
        suite.addTestSuite(tm.javaLang.ast.ConstStrTest.class);
        suite.addTestSuite(tm.javaLang.ast.ExpNewTest.class);
        suite.addTestSuite(tm.javaLang.ast.ExpNewArrayTest.class);
        suite.addTestSuite(tm.javaLang.ast.OpBooleanTest.class);
        
        // Plugin system
        suite.addTestSuite( TestPluginManagerDialog.class ) ;

        return suite;
    }
}
