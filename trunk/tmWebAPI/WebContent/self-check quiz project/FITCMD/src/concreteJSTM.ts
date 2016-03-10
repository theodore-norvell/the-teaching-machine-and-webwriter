/// <reference path="JSTM.ts" />
/// <reference path="Promise.ts"/>
/// <reference path="../builder/quizBuilder.ts"/>
/// <reference path="../library/jquery.d.ts" />

module jstm{
    // var json={ "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":"","filename":""}],
       //         "flag":[{"expEffect":0,"responseWantedFlag":null}]}
 
    
    import Promise = P.Promise ;
    
    
   export class concreteJSTM implements jstm.JSTM{
        status:string;
        message:string;
        guid:string;
        programText:string;
        
       // qz:quizBuilder.Quiz;
        
        
        constructor(guid:string){
            this.guid=guid;
        }
        
       goForwardButton:HTMLElement;
       
       goBackButton:HTMLElement;
        //questiionText
       questionDisplay:HTMLElement;
       
       functiondescription:HTMLElement;
       //<span> element for expression
       inputExpressionSpanElement:HTMLElement;
       //<input> elelement for expression
       inputExpressionInputElement:HTMLElement;
       //<span> element for variables
       inputVarsSpanElement = new Array<HTMLElement>();
       //<input> element for variables
       inputVarsInputElement = new Array<HTMLElement>();
       //
    

        makeGoForwardButton(onDone ?: (jstm:JSTM) => void,onFail ?: (r:P.Rejection) => void):HTMLElement{
            this.goForwardButton=document.getElementById('goFoward');
            return this.goForwardButton;
        }
        
        makeGoBackButton(onDone? : (jstm:JSTM) => void,onFail? : (r:P.Rejection) => void):HTMLElement{
            this.goBackButton=document.getElementById('goBack');
            return this.goBackButton;
        }
    
    
    
    //------------------------------------------------
      // i will receive the inputVars object and return inputVarsSpan;
    public setquestionDisplay(tempIndex:any){
      this.questionDisplay = document.getElementById('questionDisplay');
      this.questionDisplay.innerHTML = tempIndex.name+" : "+tempIndex.text;
      
      this.functiondescription = document.getElementById('functionDisplay');
      this.functiondescription.innerHTML='implement the function  '+tempIndex.functiondescription;
  }
   
  public setInputExpressionSpanElement(){
      this.inputExpressionSpanElement=document.createElement('span');
      this.inputExpressionSpanElement.innerHTML='implement the function: ';
      
      return this.inputExpressionSpanElement;
      
                                        
                                        }
                                        
   public setinputExpressionInputElement(){
      this.inputExpressionInputElement=document.createElement('input');
      this.inputExpressionInputElement.setAttribute('type','text');
      this.inputExpressionInputElement.setAttribute('value','');
      return this.inputExpressionInputElement;
      
                                            }
  
  
  public setinputVarsSpanElement(inputVars:any){
     // console.log(inputVars);
      var count = Object.keys(inputVars).length;
       for(var i=0;i<count;i++)
        {
            this.inputVarsSpanElement[i]=document.createElement('span');
            //this.inputVarsElement[i].setAttribute('id','Z1');
            this.inputVarsSpanElement[i].innerHTML="Initial Value of :"+inputVars[i].name;                                        
        }
          
          return this.inputVarsSpanElement;
                                                }
  //i will receive the inputVars object and return inputVarsInputElement;
  public setinputVarsinputElement(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++)
      {
          this.inputVarsInputElement[i]=document.createElement('input');
          //this.inputVarsSpanElement[i].setAttribute('id','Z2');
          this.inputVarsInputElement[i].setAttribute('type','text');
          this.inputVarsInputElement[i].setAttribute('value',inputVars[i].defaultInitValue);
          //console.log(inputVars[i].defaultInitValue);
      }
      return this.inputVarsInputElement;
                                                  }
  
  

  

  

  //create the html element
  public buildInputElement(){
        //console.log(tempIndex.inputVars);
        this.setquestionDisplay(quizBuilder.Quiz.tempIndex);
        this.setInputExpressionSpanElement();
        this.setinputExpressionInputElement();
        this.setinputVarsSpanElement(quizBuilder.Quiz.tempIndex.inputVars);
        this.setinputVarsinputElement(quizBuilder.Quiz.tempIndex.inputVars);
  }
  
