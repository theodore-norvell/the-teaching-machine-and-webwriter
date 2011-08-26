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

package tm.javaLang;

import java.util.ArrayList;
import java.util.Vector;

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.ScopeHolder;
import tm.clc.ast.AbstractFunctionDefn;
import tm.clc.ast.Clc_ASTUtilities;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.FunctionDefnCompiled;
import tm.clc.ast.TypeNode;
import tm.clc.ast.Stepper;
import tm.clc.datum.AbstractDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.ViewableST;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.ast.ConstBool;
import tm.javaLang.ast.ExpEnsureClassInit;
import tm.javaLang.ast.JavaLangASTUtilities;
import tm.javaLang.ast.Java_StepperBuiltInGeneric;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.Java_StepperBuiltIn;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyVoid;
import tm.javaLang.datum.ArrayDatum;
import tm.javaLang.datum.DatumUtilities;
import tm.javaLang.datum.ObjectDatum;
import tm.javaLang.datum.PointerDatum;
import tm.javaLang.parser.FourthPass;
import tm.javaLang.parser.JavaParser;
import tm.javaLang.parser.SecondPass;
import tm.javaLang.parser.SimpleNode;
import tm.javaLang.parser.SimpleVisitor;
import tm.javaLang.parser.ThirdPass;
import tm.languageInterface.Language;
import tm.languageInterface.StatementInterface;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMFile;
import tm.virtualMachine.ExpressionEvaluation;
import tm.virtualMachine.FunctionEvaluation;
import tm.virtualMachine.VMState;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author Theodore Norvell & Michael Bruce-Lockhart
 * @version 1.0
 */

public class JLang implements Language {
    private static Debug d = Debug.getInstance();
    private VMState vms;
    private Java_CTSymbolTable ctSymTab;
    private JavaFileManager theFileManager;
    private int pass = 1;
    private ArrayList<String> programArguments = new ArrayList<String>() ;

    JLang() {} 
    
    public void addProgramArgument( String argument ) {
    	Debug.getInstance().msg(Debug.EXECUTE, "Adding " + argument + " to java command line.");
        programArguments.add( argument ) ;
    }
    
    /** Create the Run Time Symbol Table
     * @param timeMan The BackTracking Time Manager
     * @return A new runTime Symbol Table
     */
    public ViewableST makeSymTab( BTTimeManager timeMan) {
        return new RT_Symbol_Table( timeMan ) ; }

