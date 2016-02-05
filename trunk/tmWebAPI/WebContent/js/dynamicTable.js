	var codes="";
	var i=2;
	var count= 1;
    function add() {  
        // 创建table 行标签tr  
        var trObj = document.createElement("tr");  
        // 设置行内容  
        trObj.innerHTML = "<td width='30px'>"+i+"</td><td width='150px'>"  
            + "<input name='lastName' id="+i+" /></td><td width='130px'><input type='button' id='btnSearch' value='Add' onclick='add()'> "  
            + "<input type='button' value='Del' onclick='del(this)'></td>";  
        // 将行内容添加到表格中  
        i++;
        document.getElementById("tb").appendChild(trObj);  
    }  
  
    function del(obj) {  
        // 直接删除当前对象的父节点的父节点 
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
    