  public buildOutputElement(){
        this.makeGoForwardButton();
        this.makeGoBackButton();
      
      
  }
  public constructHTMLElement(){
      this.buildInputElement();
      this.buildOutputElement();
  }
  
  
  
   //constructDisplay area1!!!!
   // div parameter 
  public constructInsertArea1(){
       $('#insert1').empty();
       $('#insert1').append(this.inputExpressionSpanElement);
       $('#insert1').append(this.inputExpressionInputElement);
       $('#insert1').append('<br></br>');
       for(var j=0; j<this.inputVarsInputElement.length;j++)
       {    
       $('#insert1').append(this.inputVarsSpanElement[j]);
       $('#insert1').append(this.inputVarsInputElement[j]);
       $('#insert1').append("<br></br>");
       }  
      
                            }   
  
    
    

    


    

  
  //setProgramText
    public setprogramText(pt:string){
        this.programText=pt;
    }  
   //getProgramText 
    public getprogramText(){
        return this.programText;
    }
       
    //getstatus
    getStatus():string{
        return this.status;
    } 
    
    //set
    setStatus(status:string):void{
        this.status=status;
    }
    
    //getMessage    
    getMessage():string{
        return this.message; 
    }
    
    //set
    setMessage(message:string):void{
        this.message=message;
    }
        
     //createRTM   
    static createRTM():Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
       // var thisJSTM=this;
        
        $.ajax({
            url : 'createRemoteTM.create',
            dataType:"json",
            type:"POST",
          })
    .done(function (data) {
        var parameterTemp =data;
        //assign back-end json object to the front end parameters in this js file;
        var guid=parameterTemp.guid;
        var reasonFlag=parameterTemp.reasonFlag;
        var reason=parameterTemp.reason;
        var status=parameterTemp.status;
        //if there is no error in the createRemoteTM step
        console.log(status);
        console.log(parameterTemp);
        //fullfill the defered state
        var thisJSTM:JSTM = new concreteJSTM(guid);
        d.resolve(thisJSTM);
    })
    .fail(function () {d.reject(this); console.log("error! in createRTM in concreteJSTM");});
   