    /** Runs the four passes needed to compile
     * @param TMFile the file to compile.
     * @param vms The Virtual Machine on which the object code is to be run
     * @throws PError
     */
    public void compile( TMFile file, VMState vms) {
        // reset all static classes
        	SecondPass.reset();
        	ThirdPass.reset();
        	FourthPass.reset();

      // Prepare the VMState
            this.vms = vms;
            JavaLangASTUtilities astUtilities = new JavaLangASTUtilities() ;
            vms.setProperty("ASTUtilities", astUtilities ) ;
            vms.setProperty("DatumUtilities", new DatumUtilities() ) ;         
            
       // Pass 1 runs the parser
        	pass = 1;
            SimpleNode compilationUnitNode = doFirstPass(file);
            theFileManager = JavaFileManager.createManager(new FileInfo(file,compilationUnitNode), this);
            
        // Pass 2 Build the compile time symbol table
            pass = 2;
 //           d.off();
            ctSymTab = new Java_CTSymbolTable() ;
            initializeTheSymbolTable( ctSymTab ) ;
            //d.on();
            //ctSymTab.dumpContents();
            //d.off();
            SecondPass.doSecondPass( compilationUnitNode, ctSymTab);
            if (d.isOn(Debug.COMPILE)){
            	ctSymTab.dumpContents(" after Second Pass", d);
                /*DBG System.out.println(SecondPass.getCrossRef().toString());/*DBG*/
            }


        // Pass 3 Build the type nodes
        	pass = 3;
            JavaFileManager.Iterator iterator = theFileManager.getIterator();
            d.msg(Debug.COMPILE, "\n********** Starting Third Pass - building the type nodes ************");
            while (!iterator.atEnd()) {
                ThirdPass.doThirdPass(iterator.getFile().compilationUnitNode, ctSymTab);
                if (d.isOn()) ctSymTab.dumpContents(
                		" after Third Pass on file " + iterator.getFile().file.toString(), d);
                iterator.increment();
            }
            if (d.isOn())ctSymTab.dumpContents(" after Third Pass on all files", d);
//            d.off();
 

        // Pass 4 generate the code
        	pass = 4;
            // for each file in the file list starting with the original
            d.msg(Debug.COMPILE, "\n********** Starting Fourth Pass - generating code ************");
            iterator.reset();
            while (!iterator.atEnd()) {
                FourthPass.doFourthPass(iterator.getFile().compilationUnitNode, ctSymTab, vms) ;
            	iterator.increment();
    		}
            //d.on();
            if (d.isOn())ctSymTab.dumpContents(" after Fourth Pass on all files", d);
            
            TyClass objectClass = ctSymTab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_OBJECT ) ;
            TyClass stringClass = ctSymTab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_STRING ) ;
            astUtilities.setObjectClass( objectClass ) ;
            astUtilities.setStringClass( stringClass ) ;
        }

    /** Initializes the Virtual Machine
     * @param vms
     */
    public void initializeTheState( VMState vms ) {
        RT_Symbol_Table symtab = (RT_Symbol_Table) vms.getSymbolTable() ;
        
        
        callMain( vms, symtab ) ; }
    
     /** CallBack to execute first two passes on a dependent file
      * Differs from original file compilation in that symbol table
      * has been created, vms state has been set up and virtual machine
      * initializations have been done.
     * @param file
      *@return 
     */
    FileInfo doPreviousPasses(TMFile file){
            ScopeHolder current = ctSymTab.getCurrentScope();
            ctSymTab.exitAllScopes(); // reverts to top level program scope
            SimpleNode compilationUnitNode = doFirstPass(file);
            SecondPass.doSecondPass( compilationUnitNode, ctSymTab) ;
            if (pass > 3) ThirdPass.doThirdPass(compilationUnitNode, ctSymTab);

            if (d.isOn()) ctSymTab.dumpContents("after catching up previous passes on " 
            		+ file.getFileName(), d);
            FileInfo fileInfo = new FileInfo(file, compilationUnitNode);
            theFileManager.addToList(fileInfo);
            ctSymTab.enterScope(current);
            return fileInfo;
    }
    
    /** Pass 1 Run the parser
     * @param file
     */
   private SimpleNode doFirstPass(TMFile file) {
            java.io.Reader inputReader = file.toReader();
            Assert.error(inputReader != null, "File " + file + " could not be opened.");
            JavaParser parser = new JavaParser( inputReader, vms.getCodeStore(), file ) ;
            SimpleNode compilationUnitNode = parser.CompilationUnit( ) ;

        // Dump the compile time syntax tree
            if (d.isOn(Debug.COMPILE)) (new SimpleVisitor(  )).visit( compilationUnitNode, 0 ) ;
            return compilationUnitNode;
    }

    private void callMain( VMState vms, RT_Symbol_Table rtSymtab ) {
        // Find Main.
            Vector mainKeyVect = rtSymtab.getMainFunctionKeys() ;
            Assert.apology(mainKeyVect.size() > 0 , "No 'main' function found." ) ;
    
            if( mainKeyVect.size() > 1 ) {
                /** @todo resolve multiple main funtions. */
                Assert.apology( "Sorry multiple candidates for 'main' function. "
                    +"This is not yet supported." ); }
            String mainKey = (String)( mainKeyVect.elementAt(0) ) ;
            TyClass mainClass = (TyClass) rtSymtab.getMainFunctionClasses().elementAt(0) ;
    
            FunctionDefnCompiled funDef = (FunctionDefnCompiled) rtSymtab.getFunctionDefn( mainKey ) ;
            Assert.apology(funDef != null, "No 'main' function found." ) ;
            Debug.getInstance().msg(Debug.EXECUTE, "Main function:" );
            Debug.getInstance().msg(Debug.EXECUTE, funDef.toString() );

        // First initialize the main class
            ExpressionNode expNode = new ExpEnsureClassInit( mainClass, new ConstBool(true) ) ;
            ExpressionEvaluation expEval = new ExpressionEvaluation(vms, expNode ) ;
            vms.push( expEval ) ;
            while( ! vms.isEmpty() ) {
                // There better not be any input required or infinite loops!
                vms.advance() ; }
        
        // Create a datum to hold the result
            TypeNode retType = TyVoid.get() ;
            JavaLangASTUtilities util = (JavaLangASTUtilities) vms.getProperty("ASTUtilities") ;
            AbstractDatum resultDatum = util.scratchDatum(  retType, vms ) ;
            //vms.pushResultDatum( resultDatum ) ;

        // Create the argument list.
            vms.pushNewArgumentList() ;
        // Make the type of the argument
            TyClass objectClass = this.ctSymTab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_OBJECT ) ;
            TyClass stringClass = this.ctSymTab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_STRING ) ;
            TyPointer ptrToString = new TyPointer( stringClass ) ;
            TyJavaArray arrayOfPtrToString = new TyJavaArray("", objectClass ) ;
            arrayOfPtrToString.addToEnd( ptrToString ) ;
            TyPointer ptrToArrayOfPtrToString = new TyPointer( arrayOfPtrToString ) ;
        // Make the array
            int numberOfArguments = programArguments.size();
            ArrayDatum newArray
                = arrayOfPtrToString.makeArrayDatum(vms, numberOfArguments);
        // Create the string objects and point the array elements to them.
            for( int i=0 ; i < numberOfArguments ; ++i ) {
                String argument = programArguments.get(i) ;
                ObjectDatum strDatum = util.makeStringObject(argument, vms) ;
                PointerDatum ptrDatum = (PointerDatum) newArray.getElement(i) ;
                ptrDatum.putValue( strDatum ) ;
            }
        // Make the datum and add it to the argument list
            PointerDatum argumentDatum = (PointerDatum) util.scratchDatum(  ptrToArrayOfPtrToString, vms ) ;
            argumentDatum.putValue( newArray ) ;
            vms.addArgument( argumentDatum ) ;

        // Push an evaluation for main on the stack.
            Assert.check( vms.isEmpty() ) ;
            StatementInterface mainStatement = funDef.getBodyLink().get() ;
            FunctionEvaluation funEval = new FunctionEvaluation( vms, mainStatement, null, resultDatum) ;
            vms.push( funEval ) ;

        // The argument type should be pointer to an array of string
//        Assert.check( ((TyFun)type).getParamCount() == 1 ) ;
//        TypeNode pType = ((TyFun)type).getParamType(0) ;
//        Assert.check( pType instanceof TyPointer);
//        PointerDatum arg = (PointerDatum) util.scratchDatum( pType, vms ) ;
//        /** @todo Create and fill in the appropriate array of strings.
//         *        In the mean time the argument will be a null pointer */
//        arg.putValue(0);
//
        // We need to call enterFunction here because the
        // return will call exitFuntion.
        rtSymtab.enterFunction( null ) ;
        vms.getStore().setStackMark() ; }


    /** Get the name of the language
     * @return "Java"
     */
    public String getName() {return "Java" ; }


    /** Add declarations for java.lang package */
    private void initializeTheSymbolTable(Java_CTSymbolTable ctSymTab) {
        /** @todo  */
    }

    public int getLanguage() {
        return EvaluatorInterface.JAVA_LANG ;
    }

    @Override
    public void callSubroutine(VMState vms, Object key, Object[] args) {
        // TODO  
    }
}