	var codes="";
	var i=2;
	var count= 1;
    function add() {  
        // ����table �б�ǩtr  
        var trObj = document.createElement("tr");  
        // ����������  
        trObj.innerHTML = "<td width='30px'>"+i+"</td><td width='150px'>"  
            + "<input name='lastName' id="+i+" /></td><td width='130px'><input type='button' id='btnSearch' value='Add' onclick='add()'> "  
            + "<input type='button' value='Del' onclick='del(this)'></td>";  
        // ����������ӵ������  
        i++;
        document.getElementById("tb").appendChild(trObj);  
    }  
  
    function del(obj) {  
        // ֱ��ɾ����ǰ����ĸ��ڵ�ĸ��ڵ� 
    	//
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
 
    	for(;count<i;count++){
    	codes=codes+document.getElementById(count).value;
    		}
    	alert(codes);
    	 //codes= document.getElementById(count).value+codes;
    	
    }
    