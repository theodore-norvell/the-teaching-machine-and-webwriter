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
                document.getElementById('start').removeAttribute('disabled');
                fite.setCurrentState(FITE.startable);
            }
            else {
                console.log('input not valid');
                document.getElementById('start').setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITE.initialize);
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
                document.getElementById('start').removeAttribute('disabled');
                document.getElementById('start').style.display = 'block';
                fite.setCurrentState(FITE.startable);
            }
            else {
                console.log('input not valid');
                document.getElementById('start').setAttribute('disabled', 'disabled');
                document.getElementById('goFoward').setAttribute('disabled', 'disabled');
                document.getElementById('goBack').setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITE.initialize);
            }
        };
        //start button clicked event
        Startable.prototype.clickStart = function (fite) {
            //i wish to fetch the program& the filename from the web pages!
            console.log('i am in startable . and i will trigger clickStart event');
            var filename = 'FITE.cpp';
            var program = fite.concrete.getprogramText();
            console.log(program);
            fite.setCurrentState(FITE.wait);
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
                fite.setCurrentState(FITE.startable);
            }
        };
        Wait.prototype.clickStart = function (fite) {
            //i wish to fetch the program& the filename from the web pages!
            var filename = 'FITE.cpp';
            var program = fite.concrete.getprogramText();
            fite.loadStringAndInitialize(filename, program);
            fite.setCurrentState(FITE.wait);
        };
        Wait.prototype.gotoStarted = function (fite) {
            console.log('i am the method gotoStatred in Wait state');
            document.getElementById('goFoward').removeAttribute('disabled');
            document.getElementById('goBack').removeAttribute('disabled');
            document.getElementById('panel').style.display = 'block';
            fite.setCurrentState(FITE.started);
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
                document.getElementById('start').innerHTML = 'Restart';
                document.getElementById('start').removeAttribute('disabled');
                document.getElementById('goFoward').setAttribute('disabled', 'disabled');
                document.getElementById('goBack').setAttribute('disabled', 'disabled');
                fite.setCurrentState(FITE.startable);
            }
            else {
                console.log('input not valid');
                document.getElementById('start').setAttribute('disabled', 'disabled');
                document.getElementById('goFoward').setAttribute('disabled', 'disabled');
                document.getElementById('goBack').setAttribute('disabled', 'disabled');
                document.getElementById('panel').style.display = 'block';
                fite.setCurrentState(FITE.initialize);
            }
        };
        Started.prototype.clickStart = function (fite) {
            alert('if you want to restart me again, please modify the input area again!');
            document.getElementById('start').setAttribute('disabled', 'disabled');
            fite.setCurrentState(FITE.started);
        };
        return Started;
    })();
    var FITE = (function () {
        function FITE(concrete, qz) {
            this.currentState = FITE.initialize;
            this.concrete = concrete;
            this.qz = qz;
            this.isInputValidFlag = false;
        }
        //user check valid
        FITE.prototype.checkValid = function () {
            this.currentState.checkValid(this);
        };
        //user click start , 
        FITE.prototype.clickStart = function () {
            this.currentState.clickStart(this);
        };
        //gotoStarted
        FITE.prototype.gotoStarted = function () {
            this.currentState.gotoStarted(this);
        };
        FITE.prototype.setCurrentState = function (s) {
            this.currentState = s;
            console.log('current state is ' + this.currentState.name);
        };
        FITE.prototype.isInputValid = function () {
            return this.isInputValidFlag;
        };
        FITE.prototype.setisInputValidFlagToBeTrue = function () {
            this.isInputValidFlag = true;
        };
        FITE.prototype.setisInputValidFlagToBeFalse = function () {
            this.isInputValidFlag = false;
        };
        //11111111create  this is a promise's callback with 3 .done cascaded{which is the callback functions}
        FITE.prototype.loadStringAndInitialize = function (filename, program) {
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
                    document.getElementById('panel').style.display = 'none';
                    alert(data.message);
                    console.log('fail');
                });
            })
                .fail(function (data) {
                document.getElementById('panel').style.display = 'none';
                alert(data.message);
                console.log('fail');
            });
        };
        //22222222loadString this method is not used in my test, because i need use create().done.done.done chain to make it work. so this methid is cascaded in the .done
        FITE.prototype.loadString = function () {
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
        FITE.prototype.initialize = function () {
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
        FITE.prototype.goForward = function () {
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
        FITE.prototype.goBack = function () {
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
        FITE.prototype.ValidWatch = function () {
            var boolFlag = true;
            var bool = new Array();
            console.log(this);
            for (var i = 0; i < this.concrete.inputVarsInputElement.length; i++) {
                bool[i] = this.concrete.inputExpressionInputElement.value != '' && !isNaN(this.concrete.inputVarsInputElement[i].value) && this.concrete.inputVarsInputElement[i].value != '';
                if (bool[i] == false) {
                    boolFlag = false;
                }
            }
            //console.log(bool);
            if (boolFlag == true) {
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
        FITE.prototype.getProgramText = function () {
            var programText;
            var codeFunction = this.qz.codeFunction;
            var codeMain = this.qz.codeMain;
            for (var i = 0; i < this.concrete.inputVarsInputElement.length; i++) {
                var replace = this.concrete.inputVarsInputElement[i].value;
                codeMain = codeMain.replace(/\$.*?\$\s?/, replace);
            }
            codeFunction = codeFunction.replace(/\$.*?\$\s?/, this.concrete.inputExpressionInputElement.value);
            //cascade
            programText = codeFunction + codeMain;
            //singleton's set method
            this.concrete.setprogramText(programText);
            this.clickStart();
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
        FITE.initialize = new Initialize(FITEState.initialize);
        FITE.startable = new Startable(FITEState.startable);
        FITE.wait = new Wait(FITEState.wait);
        FITE.started = new Started(FITEState.started);
        return FITE;
    })();
    State.FITE = FITE;
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
