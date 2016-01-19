var resp = null;
var line = 0;
var guid;

$(document).ready(function() {
	
        $('#start').click(function() {
                var code = $('#Codes').val();
                $.post('createRemoteTM.do', {
                	Codes : code
                }, function(responseText) {
                     resp = responseText.split("\n");
                     guid=resp[0];
                     $('#ajaxResponseforexp').text(resp[0]);
                });
        });
        
        $("#go").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('go.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#ajaxResponseforexp').text(responseText);
            });
        });
        
        $("#goBack").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('goBack.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#ajaxResponseforexp').text(responseText);
            });
        });
        
        $("#redo").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('redo.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#ajaxResponseforexp').text(responseText);
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
        		
                   $('#ajaxResponseforexp').text(s1);
            });
        });
        
         
        $('#quizanswer').click(function() {
            $.post('answer.go', {
            	myguid:guid
            }, function(responseText) {
                   $('#ajaxResponseforanswer').text(responseText);
            });
    });
        
        
        
        
        
});