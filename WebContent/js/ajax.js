var resp = null;
var line = 0;

$(document).ready(function() {
	
        $('#start').click(function(event) {
                var code = $('#Codes').val();
                $.post('createRemoteTM.do', {
                	Codes : code
                }, function(responseText) {
                      // resp = responseText.split("\n");
                       $('#ajaxResponseforexp').text(responseText);
                });
        });
        
        $("#state").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('state.go', {
            }, function(responseText) {
                   $('#ajaxResponseforexp').text(responseText);
            });
        });
        
        $("#expression").click(function(){
        	// $('#ajaxResponseforexp').text(resp[line]);
        	// line++;
            $.post('expression.go', {
            }, function(responseText) {
                   $('#ajaxResponseforexp').text(responseText);
            });
        });
        
        
        $('#quizanswer').click(function(event) {
            $.post('answer.go', {
            }, function(responseText) {
                   $('#ajaxResponseforanswer').text(responseText);
            });
    });
        
        
        
        
        
});