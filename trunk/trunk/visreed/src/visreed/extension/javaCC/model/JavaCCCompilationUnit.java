/**
 * JavaCCCompilationUnit.java
 * 
 * @date: Sep 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xiaoyu Guo
 *
 */
public class JavaCCCompilationUnit {
	private List<String> modifiers;
	
	private String packageName;
	
	private List<String> importDeclaration;
	
	private List<String> typeDeclaration;
	
	public List<String> getModifiers(){
		return this.modifiers;
	}
	
	public void addModifier(String mod){
		if(!this.modifiers.contains(mod)){
			this.modifiers.add(mod);
		}
	}
	
	public List<String> getImportDeclarations(){
		return this.importDeclaration;
	}
	/**
	 * Add an import declaration to the compilation unit
	 * @param declaration "package.name" or "package.*"
	 */
	public void addImportDeclaration(String declaration){
		if(!this.importDeclaration.contains(declaration)){
			this.importDeclaration.add(declaration);
		}
	}
	
	public List<String> getTypeDeclarations(){
		return this.typeDeclaration;
	}
	/**
	 * Add a type declaration to the compilation unit
	 * @param declaration
	 */
	public void addTypeDeclaration(String declaration){
		if(!this.typeDeclaration.contains(declaration)){
			this.typeDeclaration.add(declaration);
		}
	}
	
	public String getPackageName(){
		return this.packageName;
	}
	/**
	 * @param value "name.of.the.package"
	 */
	public void setPackageName(String value){
		this.packageName = value;
	}
	
	public JavaCCCompilationUnit(){
		this.modifiers = new ArrayList<String>();
		this.importDeclaration = new ArrayList<String>();
		this.packageName = "";
		this.typeDeclaration = new ArrayList<String>();
	}
}
