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
var QuizDirector:quizBuilder.FITCMDQuizDirector = new quizBuilder.FITCMDQuizDirector();
//selectedQuestion are get here : is a json object
var ficmdQuizBuilder:quizBuilder.FITCMDQuizQuizBuilder = new quizBuilder.FITCMDQuizQuizBuilder(selectedQuestion);

//set director
QuizDirector.setQuizBuilder(ficmdQuizBuilder);

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
          var ficmdController:State.FITCMDController;
          ficmdController= new State.FITCMDController(thisJSTM,FITCMDQuestion);
          
          //set fiteController reference to the instance variable of FITEQuestion
          FITCMDQuestion.setController(ficmdController);
          
          
          
           //add all the html element in the FICMDQuestion that are related with the user reaction to the event handler.
          FITCMDQuestion.addeventListener();

                        })
          .fail(function(data){alert('error in the testForFactory');});

   })


}



}






}




