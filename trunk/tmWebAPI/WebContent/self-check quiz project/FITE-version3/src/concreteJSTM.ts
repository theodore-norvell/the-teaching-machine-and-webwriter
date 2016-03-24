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
        //guid       
        guid:string;
        
        programText:string;
        
        //the mirrorData object received from the remoteTM.        
        mirrorData:any;
        
        //qz:quizBuilder.FITEQuestion;
        
        
        constructor(guid:string){
            this.guid=guid;
                                }
        
       goForwardButton:HTMLElement;
       
       goBackButton:HTMLElement;
        //questiionText
       expressionDisplay:HTMLElement;
       
       VariableWatcher:HTMLElement;

      //makeGoForwardButton
      public makeGoForwardButton(onDone ?: (jstm:JSTM) => void,onFail ?: (r:P.Rejection) => void):HTMLElement{
      this.goForwardButton = document.createElement('button');
      this.goForwardButton.setAttribute('disabled','disabled');
      this.goForwardButton.innerHTML='goForward';
      return this.goForwardButton;
      }
      
      //makeGoBackButton
      public makeGoBackButton(onDone? : (jstm:JSTM) => void,onFail? : (r:P.Rejection) => void):HTMLElement{
      this.goBackButton = document.createElement('button');
      this.goBackButton.setAttribute('disabled','disabled');
      this.goBackButton.innerHTML='goBack';
      return this.goBackButton;
      }
      
      //makeExpressionDisplay
      public makeExpressionDisplay(onDone? : (jstm:JSTM) => void,onFail? : (r:P.Rejection) => void):HTMLElement{
      this.expressionDisplay = document.createElement('span');
      return this.expressionDisplay;
      }
      
      //makeVariableWatcher
      public makeVariableWatcher( Name : string,initValue:string):HTMLElement {
          var VariableWatcherSpan:HTMLElement;
          VariableWatcherSpan=document.createElement('span');
          VariableWatcherSpan.innerHTML = Name;
          
          var VariableWatcherInput:HTMLElement;
          VariableWatcherInput= document.createElement('input');
          VariableWatcherInput.setAttribute('type','text');
          VariableWatcherInput.setAttribute('value',initValue);
          
          this.VariableWatcher=document.createElement('div');
          this.VariableWatcher.appendChild(VariableWatcherSpan);
          this.VariableWatcher.appendChild(VariableWatcherInput);
          return this.VariableWatcher;
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
        var tempVar=data;
        //assign back-end json object to the front end parameters in this js file;
        var guid=tempVar.guid;
        //if there is no error in the createRemoteTM step
        //console.log(status);
        //console.log(thisJSTM.mirrorData);
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
                        
                     thisJSTM.mirrorData=data;   
                        
                     //var parameterTemp =data;
                     //assign back-end json object to the front end parameters in this js file;
                     var reasonFlag=thisJSTM.mirrorData.reasonFlag;
                     var reason=thisJSTM.mirrorData.reason;
                     var status=thisJSTM.mirrorData.status;
                     //if there is no error in the createRemoteTM step
                     thisJSTM.setStatus(status);
                     //console.log(thisJSTM.getStatus());
                     //console.log(thisJSTM.mirrorData); 
                    if(thisJSTM.getStatus()=='3'){
                        d.resolve(thisJSTM);
                    } 
                    else{
                        //thisJSTM.qz.fieldSet.style.display='none';
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
                 
                    thisJSTM.mirrorData=data;
                    //var parameterTemp =data;
    	            var reasonFlag=thisJSTM.mirrorData.reasonFlag;
    	            var reason=thisJSTM.mirrorData.reason;
    	            var status=thisJSTM.mirrorData.status;
                    thisJSTM.setStatus(status);
                    //console.log(thisJSTM.getStatus());
                    //console.log(thisJSTM.mirrorData); 
                    //fuilfull
                    if (status=='4'){
                        d.resolve(thisJSTM);
                     //goForwardButton.removeAttribute('disabled');
                     //goBackButton.removeAttribute('disabled');
                       }
                     else{
                         //thisJSTM.qz.fieldSet.style.display='none';
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
            thisJSTM.mirrorData=data;
            //var parameterTemp =data;
            var reason=thisJSTM.mirrorData.reason;
            var status=thisJSTM.mirrorData.status;
            var expression=thisJSTM.mirrorData.expression;
            /**parse the characters of the expression**/
            var expTemp = thisJSTM.ExpressionEffect(expression);
            //fuilfull
            d.resolve(thisJSTM);
            if(status!='6'&&status!='7'){
 		             thisJSTM.expressionDisplay.innerHTML='';
                     thisJSTM.expressionDisplay.innerHTML="<span>"+expTemp+"</span>";
                }
                if(status=='6'){
    			    thisJSTM.expressionDisplay.innerHTML='';
                    thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";
                    }
                if(status=='7')
                    {
                    thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";
                    
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
            thisJSTM.mirrorData=data;
            //var parameterTemp =data;
        	var reason=thisJSTM.mirrorData.reason;
        	var status=thisJSTM.mirrorData.status;
        	var expression=thisJSTM.mirrorData.expression;
        	/**parse the characters of the expression**/
        	var expTemp = thisJSTM.ExpressionEffect(expression);
            //fuilfill
            d.resolve(thisJSTM);
            
    		if(status!='6'&&status!='7'){
 		    thisJSTM.expressionDisplay.innerHTML='';
            thisJSTM.expressionDisplay.innerHTML="<span>"+expTemp+"</span>";

        	}
    		if(status=='6'){
    			thisJSTM.expressionDisplay.innerHTML='';
                thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";
    			}
    		if(status=='7')
    			{
    			//thisJSTM.qz.expressionDisplay.innerHTML='';
                thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";

    			
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
            thisJSTM.mirrorData=data;
            var reason=thisJSTM.mirrorData.reason;
            var status=thisJSTM.mirrorData.status;
            var expression=thisJSTM.mirrorData.expression;
            /**parse the characters of the expression**/
            var expTemp = thisJSTM.ExpressionEffect(expression);
            //fuillfill
            d.resolve(thisJSTM);
            
            if(status!='6'&&status!='7'){
 		    thisJSTM.expressionDisplay.innerHTML='';
            thisJSTM.expressionDisplay.innerHTML="<span>"+expTemp+"</span>";
                }
                if(status=='6'){
    			thisJSTM.expressionDisplay.innerHTML='';
                thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";
                    }
                if(status=='7')
                    {
    			thisJSTM.expressionDisplay.innerHTML='';
                thisJSTM.expressionDisplay.innerHTML="<span>"+' EXECUTION_COMPLETE '+"</span>"+"<br>";
                    }
                })  
                .fail(function(){ d.reject(this);console.log('error!'); });
           
           
           
           return p;
       }
       
    // replace the Unicode to make some font effect.
    public ExpressionEffect(exp){
	    exp=exp.replace(/[\uffff]/g,"<span class='tm-red'>");
	    exp=exp.replace(/[\ufffe]/g,"");
	    exp=exp.replace(/[\ufffd]/g,"<span class='tm-underline'>");
	    exp=exp.replace(/[\ufffc]/g,"<span class='tm-blue'>");
	    exp=exp.replace(/[\ufffb]/g,"</span>");
	    var expEffect=1;
	
	    return exp;
	                            }
                                

}
    

    
}