        return p;
    }    
        
        
        
  //loadString
    loadString( program : string, filename : string) :Promise<JSTM>  {
            var d=P.defer<JSTM>();
            var p=d.promise();
            var thisJSTM=this;
            console.log(thisJSTM);
            console.log(thisJSTM.guid);
            
            $.ajax({
             url : 'loadString.load',
             dataType:"json",
             type:"POST",
             data : {'guid':thisJSTM.guid,
                    'Codes' : program,
                    'filename':filename },
            })
            .done(function (data) {
                     var parameterTemp =data;
                     //assign back-end json object to the front end parameters in this js file;
                     var reasonFlag=parameterTemp.reasonFlag;
                     var reason=parameterTemp.reason;
                     var status=parameterTemp.status;
                     //if there is no error in the createRemoteTM step
                     thisJSTM.setStatus(status);
                     console.log(thisJSTM.getStatus());
                     console.log(parameterTemp); 
                    if(status=='3'){
                        d.resolve(thisJSTM);
                    } 
                    else{
                        document.getElementById('panel').style.display='none';
                        thisJSTM.setMessage(reason);
                        alert(thisJSTM.getMessage());
                    }
                    
                })
            .fail(function () { d.reject(this); console.log('error! in loadString in concreteJSTM'); });

            return p;
        }
        
      //initialize  
       initialize(responseWantedFlag:string):Promise<JSTM>{
            var d=P.defer<JSTM>();
            var p=d.promise();
            var thisJSTM=this;
           
           	$.ajax({
	         url : 'initializeTheState.initialize',
	         dataType:"json",
	         type:"POST",
	         data : { 'guid':thisJSTM.guid,
                      'responseWantedFlag':responseWantedFlag }})
             .done(function(data){
                    var parameterTemp =data;
    	            var reasonFlag=parameterTemp.reasonFlag;
    	            var reason=parameterTemp.reason;
    	            var status=parameterTemp.status;
                    thisJSTM.setStatus(status);
                    console.log(thisJSTM.getStatus());
                    console.log(parameterTemp); 
                    //fuilfull
                    if (status=='4'){
                        d.resolve(thisJSTM);
                     //goForwardButton.removeAttribute('disabled');
                     //goBackButton.removeAttribute('disabled');
                       }
                     else{
                         document.getElementById('panel').style.display='none';
                         thisJSTM.setMessage(reason);
                         alert(thisJSTM.getMessage());
                                 }
                        })
             .fail(function(data){ d.reject(this); console.log('error! in the initialize in concreteJSTM');});
             
             return p;
       }
       
        //go
        go( commandString : string):Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           	
        $.ajax({
        url : 'go.g',
        dataType:"json",
        type:"POST",
        data : { 'guid':thisJSTM.guid ,
                'command':commandString}})            
          .done(function(data){
            var parameterTemp =data;
            var reason=parameterTemp.reason;
            var status=parameterTemp.status;
            var expression=parameterTemp.expression;
            /**parse the characters of the expression**/
            var expTemp = ExpressionEffect(expression);
            //fuilfull
            d.resolve(thisJSTM);
            if(status!='6'&&status!='7'){
                $('#expressioninpanel').text("");
                $('#expressioninpanel').append("<p>"+expTemp+"</p>");
                }
                if(status=='6'){
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+' EXECUTION_COMPLETE '+"</font>"+"<br>");
                    }
                if(status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+' EXECUTION_FAILED '+"</font>"+"<br>");
                    
                    }
                 })
          .fail(function(data){  d.reject(this); console.log('error! in the go in concreteJSTM');   }); 
            
            
            return p;
        } 
        
        
        
        //goForward
    
       goForward():Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           
           
        $.ajax({
	    url : 'goForward.goForward',
	    dataType:"json",
	    type:"POST",
	    data : {'myguid':thisJSTM.guid} })
        .done(function(data){
            //data comes back from the server. 
            var parameterTemp =data;
        	var reason=parameterTemp.reason;
        	var status=parameterTemp.status;
        	var expression=parameterTemp.expression;
        	/**parse the characters of the expression**/
        	var expTemp = ExpressionEffect(expression);
            //fuilfill
            d.resolve(thisJSTM);
            
    		if(status!='6'&&status!='7'){
 		    $('#expressioninpanel').text("");
            $('#expressioninpanel').append("<span>"+expTemp+"</span>");

        	}
    		if(status=='6'){
    			$('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>");
    			}
    		if(status=='7')
    			{
    			$('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+' EXECUTION_FAILED '+"</span>"+"<br>");

    			
    			};
            
        })
        .fail(function(){ d.reject(this);console.log('error!'); });

         return p;
       }        


        //goBack
       goBack():Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           
        $.ajax({
	    url : 'goBack.goBack',
	    dataType:"json",
	    type:"POST",
	    data : {'myguid':thisJSTM.guid} })
        .done(function(data){
            var parameterTemp =data;
            var reason=parameterTemp.reason;
            var status=parameterTemp.status;
            var expression=parameterTemp.expression;
            /**parse the characters of the expression**/
            var expTemp = ExpressionEffect(expression);
            //fuillfill
            d.resolve(thisJSTM);
            
            if(status!='6'&&status!='7'){
                $('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+expTemp+"</span>");
                }
                if(status=='6'){
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+' EXECUTION_COMPLETE '+"</font>"+"<br>");
                    }
                if(status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+' EXECUTION_FAILED '+"</font>"+"<br>");
                    }
                })  
                .fail(function(){ d.reject(this);console.log('error!'); });
           
           
           
           return p;
       }


















}
    
    function ExpressionEffect(exp){
	exp=exp.replace(/[\uffff]/g,"<span class='tm-red'>");
	exp=exp.replace(/[\ufffe]/g,"");
	exp=exp.replace(/[\ufffd]/g,"<span class='tm-underline'>");
	exp=exp.replace(/[\ufffc]/g,"<span class='tm-blue'>");
	exp=exp.replace(/[\ufffb]/g,"</span>");
	var expEffect=1;
	
	return exp;
	
	
	
	
}
    
}