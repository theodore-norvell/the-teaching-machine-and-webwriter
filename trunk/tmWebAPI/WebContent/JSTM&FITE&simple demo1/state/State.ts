/// <reference path="concreteJSTM.ts" />
/// <reference path="../descriptquestion/description.ts" />
/// <reference path="../src/JSTM.ts" />

module State{



    export var isInputValidFlag=false;

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
        gotoStarted?:(c:FITE)=>void;
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
           var filename = 'FITE.cpp';
            
           var program=DESCRIP.cascadeProgram();
            console.log(program);
            
            fite.loadStringAndInitialize(filename,program);
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
           var filename = 'FITE.cpp';
            
           var program=DESCRIP.cascadeProgram();
            
           fite.loadStringAndInitialize(filename,program);
            
           fite.setCurrentState(FITE.wait);
        }
        gotoStarted(fite:FITE){
            
            fite.setCurrentState(FITE.started);
            document.getElementById('goFoward').removeAttribute('disabled');
            document.getElementById('goBack').removeAttribute('disabled');
            document.getElementById('panel').style.display='block';
 
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
       
        concrete:jstm.JSTM;
        constructor(concrete:jstm.JSTM){
            this.concrete=concrete;  
        }
        
        static initialize:state = new Initialize(FITEState.initialize);
        static startable:state = new Startable(FITEState.startable);
        static wait:state = new Wait(FITEState.wait);
        static started:state = new Started(FITEState.started);
       
        currentState:state = FITE.initialize;
        
        //user check valid
        checkValid(){
            this.currentState.checkValid(this);
        }
        
        //user click start , 
        clickStart(){
            this.currentState.clickStart(this);
        }
        
        //gotoStarted
        gotoStarted(){
            this.currentState.gotoStarted(this);   
        }
        
        
        
        setCurrentState(s:state){
            this.currentState=s;
            console.log('current state is '+this.currentState.name);
            
            
        }

        isInputValid(){
            return isInputValidFlag;
        }
        
        

    //11111111create  this is a promise's callback with 3 .done cascaded{which is the callback functions}
    public loadStringAndInitialize(filename,program){
     var thisFITE = this ;
     var thisConcreteJSTM = this.concrete;
     
    //call the loadString in the concrete JSTM        
        var program=program;
        var filename=filename;
        console.log(program+" "+filename+" "+thisConcreteJSTM.guid);
        
        //call the loadString in the concrete JSTM
    thisConcreteJSTM.loadString(program,filename)
    .done(function(data){
        console.log('loadString callback for the resolve');
        console.log(data);
        //'1' means, i want the response back from the server
        var responseWantedFlag='1';
        //call the initialize in concrete JSTM
    thisConcreteJSTM.initialize(responseWantedFlag)
    .done(function(data){
            console.log('initialize callback for the resolve');
            console.log(data);
          // DESCRIP.updateVariablesToPanelArea();
            thisFITE.gotoStarted();
        })
        .fail(function(data){
            document.getElementById('panel').style.display='none';
            alert(data.message);
            console.log('fail');
            
        })   
    })
    .fail(function(data){
        document.getElementById('panel').style.display='none';
        alert(data.message);
        console.log('fail');
        })
    
    }

    
    //22222222loadString this method is not used in my test, because i need use create().done.done.done chain to make it work. so this methid is cascaded in the .done
    public loadString(){
       var thisConcreteJSTM=this.concrete;
       
        var program=program;
        var filename=filename;
        
        //call the loadString in the concrete JSTM
       thisConcreteJSTM.loadString(program,filename)
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
        var thisConcreteJSTM=this.concrete;
        //'1' means, i want the response back from the server
        var responseWantedFlag='1';
        //call the initialize in concrete JSTM
        thisConcreteJSTM.initialize(responseWantedFlag)
        .done(function(data){
            console.log('initialize callback for the resolve');
            console.log(data);
        })
        .fail(function(data){
            console.log('fail');
        })
        
    }
    
    public goForward(){
        var thisConcreteJSTM=this.concrete;
        console.log(this);
        console.log(this.currentState);
        console.log(this.concrete);
        thisConcreteJSTM.goForward()
        .done(function(data){
            console.log('goForward callback for the resolve');
            console.log(data);
            
        })
        .fail(function(data){
            console.log('fail');
            
        })
        
        
        
    }
    
    public goBack(){
        var thisConcreteJSTM=this.concrete;
        
        thisConcreteJSTM.goBack()
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