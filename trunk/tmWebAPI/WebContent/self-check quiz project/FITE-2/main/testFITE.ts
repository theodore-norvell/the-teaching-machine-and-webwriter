/// <reference path="../library/jquery.d.ts" />
///<reference path="../builder/quizbuilder.ts" />
///<reference path="../state/State.ts" />
///<reference path="../src/concreteJSTM.ts" />




window.onload=function(){
    
//fite
FITE();



function FITE(){
        console.log('FITE');
//document.getElementById('divquestions').addEventListener('change',FITEType,false);

   //call ajax function to read the whole json file and then store in a var called globalvar;
     //1
     quizBuilder.Quiz.AJAX_JSON_Req ('description.json');
 
     //2 push to index
    // var index = [];
    // var tempQuiz; 
     quizBuilder.Quiz.tansferarray(quizBuilder.Quiz.index);
     
     //3 
     var select = document.getElementById('questionslist'); 
     select.addEventListener('change',selec,false);

     //declare quiz object
     var qz:quizBuilder.Quiz;
     //declare fite object
     var fiteController:State.FITE;
     //declar concreteJSTM object
     var concretejstm:jstm.JSTM;
     
     function selec() {
     var i =this.selectedIndex;  
      //console.log(index[i].value.inputVars.length);
      //console.log(index[i]);
      quizBuilder.Quiz.descript(quizBuilder.Quiz.index[i])
  .done(function(data:any){
      //tempQuiz=data;
    // console.log(data);
    //instantiate director
   var qd:quizBuilder.QuizDirector = new quizBuilder.QuizDirector();
   //instantiate concrete builder FITEQuiz
   var fitequizBuilder:quizBuilder.FITEQuizQuizBuilder = new quizBuilder.FITEQuizQuizBuilder(data.category,data.name,data.language);

   //set director
   qd.setQuizBuilder(fitequizBuilder);
   //cal construct method
   qd.constructQuiz();
   //het quiz product 
   qz = qd.getQuiz();

   //static method to instantiate a jstm , then instantiate fitecontroller
    jstm.concreteJSTM.createRTM()
    .done(function(data){
          fiteController= new State.FITE(data,qz);
          console.log(data);
          console.log(fiteController.concrete);
          //fite.checkValid();
          concretejstm=data;
          //set concretejstm hold reference to the Quiz object
         // concretejstm.setQuiz(qz);
          //construct html element;
          concretejstm.constructHTMLElement();
          //add element into #insert1
          concretejstm.constructInsertArea1();
          //add element into #insert2
          concretejstm.constructInsertArea2();
        
           concretejstm.inputExpressionInputElement.addEventListener('input',ValidWatch,false);
          for(var i=0;i<concretejstm.inputVarsInputElement.length;i++){
            concretejstm.inputVarsInputElement[i].addEventListener('input',ValidWatch,false);
                                                                    }  
    
    

 
     })
     .fail(function(data){alert('error in the testForFactory');});

   
   
     //done
       }); 
  //selec function
        }  
      
      
        function ValidWatch(){
            fiteController.ValidWatch();
            
        }
        


    var startButton = document.getElementById('start'); 
    startButton.addEventListener('click',start,false); 
    
    function start(){
    //fite.clickStart();
    console.log('i have start');
    fiteController.getProgramText();
    }
    
    var goForwardButton = document.getElementById('goFoward');
    var goBackButton = document.getElementById('goBack');
    var closeButton = document.getElementById('closebtn');
    goForwardButton.addEventListener('click',goForward,false);
    goBackButton.addEventListener('click',goBack,false);
    closeButton.addEventListener('click',close,false);
    
    function goForward(){
        fiteController.goForward();
    }
    function goBack(){
        fiteController.goBack();
    }
    
    function close(){
        document.getElementById('panel').style.display='none';
        startButton.setAttribute('disabled','disabled');
        document.getElementById('expressioninpanel').innerHTML='';
    }

}









}




