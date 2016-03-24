/// <reference path="../library/jquery.d.ts" />
///<reference path="../builder/quizbuilder.ts" />
///<reference path="../state/State.ts" />
///<reference path="../src/concreteJSTM.ts" />
window.onload = function () {
    //fite, call the function to make the FITE question. 
    FITE();
    function FITE() {
        // iterate from 0-9, make 9 questions from description.
        for (var i = 0; i < 10; i++) {
            //console.log('FITE');
            console.log(i);
            //the only dependency is here, the selected Number that the client gives and the url.
            var selectedNumber = i;
            var url = 'description.json';
            var fetchFile = new quizBuilder.fetchFile(url, selectedNumber);
            //fetchFile.AJAX_JSON_Req();
            //fetchFile.setSelectedNumber(selectedNumber);
            //selectedQuestion are get here
            //var selectedQuestion =fetchFile.getselectedQuestion();
            //console.log(selectedQuestion);
            //data is selectedQuestion, a json onject    
            fetchFile.AJAX_JSON_Req()
                .done(function () {
                var selectedQuestion = fetchFile.getselectedQuestion();
                //declare quiz object
                var FITEQuestion;
                //instantiate director
                var QuizDirector = new quizBuilder.QuizDirector();
                //selectedQuestion are get here : is a json object
                var fitequizBuilder = new quizBuilder.FITEQuizQuizBuilder(selectedQuestion);
                //set director
                QuizDirector.setQuizBuilder(fitequizBuilder);
                //static method to instantiate a jstm , then instantiate fitecontroller
                jstm.concreteJSTM.createRTM()
                    .done(function (data) {
                    //
                    console.log(i);
                    //declar concreteJSTM object
                    var thisJSTM;
                    thisJSTM = data;
                    //call construct method
                    QuizDirector.constructQuiz();
                    //get quiz product 
                    FITEQuestion = QuizDirector.getQuiz();
                    //set thisJSTM reference to the instance variable of FITEQuestion
                    FITEQuestion.setConcreteJSTM(thisJSTM);
                    FITEQuestion.makeHTML();
                    var html = FITEQuestion.getHTML();
                    //console.log(html);
                    document.getElementById('FITE').appendChild(html);
                    //declare fite object
                    var fiteController;
                    fiteController = new State.FITEController(thisJSTM, FITEQuestion);
                    //set fiteController reference to the instance variable of FITEQuestion
                    FITEQuestion.setController(fiteController);
                    //
                    FITEQuestion.addeventListener();
                    //addEventListener
                    /**
                              function addeventListener(){
                                FITEQuestion.inputExpressionValue.addEventListener('input',ValidWatch,false);
                                for(var i=0;i<FITEQuestion.inputVarsValue.length;i++){
                                FITEQuestion.inputVarsValue[i].addEventListener('input',ValidWatch,false);
                                                                                        }
                                FITEQuestion.concreteJSTM.goForwardButton.addEventListener('click',goForward,false);
                                FITEQuestion.concreteJSTM.goBackButton.addEventListener('click',goBack,false);
                                FITEQuestion.aHref.addEventListener('click',close,false);
                                FITEQuestion.startButton.addEventListener('click',start,false);
                                                                                                 
                              }
                              **/
                    /**
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
                          FITEQuestion.fieldSet.style.display='none';
                          FITEQuestion.startButton.setAttribute('disabled','disabled');
                          //FITEQuestion.innerDiv2.innerHTML='';
                              }
                    **/
                })
                    .fail(function (data) { alert('error in the testForFactory'); });
            });
        }
    }
};
