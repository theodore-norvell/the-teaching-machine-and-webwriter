


/**  variables for ajax **/
var resp = null;
var line = 0;

 ajaxCall = {
	"ajCall":[{"code" : "","guid":"" },
	          {"focus": "", "reason": ""},
			  {"status":""}
		     ]}
/**  variables for ajax **/

 
 var programText=null;
 
 
 
 
 
/**  variables for dynamicTable**/

var codes="";
var m=12;
var count= 1;
/**  variables for dynamicTable**/


/**functions for ajax**/
$(document).ready(function() {
	
	
	
	
        $('#createRTM').click(function() {
        	   ajaxCall.ajCall[0].code=submit();
        		var code=ajaxCall.ajCall[0].code;
        		codes="";
                $.post('createRemoteTM.do', {
                	Codes : code
                }, function(responseText) {
                     resp = responseText.split("\n");
                     ajaxCall.ajCall[2].status=resp[0];
                     ajaxCall.ajCall[0].guid=resp[1];
                     ajaxCall.ajCall[1].reason=resp[2];
                     programText=resp[3];
                     
                     alert(ajaxCall.ajCall[2].status);
                     alert(ajaxCall.ajCall[0].guid);
                     alert(ajaxCall.ajCall[1].reason);
                });
        });
        
        $('#loadString').click(function() {
            $.post('loadString.lo', {
            	myguid:ajaxCall.ajCall[0].guid,
            	p : programText
            }, function(responseText) {
                 resp = responseText.split("\n");
                 ajaxCall.ajCall[2].status=resp[0];
                 ajaxCall.ajCall[1].reason=resp[1];
                 
                 alert(ajaxCall.ajCall[2].status);
                 alert(ajaxCall.ajCall[1].reason);
            });
    });
        
        $('#initializeTheState').click(function() {
            //var code = $('#Codes').val();
    	   ajaxCall.ajCall[0].code=submit();
    		var code=ajaxCall.ajCall[0].code;
    		//alert("HAVE SENT   "+code);
    		codes="";
            $.post('initializeTheState.io', {
            	Codes : code
            }, function(responseText) {
                 resp = responseText.split("\n");
                 ajaxCall.ajCall[0].guid=resp[0];
                // ajaxCall.ajCall[1].focus=resp[1];
                 //alert("THE GUID IS   "+ajaxCall.ajCall[0].guid);
                // alert("The focus number is"+ ajaxCall.ajCall[1].focus);
                // $('#responseconsole').append(responseText+"\n");
            });
    });
        
        
        
        $("#go").click(function(){
        	var CommandString=$('#command').val();
            $.post('go.go', {
            	myguid:ajaxCall.ajCall[0].guid,
            	command:CommandString
            }, function(responseText) {
            	$('#responseconsole').append(responseText);
            });
        });
        
        
        $("#goForward1").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   //$('#responseconsole').append(responseText);
            	//ajaxCall.ajCall[1].focus=responseText;
            });
            
            
            
            $.post('expression.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
        		var s1="";
        		var s2= responseText;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
        		
     		   	   $('#responseconsole').text("");
                   $('#responseconsole').append("<font color='black'>"+s1+"</font>"+"<br>");
                  // alert(ajaxCall.ajCall[1].focus);
            });
            
            
        });
        
        $("#intoSub").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoSub.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#intoExp").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoExp.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#overAll").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('overAll.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#microStep").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('microStep.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#goBack1").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
            
            $.post('expression.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
        		var s1="";
        		var s2= responseText;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
        		
        		
        		   $('#responseconsole').text("");
                   $('#responseconsole').append("<font color='black'>"+s1+"</font>"+"<br>");
            });
            
        });
        
        $("#redo").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('redo.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#goForward2").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                  // $('#responseconsole').append(responseText);
            });
        });
        
        $("#goBack2").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:ajaxCall.ajCall[0].guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#expression").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('expression.go', {
            	myguid:ajaxCall.ajCall[0].guid
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
            	myguid:ajaxCall.ajCall[0].guid
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



