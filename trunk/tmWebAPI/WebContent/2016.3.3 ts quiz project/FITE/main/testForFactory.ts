/// <reference path="../library/jquery.d.ts" />
///<reference path="quizbuilder.ts" />
///<reference path="../state/State.ts" />
///<reference path="../src/concreteJSTM.ts" />
///<reference path="../singleton/singleton.ts" />



window.onload=function(){


    
   //call ajax function to read the whole json file and then store in a var called globalvar;
     //1
     quizBuilder.AJAX_JSON_Req ('description.json');
 
     //2 push to index
     var index = [];
     var tempQuiz; 
     
     index=quizBuilder.tansferarray(index);
     
     //3 
     var select = document.getElementById('questionslist'); 
     select.addEventListener('change',selec,false);

     //declare quiz object
     var qz:quizBuilder.Quiz;
     //declare fite object
     var fite:State.FITE;
     
     function selec() {
     var i =this.selectedIndex;  
      //console.log(index[i].value.inputVars.length);
      //console.log(index[i]);
      quizBuilder.descript(index[i])
  .done(function(data:any){
      tempQuiz=data;
    // console.log(data);
    //instantiate director
   var qd:quizBuilder.QuizDirector = new quizBuilder.QuizDirector();
   //instantiate concrete builder FITEQuiz
   var fitequiz:quizBuilder.FITEQuiz = new quizBuilder.FITEQuiz(data.category,data.name,data.language);

   //set director
   qd.setQuizBuilder(fitequiz);
   //cal construct method
   qd.constructQuiz();
   //het quiz product 
   qz = qd.getQuiz();

   qz.constructInertArea1();
   qz.constructInsertArea2();
   
    qz.inputExpressionInputElement[0].addEventListener('input',ValidWatch,false);
   for(var i=0;i<qz.inputVarsInputElement.length;i++){
    qz.inputVarsInputElement[i].addEventListener('input',ValidWatch,false);
                                                      }
   
   
     //done
       }); 
  //selec function
        }  
        
        
    jstm.concreteJSTM.createRTM()
    .done(function(data){
          fite= new State.FITE(data);
          //fite.checkValid();
     })
     .fail(function(data){alert('error in the testForFactory');});


    var startButton = document.getElementById('start'); 
    startButton.addEventListener('click',start,false); 
    
    function start(){
    //fite.clickStart();
    console.log('i have start');
    getProgramText();
    }
    
    var goForwardButton = document.getElementById('goFoward');
    var goBackButton = document.getElementById('goBack');
    var closeButton = document.getElementById('closebtn');
    
    goForwardButton.addEventListener('click',goForward,false);
    goBackButton.addEventListener('click',goBack,false);
    closeButton.addEventListener('click',close,false);
    
    function goForward(){
        fite.goForward();
    }
    function goBack(){
        fite.goBack();
    }
    
    function close(){
        document.getElementById('panel').style.display='none';
        startButton.setAttribute('disabled','disabled');
        document.getElementById('expressioninpanel').innerHTML='';
    }


























  //var concrete = new jstm.concreteJSTM('i will be send to FITE');
   // check the user - input validity at run time 
   function ValidWatch(){
       var boolFlag=true;
       var bool = new Array<boolean>();
       for(var i=0;i<qz.inputVarsInputElement.length;i++)
       {  
           bool[i]=qz.inputExpressionInputElement[0].value!=''&&!isNaN(qz.inputVarsInputElement[i].value)&&qz.inputVarsInputElement[i].value!='';
           if (bool[i]==false)
           {
               boolFlag=false;
           }
       }
       //console.log(bool);
       if(boolFlag==true){
           //if valid check pass, update outputarea.
           for(var i=0;i<qz.outputVarsInputElement.length;i++)
           {
               console.log('i am in the update for loop');
               qz.outputVarsInputElement[i].value=qz.inputVarsInputElement[i].value;
           }
           //update finishes
            fite.setisInputValidFlagToBeTrue();
            fite.checkValid();
           }
        if(boolFlag==false){
            fite.setisInputValidFlagToBeFalse();
            fite.checkValid();
        }   
                        }
                        
                        
   //get the program text at run time, then call clickStart method.
       function getProgramText(){
           var programText:string;
           programText=qz.code;
         for(var i=0;i<qz.inputVarsInputElement.length;i++)
         {  
             var replace = qz.inputVarsInputElement[i].value;
             programText=programText.replace(/\$.*?\$\s?/,replace);
          }
          programText=programText.replace(/\$.*?\$\s?/,'double '+qz.inputExpressionInputElement[0].value);  

          //singleton's set method
          Singleton.singleton.getSingleton().setProgramText(programText);
          fite.clickStart();
                                }
        


     
    



}