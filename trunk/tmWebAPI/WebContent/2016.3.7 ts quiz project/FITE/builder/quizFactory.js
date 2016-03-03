/// <reference path="../library/jquery.d.ts" />
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var uizBuilder;
(function (uizBuilder) {
    //ajax get function with synchronous settings!!!!@false
    function AJAX_JSON_Req(url) {
        var AJAX_req = new XMLHttpRequest();
        //@false
        AJAX_req.open("GET", url, false);
        AJAX_req.setRequestHeader("Content-type", "application/json");
        AJAX_req.send();
        var response = JSON.parse(AJAX_req.response);
        uizBuilder.tempGlobalvar = response;
        //return response;
    }
    uizBuilder.AJAX_JSON_Req = AJAX_JSON_Req;
    //transfer ajax response to array.
    function tansferarray(index) {
        //console.log(globalvar);
        for (var x in uizBuilder.tempGlobalvar[0]) {
            index.push({ 'key': x, value: uizBuilder.tempGlobalvar[0][x] });
        }
        return index;
        /**
        for (var i=0;i<index.length;i++){
            console.log(index[i].value);
        }
        **/
    }
    uizBuilder.tansferarray = tansferarray;
    function descript(index) {
        uizBuilder.tempIndex = index;
        this.defaultXY(index);
        this.makeHTMLinputVars(index);
        this.makeHTMLoutputVars(index);
        document.getElementById('start').innerHTML = 'Start';
        document.getElementById('start').setAttribute('disabled', 'disabled');
    }
    uizBuilder.descript = descript;
    //-----------------------------
    var quizobject2 = {
        "category": "fillInTheExpression",
        "name": "Question 2",
        "language": "c++",
        "text": "write an to calculate x+y+z",
        "code": "int void main(){\n double x=$x$; \n double y=$y$; \n double z=$z$; \n $exp$; \n return 0; \n }",
        "inputVars": [{ "name": "x", "defaultInitValue": 21.0 },
            { "name": "y", "defaultInitValue": 20.0 },
            { "name": "z", "defaultInitValue": 30.0 }],
        "outPutVars": [{ "name": "a", "initValue": 0.0 },
            { "name": "b", "initValue": 1.0 }]
    };
    //product
    var Quiz = (function () {
        function Quiz(category, name, language) {
            this.inputVarsSpanElement = new Array();
            this.inputVarsInputElement = new Array();
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
        // i will receive the inputVars object and return inputVarsSpan;
        Quiz.prototype.setinputVarsSpanElement = function (inputVars) {
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
        Quiz.prototype.setExpression = function () {
            document.getElementById('E1').innerHTML = 'expression:';
            //cast to html element(<HTML>)
            document.getElementById('E2').setAttribute('value', '');
        };
        Quiz.prototype.setOutputVars = function (outputVars) {
            this.outputVars = outputVars;
        };
        return Quiz;
    })();
    uizBuilder.Quiz = Quiz;
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
            this.quiz.setText(DESCRIP.tempIndex.text);
        };
        ;
        FITEQuiz.prototype.buildCode = function () {
            this.quiz.setCode(DESCRIP.tempIndex.code);
        };
        ;
        FITEQuiz.prototype.buildInputVars = function () {
            this.quiz.setinputVarsSpanElement(DESCRIP.tempIndex.inputVars);
            this.quiz.setinputVarsinputElement(DESCRIP.tempIndex.inputVars);
            this.quiz.setExpression();
        };
        ;
        FITEQuiz.prototype.buildoutPutVars = function () {
            this.quiz.setOutputVars(DESCRIP.tempIndex.outPutVars);
        };
        ;
        return FITEQuiz;
    })(QuizBuilder);
    uizBuilder.FITEQuiz = FITEQuiz;
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
            this.quizbuilder.buildoutPutVars();
        };
        return QuizDirector;
    })();
    uizBuilder.QuizDirector = QuizDirector;
    window.onload = function go() {
        //instantiate director
        var qd = new QuizDirector();
        //instantiate concrete builder FITEQuiz
        var fitequiz = new FITEQuiz(quizobject2.category, quizobject2.name, quizobject2.language);
        //desclar quiz product
        var qz;
        //
        qd.setQuizBuilder(fitequiz);
        qd.constructQuiz();
        qz = qd.getQuiz();
        // console.log(qz);
        console.log(qz.inputVarsInputElement.length);
        for (var i = 0; i < qz.inputVarsInputElement.length; i++) {
            $('#insert1').append(qz.inputVarsSpanElement[i]);
            $('#insert1').append(qz.inputVarsInputElement[i]);
            $('#insert1').append("<br></br>");
        }
        /**
       // console.log(qz.category+"/"+qz.name+"/"+qz.language+"/"+qz.code+"/"+qz.inputVars +"/"+qz.outputVars+"/"+qz.text+"is completed and ready");
        console.log(qz.inputVarsSpanElement);
        console.log(qz.inputVarsInputElement);
        //console.log(qz.inputVars[0].name);
        //console.log(Object.keys(qz.inputVars).length);
        console.log(qz.outputVars);
        document.body.appendChild(qz.inputVarsSpanElement[0]);
        document.body.appendChild(qz.inputVarsInputElement[0]);
        
        
        **/
    };
})(uizBuilder || (uizBuilder = {}));
