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
        //code field,which is a Array[], each index contains a object
        code:any;
        //qz:quizBuilder.FITEQuestion;
        
        
        constructor(guid:string){
            this.guid=guid;
                                }
        
       goForwardButton:HTMLElement;
       
       goBackButton:HTMLElement;
        //questiionText
       expressionDisplay:HTMLElement;
       

       //make disolay
       public makeTMDisplay():HTMLElement{
           //code object
           var code = [];
           code=this.mirrorData.code;
           var focus = this.mirrorData.focus;
           var focusNumber=focus.lineNumber;
           console.log(code.length);
           
           var table:HTMLElement = document.createElement('table');
           table.setAttribute("class","dispaly-program");
           console.log(table.className);
           //number of rows
           for (var i = 0; i < code.length; i++){
            //lineNumber
            var lineNumber=code[i].coords.lineNumber;
            //chars
            var chars=code[i].chars
            //Array []
            var markUp = code[i].markUp;
            
            var tr = document.createElement('tr');   
            
            var td1 = document.createElement('td');
            var td2 = document.createElement('td');
            
            //var text1 = document.createTextNode(lineNumber+':');
            var textafterprocess=this.DisplayEffect(chars,markUp);
            var line = document.createElement('span');
            line.setAttribute('id',lineNumber);
            line.innerHTML="<span class=tm-red>"+lineNumber+':'+"</span>";
            var text = document.createElement('pre');
            text.innerHTML=textafterprocess;
            //var text2 = textNode;
            
            console.log(textafterprocess);
            
            //td.appendChild(text1);
            td1.appendChild(line);
            td2.appendChild(text);
            tr.appendChild(td1);
            tr.appendChild(td2);
            table.appendChild(tr);
           }
           //
           //console.log(focusNumber);
           //console.log(document.getElementById(focusNumber));
           
           
           
           return table ;
       }



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

           // console.log(document.getElementById(focusNumber));
            
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
                                
                                
                                
      public DisplayEffect(chars,markUp){
         // var PTag = document.createElement('P');
         /** 
          var newChar = chars.replace(/[<]/g,'&lt');
          var newChar = newChar.replace(/[>]/g,'&gt');
          **/
          //var newChar = chars;
          var effectChars=chars;
          var effectChars = effectChars.replace(/[<]/g,'&lt');
          var effectChars = effectChars.replace(/[>]/g,'&gt');
          
          if (markUp==null){
              chars=chars.replace(/[<]/g,'&lt');
               return chars;
          }
         
          
          else{
          var iterateTimes = markUp.length/2;
          for(var i=0;i<iterateTimes;i++) {
              
          var command = markUp[i*2].command;
          if(command==5){
              return effectChars;
          }
          var columnDown=markUp[i*2].column;
          var columnUp=markUp[i*2+1].column;
          //console.log(columnDown);
          //console.log(columnUp);
          var replacePart = [];
          replacePart[i]=chars.substring(columnDown,columnUp);
          switch(command){
              //command = 1 means brown. command = 3 means brown
              case 1:      
                         if (i==0){
                        var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-brown'>"+replacePart[i]+"</span>")
                        // console.log(i);
                         console.log(effectChars);
                         break;
                         }
                         else if(replacePart[i]!=replacePart[i-1]){
                         var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-brown'>"+replacePart[i]+"</span>")
                        // console.log(i);
                         console.log(effectChars);
                         break;
                         }
                         break;
              case 3:

                         if (i==0){
                        var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-brown'>"+replacePart[i]+"</span>")
               
                         console.log(replacePart[i]);
                         console.log(effectChars);
                         break;
                         }
                         else if(replacePart[i]!=replacePart[i-1]){
                         var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-brown'>"+replacePart[i]+"</span>")
                        // console.log(i);
                         console.log(effectChars);
                         break;
                         }
                         
                         break;
                                            
                        
              case 4:   
              
                        if (i==0){
                        var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-blue'>"+replacePart[i]+"</span>")
                        // console.log(i);
                         console.log(effectChars);
                         break;
                         }
                         else if(replacePart[i]!=replacePart[i-1]){
                         var regexp = new RegExp(replacePart[i],"g");
                         effectChars = effectChars.replace(regexp,"<span class='tm-blue'>"+replacePart[i]+"</span>")
                        // console.log(i);
                         console.log(effectChars);
                         break;
                         }
                         
                         break;
                         /** 
                         var effectChars = chars.substring(columnDown,columnUp);
                        //subString that need css effect.
                        var spanTag = document.createElement('span');
                        spanTag.innerHTML="<span class='tm.blue'>"+effectChars+"</span>";
                        PTag.appendChild(spanTag);
                        **/
                       // console.log(effectChars);
               case 5:
                                break; 
                        
              //command = 4 means blue
              default : 
                            break;
                        }
                                    }
                                     
                                    
          
          return  effectChars ;
                }                      
                                

                                            }
                                            /** 
public expressionStringToHTML( str ) {
	var i ;
	var html = "" ;
	for( i=0 ; i < str.length ; ++i ) {
		var c = str.charAt(i) ;
		switch( c ) {
		case "<" : html += "&lt;" ; break ;
		case ">" : html += "&gt;" ; break ;
		case "&" : html += "&amp;" ; break ; 
		case "\uffff" : html += '<span class="tm-red">' ; break ;
		case "\ufffe" : html += '<span class="tm-underline">' ; break ;
		case "\ufffc" : html += '<span class="tm-blue">' ; break ;
		case "\ufffb" : html += '</span>' ; break ;
		default : html += c ; } }
	return html ; }
}
    **/

    
}
}