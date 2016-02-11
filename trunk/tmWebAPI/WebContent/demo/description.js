$(document).ready(function(){
    
 json={
		 "parameter":[{"guid":"","reasonFlag":"","reason":"","focus":"","code":"","status":"","expression":"","filename":""}],
		 "flag":[{"expEffect":0,"responseWantedFlag":null}]
		 
 };    


var globalvar;
var questionTemp;
var index = [];
var front;
//var middle='x=10.0; \n y=20.0; \n a=(y*y-x*x)*3.1415; \n';
var tail='return 0; \n }';
var program;
console.log(program);
//display area 1
var expressionDisplay = document.getElementById('expressionDisplay');
var initialValueOfX = document.getElementById('initialValueOfX');
var initialValueOfY = document.getElementById('initialValueOfY');
var X=document.getElementById('X');
var Y=document.getElementById('Y');
var expression=document.getElementById('expression');
var expressioninpanel=document.getElementById('expressioninpanel');
var start = document.getElementById('start'); 
var div3= document.getElementById('div3');

//insert


var insert1=document.getElementById('insert1');
var insert2=document.getElementById('insert2');
//panel
var panel = document.getElementById('panel');
var initialValueOfXinpanel=document.getElementById('initialValueOfXinpanel');
var initialValueOfYinpanel=document.getElementById('initialValueOfYinpanel');
var initialValueOfainpanel=document.getElementById('initialValueOfainpanel');
var Xinpanel=document.getElementById('Xinpanel');
var Yinpanel=document.getElementById('Yinpanel');
var ainpanel=document.getElementById('ainpanel');
var goForwardButton=document.getElementById('goFoward');
var goBackButton=document.getElementById('goBack');
var closebtn= document.getElementById('closebtn');
var expressionpanel=document.getElementById('expressionpanel');
panel.style.display='none';
div3.style.display='none';
//initia

    expressionDisplay.innerHTML='Expression';
    initialValueOfX.innerHTML='Initial Value of x:';
    initialValueOfY.innerHTML='Initial Value of y:';
    X.value='10.0';
    Y.value='20.0';
    start.innerHTML='Start';
    
    initialValueOfXinpanel.innerHTML='X';
    initialValueOfYinpanel.innerHTML='Y';
    initialValueOfainpanel.innerHTML='a';
    
    //inser1 part ini
        var initialValueOfZ=document.createElement('span');
        var Z=document.createElement('input');
        var initialValueOfU=document.createElement('span');
        var U=document.createElement('input');
        
     //insert2 part ini
     var initialValueOfZinpanel=document.createElement('span');
     var Zinpanel=document.createElement('input');
     var initialValueOfUinpanel=document.createElement('span');
     var Uinpanel=document.createElement('input');
    //
//AJAX call to get the local .json file
function AJAX_JSON_Req( url )
{
    var AJAX_req = new XMLHttpRequest();
    AJAX_req.open( "GET", url, true );
    AJAX_req.setRequestHeader("Content-type", "application/json");
 
    AJAX_req.onreadystatechange = function()
    {
        if( AJAX_req.readyState == 4 && AJAX_req.status == 200 )
        {
            var response = JSON.parse( AJAX_req.response );
            globalvar=response;
             //console.log(response);
             //console.log(response[0].questionstart);
            //document.write( response.name );
        }
    }
    AJAX_req.send();
}

 
AJAX_JSON_Req( 'description.json' );

var button = document.getElementById('questionslist');
//transfer json object to array
button.addEventListener('click',tansferarray,false);

function tansferarray(){
    console.log('i am under focus');
    $('#expression').val('');
    $('#expressioninpanel').text('');
    for (var x in globalvar[0]){
        index.push({'key':x,value:globalvar[0][x]});
    }
    /** 
    for (var i=0;i<index.length;i++){
        console.log(index[i].value);
    }
    **/
  }

var select = document.getElementById('questionslist');

select.onchange=function selec(e){
    var i =this.selectedIndex;
    var questiondisplay = document.getElementById('questionDisplay');   
   // console.log("select"+select.selectedIndex);
    switch(i){
       case 0:  questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;
       case 1: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;           
       case 2: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;       
       case 3: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;       
       case 4: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;
       case 5: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;           
       case 6: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;       
       case 7: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;  
       case 8: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;       
       case 9: questionTemp=index[i].value;
                questiondisplay.innerHTML=questionTemp.content;
                console.log(questionTemp);
                descript(questionTemp);
                break;                         
   }
    
      
}

function descript(questionTemp){
    if(questionTemp.numberofvariable==2){
             $('#insert1').text("");
            div3.style.display='block';
        
    }
    if(questionTemp.numberofvariable==3){
        //the add part 
    initialValueOfZ.setAttribute('id','initialValueOfZ');
    initialValueOfZ.innerHTML="Initial Value of z:"+"&nbsp";
   
    Z.setAttribute('id','Z');
    Z.setAttribute('type','text');
    Z.innerHTML="30.0";
    $('#insert1').text("");
    $('#insert1').append(initialValueOfZ);
    $('#insert1').append(Z);    
    div3.style.display='block';
    }
    if(questionTemp.numberofvariable==4){
    initialValueOfZ.setAttribute('id','initialValueOfZ');
    initialValueOfZ.innerHTML="Initial Value of z:"+"&nbsp";
    
    Z.setAttribute('id','Z');
    Z.setAttribute('type','text');
    Z.value="30.0";
    
    $('#insert1').text("");
    $('#insert1').append(initialValueOfZ);
    $('#insert1').append(Z);  
    initialValueOfU.setAttribute('id','initialValueOfU');
    initialValueOfU.innerHTML="Initial Value of u:"+"&nbsp";
    
    U.setAttribute('id','U');
    U.setAttribute('type','text');
    U.value="40.0";
    $('#insert1').append("<P></P>"); 
    $('#insert1').append(initialValueOfU);
    $('#insert1').append(U);    
    div3.style.display='block';
        
    }
    
}

expression.addEventListener('input',updatepanelexpression,false);
X.addEventListener('input',updatepanelexpression,false);
Y.addEventListener('input',updatepanelexpression,false);
Z.addEventListener('input',updatepanelexpression,false);
U.addEventListener('input',updatepanelexpression,false);

start.setAttribute('disabled','disabled');
goForwardButton.setAttribute('disabled','disabled');
goBackButton.setAttribute('disabled','disabled');

function updatepanelexpression(){
    console.log('underlistener');
    if (expression.value!=''&&!isNaN(X.value)&&!isNaN(Y.value))
    {
    expressioninpanel.innerHTML=expression.value;
    Xinpanel.value=X.value;
    Yinpanel.value=Y.value;
    Zinpanel.value=Z.value;
    Uinpanel.value=U.value;
    start.removeAttribute('disabled');  
    }
   // Xinpanel.value=X.value;
   // console.log(Xinpanel.value);
}

//construct the string
var middle;

function getmiddle(){
    if(questionTemp.numberofvariable==2){
    //part1
    var p1= expression.value+';'+'\n';
    //part2
    var p2temp= X.value;
    var p2='x='+p2temp+';'+'\n';
    //part3
    var p3temp= Y.value;
    var p3='y='+p3temp+';'+'\n';
    middle = p2+p3+p1;
    front='#include<iostream> \n using namespace std; \n int main(){ \n  double x,y,a; \n ';
    program= front+middle+tail;
    console.log(program);
      return program;
    }
    if(questionTemp.numberofvariable==3){
    var p1= expression.value+';'+'\n';
    //part2
    var p2temp= X.value;
    var p2='x='+p2temp+';'+'\n';
    //part3
    var p3temp= Y.value;
    var p3='y='+p3temp+';'+'\n';
    //part4
    var p4temp=Z.value;
    var p4='z='+p4temp+';'+'\n';
    
    middle = p2+p3+p4+p1;
    front='#include<iostream> \n using namespace std; \n int main(){ \n  double x,y,z,a; \n ';
     program= front+middle+tail;
    console.log(program);
      return program;      
    }
    
    if(questionTemp.numberofvariable==4){
    var p1= expression.value+';'+'\n';
    //part2
    var p2temp= X.value;
    var p2='x='+p2temp+';'+'\n';
    //part3
    var p3temp= Y.value;
    var p3='y='+p3temp+';'+'\n';
    //part4
    var p4temp=Z.value;
    var p4='z='+p4temp+';'+'\n';
    //part5
    var p5temp=U.value;
    var p5='u='+p5temp+';'+'\n';
    middle = p2+p3+p4+p5+p1;
        front='#include<iostream> \n using namespace std; \n int main(){ \n  double x,y,z,u,a; \n ';
     program= front+middle+tail;
    console.log(program);
      return program;      
    }

}


start.addEventListener('click',ajaxsend,false);

function ajaxsend(){
   createRemoteTM();
    panel.style.display='block';
    start.innerHTML='ReStart';
  }

closebtn.addEventListener('click',closepanel,false);
  
function closepanel(){
      //console.log();
      panel.style.display='none';
  }
  
  
goForwardButton.addEventListener('click',goForward1,false);
goBackButton.addEventListener('click',goBack1,false);





//from 980b-1.31.js

	function createRemoteTM(){
        $.post('createRemoteTM.create', {
        }, function(response) {
        	var parameterTemp =JSON.parse(response);
        	//assign back-end json object to the front end parameters in this js file;
        	json.parameter[0].guid=parameterTemp.guid;
        	json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
        	json.parameter[0].reason=parameterTemp.reason;
        	json.parameter[0].status=parameterTemp.status;
        	//if there is no error in the createRemoteTM step
            console.log(json.parameter[0].status);
        	if (json.parameter[0].reasonFlag==0){
            loadString(getmiddle());
        }
        //if there is error in the loadString step
        else{
                panel.style.display='none';
                alert(json.parameter[0].reason);
        	//$('#responseconsole').text(json.parameter[0].reason);
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
// the parametehr is the whole program string , notice you need to construct the string before parse it into the function 
		function loadString(prog) {
			var filename='test.cpp';
			json.parameter[0].filename==filename;
    	    json.parameter[0].code=prog;
    		var code=json.parameter[0].code;
    		codes="";
            $.post('loadString.load', {
            	guid:json.parameter[0].guid,
            	Codes : code,
            	filename:filename
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
        	    json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
        	    json.parameter[0].reason=parameterTemp.reason;
        	    json.parameter[0].status=parameterTemp.status;
        	//if there is no error in the createRemoteTM step
        	if (json.parameter[0].reasonFlag==0){
            //alert(json.parameter[0].status);
            initializeTheState();
        }
        //if there is error in the loadString step
        else{
            panel.style.display='none';
            alert(json.parameter[0].reason);
        	//$('#responseconsole').text(json.parameter[0].reason);
        }
        

        });
        }
        
		function initializeTheState() {
			json.flag[0].responseWantedFlag=1;
            $.post('initializeTheState.initialize', {
            	guid:json.parameter[0].guid,
            	responseWantedFlag:json.flag[0].responseWantedFlag
            }, function(response) {
            	var parameterTemp =JSON.parse(response);
        	    json.parameter[0].reasonFlag=parameterTemp.reasonFlag;
        	    json.parameter[0].reason=parameterTemp.reason;
        	    json.parameter[0].status=parameterTemp.status;
                console.log(json.parameter[0].status);
                if (getStatus()=='4'){
                    goForwardButton.removeAttribute('disabled');
                    goBackButton.removeAttribute('disabled');
                    
                }
                else{
                panel.style.display='none';
                alert(json.parameter[0].reason);
                }
                
                
            	//alert(parameterTemp.reason);

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
     		    $('#expressioninpanel').text("");
                $('#expressioninpanel').append("<span>"+expTemp+"</span>");
                //alert(expTemp);
            	}
        		if(json.parameter[0].status==6){
        			$('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>"+json.parameter[0].status+"</span>"+"<br>");
        			}
        		if(json.parameter[0].status==7)
        			{
        			$('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>"+json.parameter[0].reason+"</span>"+"<br>");

        			
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
        			$('#expressioninpanel').text("");
                    $('#expressioninpanel').append("<span>"+expTemp+"</span>");
                	}
            		if(json.parameter[0].status==6){
            			$('#expressioninpanel').text("");
                        $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].status+"</font>"+"<br>");
            			}
            		if(json.parameter[0].status==7)
            			{
            			$('#expressioninpanel').text("");
                        $('#expressioninpanel').append("<font color='black'>"+json.parameter[0].reason+"</font>"+"<br>");
            			
            			}
                   
                   
               
            });
           
            
        }
        
        function getStatus(){
            return json.parameter[0].status;
            
        }




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


        });