/// <reference path="../src/JSTM.ts" />
/// <reference path="../src/Promise.ts"/>
/// <reference path="../library/jquery.d.ts" />

module jstm{
    export var json={ "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":"","filename":""}],
                "flag":[{"expEffect":0,"responseWantedFlag":null}]}
 
    
    import Promise = P.Promise ;
    
    
   export class concreteJSTM implements jstm.JSTM{
       public name:string;
        constructor(name:string){
            this.name=name;
        }
        
        
    //getstatus
    getStatus():string{
        return json.parameter[0].status;
    } 
    //getMessage    
    getMessage():string{
        return json.parameter[0].reason; 
    }
        
     //createRTM   
     createRTM():Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
        
        $.ajax({
            url : 'createRemoteTM.create',
            dataType:"json",
            type:"POST",
          })
    .done(function (data) {
        var parameterTemp =data;
        //assign back-end json object to the front end parameters in this js file;
        json.parameter[0].guid=parameterTemp.guid;
        json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
        json.parameter[0].reason=parameterTemp.reason;
        json.parameter[0].status=parameterTemp.status;
        //if there is no error in the createRemoteTM step
        console.log(json.parameter[0].status);
        console.log(parameterTemp);
        //fullfill the defered state
        d.resolve(thisJSTM)
    })
    .fail(function () {d.reject(this); console.log("error! in createRTM in concreteJSTM");});
   
        return p;
    }    
        
        
        
  //loadString
    loadString( program : string, filename : string, guid:string ) :Promise<JSTM>  {
            var d=P.defer<JSTM>();
            var p=d.promise();
            var thisJSTM=this;
            
            $.ajax({
             url : 'loadString.load',
             dataType:"json",
             type:"POST",
             data : {'guid':guid,
                    'Codes' : program,
                    'filename':filename },
            })
            .done(function (data) {
                     var parameterTemp =data;
                     //assign back-end json object to the front end parameters in this js file;
                     json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
                     json.parameter[0].reason=parameterTemp.reason;
                     json.parameter[0].status=parameterTemp.status;
                     //if there is no error in the createRemoteTM step
                     console.log(json.parameter[0].status);
                     console.log(parameterTemp); 
                    if(json.parameter[0].status=='3'){
                        d.resolve(thisJSTM);
                    } 
                    else{
                        document.getElementById('panel').style.display='none';
                        alert(jstm.json.parameter[0].reason);
                    }
                    
                })
            .fail(function () { d.reject(this); console.log('error! in loadString in concreteJSTM'); });

            return p;
        }
        
      //initialize  
       initialize(guid:string,responseWantedFlag:string):Promise<JSTM>{
            var d=P.defer<JSTM>();
            var p=d.promise();
            var thisJSTM=this;
           
           	$.ajax({
	         url : 'initializeTheState.initialize',
	         dataType:"json",
	         type:"POST",
	         data : { 'guid':guid,
                      'responseWantedFlag':responseWantedFlag }})
             .done(function(data){
                    var parameterTemp =data;
    	            json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
    	            json.parameter[0].reason=parameterTemp.reason;
    	            json.parameter[0].status=parameterTemp.status;
                    console.log(json.parameter[0].status);
                    console.log(parameterTemp); 
                    //fuilfull
                    if (json.parameter[0].status=='4'){
                        d.resolve(thisJSTM);
                     //goForwardButton.removeAttribute('disabled');
                     //goBackButton.removeAttribute('disabled');
                       }
                     else{
                         document.getElementById('panel').style.display='none';
                          alert(jstm.json.parameter[0].reason);
                                 }
                        })
             .fail(function(data){ d.reject(this); console.log('error! in the initialize in concreteJSTM');});
             
             return p;
       }
       
        //go
        go( commandString : string, guid:string ):Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           	
        $.ajax({
        url : 'go.g',
        dataType:"json",
        type:"POST",
        data : { 'guid':guid ,
                'command':commandString}})            
          .done(function(data){
            var parameterTemp =data;
            json.parameter[0].reason=parameterTemp.reason;
            json.parameter[0].status=parameterTemp.status;
            json.parameter[0].expression=parameterTemp.expression;
            /**parse the characters of the expression**/
            var expTemp = ExpressionEffect(json.parameter[0].expression);
            //fuilfull
            d.resolve(thisJSTM);
            if(json.parameter[0].status!='6'&&json.parameter[0].status!='7'){
                $('#expressioninpanel').text("");
                $('#expressioninpanel').append("<p>"+expTemp+"</p>");
                }
                if(json.parameter[0].status=='6'){
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
                    }
                if(json.parameter[0].status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
                    
                    }
                 })
          .fail(function(data){  d.reject(this); console.log('error! in the go in concreteJSTM');   }); 
            
            
            return p;
        } 
        
        
        
        //goForward
    
       goForward(guid:string):Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           
           
        $.ajax({
	    url : 'goForward.goForward',
	    dataType:"json",
	    type:"POST",
	    data : {'myguid':guid} })
        .done(function(data){
            var parameterTemp =data;
        	json.parameter[0].reason=parameterTemp.reason;
        	json.parameter[0].status=parameterTemp.status;
        	json.parameter[0].expression=parameterTemp.expression;
        	/**parse the characters of the expression**/
        	var expTemp = ExpressionEffect(json.parameter[0].expression);
            //fuilfill
            d.resolve(thisJSTM);
            
    		if(json.parameter[0].status!='6'&&json.parameter[0].status!='7'){
 		    $('#expressioninpanel').text("");
            $('#expressioninpanel').append("<span>"+expTemp+"</span>");

        	}
    		if(json.parameter[0].status=='6'){
    			$('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+json.parameter[0].status+"</span>"+"<br>");
    			}
    		if(json.parameter[0].status=='7')
    			{
    			$('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+json.parameter[0].reason+"</span>"+"<br>");

    			
    			};
            
        })
        .fail(function(){ d.reject(this);console.log('error!'); });

         return p;
       }        


        //goBack
       goBack(guid:string):Promise<JSTM>{
        var d=P.defer<JSTM>();
        var p=d.promise();
        var thisJSTM=this;
           
        $.ajax({
	    url : 'goBack.goBack',
	    dataType:"json",
	    type:"POST",
	    data : {'myguid':guid} })
        .done(function(data){
            var parameterTemp =data;
            json.parameter[0].reason=parameterTemp.reason;
            json.parameter[0].status=parameterTemp.status;
            json.parameter[0].expression=parameterTemp.expression;
            /**parse the characters of the expression**/
            var expTemp = ExpressionEffect(json.parameter[0].expression);
            //fuillfill
            d.resolve(thisJSTM);
            
            if(json.parameter[0].status!='6'&&json.parameter[0].status!='7'){
                $('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+expTemp+"</span>");
                }
                if(json.parameter[0].status=='6'){
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
                    }
                if(json.parameter[0].status=='7')
                    {
                    $('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].reason+"</font>"+"<br>");
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
	json.flag[0].expEffect=1;
	
	return exp;
	
	
	
	
}
    
}