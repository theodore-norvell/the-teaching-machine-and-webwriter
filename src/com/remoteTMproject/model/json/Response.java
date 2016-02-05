package com.remoteTMproject.model.json;

import tm.interfaces.CodeLine;
import tm.interfaces.SourceCoords;

public class Response {

	
	private String guid;
	private int reasonFlag;
	private String reason;
	private int status;
	private String expression;
	private String[]  consolearray; 
	private String[]  outputarray;
	private CodeLine[] code;
	private SourceCoords focus;
	
	
	
	
	public Response(int status,String reason){
		this.status=status;
		this.reason=reason;
		
	}
	
	public Response(int status,String guid,String reason){
		this.status=status;
		this.guid=guid;
		this.reason=reason;
	}
	
	public Response(int status,int reasonFlag,String reason){
		this.status=status;
		this.reasonFlag=reasonFlag;
		this.reason=reason;

		
	}
	
	public Response(String reason,int status,String expression){

		this.reason=reason;
		this.status=status;
		this.expression=expression;
	}
	
	public Response(String guid,int reasonFlag,String reason,int status){
		this.guid=guid;
		this.reasonFlag=reasonFlag;
		this.reason=reason;
		this.status=status;
	}

	
	public Response(String guid,int reasonFlag,String reason,int status,String expression){
		this.guid=guid;
		this.reasonFlag=reasonFlag;
		this.reason=reason;
		this.status=status;
		this.expression=expression;
	}
	
	public Response(String reason,int status,String expression,String[]  outputarray,String[]  consolearray, CodeLine[] code,SourceCoords focus)
	{
		this.reason=reason;
		this.status=status;
		this.expression=expression;
		this.outputarray=outputarray;
		this.consolearray=consolearray;
		this.code=code;
		this.focus=focus;
		
		
		
	}
	
	
	
	
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getReasonFlag() {
		return reasonFlag;
	}
	public void setReasonFlag(int reasonFlag) {
		this.reasonFlag = reasonFlag;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}



	public SourceCoords getFocus() {
		return focus;
	}

	public void setFocus(SourceCoords focus) {
		this.focus = focus;
	}

	public String[]  getOutputarray() {
		return outputarray;
	}

	public void setOutputarray(String[]  outputarray) {
		this.outputarray = outputarray;
	}

	public String[]  getConsolearray() {
		return consolearray;
	}

	public void setConsolearray(String[]  consolearray) {
		this.consolearray = consolearray;
	}

	public CodeLine[] getCode() {
		return code;
	}

	public void setCode(CodeLine[] code) {
		this.code = code;
	}
	
	
	
}
