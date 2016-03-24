/// <reference path="../src/concreteJSTM.ts" />
/// <reference path="../src/JSTM.ts" />
var State;
(function (State) {
    //export var programTemp=null;
    //var isInputValidFlag=false;
    //1,2,3,4
    var FITEState = {
        initialize: 'initialize',
        startable: 'startable',
        wait: 'wait',
        started: 'started'
    };
    //1
    var Initialize = (function () {
        function Initialize(name) {
            this.name = name;
        }
        Initialize.prototype.checkValid = function (fite) {
            if (fite.isInputValid()) {
                console.log('input valid');
                fite.qz.startButton.removeAttribute('disabled');
                fite.qz.startButton.style.display = 'block';
                fite.setCurrentState(FITEController.startable);
            }
            else {
                console.log('input not valid');
                fite.qz.startButton.setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITEController.initialize);
            }
        };
        return Initialize;
    })();
    //2
    var Startable = (function () {
        function Startable(name) {
            this.name = name;
        }
        //rechecked 
        Startable.prototype.checkValid = function (fite) {
            if (fite.isInputValid()) {
                console.log('input valid');
                fite.qz.startButton.removeAttribute('disabled');
                fite.setCurrentState(FITEController.startable);
            }
            else {
                console.log('input not valid');
                fite.qz.startButton.setAttribute('disabled', 'disabled');
                fite.concrete.goForwardButton.setAttribute('disabled', 'disabled');
                fite.concrete.goBackButton.setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITEController.initialize);
            }
        };
        //start button clicked event
        Startable.prototype.clickStart = function (fite) {
            //i wish to fetch the program& the filename from the web pages!
            console.log('i am in startable . and i will trigger clickStart event');
            var filename = 'FITE.cpp';
            var program = fite.concrete.getprogramText();
            console.log(program);
            fite.setCurrentState(FITEController.wait);
            fite.loadStringAndInitialize(filename, program);
        };
        return Startable;
    })();
    //3
    var Wait = (function () {
        function Wait(name) {
            this.name = name;
        }
        //rechecked 
        Wait.prototype.checkValid = function (fite) {
            if (fite.isInputValid()) {
                console.log('input valid');
                fite.setCurrentState(FITEController.startable);
            }
        };
        Wait.prototype.clickStart = function (fite) {
            //i wish to fetch the program& the filename from the web pages!
            var filename = 'FITE.cpp';
            var program = fite.concrete.getprogramText();
            fite.loadStringAndInitialize(filename, program);
            fite.setCurrentState(FITEController.wait);
        };
        Wait.prototype.gotoStarted = function (fite) {
            console.log('i am the method gotoStatred in Wait state');
            fite.concrete.goForwardButton.removeAttribute('disabled');
            fite.concrete.goBackButton.removeAttribute('disabled');
            fite.qz.fieldSet.style.display = 'block';
            fite.setCurrentState(FITEController.started);
        };
        return Wait;
    })();
    //4
    var Started = (function () {
        function Started(name) {
            this.name = name;
        }
        //rechecked 
        Started.prototype.checkValid = function (fite) {
            if (fite.isInputValid()) {
                console.log('input valid');
                fite.qz.startButton.innerHTML = 'Restart';
                fite.qz.startButton.removeAttribute('disabled');
                fite.concrete.goForwardButton.setAttribute('disabled', 'disabled');
                fite.concrete.goBackButton.setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITEController.startable);
            }
            else {
                console.log('input not valid');
                fite.qz.startButton.setAttribute('disabled', 'disabled');
                fite.concrete.goForwardButton.setAttribute('disabled', 'disabled');
                fite.concrete.goBackButton.setAttribute('disabled', 'disabled');
                fite.qz.fieldSet.style.display = 'block';
                fite.setCurrentState(FITEController.initialize);
            }
        };
        Started.prototype.clickStart = function (fite) {
            alert('if you want to restart me again, please modify the input area again!');
            fite.qz.startButton.setAttribute('disabled', 'disabled');
            fite.setCurrentState(FITEController.started);
        };
        return Started;
    })();
    var FITEController = (function () {
        function FITEController(concrete, qz) {
            this.currentState = FITEController.initialize;
            this.concrete = concrete;
            this.qz = qz;
            this.isInputValidFlag = false;
        }
        //user check valid
        FITEController.prototype.checkValid = function () {
            this.currentState.checkValid(this);
        };
        //user click start , 
        FITEController.prototype.clickStart = function () {
            this.currentState.clickStart(this);
        };
        //gotoStarted
        FITEController.prototype.gotoStarted = function () {
            this.currentState.gotoStarted(this);
        };
        FITEController.prototype.setCurrentState = function (s) {
            this.currentState = s;
            console.log('current state is ' + this.currentState.name);
        };
        FITEController.prototype.isInputValid = function () {
            return this.isInputValidFlag;
        };
        FITEController.prototype.setisInputValidFlagToBeTrue = function () {
            this.isInputValidFlag = true;
        };
        FITEController.prototype.setisInputValidFlagToBeFalse = function () {
            this.isInputValidFlag = false;
        };
        //11111111create  this is a promise's callback with 3 .done cascaded{which is the callback functions}
        FITEController.prototype.loadStringAndInitialize = function (filename, program) {
            var thisFITE = this;
            var thisConcreteJSTM = this.concrete;
            //call the loadString in the concrete JSTM
            // i will change it       
            var program = program;
            //to it in the fill in the command question
            //program = "#include <iostream> \n double compare(double a,double b){ \n  if (a>b) \n return a; \n  else \n  return b; } \n int main(){ \n double x=1; \n double y=0; \n double sum=0; \n  x=compare(10.2,20.4); \n cout<<x;  \n  return 0;     } ";
            var filename = filename;
            console.log(program + " " + filename + " " + thisConcreteJSTM.guid);
            //call the loadString in the concrete JSTM
            thisConcreteJSTM.loadString(program, filename)
                .done(function (data) {
                console.log('loadString callback for the resolve');
                console.log(data);
                //'1' means, i want the response back from the server
                var responseWantedFlag = '1';
                //call the initialize in concrete JSTM
                thisConcreteJSTM.initialize(responseWantedFlag)
                    .done(function (data) {
                    console.log('initialize callback for the resolve');
                    console.log(data);
                    // DESCRIP.updateVariablesToPanelArea();
                    thisFITE.gotoStarted();
                })
                    .fail(function (data) {
                    thisFITE.qz.fieldSet.style.display = 'none';
                    alert(data.message);
                    console.log('fail');
                });
            })
                .fail(function (data) {
                thisFITE.qz.fieldSet.style.display = 'none';
                alert(data.message);
                console.log('fail');
            });
        };
        //22222222loadString this method is not used in my test, because i need use create().done.done.done chain to make it work. so this methid is cascaded in the .done
        FITEController.prototype.loadString = function () {
            var thisConcreteJSTM = this.concrete;
            var program = program;
            var filename = filename;
            //call the loadString in the concrete JSTM
            thisConcreteJSTM.loadString(program, filename)
                .done(function (data) {
                console.log('loadString callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        //333333333initialize this method is not used in my test, because i need use create().done.done.done chain to make it work.so this methid is cascaded in the .done
        FITEController.prototype.initialize = function () {
            var thisConcreteJSTM = this.concrete;
            //'1' means, i want the response back from the server
            var responseWantedFlag = '1';
            //call the initialize in concrete JSTM
            thisConcreteJSTM.initialize(responseWantedFlag)
                .done(function (data) {
                console.log('initialize callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        FITEController.prototype.goForward = function () {
            var thisConcreteJSTM = this.concrete;
            console.log(this);
            console.log(this.currentState);
            console.log(this.concrete);
            thisConcreteJSTM.goForward()
                .done(function (data) {
                console.log('goForward callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        FITEController.prototype.goBack = function () {
            var thisConcreteJSTM = this.concrete;
            thisConcreteJSTM.goBack()
                .done(function (data) {
                console.log('goBack callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        //-----------------------------------------------------
        //var concrete = new jstm.concreteJSTM('i will be send to FITE');
        // check the user - input validity at run time 
        FITEController.prototype.ValidWatch = function () {
            var boolFlag = true;
            var bool = new Array();
            console.log(this);
            for (var i = 0; i < this.qz.inputVarsValue.length; i++) {
                bool[i] = this.qz.inputVarsValue.value != '' && !isNaN(this.qz.inputVarsValue[i].value) && this.qz.inputVarsValue[i].value != '';
                if (bool[i] == false) {
                    boolFlag = false;
                }
            }
            //console.log(bool);
            if (boolFlag == true) {
                //if valid check pass, update outputarea.
                for (var i = 0; i < this.qz.outputMirrorValue.length; i++) {
                    console.log('i am in the update for loop');
                    this.qz.outputMirrorValue[i].value = this.qz.inputVarsValue[i].value;
                }
                //update finishes
                this.setisInputValidFlagToBeTrue();
                this.checkValid();
            }
            if (boolFlag == false) {
                this.setisInputValidFlagToBeFalse();
                this.checkValid();
            }
        };
        //get the program text at run time, then call clickStart method.
        FITEController.prototype.getProgramText = function () {
            var programText;
            programText = this.qz.code;
            for (var i = 0; i < this.qz.inputVarsValue.length; i++) {
                var replace = this.qz.inputVarsValue[i].value;
                programText = programText.replace(/\$.*?\$\s?/, replace);
            }
            programText = programText.replace(/\$.*?\$\s?/, 'double ' + this.qz.inputExpressionValue.value);
            //singleton's set method
            this.concrete.setprogramText(programText);
            this.clickStart();
        };
        FITEController.prototype.start = function () {
            //fite.clickStart();
            console.log('i have start');
            this.getProgramText();
        };
        FITEController.prototype.close = function () {
            this.qz.fieldSet.style.display = 'none';
            this.qz.startButton.setAttribute('disabled', 'disabled');
            //FITEQuestion.innerDiv2.innerHTML='';
        };
        /**
                private static instance:FITE = null;
                
                public static getInstance(){
                    if (FITE.instance==null){
                        FITE.instance= new FITE();
                        return FITE.instance;
                    }
                }
                **/
        FITEController.initialize = new Initialize(FITEState.initialize);
        FITEController.startable = new Startable(FITEState.startable);
        FITEController.wait = new Wait(FITEState.wait);
        FITEController.started = new Started(FITEState.started);
        return FITEController;
    })();
    State.FITEController = FITEController;
})(State || (State = {}));
/**
window.onload=function(){
//
var c= new State.FITE();

State.isInputValidFlag = true;
console.log('currentState is '+c.currentState.name);
c.checkValid();
c.clickStart();

}
**/ 
