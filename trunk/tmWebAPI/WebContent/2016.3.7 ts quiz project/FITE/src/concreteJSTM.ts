/// <reference path="JSTM.ts" />
/// <reference path="Promise.ts"/>
/// <reference path="../library/jquery.d.ts" />

module jstm{
    // var json={ "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":"","filename":""}],
       //         "flag":[{"expEffect":0,"responseWantedFlag":null}]}
 
    
    import Promise = P.Promise ;
    
    
   export class concreteJSTM implements jstm.JSTM{
        status:string;
        message:string;
        guid:string;
        constructor(guid:string){
            this.guid=guid;
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
                    $('#expressioninpanel').append("<font color='black'>"+status+"</font>"+"<br>");
                    }
                if(status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+status+"</font>"+"<br>");
                    
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
                $('#expressioninpanel').append("<span>"+status+"</span>"+"<br>");
    			}
    		if(status=='7')
    			{
    			$('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+reason+"</span>"+"<br>");

    			
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
                    $('#expressioninpanel').append("<font color='black'>"+status+"</font>"+"<br>");
                    }
                if(status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+reason+"</font>"+"<br>");
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