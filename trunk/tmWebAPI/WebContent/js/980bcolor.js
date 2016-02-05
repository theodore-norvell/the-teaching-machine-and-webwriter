/**  variables for ajax **/
var resp = null;
var line = 0;
 //json array to store parameters 
 json={
		 "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":""}],
		 "flag":[]
		 
 };
/**  variables for ajax **/

 
/**  variables for dynamicTable**/
var codes="";
var m=12;
var count= 1;
/**  variables for dynamicTable**/


/**functions for ajax**/
$(document).ready(function() {
       
		$('#start').click(function() {
        	    json.parameter[0].code=submit();
        		var code=json.parameter[0].code;
        		codes="";
                $.post('createRemoteTM.do', {
                	Codes : code
                }, function(response) {
                	var parameterTemp =JSON.parse(response);
                	//assign back-end json object to the front end parameters in this js file;
                	json.parameter[0].guid=parameterTemp.guid;
                	json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
                	json.parameter[0].reason=parameterTemp.reason;
                	json.parameter[0].status=parameterTemp.status;
                	//if there is no error in the loadString step
                	if (json.parameter[0].reasonFlag==0){
/*                	alert(json.parameter[0].guid);
                	alert(json.parameter[0].reasonFlag);
                	alert(json.parameter[0].reason);
                	alert(json.parameter[0].status);
                	alert("no error");*/
                	
                }
                //if there is error in the loadString step
                else{
         /*       	alert(json.parameter[0].guid);
                	alert(json.parameter[0].reasonFlag);
                	alert(json.parameter[0].reason);
                	alert(json.parameter[0].status);
                	alert("have error");*/
                	$('#responseconsole').text(json.parameter[0].reason);
                }

                });
        });
        
     
        
        $("#go").click(function(){
        	var CommandString=$('#command').val();
            $.post('go.go', {
            	myguid:json.parameter[0].guid,
            	command:CommandString
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
        		var s1="";
        		var s2= json.parameter[0].expression;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
        		//after parsing the expression, assign it to the json object.
        		json.parameter[0].expression=s1;
        		
        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
         		    $('#responseconsole').text("");
                    $('#responseconsole').append("<font color='black'>"+json.parameter[0].expression+"</font>"+"<br>");
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
        });
        
        
        $("#goForward1").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.go', {
            	myguid:json.parameter[0].guid
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
/*        		var s1="";
        		var s2= json.parameter[0].expression;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}*/
        		//after parsing the expression, assign it to the json object.
        		//json.parameter[0].expression=s1;
            	
        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
     		    $('#responseconsole').text("");
                $('#responseconsole').append("<font color='black'>"+json.parameter[0].expression+"</font>"+"<br>");
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
            
        
        });
        
        $("#goBack1").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:json.parameter[0].guid
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
            	json.parameter[0].reason=parameterTemp.reason;
            	json.parameter[0].status=parameterTemp.status;
            	json.parameter[0].expression=parameterTemp.expression;
            	/**parse the characters of the expression**/
        		var s1="";
        		var s2= json.parameter[0].expression;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
        		//after parsing the expression, assign it to the json object.
        		json.parameter[0].expression=s1;
        		
        		if(json.parameter[0].status!=6&&json.parameter[0].status!=7){
         		    $('#responseconsole').text("");
                    $('#responseconsole').append("<font color='black'>"+json.parameter[0].expression+"</font>"+"<br>");
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
           
            
        });
        
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
       
        
        $("#redo").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('redo.go', {
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



