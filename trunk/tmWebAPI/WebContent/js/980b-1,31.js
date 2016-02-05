/**  variables for ajax **/
var resp = null;
var line = 0;
 //json array to store parameters 
 json={
		 "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":"","filename":""}],
		 "flag":[{"expEffect":0,"responseWantedFlag":null}]
		 
 };
/**  variables for ajax **/

 
/**  variables for dynamicTable**/
var codes="";
var m=12;
var count= 1;
/**  variables for dynamicTable**/


/**functions for ajax**/
$(document).ready(function() {
	
	 var btn1=document.getElementById("createRemoteTM");
	 btn1.onclick=createRemoteTM;
	 
	 var btn2=document.getElementById("retireRemoteTM");
	 btn2.onclick=retireRemoteTM;
	 
	 var btn3=document.getElementById("createRemoteTM");
	 btn3.onclick=createRemoteTM;
	 
	 var btn4=document.getElementById("loadString");
	 btn4.onclick=loadString;
	 
	 var btn5=document.getElementById("initializeTheState");
	 btn5.onclick=initializeTheState;
	 
	 var btn9=document.getElementById("initializeTheStateNoResponse");
	 btn9.onclick=initializeTheStateNoResponse;
	 
	 var btn6=document.getElementById("go");
	 btn6.onclick=go;
	 
	 var btn7=document.getElementById("goForward1");
	 btn7.onclick=goForward1;
	 
	 var btn8=document.getElementById("goBack1");
	 btn8.onclick=goBack1;
	 
	 var btn10=document.getElementById("redo");
	 btn10.onclick=redo;
	
	function createRemoteTM(){
        $.post('createRemoteTM.create', {
        }, function(response) {
        	var parameterTemp =JSON.parse(response);
        	//assign back-end json object to the front end parameters in this js file;
        	json.parameter[0].guid=parameterTemp.guid;
        	json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
        	json.parameter[0].reason=parameterTemp.reason;
        	json.parameter[0].status=parameterTemp.status;
        	//if there is no error in the loadString step
        	if (json.parameter[0].reasonFlag==0){

        }
        //if there is error in the loadString step
        else{

        	$('#responseconsole').text(json.parameter[0].reason);
        }

        });
		
		
	}
	
		function retireRemoteTM() {

            $.post('retireRemoteTM.retire', {
            	guid:json.parameter[0].guid,

            }, function(response) {
            	var parameterTemp =JSON.parse(response);

            });
		}
        
		function loadString() {
			var filename=$('#filename').val();
			json.parameter[0].filename==filename;
    	    json.parameter[0].code=submit();
    		var code=json.parameter[0].code;
    		codes="";
            $.post('loadString.load', {
            	guid:json.parameter[0].guid,
            	Codes : code,
            	filename:filename
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	
            	$('#responseconsole').text("");
                $('#responseconsole').append("<p>"+parameterTemp.reason+"</p>");

            });
		}
     
		function initializeTheState() {
			json.flag[0].responseWantedFlag=1;
    		codes="";
            $.post('initializeTheState.initialize', {
            	guid:json.parameter[0].guid,
            	responseWantedFlag:json.flag[0].responseWantedFlag
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	//alert(parameterTemp.reason);

            });
		}
		
		function initializeTheStateNoResponse(){
			json.flag[0].responseWantedFlag=0;
    		codes="";
            $.post('initializeTheState.initialize', {
            	guid:json.parameter[0].guid,
            	responseWantedFlag:json.flag[0].responseWantedFlag
            });
			
			
			
			
		}
        
        function go(){
        	var CommandString=$('#command').val();
            $.post('go.g', {
            	myguid:json.parameter[0].guid,
            	command:CommandString
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
            	var expTemp = ExpressionEffect(json.parameter[0].expression);
        		
        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
         		    $('#responseconsole').text("");
                    $('#responseconsole').append("<p>"+expTemp+"</p>");
                	}
            		if(json.parameter[0].status==6){
            			$('#responseconsole').text("");
                        $('#responseconsole').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
            			}
            		if(json.parameter[0].status==7)
            			{
            			$('#responseconsole').text("");
                        $('#responseconsole').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
            			
            			}
            });
        }
        
        
        function goForward1(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.goForward', {
            	myguid:json.parameter[0].guid
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
            	var expTemp = ExpressionEffect(json.parameter[0].expression);

        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
     		    $('#responseconsole').text("");
                $('#responseconsole').append("<p>"+expTemp+"</p>");
                //alert("<p>"+json.parameter[0].expression+"</p>"+"<br>");
            	}
        		if(json.parameter[0].status==6){
        			$('#responseconsole').text("");
                    $('#responseconsole').append("<p>"+json.parameter[0].status+"</p>"+"<br>");
        			}
        		if(json.parameter[0].status==7)
        			{
        			$('#responseconsole').text("");
                    $('#responseconsole').append("<p>"+json.parameter[0].status+"</p>"+"<br>");
        			
        			}
        		
            });
            
        
        }
        
       function goBack1(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.goBack', {
            	myguid:json.parameter[0].guid
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
            	var expTemp = ExpressionEffect(json.parameter[0].expression);
        		
        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
        			$('#responseconsole').text("");
                    $('#responseconsole').append("<p>"+expTemp+"</p>"+"<br>");
                	}
            		if(json.parameter[0].status==6){
            			$('#responseconsole').text("");
                        $('#responseconsole').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
            			}
            		if(json.parameter[0].status==7)
            			{
            			$('#responseconsole').text("");
                        $('#responseconsole').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
            			
            			}
                   
                   
               
            });
           
            
        }
       
       
       	function redo(){
       	// $('#ajaxResponseforexp').text(resp[line]);
       	// line++;
           $.post('redo.redo', {
           	myguid:json.parameter[0].guid
           }, function(responseText) {
                  $('#responseconsole').append(responseText);
           });
       }
        
        $("#intoSub").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoSub.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#intoExp").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoExp.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#overAll").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('overAll.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#microStep").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('microStep.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
       
        

        
        $("#goForward2").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                  // $('#responseconsole').append(responseText);
            });
        });
        
        $("#goBack2").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#expression").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('expression.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
        		var s1="";
        		var s2= responseText;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
                   $('#responseconsole').append("<font color='black'>"+s1+"</font>"+"<br>");
            });

        });
        
         
        $('#quizanswer').click(function() {
            $.post('answer.go', {
            	myguid:json.parameter[0].guid
            }, function(responseText) {
                   $('#ajaxResponseforanswer').text(responseText);
            });
    });
        $('#clear').click(function() {
        	$('#responseconsole').text("");
        });            
});
/**functions for ajax**/




