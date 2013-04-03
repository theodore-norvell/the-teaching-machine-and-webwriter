package tm;
import tm.evaluator.TestCommandStringInterpreter;
import tm.plugins.TestPluginManagerDialog;
import junit.framework.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite ;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	tm.clc.ast.ConstIntTest.class,
    tm.clc.ast.ConstFloatTest.class,
    tm.clc.ast.ConstPtrTest.class,
    tm.clc.ast.ExpFetchTest.class,
    tm.clc.ast.ExpUnimplementedTest.class,
    tm.clc.ast.OpAssignTest.class,
    tm.clc.ast.OpIntTest.class,
    tm.clc.ast.OpFloatTest.class,
    tm.clc.ast.OpPointerTest.class,
    tm.clc.ast.OpArithmeticConversionTest.class,
    tm.clc.ast.OpParenthesesTest.class,
    tm.clc.ast.OpMemberTest.class,
    tm.clc.ast.ExpThisMemberTest.class,
    tm.clc.ast.OpUpConversionTest.class,
    tm.clc.ast.OpDownConversionTest.class,
    tm.clc.ast.ExpThisTest.class,
    tm.clc.ast.OpFunctionTest.class,
    tm.clc.ast.OpMemberCallTest.class,
    tm.clc.ast.OpThisMemberCallTest.class,
    tm.clc.ast.OpLogicalTest.class,
    tm.clc.ast.OpIfThenElseTest.class,
    tm.clc.ast.OpPointerTest2.class,
    tm.clc.ast.OpOpAssignTest.class,
    tm.clc.ast.OpIncrementTest.class,

    // Clc.Analysis
    tm.clc.analysis.ScopedNameTest.class,
    tm.clc.analysis.LFlagsTest.class,
    tm.clc.analysis.DeclarationTest.class,
    tm.clc.analysis.DeclarationSetTest.class,
    tm.clc.analysis.ScopeTest.class,
    tm.clc.analysis.ScopeHolderTest.class,

    // Clc.RT_Sym_Tab
    tm.clc.rtSymTab.InitializationChainTest.class,

    // Cpp.Ast
    tm.cpp.ast.ExpNewTest.class,
    tm.cpp.ast.ExpNewArrayTest.class,

    // Cpp.Analysis
    tm.cpp.analysis.Cpp_LFlagsTest.class,
    tm.cpp.analysis.CommonSHTest.class,
    tm.cpp.analysis.ClassSHTest.class,
    tm.cpp.analysis.FunctionSHTest.class,
    tm.cpp.analysis.CTSymbolTableTest.class,
    tm.cpp.analysis.ClassLookupTest.class,
    tm.cpp.analysis.ClassQualLookupTest.class,
    tm.cpp.analysis.FnLookupTest.class,
    tm.cpp.analysis.NsLookupTest.class,
    tm.cpp.analysis.StandardConversionTest.class,
    /*
    Cpp.Analysis.OverloadResolverTest.class,
    */
    tm.cpp.analysis.RuntimeIdTest.class,
    tm.cpp.analysis.LineMapTest.class,
    /*
    Cpp.Analysis.Eb_OperatorTest.class,
    */

    // JavaLang.Ast
    tm.javaLang.ast.ConstStrTest.class,
    tm.javaLang.ast.ExpNewTest.class,
    tm.javaLang.ast.ExpNewArrayTest.class,
    tm.javaLang.ast.OpBooleanTest.class,
    
    // Plugin system
    TestPluginManagerDialog.class,
    
    // Command string interpretation
    TestCommandStringInterpreter.class,
    
    // Undo and redo
    tm.backtrack.BackTrackTests2.class })   
public class AllTests
{
        public static void main(String[] args) {
        	//TODO 
    }
}
