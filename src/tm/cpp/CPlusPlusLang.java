//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.cpp;

import java.util.* ;
import java.io.* ;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.AbstractFunctionDefn;
import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.FunctionDefnBuiltIn;
import tm.clc.ast.FunctionDefnCompiled;
import tm.clc.ast.StatementNodeLink;
import tm.clc.ast.Stepper;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.cpp.analysis.FileMap;
import tm.cpp.analysis.ParserContext;
import tm.cpp.ast.Cpp_ASTUtilities;
import tm.cpp.ast.Cpp_StepperBuiltIn;
import tm.cpp.ast.TyFun;
import tm.cpp.ast.TyInt;
import tm.cpp.ast.TyVoid;
import tm.cpp.datum.DatumUtilities;
import tm.cpp.parser.Parser;
import tm.cpp.parser.ParserTokenManager;
import tm.cpp.preprocessor.Preprocessor;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.ViewableST;
import tm.languageInterface.Language;
import tm.languageInterface.StatementInterface;
import tm.utilities.Assert;
import tm.utilities.CachingFileSource;
import tm.utilities.Debug;
import tm.utilities.FileSource;
import tm.utilities.LocalResourceFileSource;
import tm.utilities.TMFile;
import tm.virtualMachine.FunctionEvaluation;
import tm.virtualMachine.VMState;

public class CPlusPlusLang implements Language {

    // We'll make this static so that the cache can work across multiple
    // loads.
    private static CachingFileSource stdFileSource = null ;
    
    CPlusPlusLang() {}

    
    public void addProgramArgument( String argument ) {
        // TODO implement program arguments
    }
    
    public ViewableST makeSymTab( BTTimeManager timeMan) {
        return new RT_Symbol_Table( timeMan ) ; }

    public void compile( TMFile file, VMState vms)
    {
        // Preprocess
            FileMap fileMap = new FileMap() ;
            Reader reader = file.toReader() ;
            Assert.error( reader != null, "Can not open file "+ file );
            if( stdFileSource == null ) {
                stdFileSource = new CachingFileSource(
                                    new LocalResourceFileSource(this.getClass(), "include/", ".inc") ) ; }
            Preprocessor preprocessor = new Preprocessor( reader, file, fileMap, vms.getCodeStore(), stdFileSource  );
            StringBuffer preprocessed = new StringBuffer ();
            preprocessor.translation_unit (preprocessed);

        // Prepare the VMState
            vms.setProperty("ASTUtilities", new Cpp_ASTUtilities() ) ;
            vms.setProperty("DatumUtilities", new DatumUtilities() ) ;
        // Parse
            Debug.getInstance().msg(Debug.COMPILE, "Input<"+preprocessed.toString ()+">");
            Reader inputReader = new BufferedReader(
                    new java.io.StringReader (preprocessed.toString ()) );
            ParserTokenManager patm= new ParserTokenManager(
                   new tm.cpp.parser.SimpleCharStream (inputReader, 1, 1));
            Parser parser = new Parser (patm);
            ParserContext pc = new ParserContext( vms, file ) ;
            parser.init( pc, fileMap ) ;
            parser.translation_unit (file.getFileName(), new StatementNodeLink ()); }

