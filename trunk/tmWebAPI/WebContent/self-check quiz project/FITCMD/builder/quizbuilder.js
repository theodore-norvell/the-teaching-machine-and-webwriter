/// <reference path="../library/jquery.d.ts" />
/// <reference path="../src/JSTM.ts" />
/// <reference path="../state/State.ts" />
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
    var fetchFile = (function () {
        function fetchFile(url, selectedNumber) {
            //this represents the whole json file in array;
            this.jsonFileArray = [];
            this.url = url;
            this.selectedNumber = selectedNumber;
        }
        /**
        //setter
        setSelectedNumber(selectedNumber:number):void{
            this.selectedNumber=selectedNumber;
        }
        //getter
        getSelectedNumber():number{
            return this.selectedNumber
        }
          **/
        //ajax get function with synchronous settings!!!!@false
        fetchFile.prototype.AJAX_JSON_Req = function () {
            var thisFetchFile = this;
            var dtd = $.Deferred();
            $.ajax({
                url: this.url,
                dataType: "json",
                type: "GET",
                async: false
            })
                .done(function (data) {
                thisFetchFile.jsonFile = data;
                //thisFetchFile.tansferarray();
                //thisFetchFile.descript();
                dtd.resolve();
            })
                .fail(function () { dtd.reject(this); console.log("error! "); });
            return dtd;
            /**
            var AJAX_req = new XMLHttpRequest();
            //@false
            AJAX_req.open( "GET", this.url, false );
            AJAX_req.setRequestHeader("Content-type", "application/json");
            AJAX_req.send();
            var response = JSON.parse( AJAX_req.response );
            //console.log(response);
            this.jsonFile=response;
            //return response;
            **/
        };
        //transfer ajax response to array.
        fetchFile.prototype.tansferarray = function () {
            //console.log(globalvar);
            for (var x in this.jsonFile[0]) {
                this.jsonFileArray.push({ 'key': x, value: this.jsonFile[0][x] });
            }
            //Quiz.index=index;
            //console.log(this.jsonFileArray);
            /**
            for (var i=0;i<index.length;i++){
                console.log(index[i].value);
            }
            **/
        };
        fetchFile.prototype.descript = function () {
            var dtd = $.Deferred();
            this.selectedQuestion = this.jsonFileArray[this.selectedNumber].value;
            // console.log(tempIndex);
            //console.log(this.selectedQuestion.inputVars);
            dtd.resolve(this.selectedQuestion);
            return dtd;
        };
        //get question method
        fetchFile.prototype.getselectedQuestion = function () {
            //console.log(this.selectedNumber);
            //this.AJAX_JSON_Req();
            this.tansferarray();
            this.descript();
            return this.selectedQuestion;
        };
        return fetchFile;
    })();
    quizBuilder.fetchFile = fetchFile;
    //base class Question.
    var Questiion = (function () {
        function Questiion(selectedQuestion) {
            this.selectedQuestion = selectedQuestion;
        }
        Questiion.prototype.setCategory = function (category) {
            this.category = category;
        };
        Questiion.prototype.setName = function (name) {
            this.name = name;
        };
        Questiion.prototype.setLanguage = function (language) {
            this.language = language;
        };
        Questiion.prototype.setText = function (text) {
            this.text = text;
        };
        Questiion.prototype.setInputVars = function (inputVars) {
            this.inputVars = inputVars;
        };
        // set the JSTM reference to the local instance of FITEQuestion
        Questiion.prototype.setConcreteJSTM = function (ThisJSTM) {
            this.concreteJSTM = ThisJSTM;
        };
        //get JSTM
        Questiion.prototype.getConcreteJSTM = function () {
            return this.concreteJSTM;
        };
        return Questiion;
    })();
    //fat product---->quiz
    var FITCMDQuestion = (function (_super) {
        __extends(FITCMDQuestion, _super);
        //
        //
        //outputVarsSpan = new Array<HTMLElement>();
        //outputVarsValue = new Array<HTMLElement>();
        function FITCMDQuestion(selectedQuestion) {
            _super.call(this, selectedQuestion);
            //<span> element for variables
            this.inputVarsSpan = new Array();
            //<input> element for variables
            this.inputVarsValue = new Array();
        }
        FITCMDQuestion.prototype.setFunctiondescription = function (functiondescription) {
            this.functiondescription = functiondescription;
        };
        FITCMDQuestion.prototype.setCodeFunction = function (codeFunction) {
            this.codeFunction = codeFunction;
        };
        FITCMDQuestion.prototype.setCodeMain = function (codeMain) {
            this.codeMain = codeMain;
        };
        //setter a reference
        FITCMDQuestion.prototype.setController = function (controller) {
            this.controller = controller;
        };
        //getter a reference
        FITCMDQuestion.prototype.getController = function () {
            return this.controller;
        };
        /////////////////////////////makeHTML
        //outer div <div id='question1'>
        FITCMDQuestion.prototype.makeDiv = function () {
            this.Div = document.createElement('div');
            this.Div.setAttribute('id', this.name);
            return this.Div;
        };
        //innerDiv1 <div id='question1=area1'>      
        FITCMDQuestion.prototype.makeInnerDiv1 = function () {
            this.innerDiv1 = document.createElement('div');
            this.innerDiv1.setAttribute('id', this.name + '-area1');
            return this.innerDiv1;
        };
        //     
        FITCMDQuestion.prototype.makeQuestionDisplay = function () {
            this.questionDisplay = document.createElement('p');
            this.questionDisplay.setAttribute('id', this.name + '-questionDisplay');
            this.questionDisplay.innerHTML = this.name + ":" + this.text;
            return this.questionDisplay;
        };
        //
        FITCMDQuestion.prototype.makeFunctionDisplay = function () {
            this.functionDisplay = document.createElement('p');
            this.functionDisplay.setAttribute('id', 'Function: ' + '-functionDisplay');
            this.functionDisplay.innerHTML = "Fill in the function::" + this.functiondescription;
            return this.functionDisplay;
        };
        //insertDiv1     
        FITCMDQuestion.prototype.makeInsertDiv1 = function () {
            this.insertDiv1 = document.createElement('div');
            this.insertDiv1.setAttribute('id', this.name + '-insertDiv1');
            this.insertDiv1.setAttribute('className', 'display-fill-in-div');
            return this.insertDiv1;
        };
        //      
        FITCMDQuestion.prototype.makeStartButton = function () {
            this.startButton = document.createElement('button');
            this.startButton.setAttribute('id', this.name + '-start');
            this.startButton.setAttribute('disabled', 'disabled');
            this.startButton.setAttribute('style', 'display:none');
            this.startButton.innerHTML = 'start';
            return this.startButton;
        };
        //innerDiv2 <div id='question-area2'>      
        FITCMDQuestion.prototype.makeDisplayDiv = function () {
            this.DisplayDiv = document.createElement('div');
            //this.DisplayDiv.setAttribute('className',"display-fill-in-div")
            return this.DisplayDiv;
        };
        //innerDiv2 <div id='question-area2'>      
        FITCMDQuestion.prototype.makeInnerDiv2 = function () {
            this.innerDiv2 = document.createElement('div');
            this.innerDiv2.setAttribute('id', this.name + '-area2');
            return this.innerDiv2;
        };
        //
        FITCMDQuestion.prototype.makeFieldSet = function () {
            this.fieldSet = document.createElement('fieldSet');
            this.fieldSet.setAttribute('id', this.name + 'panel');
            this.fieldSet.style.display = 'none';
            return this.fieldSet;
        };
        //      
        FITCMDQuestion.prototype.makeAHref = function () {
            this.aHref = document.createElement('a');
            this.aHref.setAttribute('id', this.name + '-close');
            this.aHref.setAttribute('href', 'javascript:void(0)');
            this.aHref.setAttribute('aria-label', 'Close Account Info Modal Box');
            this.aHref.setAttribute('style', 'float:right;');
            this.aHref.innerHTML = '&#x274C;';
            return this.aHref;
        };
        //#insertDiv2
        FITCMDQuestion.prototype.makeInsertDiv2 = function () {
            this.insertDiv2 = document.createElement('div');
            this.insertDiv2.setAttribute('id', this.name + '-insertDiv2');
            return this.insertDiv2;
        };
        //
        FITCMDQuestion.prototype.makeSpan = function () {
            this.span = document.createElement('span');
            this.span.innerHTML = '&nbsp';
            return this.span;
        };
        //
        /////////////////////make insertArea1 and 2
        FITCMDQuestion.prototype.setinputExpressionSpan = function () {
            this.inputExpressionSpan = document.createElement('span');
            this.inputExpressionSpan.innerHTML = 'fill in the function: ';
            return this.inputExpressionSpan;
        };
        FITCMDQuestion.prototype.setinputExpressionValue = function () {
            this.inputExpressionValue = document.createElement('textarea');
            this.inputExpressionValue.setAttribute('type', 'text');
            this.inputExpressionValue.setAttribute('value', '');
            //this.inputExpressionValue.setAttribute('className','display-fill-in-div');
            return this.inputExpressionValue;
        };
        FITCMDQuestion.prototype.setinputVarsSpan = function (inputVars) {
            // console.log(inputVars);
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsSpan[i] = document.createElement('span');
                //this.inputVarsElement[i].setAttribute('id','Z1');
                this.inputVarsSpan[i].innerHTML = "Initial Value of " + inputVars[i].name;
            }
            return this.inputVarsSpan;
        };
        //i will receive the inputVars object and return inputVarsInputElement;
        FITCMDQuestion.prototype.setinputVarsValue = function (inputVars) {
            var count = Object.keys(inputVars).length;
            for (var i = 0; i < count; i++) {
                this.inputVarsValue[i] = document.createElement('input');
                //this.inputVarsSpanElement[i].setAttribute('id','Z2');
                this.inputVarsValue[i].setAttribute('type', 'text');
                this.inputVarsValue[i].setAttribute('value', inputVars[i].defaultInitValue);
            }
            return this.inputVarsValue;
        };
        /**
       public setoutputVarsSpan(outPutVars:any){
        var count = Object.keys(outPutVars).length;
        for(var i=0;i<count;i++){
            this.outputVarsSpan[i]=document.createElement('span');
            this.outputVarsSpan[i].innerHTML = outPutVars[i].name;
         }
               return this.outputVarsSpan;
                                                    }
                                               
       public setoutputVarsValue(outPutVars:any){
         //console.log(outPutVars);
        var count = Object.keys(outPutVars).length;
        for(var i=0;i<count;i++)
        {
            this.outputVarsValue[i]=document.createElement('input');
            this.outputVarsValue[i].setAttribute('type','text');
            this.outputVarsValue[i].setAttribute('value',outPutVars[i].initValue);
        }
        return this.outputVarsValue;
         
         
                                                          }
            **/
        //constructDisplay area1!!!!
        // div parameter 
        FITCMDQuestion.prototype.constructInsertArea1 = function () {
            var fn = this.insertDiv1.firstChild;
            while (fn) {
                this.insertDiv1.removeChild(fn);
                fn = this.insertDiv1.firstChild;
            }
            this.insertDiv1.appendChild(this.inputExpressionSpan);
            this.insertDiv1.appendChild(this.inputExpressionValue);
            this.insertDiv1.appendChild(document.createElement('br'));
            for (var j = 0; j < this.inputVarsValue.length; j++) {
                this.insertDiv1.appendChild(this.inputVarsSpan[j]);
                this.insertDiv1.appendChild(this.inputVarsValue[j]);
                this.insertDiv1.appendChild(document.createElement('br'));
            }
        };
        FITCMDQuestion.prototype.makeHTML = function () {
            //make the sturcture
            this.makeDiv();
            this.makeInnerDiv1();
            this.makeQuestionDisplay();
            this.makeFunctionDisplay();
            this.makeInsertDiv1();
            this.makeStartButton();
            this.makeDisplayDiv();
            this.makeInnerDiv2();
            this.makeFieldSet();
            var goForwardButton = this.concreteJSTM.makeGoForwardButton();
            var goBackButton = this.concreteJSTM.makeGoBackButton();
            var expressionDisplay = this.concreteJSTM.makeExpressionDisplay();
            this.makeAHref();
            this.makeInsertDiv2();
            this.makeSpan();
            //this.makeBr();
            //the span/input element in the insertDiv1 and insertDiv2
            this.setinputExpressionSpan();
            this.setinputExpressionValue();
            this.setinputVarsSpan(this.selectedQuestion.inputVars);
            this.setinputVarsValue(this.selectedQuestion.inputVars);
            //this.setoutputVarsSpan(this.selectedQuestion.outPutVars);
            //this.setoutputVarsValue(this.selectedQuestion.outPutVars)
            //
            this.Div.appendChild(this.innerDiv1);
            this.Div.appendChild(this.DisplayDiv);
            this.Div.appendChild(this.innerDiv2);
            this.innerDiv1.appendChild(this.questionDisplay);
            this.innerDiv1.appendChild(this.functionDisplay);
            this.innerDiv1.appendChild(this.insertDiv1);
            this.innerDiv1.appendChild(this.startButton);
            this.innerDiv2.appendChild(this.fieldSet);
            this.fieldSet.appendChild(goForwardButton);
            this.fieldSet.appendChild(goBackButton);
            this.fieldSet.appendChild(this.span);
            this.fieldSet.appendChild(this.span);
            this.fieldSet.appendChild(expressionDisplay);
            this.fieldSet.appendChild(this.span);
            this.fieldSet.appendChild(this.aHref);
            this.fieldSet.appendChild(document.createElement('br'));
            this.fieldSet.appendChild(document.createElement('br'));
            this.fieldSet.appendChild(this.insertDiv2);
            //
            this.constructInsertArea1();
        };
        FITCMDQuestion.prototype.getHTML = function () {
            return this.Div;
        };
        FITCMDQuestion.prototype.addeventListener = function () {
            var _this = this;
            //lambda expression to hold the context this/controller
            var handler1 = function (e) { _this.controller.ValidWatch(); };
            this.inputExpressionValue.addEventListener('input', handler1, false);
            for (var i = 0; i < this.inputVarsValue.length; i++) {
                this.inputVarsValue[i].addEventListener('input', handler1, false);
            }
            //lambda expression to hold the context this/controller                                                       
            var handler2 = function (e) { _this.controller.goForward(); };
            this.concreteJSTM.goForwardButton.addEventListener('click', handler2, false);
            //lambda expression to hold the context this/controller
            var handler3 = function (e) { _this.controller.goBack(); };
            this.concreteJSTM.goBackButton.addEventListener('click', handler3, false);
            //lambda expression to hold the context this/controller
            var handler4 = function (e) { _this.controller.close(); };
            this.aHref.addEventListener('click', handler4, false);
            //lambda expression to hold the context this/controller
            var handler5 = function (e) { _this.controller.start(); };
            this.startButton.addEventListener('click', handler5, false);
        };
        return FITCMDQuestion;
    })(Questiion);
    quizBuilder.FITCMDQuestion = FITCMDQuestion;
    //anstract builder
    var QuizBuilder = (function () {
        function QuizBuilder(selectedQuestion) {
            this.selectedQuestion = selectedQuestion;
        }
        QuizBuilder.prototype.getQuiz = function () {
            return this.quiz;
        };
        QuizBuilder.prototype.createNewQuiz = function () {
            this.quiz = new FITCMDQuestion(this.selectedQuestion);
        };
        return QuizBuilder;
    })();
    //builder
    var FITCMDQuizQuizBuilder = (function (_super) {
        __extends(FITCMDQuizQuizBuilder, _super);
        function FITCMDQuizQuizBuilder(selectedQuestion) {
            _super.call(this, selectedQuestion);
        }
        FITCMDQuizQuizBuilder.prototype.buildCategory = function () {
            this.quiz.setCategory(this.selectedQuestion.category);
        };
        FITCMDQuizQuizBuilder.prototype.buildName = function () {
            this.quiz.setName(this.selectedQuestion.name);
        };
        FITCMDQuizQuizBuilder.prototype.buildLanguage = function () {
            this.quiz.setLanguage(this.selectedQuestion.language);
        };
        FITCMDQuizQuizBuilder.prototype.buildText = function () {
            this.quiz.setText(this.selectedQuestion.text);
            this.quiz.setFunctiondescription(this.selectedQuestion.functiondescription);
        };
        FITCMDQuizQuizBuilder.prototype.buildCode = function () {
            this.quiz.setCodeFunction(this.selectedQuestion.codeFunction);
            this.quiz.setCodeMain(this.selectedQuestion.codeMain);
        };
        FITCMDQuizQuizBuilder.prototype.buildInputVars = function () {
            //console.log(tempIndex.inputVars);
            this.quiz.setInputVars(this.selectedQuestion.inputVars);
        };
        return FITCMDQuizQuizBuilder;
    })(QuizBuilder);
    quizBuilder.FITCMDQuizQuizBuilder = FITCMDQuizQuizBuilder;
    //director
    var FITCMDQuizDirector = (function () {
        function FITCMDQuizDirector() {
        }
        FITCMDQuizDirector.prototype.setQuizBuilder = function (qb) {
            this.quizbuilder = qb;
        };
        FITCMDQuizDirector.prototype.getQuiz = function () {
            return this.quizbuilder.getQuiz();
        };
        //take a selectedQuestion as parameter,which is an json object
        FITCMDQuizDirector.prototype.constructQuiz = function () {
            this.quizbuilder.createNewQuiz();
            this.quizbuilder.buildCategory();
            this.quizbuilder.buildName();
            this.quizbuilder.buildLanguage();
            this.quizbuilder.buildText();
            this.quizbuilder.buildCode();
            this.quizbuilder.buildInputVars();
        };
        return FITCMDQuizDirector;
    })();
    quizBuilder.FITCMDQuizDirector = FITCMDQuizDirector;
})(quizBuilder || (quizBuilder = {}));