/**functions for dynamicTable**/


function add() {  
	//create table row tag tr
    var trObj = document.createElement("tr");  
    // set the element of the row  
    trObj.innerHTML = "<td width='30px'>"+m+"</td><td width='150px'>"  
        + "<input style=width:300px name='partialcode' id="+m+" /></td><td width='130px'><input type='button' id='btnSearch' value='Add' onclick='add()'> "  
        + "<input type='button' value='Del' onclick='del(this)'></td>";  
    // add row content to the table 
    m++;
    document.getElementById("tb").appendChild(trObj);  
}  

function del(obj) {  
    // delete current node's farther's farther 
	
    document.getElementById("tb").removeChild(obj.parentNode.parentNode);  
}  

function searchKeyPress(e){
	//look for window.event
		e=e||window.event;
	if(e.keyCode==13)
		{
		document.getElementById("btnSearch").click();
	
		}
}

function submit(){

	for(;count<m;count++){
		
	codes=codes+document.getElementById(count).value+"\n";
	
		}
	count=1;
	return codes;
	 //codes= document.getElementById(count).value+codes;
	
}

/**functions for dynamicTable**/

//generate css effect of the expression
function ExpressionEffect(exp){
	exp=exp.replace(/[\uffff]/g,"<span class='tm-red'>");
	exp=exp.replace(/[\ufffe]/g,"");
	exp=exp.replace(/[\ufffd]/g,"<span class='tm-underline'>");
	exp=exp.replace(/[\ufffc]/g,"<span class='tm-blue'>");
	exp=exp.replace(/[\ufffb]/g,"</span>");
	json.flag[0].expEffect=1;
	
	return exp;
	
	
	
	
}