    public void initializeTheState( VMState vms )  {
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
        
        // Stdlib.h
        

        Stepper rand_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.RAND  ) ;
        Stepper srand_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SRAND  ) ;
        
        addBuiltInFunctionDefinition(symtab, "::rand$int()", rand_stepper );
        addBuiltInFunctionDefinition(symtab, "::srand$void(unsigned)", srand_stepper );

        // Assertion support
        
        Stepper assert_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.ASSERT ) ;
        addBuiltInFunctionDefinition(symtab, "::assert$void(bool)", assert_stepper );
        
        // Math
        Stepper acos_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.ACOS  ) ;
        Stepper asin_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.ASIN  ) ;
        Stepper atan_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.ATAN  ) ;
        Stepper atan2_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.ATAN2  ) ;
        Stepper ceil_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.CEIL  ) ;
        Stepper cos_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.COS ) ;
        Stepper exp_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.EXP  ) ;
        Stepper fabs_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.FABS  ) ;
        Stepper floor_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.FLOOR  ) ;
        Stepper log_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.LOG  ) ;
        Stepper log10_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.LOG10  ) ;
        Stepper pow_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.POW  ) ;
        Stepper sin_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SIN  ) ;
        Stepper sqrt_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SQRT ) ;
        Stepper tan_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.TAN  ) ;

        addBuiltInFunctionDefinition(symtab, "::abs$float(float)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::acos$float(float)", acos_stepper );
        addBuiltInFunctionDefinition(symtab, "::asin$float(float)", asin_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan$float(float)", atan_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan2$float(float, float)", atan2_stepper );
        addBuiltInFunctionDefinition(symtab, "::ceil$float(float)", ceil_stepper );
        addBuiltInFunctionDefinition(symtab, "::cos$float(float)", cos_stepper );
        addBuiltInFunctionDefinition(symtab, "::exp$float(float)", exp_stepper );
        addBuiltInFunctionDefinition(symtab, "::fabs$float(float)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::floor$float(float)", floor_stepper );
        addBuiltInFunctionDefinition(symtab, "::log$float(float)", log_stepper );
        addBuiltInFunctionDefinition(symtab, "::log10$float(float)", log10_stepper );
        addBuiltInFunctionDefinition(symtab, "::pow$float(float, float)", pow_stepper );
        addBuiltInFunctionDefinition(symtab, "::sin$float(float)", sin_stepper );
        addBuiltInFunctionDefinition(symtab, "::sqrt$float(float)", sqrt_stepper );
        addBuiltInFunctionDefinition(symtab, "::tan$float(float)", tan_stepper );

        addBuiltInFunctionDefinition(symtab, "::abs$double(double)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::acos$double(double)", acos_stepper );
        addBuiltInFunctionDefinition(symtab, "::asin$double(double)", asin_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan$double(double)", atan_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan2$double(double, double)", atan2_stepper );
        addBuiltInFunctionDefinition(symtab, "::ceil$double(double)", ceil_stepper );
        addBuiltInFunctionDefinition(symtab, "::cos$double(double)", cos_stepper );
        addBuiltInFunctionDefinition(symtab, "::exp$double(double)", exp_stepper );
        addBuiltInFunctionDefinition(symtab, "::fabs$double(double)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::floor$double(double)", floor_stepper );
        addBuiltInFunctionDefinition(symtab, "::log$double(double)", log_stepper );
        addBuiltInFunctionDefinition(symtab, "::log10$double(double)", log10_stepper );
        addBuiltInFunctionDefinition(symtab, "::pow$double(double, double)", pow_stepper );
        addBuiltInFunctionDefinition(symtab, "::sin$double(double)", sin_stepper );
        addBuiltInFunctionDefinition(symtab, "::sqrt$double(double)", sqrt_stepper );
        addBuiltInFunctionDefinition(symtab, "::tan$double(double)", tan_stepper );


        addBuiltInFunctionDefinition(symtab, "::abs$long double(long double)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::acos$long double(long double)", acos_stepper );
        addBuiltInFunctionDefinition(symtab, "::asin$long double(long double)", asin_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan$long double(long double)", atan_stepper );
        addBuiltInFunctionDefinition(symtab, "::atan2$long double(long double, long double)", atan2_stepper );
        addBuiltInFunctionDefinition(symtab, "::ceil$long double(long double)", ceil_stepper );
        addBuiltInFunctionDefinition(symtab, "::cos$long double(long double)", cos_stepper );
        addBuiltInFunctionDefinition(symtab, "::exp$long double(long double)", exp_stepper );
        addBuiltInFunctionDefinition(symtab, "::fabs$long double(long double)", fabs_stepper );
        addBuiltInFunctionDefinition(symtab, "::floor$long double(long double)", floor_stepper );
        addBuiltInFunctionDefinition(symtab, "::log$long double(long double)", log_stepper );
        addBuiltInFunctionDefinition(symtab, "::log10$long double(long double)", log10_stepper );
        addBuiltInFunctionDefinition(symtab, "::pow$long double(long double, long double)", pow_stepper );
        addBuiltInFunctionDefinition(symtab, "::sin$long double(long double)", sin_stepper );
        addBuiltInFunctionDefinition(symtab, "::sqrt$long double(long double)", sqrt_stepper );
        addBuiltInFunctionDefinition(symtab, "::tan$long double(long double)", tan_stepper );

        // Input
        Stepper get_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.GET  ) ;
        Stepper input_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.INPUT  ) ;
        Stepper eof_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.EOF  ) ;
        Stepper fail_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.FAIL  ) ;
        addBuiltInFunctionDefinition(symtab, "::istream::get$istream&(char&)", get_stepper );
        addBuiltInFunctionDefinition(symtab, "::istream::eof$bool()", eof_stepper );
        addBuiltInFunctionDefinition(symtab, "::istream::fail$bool()", fail_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, char&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, signed char&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, unsigned char&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, bool&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, short&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, unsigned short&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, int&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, unsigned&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, long&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, unsigned long&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, float&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, double&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, long double&)", input_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator>>$istream&(istream&, char*)", input_stepper );


        // Output
        Stepper put_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.PUT ) ;
        Stepper output_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.OUTPUT  ) ;
        addBuiltInFunctionDefinition(symtab, "::ostream::put$ostream&(char)", put_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, char)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, signed char)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, unsigned char)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, bool)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, short)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, unsigned short)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, int)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, unsigned)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, long)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, unsigned long)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, float)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, double)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, long double)", output_stepper );
        addBuiltInFunctionDefinition(symtab, "::operator<<$ostream&(ostream&, char*)", output_stepper );
        
        // Internal Scripting
        Stepper relay_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SCRIPT_RELAY );
        Stepper relayRtnInt_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SCRIPT_RELAY_RTN_INT );
        Stepper relayRtnDouble_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SCRIPT_RELAY_RTN_DOUBLE );
           Stepper snapshot_stepper = new Cpp_StepperBuiltIn( Cpp_StepperBuiltIn.SCRIPT_SNAPSHOT);
        addBuiltInFunctionDefinition(symtab, "::ScriptManager::relay$void(char*, char*)", relay_stepper);
        addBuiltInFunctionDefinition(symtab, "::ScriptManager::relayRtnInt$int(char*, char*)", relayRtnInt_stepper);
        addBuiltInFunctionDefinition(symtab, "::ScriptManager::relayRtnDouble$double(char*, char*)", relayRtnDouble_stepper);
             addBuiltInFunctionDefinition(symtab, "::ScriptManager::snapshot$void(char*, char*)", snapshot_stepper);

        executeInitializations( vms, symtab ) ;

        callMain( vms, symtab ) ;
     }

    /** Add a new Built-in function to the symbol table if there is no
     * previous defnition for it.
     * @param symtab
     * @param key
     * @param stepper
     */
    private void addBuiltInFunctionDefinition(RT_Symbol_Table symtab, Object key, Stepper stepper) {
        if( symtab.getFunctionDefn( key ) == null ) {
            AbstractFunctionDefn funDef = new FunctionDefnBuiltIn( key, stepper ) ;
            symtab.addFunctionDefinition( funDef ) ; }
    }

    private void executeInitializations( VMState vms, RT_Symbol_Table symtab ) {
        Enumeration e = symtab.getInitializationChains() ;
        while( e.hasMoreElements() ) {
            StatementNodeLink link = (StatementNodeLink) e.nextElement() ;

            // Create a void datum to hold the result
            // TODO Check if this is really needed.
            TypeNode retType = TyVoid.get() ;
            Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractDatum resultDatum = util.scratchDatum(  retType, vms ) ;
            //vms.pushResultDatum( resultDatum ) ;

            vms.pushNewArgumentList() ;

            // Push an evaluation the chain the stack.
            Assert.check( vms.isEmpty() ) ;
            Assert.check( link.get() != null ) ;
            StatementInterface firstStatement = link.get() ;
            FunctionEvaluation funEval = new FunctionEvaluation( vms, firstStatement, null, resultDatum ) ;
            vms.push( funEval ) ;

            // We need to call enterFunction here because the
            // return will call exitFuntion.
            symtab.enterFunction( null ) ;
            vms.getStore().setStackMark() ;

            //  Now we execute the chain.
            while( ! vms.isEmpty() ) {
                vms.advance() ; }
        
            //vms.popResultDatum( ) ;
        }
    }

    private void callMain( VMState vms, RT_Symbol_Table symtab ) {
        // Find Main.
            Vector mainKeyVect = symtab.getMainFunctionKeys() ;
            Assert.apology( mainKeyVect.size() >0 , "No 'main' function found." ) ;
            Assert.apology( mainKeyVect.size() == 1, "Sorry multiple candidates for 'main' function. ");
            String mainKey = (String)( mainKeyVect.elementAt(0) ) ;
    
            FunctionDefnCompiled funDef = (FunctionDefnCompiled) symtab.getFunctionDefn( mainKey ) ;
            Assert.apology(funDef != null, "No 'main' function found." ) ;
            Debug.getInstance().msg(Debug.EXECUTE,  "Main function:" );
            Debug.getInstance().msg(Debug.EXECUTE,  funDef.toString() );

        // Create a datum to hold the result
            TypeNode retType = TyInt.get() ;
            Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractDatum resultDatum = util.scratchDatum(  retType, vms ) ;
        //vms.pushResultDatum( resultDatum ) ;

            vms.pushNewArgumentList() ;

        // Push an evaluation for main on the stack.
            Assert.check( vms.isEmpty() ) ;
            StatementInterface mainStatement = funDef.getBodyLink().get() ;
            FunctionEvaluation funEval = new FunctionEvaluation( vms, mainStatement, null, resultDatum ) ;
            vms.push( funEval ) ;

        // Argv and argc would be entered into the vms here.

        // We need to call enterFunction here because the
        // return will call exitFuntion.
            symtab.enterFunction( null ) ;
            vms.getStore().setStackMark() ;  }

    public String getName() {return "C++ (TNG)" ; }

    public int getLanguage() {
        return EvaluatorInterface.CPP_LANG ;
    }


    @Override
    public void callSubroutine(VMState vms, Object key, Object[] args) {
        Debug.getInstance().msg(Debug.EXECUTE,  "Calling function"+key ) ;
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
         // Find Main.
            AbstractFunctionDefn absFunDef = symtab.getFunctionDefn(key) ;
            Assert.scriptingError(absFunDef != null, "Function "+key+" Not found" ) ;
            Assert.scriptingError( absFunDef instanceof FunctionDefnCompiled, "Function "+key+" is not a compiled function") ;
            FunctionDefnCompiled funDef = (FunctionDefnCompiled) absFunDef ;  
            

        // Create a datum to hold the result
            TypeNode retType = TyInt.get() ;
            Clc_ASTUtilities util = (Clc_ASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractDatum resultDatum = util.scratchDatum(  retType, vms ) ;
        //vms.pushResultDatum( resultDatum ) ;

            vms.pushNewArgumentList() ;

        // Push an evaluation for main on the stack.
            Assert.check( vms.isEmpty() ) ;
            StatementInterface firstStatement = funDef.getBodyLink().get() ;
            FunctionEvaluation funEval = new FunctionEvaluation( vms, firstStatement, null, resultDatum ) ;
            vms.push( funEval ) ;

        // TODO deal with arguments

        // We need to call enterFunction here because the
        // return will call exitFuntion.
            symtab.enterFunction( null ) ;
            vms.getStore().setStackMark() ;
    }
}
