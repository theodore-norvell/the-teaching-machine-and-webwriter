/// <reference path="../library/jquery.d.ts" />
///<reference path="../builder/quizbuilder.ts" />
///<reference path="../state/State.ts" />
///<reference path="../src/concreteJSTM.ts" />




window.onload=function(){
    
//fite, call the function to make the FITE question. 
FITCMD();















function FITCMD(){
// iterate from 0-9, make 9 questions from description.
for (var i=0;i<10;i++){
    
//console.log('FITE');
console.log(i);

//the only dependency is here, the selected Number that the client gives and the url.
var selectedNumber=i;
var url ='description.json';
var fetchFile = new quizBuilder.fetchFile(url,selectedNumber);    
//fetchFile.AJAX_JSON_Req();
//fetchFile.setSelectedNumber(selectedNumber);
//selectedQuestion are get here
//var selectedQuestion =fetchFile.getselectedQuestion();
//console.log(selectedQuestion);
//data is selectedQuestion, a json onject    

fetchFile.AJAX_JSON_Req()
.done(function(){  
    
var selectedQuestion=fetchFile.getselectedQuestion();
   
//declare quiz object
var FITCMDQuestion:quizBuilder.FITCMDQuestion;
//instantiate director
var QuizDirector:quizBuilder.QuizDirector = new quizBuilder.QuizDirector();
//selectedQuestion are get here : is a json object
var fitequizBuilder:quizBuilder.FITEQuizQuizBuilder = new quizBuilder.FITEQuizQuizBuilder(selectedQuestion);

//set director
QuizDirector.setQuizBuilder(fitequizBuilder);

//static method to instantiate a jstm , then instantiate fitecontroller
    jstm.concreteJSTM.createRTM()
    .done(function(data){
          //
          console.log(i);
          //declar concreteJSTM object
          var thisJSTM:jstm.JSTM;
          thisJSTM=data;
          //call construct method
          QuizDirector.constructQuiz();
          //get quiz product 
          FITCMDQuestion = QuizDirector.getQuiz();
          //set thisJSTM reference to the instance variable of FITEQuestion
          FITCMDQuestion.setConcreteJSTM(thisJSTM);
          FITCMDQuestion.makeHTML();
          var html =FITCMDQuestion.getHTML();
          //console.log(html);

          document.getElementById('FITCMD').appendChild(html);
          
          //declare fite object
          var fiteController:State.FITCMDController;
          fiteController= new State.FITCMDController(thisJSTM,FITCMDQuestion);
          
          
          //
          addeventListener();








//addEventListener
          function addeventListener(){
            FITCMDQuestion.inputExpressionValue.addEventListener('input',ValidWatch,false);
            for(var i=0;i<FITCMDQuestion.inputVarsValue.length;i++){
            FITCMDQuestion.inputVarsValue[i].addEventListener('input',ValidWatch,false);
                                                                    }   
            FITCMDQuestion.concreteJSTM.goForwardButton.addEventListener('click',goForward,false);
            FITCMDQuestion.concreteJSTM.goBackButton.addEventListener('click',goBack,false);
            FITCMDQuestion.aHref.addEventListener('click',close,false); 
            FITCMDQuestion.startButton.addEventListener('click',start,false);                                                        
                                                                             
          }
          
           function ValidWatch(){
                fiteController.ValidWatch();
                }
           function goForward(){
                fiteController.goForward();
                }
           function goBack(){
                fiteController.goBack();
                            }
           function start(){
                //fite.clickStart();
                console.log('i have start');
                fiteController.getProgramText();
                            }
           function close(){
                FITCMDQuestion.fieldSet.style.display='none';
                FITCMDQuestion.startButton.setAttribute('disabled','disabled');
                //FITCMDQuestion.innerDiv2.innerHTML='';
                    }
          
                     
                        })
          .fail(function(data){alert('error in the testForFactory');});

   })


}



}






}




