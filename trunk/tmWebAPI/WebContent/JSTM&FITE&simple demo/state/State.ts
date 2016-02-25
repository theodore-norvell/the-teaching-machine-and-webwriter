/// <reference path="concreteJSTM.ts" />
/// <reference path="../descriptquestion/description.ts" />

module State{
    //export var test = '1';
    export var isInputValidFlag=false;
    export var concrete:jstm.concreteJSTM;
    export      var filename:string;
    export      var program:string;
    //1,2,3,4
    var FITEState={
        initialize:'initialize',
        startable:'startable',
        wait:'wait',
        started:'started'  
    }
    //interface
    interface state{
        name:number;
        checkValid ?:(c:FITE)=>void;
        clickStart ?:(c:FITE)=> void;
    }
    
    //1
    class Initialize implements state{
        
        constructor(name){
            this.name=name;
        }
        name;
        
        checkValid(fite:FITE){
            if(fite.isInputValid())
            {	  
                console.log('input valid'); 
                fite.setCurrentState(FITE.startable);
            }
            else{
                console.log('input not valid'); 
                fite.setCurrentState(FITE.initialize);
            }
            
            
  
            
        }
    }
    
    //2
    class Startable implements state{
        
        constructor(name){
            this.name=name;
        }
        name;
        //rechecked 
        checkValid(fite:FITE){
            if(fite.isInputValid())
            {	  
                console.log('input valid'); 
                fite.setCurrentState(FITE.startable);
            }    
        }
        //start button clicked event
        clickStart(fite:FITE){
            //i wish to fetch the program& the filename from the web pages!
            filename = 'FITE.cpp';
            
            program=DESCRIP.cascadeProgram();
            console.log(program);
            
            fite.create();
            fite.setCurrentState(FITE.wait);
        }
    }
    
    //3
    class Wait implements state{
        
        constructor(name){
            this.name=name;
        }
        name;
        
        //rechecked 
        checkValid(fite:FITE){
            if(fite.isInputValid())
            {	  
                console.log('input valid'); 
                fite.setCurrentState(FITE.startable);
            }    
        }
        clickStart(fite:FITE){
            //i wish to fetch the program& the filename from the web pages!
            filename = 'FITE.cpp';
            
            program=DESCRIP.cascadeProgram();
            
            fite.create();
            
            fite.setCurrentState(FITE.wait);
        }

    }
    
    //4
    class Started implements state{
        
        constructor(name){
            this.name=name;
        }
        name;
        

    }
    
   export class FITE{
        concrete:jstm.concreteJSTM;
        constructor(concrete){
            this.concrete=concrete;
            
        }
        static initialize:state = new Initialize(FITEState.initialize);
        static startable:state = new Startable(FITEState.startable);
        static wait:state = new Wait(FITEState.wait);
        static started:state = new Started(FITEState.started);
       
        currentState:state = FITE.initialize;
        
        //
        checkValid(){
            this.currentState.checkValid(this);
        }
        
        //
        clickStart(fite:State.FITE){
            this.currentState.clickStart(fite);
        }
        
        
        
        
        setCurrentState(s:state){
            this.currentState=s;
            console.log('current state is '+this.currentState.name);
            
            
        }
        
        
        
        
        
        isInputValid(){
            return isInputValidFlag;
        }
        
    //11111111create  this is a promise's callback with 3 .done cascaded{which is the callback functions}
    public create(){
      
     
     concrete = this.concrete;
    //call the loadString in the concrete JSTM        
     concrete.createRTM()
    .done(function(data){
        console.log('createRTM callback for the resolve');
        console.log(data);
        
        var program=State.program;
        var filename=State.filename;
        var guid=jstm.json.parameter[0].guid;
        console.log(program+" "+filename+" "+guid);
        
        //call the loadString in the concrete JSTM
       concrete.loadString(program,filename,guid)
    .done(function(data){
        console.log('loadString callback for the resolve');
        console.log(data);
        var guid=jstm.json.parameter[0].guid;
        //'1' means, i want the response back from the server
        var responseWantedFlag='1';
        //call the initialize in concrete JSTM
        concrete.initialize(guid,responseWantedFlag)
    .done(function(data){
            console.log('initialize callback for the resolve');
            console.log(data);
            document.getElementById('goFoward').removeAttribute('disabled');
            document.getElementById('goBack').removeAttribute('disabled');
            document.getElementById('panel').style.display='block';
        })
        .fail(function(data){
            document.getElementById('panel').style.display='none';
            alert(jstm.json.parameter[0].reason);
            console.log('fail');
            
        })
        
    })
    .fail(function(data){
        document.getElementById('panel').style.display='none';
        alert(jstm.json.parameter[0].reason);
        console.log('fail');
        })
        })
    .fail(function(data){
        console.log('fail');
        })
    
    }
    
    
    //22222222loadString this method is not used in my test, because i need use create().done.done.done chain to make it work. so this methid is cascaded in the .done
    public loadString(){
       
        var program=program;
        var filename=filename;
        var guid=jstm.json.parameter[0].guid;
        
        //call the loadString in the concrete JSTM
       concrete.loadString(program,filename,guid)
        .done(function(data){
        console.log('loadString callback for the resolve');
        console.log(data);
        
    })
    .fail(function(data){
        console.log('fail');
        })
  
    }
    
    //333333333initialize this method is not used in my test, because i need use create().done.done.done chain to make it work.so this methid is cascaded in the .done
    public initialize(){
        var guid=jstm.json.parameter[0].guid;
        //'1' means, i want the response back from the server
        var responseWantedFlag='1';
        //call the initialize in concrete JSTM
        concrete.initialize(guid,responseWantedFlag)
        .done(function(data){
            console.log('initialize callback for the resolve');
            console.log(data);
        })
        .fail(function(data){
            console.log('fail');
        })
        
    }
    
    public goForward(){
        var guid=jstm.json.parameter[0].guid;
        
        concrete.goForward(guid)
        .done(function(data){
            console.log('goForward callback for the resolve');
            console.log(data);
            
        })
        .fail(function(data){
            console.log('fail');
            
        })
        
        
        
    }
    
    public goBack(){
        var guid=jstm.json.parameter[0].guid;
        
        concrete.goBack(guid)
        .done(function(data){
            console.log('goBack callback for the resolve');
            console.log(data);
            
        })
        .fail(function(data){
            console.log('fail');
            
        })
        
        
        
        
    }
    
    
    
    
    
    
    
    
    
    
    
        
    }
   

    
    
}

/** 
window.onload=function(){
//
var c= new State.FITE();

State.isInputValidFlag = true;
console.log('currentState is '+c.currentState.name);
c.checkValid();
c.clickStart();

}
**/