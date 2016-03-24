var resp = null;
var line = 0;
var guid;

$(document).ready(function() {
	
        $('#start').click(function() {
                var code = $('#Codes').val();
                alert(code);
                $.post('createRemoteTM.do', {
                	Codes : code
                }, function(responseText) {
                     resp = responseText.split("\n");
                     guid=resp[0];
                     $('#responseconsole').append(responseText+"\n");
                });
        });
        
        $("#go").click(function(){
        	var CommandString=$('#command').val();
            $.post('go.go', {
            	myguid:guid,
            	command:CommandString
            }, function(responseText) {
            	$('#responseconsole').append(responseText);
            });
        });
        
        
        $("#goForward").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goForward.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#intoSub").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoSub.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#intoExp").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('intoExp.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#overAll").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('overAll.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#microStep").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('microStep.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#goBack").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#redo").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('redo.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#responseconsole').append(responseText);
            });
        });
        
        $("#expression").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('expression.go', {
            	myguid:guid
            }, function(responseText) {
        		var s1="";
        		var s2= responseText;
        		for(var i=0;i<s2.length;i++)
        			{
        			var str = s2.substring(i,i+1);
        			if(str.charCodeAt(0)>=33 && str.charCodeAt(0)<=126)
        				s1+=str;
        			}
                   $('#responseconsole').append("<font color='red'>"+s1+"</font>"+"<br>");
            });
        });
        
         
        $('#quizanswer').click(function() {
            $.post('answer.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#ajaxResponseforanswer').text(responseText);
            });
    });
        $('#clear').click(function() {
        	$('#responseconsole').text("");
        });
        
        
        
        
});