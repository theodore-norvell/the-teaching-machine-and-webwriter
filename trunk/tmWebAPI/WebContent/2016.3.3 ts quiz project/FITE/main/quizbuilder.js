/// <reference path="../library/jquery.d.ts" />
/// <reference path="../singleton/singleton.ts" />
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var quizBuilder;
(function (quizBuilder) {
    //------------------------------
    var tempGlobalvar;
    var tempIndex;
    //ajax get function with synchronous settings!!!!@false
    function AJAX_JSON_Req(url) {
        var AJAX_req = new XMLHttpRequest();
        //@false
        AJAX_req.open("GET", url, false);
        AJAX_req.setRequestHeader("Content-type", "application/json");
        AJAX_req.send();
        var response = JSON.parse(AJAX_req.response);
        tempGlobalvar = response;
        //return response;
    }
    quizBuilder.AJAX_JSON_Req = AJAX_JSON_Req;
    //transfer ajax response to array.
    function tansferarray(index) {
        //console.log(globalvar);
        for (var x in tempGlobalvar[0]) {
            index.push({ 'key': x, value: tempGlobalvar[0][x] });
        }
        return index;
        /**
        for (var i=0;i<index.length;i++){
            console.log(index[i].value);
        }
        **/
    }
    quizBuilder.tansferarray = tansferarray;
    function descript(index) {
        var dtd = $.Deferred();
        tempIndex = index.value;
        document.getElementById('start').innerHTML = 'Start';
        document.getElementById('start').setAttribute('disabled', 'disabled');
        // console.log(tempIndex);
        console.log(tempIndex.inputVars);
        dtd.resolve(tempIndex);
        return dtd;
    }
    quizBuilder.descript = descript;
    //fat product---->quiz
    var Quiz = (function () {
        function Quiz(category, name, language) {
            this.questionDisplay = new Array();
            //
            this.inputExpressionSpanElement = new Array();
            this.inputExpressionInputElement = new Array();
            this.inputVarsSpanElement = new Array();
            this.inputVarsInputElement = new Array();
            //
            this.outputVarsSpanElement = new Array();
            this.outputVarsInputElement = new Array();
            this.outputVariableSpanElement = new Array();
            this.outputVariableinputElement = new Array();
            this.category = category;
            this.name = name;
            this.language = language;
        }
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
        // i will receive the inputVars object and return inputVarsSpan;
        Quiz.prototype.setquestionDisplay = function (tempIndex) {
            this.questionDisplay[0] = document.getElementById('questionDisplay');
            this.questionDisplay[0].innerHTML = tempIndex.name + " : " + tempIndex.text;
        };
        Quiz.prototype.setInputExpressionSpanElement = function () {
            this.inputExpressionSpanElement[0] = document.createElement('span');
            this.inputExpressionSpanElement[0].innerHTML = 'expression: ';
            return this.inputExpressionSpanElement[0];
        };
        Quiz.prototype.setinputExpressionInputElement = function () {
            this.inputExpressionInputElement[0] = document.createElement('input');
            this.inputExpressionInputElement[0].setAttribute('type', 'text');
            this.inputExpressionInputElement[0].setAttribute('value', '');
            return this.inputExpressionInputElement[0];
        };
        Quiz.prototype.setinputVarsSpanElement = function (inputVars) {
            // console.log(inputVars);
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsSpanElement[i] = document.createElement('span');
                //this.inputVarsElement[i].setAttribute('id','Z1');
                this.inputVarsSpanElement[i].innerHTML = "Initial Value of " + inputVars[i].name;
            }
            return this.inputVarsSpanElement;
        };
        //i will receive the inputVars object and return inputVarsInputElement;
        Quiz.prototype.setinputVarsinputElement = function (inputVars) {
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsInputElement[i] = document.createElement('input');
                //this.inputVarsSpanElement[i].setAttribute('id','Z2');
                this.inputVarsInputElement[i].setAttribute('type', 'text');
                this.inputVarsInputElement[i].setAttribute('value', inputVars[i].defaultInitValue);
            }
            return this.inputVarsInputElement;
        };
        Quiz.prototype.setoutputVarsSpanElement = function (inputVars) {
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.outputVarsSpanElement[i] = document.createElement('span');
                this.outputVarsSpanElement[i].innerHTML = inputVars[i].name;
            }
            return this.outputVarsSpanElement;
        };
        Quiz.prototype.setoutputVarsInputElement = function (inputVars) {
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.outputVarsInputElement[i] = document.createElement('input');
                this.outputVarsInputElement[i].setAttribute('type', 'text');
                this.outputVarsInputElement[i].setAttribute('value', inputVars[i].defaultInitValue);
            }
            return this.outputVarsInputElement;
        };
        Quiz.prototype.setoutputVariableSpanElement = function (outPutVars) {
            var count = Object.keys(outPutVars).length;
            for (var i = 0; i < count; i++) {
                this.outputVariableSpanElement[i] = document.createElement('span');
                this.outputVariableSpanElement[i].innerHTML = outPutVars[i].name;
            }
            return this.outputVariableSpanElement;
        };
        Quiz.prototype.setoutputVariableinputElement = function (outPutVars) {
            //console.log(outPutVars);
            var count = Object.keys(outPutVars).length;
            for (var i = 0; i < count; i++) {
                this.outputVariableinputElement[i] = document.createElement('input');
                this.outputVariableinputElement[i].setAttribute('type', 'text');
                this.outputVariableinputElement[i].setAttribute('value', outPutVars[i].initValue);
            }
            return this.outputVariableinputElement;
        };
        //constructDisplay area1!!!!
        Quiz.prototype.constructInertArea1 = function () {
            $('#insert1').empty();
            $('#insert1').append(this.inputExpressionSpanElement);
            $('#insert1').append(this.inputExpressionInputElement);
            $('#insert1').append('<br></br>');
            for (var j = 0; j < this.inputVarsInputElement.length; j++) {
                $('#insert1').append(this.inputVarsSpanElement[j]);
                $('#insert1').append(this.inputVarsInputElement[j]);
                $('#insert1').append("<br></br>");
            }
        };
        Quiz.prototype.constructInsertArea2 = function () {
            $('#insert2').empty();
            for (var j = 0; j < this.outputVarsInputElement.length; j++) {
                $('#insert2').append(this.outputVarsSpanElement[j]);
                $('#insert2').append(this.outputVarsInputElement[j]);
                $('#insert2').append("<br></br>");
            }
            for (var j = 0; j < this.outputVariableinputElement.length; j++) {
                $('#insert2').append(this.outputVariableSpanElement[j]);
                $('#insert2').append(this.outputVariableinputElement[j]);
                $('#insert2').append("<br></br>");
            }
        };
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
    var FITEQuiz = (function (_super) {
        __extends(FITEQuiz, _super);
        function FITEQuiz(category, name, language) {
            _super.call(this, category, name, language);
        }
        FITEQuiz.prototype.buildText = function () {
            this.quiz.setText(tempIndex.text);
        };
        ;
        FITEQuiz.prototype.buildCode = function () {
            this.quiz.setCode(tempIndex.code);
        };
        ;
        FITEQuiz.prototype.buildInputVars = function () {
            //console.log(tempIndex.inputVars);
            this.quiz.setquestionDisplay(tempIndex);
            this.quiz.setInputVars(tempIndex.inputVars);
            this.quiz.setInputExpressionSpanElement();
            this.quiz.setinputExpressionInputElement();
            this.quiz.setinputVarsSpanElement(tempIndex.inputVars);
            this.quiz.setinputVarsinputElement(tempIndex.inputVars);
        };
        ;
        FITEQuiz.prototype.buildoutputVars = function () {
            //console.log()
            this.quiz.setOutputVars(tempIndex.outPutVars);
            this.quiz.setoutputVarsSpanElement(tempIndex.inputVars);
            this.quiz.setoutputVarsInputElement(tempIndex.inputVars);
            this.quiz.setoutputVariableSpanElement(tempIndex.outPutVars);
            this.quiz.setoutputVariableinputElement(tempIndex.outPutVars);
        };
        ;
        return FITEQuiz;
    })(QuizBuilder);
    quizBuilder.FITEQuiz = FITEQuiz;
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
