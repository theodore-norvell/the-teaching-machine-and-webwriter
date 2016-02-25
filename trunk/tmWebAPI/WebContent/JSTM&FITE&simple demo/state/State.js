/// <reference path="concreteJSTM.ts" />
/// <reference path="../descriptquestion/description.ts" />
var State;
(function (State) {
    //export var test = '1';
    State.isInputValidFlag = false;
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
                fite.setCurrentState(FITE.startable);
            }
            else {
                console.log('input not valid');
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
                fite.setCurrentState(FITE.startable);
            }
        };
        //start button clicked event
        Startable.prototype.clickStart = function (fite) {
            //i wish to fetch the program& the filename from the web pages!
            State.filename = 'FITE.cpp';
            State.program = DESCRIP.cascadeProgram();
            console.log(State.program);
            fite.create();
            fite.setCurrentState(FITE.wait);
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
            State.filename = 'FITE.cpp';
            State.program = DESCRIP.cascadeProgram();
            fite.create();
            fite.setCurrentState(FITE.wait);
        };
        return Wait;
    })();
    //4
    var Started = (function () {
        function Started(name) {
            this.name = name;
        }
        return Started;
    })();
    var FITE = (function () {
        function FITE(concrete) {
            this.currentState = FITE.initialize;
            this.concrete = concrete;
        }
        //
        FITE.prototype.checkValid = function () {
            this.currentState.checkValid(this);
        };
        //
        FITE.prototype.clickStart = function (fite) {
            this.currentState.clickStart(fite);
        };
        FITE.prototype.setCurrentState = function (s) {
            this.currentState = s;
            console.log('current state is ' + this.currentState.name);
        };
        FITE.prototype.isInputValid = function () {
            return State.isInputValidFlag;
        };
        //11111111create  this is a promise's callback with 3 .done cascaded{which is the callback functions}
        FITE.prototype.create = function () {
            State.concrete = this.concrete;
            //call the loadString in the concrete JSTM        
            State.concrete.createRTM()
                .done(function (data) {
                console.log('createRTM callback for the resolve');
                console.log(data);
                var program = State.program;
                var filename = State.filename;
                var guid = jstm.json.parameter[0].guid;
                console.log(program + " " + filename + " " + guid);
                //call the loadString in the concrete JSTM
                State.concrete.loadString(program, filename, guid)
                    .done(function (data) {
                    console.log('loadString callback for the resolve');
                    console.log(data);
                    var guid = jstm.json.parameter[0].guid;
                    //'1' means, i want the response back from the server
                    var responseWantedFlag = '1';
                    //call the initialize in concrete JSTM
                    State.concrete.initialize(guid, responseWantedFlag)
                        .done(function (data) {
                        console.log('initialize callback for the resolve');
                        console.log(data);
                        document.getElementById('goFoward').removeAttribute('disabled');
                        document.getElementById('goBack').removeAttribute('disabled');
                        document.getElementById('panel').style.display = 'block';
                    })
                        .fail(function (data) {
                        document.getElementById('panel').style.display = 'none';
                        alert(jstm.json.parameter[0].reason);
                        console.log('fail');
                    });
                })
                    .fail(function (data) {
                    document.getElementById('panel').style.display = 'none';
                    alert(jstm.json.parameter[0].reason);
                    console.log('fail');
                });
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        //22222222loadString this method is not used in my test, because i need use create().done.done.done chain to make it work. so this methid is cascaded in the .done
        FITE.prototype.loadString = function () {
            var program = program;
            var filename = filename;
            var guid = jstm.json.parameter[0].guid;
            //call the loadString in the concrete JSTM
            State.concrete.loadString(program, filename, guid)
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
            var guid = jstm.json.parameter[0].guid;
            //'1' means, i want the response back from the server
            var responseWantedFlag = '1';
            //call the initialize in concrete JSTM
            State.concrete.initialize(guid, responseWantedFlag)
                .done(function (data) {
                console.log('initialize callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        FITE.prototype.goForward = function () {
            var guid = jstm.json.parameter[0].guid;
            State.concrete.goForward(guid)
                .done(function (data) {
                console.log('goForward callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
        FITE.prototype.goBack = function () {
            var guid = jstm.json.parameter[0].guid;
            State.concrete.goBack(guid)
                .done(function (data) {
                console.log('goBack callback for the resolve');
                console.log(data);
            })
                .fail(function (data) {
                console.log('fail');
            });
        };
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
