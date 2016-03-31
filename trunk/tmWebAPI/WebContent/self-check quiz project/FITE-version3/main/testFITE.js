/// <reference path="../library/jquery.d.ts" />
///<reference path="../builder/quizbuilder.ts" />
///<reference path="../../FITCMD/builder/quizbuilder.ts" />
///<reference path="../state/State.ts" />
///<reference path="../src/concreteJSTM.ts" />
window.onload = function () {
    //fite, call the function to make the FITE question. 
    FITE();
    FITCMD();
    //fite
    function FITE() {
        // iterate from 0-9, make 9 questions from description.
        for (var i = 0; i < 10; i++) {
            //console.log('FITE');
            console.log(i);
            //the only dependency is here, the selected Number that the client gives and the url.
            var selectedNumber = i;
            var url = 'fitedescription.json';
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
                var QuizDirector = new quizBuilder.FITEQuizDirector();
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
                    //add all the html element in the FITEQuestion that are related with the user reaction to the event handler.
                    FITEQuestion.addeventListener();
                })
                    .fail(function (data) { alert('error in the testForFactory'); });
            });
        }
    }
    //fitcmd
    function FITCMD() {
        // iterate from 0-9, make 9 questions from description.
        for (var i = 0; i < 10; i++) {
            //console.log('FITE');
            console.log(i);
            //the only dependency is here, the selected Number that the client gives and the url.
            var selectedNumber = i;
            var url = 'fitcmddescription.json';
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
                var FITCMDQuestion;
                //instantiate director
                var QuizDirector = new quizBuilder.FITCMDQuizDirector();
                //selectedQuestion are get here : is a json object
                var ficmdQuizBuilder = new quizBuilder.FITCMDQuizQuizBuilder(selectedQuestion);
                //set director
                QuizDirector.setQuizBuilder(ficmdQuizBuilder);
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
                    FITCMDQuestion = QuizDirector.getQuiz();
                    //set thisJSTM reference to the instance variable of FITEQuestion
                    FITCMDQuestion.setConcreteJSTM(thisJSTM);
                    FITCMDQuestion.makeHTML();
                    var html = FITCMDQuestion.getHTML();
                    //console.log(html);
                    document.getElementById('FITCMD').appendChild(html);
                    //declare fite object
                    var ficmdController;
                    ficmdController = new State.FITCMDController(thisJSTM, FITCMDQuestion);
                    //set fiteController reference to the instance variable of FITEQuestion
                    FITCMDQuestion.setController(ficmdController);
                    //add all the html element in the FICMDQuestion that are related with the user reaction to the event handler.
                    FITCMDQuestion.addeventListener();
                })
                    .fail(function (data) { alert('error in the testForFactory'); });
            });
        }
    }
};
