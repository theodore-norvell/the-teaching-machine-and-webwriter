/// <reference path="../library/jquery.d.ts" />
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var quizBuilder;
(function (quizBuilder) {
    //------------------------------
    //var tempGlobalvar:any;  
    //var tempIndex:any;
    //fat product---->quiz
    var Quiz = (function () {
        //expression:HTMLElement;
        function Quiz(category, name, language) {
            this.category = category;
            this.name = name;
            this.language = language;
        }
        //ajax get function with synchronous settings!!!!@false
        Quiz.AJAX_JSON_Req = function (url) {
            var AJAX_req = new XMLHttpRequest();
            //@false
            AJAX_req.open("GET", url, false);
            AJAX_req.setRequestHeader("Content-type", "application/json");
            AJAX_req.send();
            var response = JSON.parse(AJAX_req.response);
            Quiz.tempGlobalvar = response;
            //return response;
        };
        //transfer ajax response to array.
        Quiz.tansferarray = function (index) {
            //console.log(globalvar);
            for (var x in Quiz.tempGlobalvar[0]) {
                Quiz.index.push({ 'key': x, value: Quiz.tempGlobalvar[0][x] });
            }
            //Quiz.index=index;
            console.log(Quiz.index);
            /**
            for (var i=0;i<index.length;i++){
                console.log(index[i].value);
            }
            **/
        };
        Quiz.descript = function (index) {
            var dtd = $.Deferred();
            Quiz.tempIndex = index.value;
            document.getElementById('start').innerHTML = 'Start';
            document.getElementById('start').setAttribute('disabled', 'disabled');
            // console.log(tempIndex);
            console.log(Quiz.tempIndex.inputVars);
            dtd.resolve(Quiz.tempIndex);
            return dtd;
        };
        Quiz.prototype.setText = function (text) {
            this.text = text;
        };
        Quiz.prototype.setCode = function (code) {
            this.code = code;
        };
        Quiz.prototype.setInputVars = function (inputVars) {
            this.inputVars = inputVars;
        };
        Quiz.prototype.setOutputVars = function (outputVars) {
            this.outputVars = outputVars;
        };
        Quiz.index = [];
        return Quiz;
    })();
    quizBuilder.Quiz = Quiz;
    //anstract builder
    var QuizBuilder = (function () {
        function QuizBuilder(category, name, language) {
            this.category = category;
            this.name = name;
            this.language = language;
        }
        QuizBuilder.prototype.getQuiz = function () {
            return this.quiz;
        };
        QuizBuilder.prototype.createNewQuiz = function () {
            this.quiz = new Quiz(this.category, this.name, this.language);
        };
        return QuizBuilder;
    })();
    //builder
    var FITEQuizQuizBuilder = (function (_super) {
        __extends(FITEQuizQuizBuilder, _super);
        function FITEQuizQuizBuilder(category, name, language) {
            _super.call(this, category, name, language);
        }
        FITEQuizQuizBuilder.prototype.buildText = function () {
            this.quiz.setText(Quiz.tempIndex.text);
        };
        ;
        FITEQuizQuizBuilder.prototype.buildCode = function () {
            this.quiz.setCode(Quiz.tempIndex.code);
        };
        ;
        FITEQuizQuizBuilder.prototype.buildInputVars = function () {
            //console.log(tempIndex.inputVars);
            this.quiz.setInputVars(Quiz.tempIndex.inputVars);
        };
        ;
        FITEQuizQuizBuilder.prototype.buildoutputVars = function () {
            //console.log()
            this.quiz.setOutputVars(Quiz.tempIndex.outPutVars);
        };
        ;
        return FITEQuizQuizBuilder;
    })(QuizBuilder);
    quizBuilder.FITEQuizQuizBuilder = FITEQuizQuizBuilder;
    //director
    var QuizDirector = (function () {
        function QuizDirector() {
        }
        QuizDirector.prototype.setQuizBuilder = function (qb) {
            this.quizbuilder = qb;
        };
        QuizDirector.prototype.getQuiz = function () {
            return this.quizbuilder.getQuiz();
        };
        QuizDirector.prototype.constructQuiz = function () {
            this.quizbuilder.createNewQuiz();
            this.quizbuilder.buildText();
            this.quizbuilder.buildCode();
            this.quizbuilder.buildInputVars();
            this.quizbuilder.buildoutputVars();
        };
        return QuizDirector;
    })();
    quizBuilder.QuizDirector = QuizDirector;
})(quizBuilder || (quizBuilder = {}));
