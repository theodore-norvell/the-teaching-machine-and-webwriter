/// <reference path="../library/jquery.d.ts" />
///<reference path="../state/State.ts" />
var DESCRIP;
(function (DESCRIP) {
    DESCRIP.watchHTMLElement = { "input": { 'length': null, "a": null, 'b': null, 'c': null, 'd': null },
        'output': { 'a': null },
        'expression': null };
    var program;
    function cascadeProgram() {
        switch (DESCRIP.watchHTMLElement.input.length) {
            case 2:
                var originalCode = DESCRIP.tempIndex.value.code;
                var replace1 = DESCRIP.watchHTMLElement.input.a.value;
                var firstTimeReplace = originalCode.replace(/\$.*?\$\s?/, replace1);
                var replace2 = DESCRIP.watchHTMLElement.input.b.value;
                var secondTimeReplace = firstTimeReplace.replace(/\$.*?\$\s?/, replace2);
                var replace3 = DESCRIP.watchHTMLElement.expression.value;
                program = secondTimeReplace.replace(/\$.*?\$\s?/, 'double ' + replace3);
                return program;
            case 3:
                var originalCode = DESCRIP.tempIndex.value.code;
                var replace1 = DESCRIP.watchHTMLElement.input.a.value;
                var firstTimeReplace = originalCode.replace(/\$.*?\$\s?/, replace1);
                var replace2 = DESCRIP.watchHTMLElement.input.b.value;
                var secondTimeReplace = firstTimeReplace.replace(/\$.*?\$\s?/, replace2);
                var replace3 = DESCRIP.watchHTMLElement.input.c.value;
                var thirdTimeReplace = secondTimeReplace.replace(/\$.*?\$\s?/, replace3);
                var replace4 = DESCRIP.watchHTMLElement.expression.value;
                program = thirdTimeReplace.replace(/\$.*?\$\s?/, 'double ' + replace4);
                return program;
            case 4:
                var originalCode = DESCRIP.tempIndex.value.code;
                var replace1 = DESCRIP.watchHTMLElement.input.a.value;
                var firstTimeReplace = originalCode.replace(/\$.*?\$\s?/, replace1);
                var replace2 = DESCRIP.watchHTMLElement.input.b.value;
                var secondTimeReplace = firstTimeReplace.replace(/\$.*?\$\s?/, replace2);
                var replace3 = DESCRIP.watchHTMLElement.input.c.value;
                var thirdTimeReplace = secondTimeReplace.replace(/\$.*?\$\s?/, replace3);
                var replace4 = DESCRIP.watchHTMLElement.input.d.value;
                var fourthTimeReplace = thirdTimeReplace.replace(/\$.*?\$\s?/, replace4);
                var replace5 = DESCRIP.watchHTMLElement.expression.value;
                program = program = fourthTimeReplace.replace(/\$.*?\$\s?/, 'double ' + replace5);
                return program;
        }
    }
    DESCRIP.cascadeProgram = cascadeProgram;
    var description = (function () {
        function description() {
        }
        //ajax get function with synchronous settings!!!!@false
        description.prototype.AJAX_JSON_Req = function (url) {
            var AJAX_req = new XMLHttpRequest();
            //@false
            AJAX_req.open("GET", url, false);
            AJAX_req.setRequestHeader("Content-type", "application/json");
            AJAX_req.send();
            var response = JSON.parse(AJAX_req.response);
            DESCRIP.tempGlobalvar = response;
            //return response;
        };
        //transfer ajax response to array.
        description.prototype.tansferarray = function (index) {
            //console.log(globalvar);
            for (var x in DESCRIP.tempGlobalvar[0]) {
                index.push({ 'key': x, value: DESCRIP.tempGlobalvar[0][x] });
            }
            return index;
            /**
            for (var i=0;i<index.length;i++){
                console.log(index[i].value);
            }
            **/
        };
        description.prototype.descript = function (index) {
            DESCRIP.tempIndex = index;
            this.defaultXY(index);
            this.makeHTMLinputVars(index);
            this.makeHTMLoutputVars(index);
            document.getElementById('start').innerHTML = 'Start';
            document.getElementById('start').setAttribute('disabled', 'disabled');
        };
        description.prototype.defaultXY = function (index) {
            document.getElementById('E1').innerHTML = 'expression:';
            document.getElementById('E2').value = '';
            document.getElementById('X1').innerHTML = "Initial Value of " + index.value.inputVars[0].name;
            document.getElementById('X2').value = index.value.inputVars[0].defaultInitValue;
            document.getElementById('Y1').innerHTML = "Initial Value of " + index.value.inputVars[1].name;
            document.getElementById('Y2').value = index.value.inputVars[1].defaultInitValue;
            //set the watchHTMLElement 
            DESCRIP.watchHTMLElement.input.length = index.value.inputVars.length;
            DESCRIP.watchHTMLElement.input.a = document.getElementById('X2');
            DESCRIP.watchHTMLElement.input.b = document.getElementById('Y2');
            DESCRIP.watchHTMLElement.expression = document.getElementById('E2');
            console.log(DESCRIP.watchHTMLElement);
        };
        description.prototype.makeHTMLinputVars = function (index) {
            $('#insert1').text("");
            switch (index.value.inputVars.length) {
                case 2: break;
                case 3:
                    var Z1 = document.createElement('span');
                    var Z2 = document.createElement('input');
                    var Z3 = document.createElement('span');
                    var Z4 = document.createElement('input');
                    Z3.setAttribute('id', 'Z3');
                    Z4.setAttribute('id', 'Z4');
                    Z1.setAttribute('id', 'Z1');
                    Z1.innerHTML = "Initial Value of " + index.value.inputVars[2].name;
                    Z2.setAttribute('id', 'Z2');
                    Z2.setAttribute('type', 'text');
                    Z2.value = index.value.inputVars[2].defaultInitValue;
                    $('#insert1').append(Z1);
                    $('#insert1').append(Z2);
                    //
                    DESCRIP.watchHTMLElement.input.c = Z2;
                    // console.log(watchHTMLElement);
                    break;
                case 4:
                    var Z1 = document.createElement('span');
                    var Z2 = document.createElement('input');
                    var Z3 = document.createElement('span');
                    var Z4 = document.createElement('input');
                    var H1 = document.createElement('span');
                    var H2 = document.createElement('input');
                    var H3 = document.createElement('span');
                    var H4 = document.createElement('input');
                    H3.setAttribute('id', 'H3');
                    H4.setAttribute('id', 'H4');
                    Z3.setAttribute('id', 'Z3');
                    Z4.setAttribute('id', 'Z4');
                    Z1.setAttribute('id', 'Z1');
                    Z1.innerHTML = "Initial Value of " + index.value.inputVars[2].name;
                    Z2.setAttribute('id', 'Z2');
                    Z2.setAttribute('type', 'text');
                    Z2.value = index.value.inputVars[2].defaultInitValue;
                    H1.setAttribute('id', 'H1');
                    H1.innerHTML = "Initial Value of " + index.value.inputVars[3].name;
                    H2.setAttribute('id', 'H2');
                    H2.setAttribute('type', 'text');
                    H2.value = index.value.inputVars[3].defaultInitValue;
                    $('#insert1').append(Z1);
                    $('#insert1').append(Z2);
                    $('#insert1').append("<P></P>");
                    $('#insert1').append(H1);
                    $('#insert1').append(H2);
                    //
                    DESCRIP.watchHTMLElement.input.c = Z2;
                    DESCRIP.watchHTMLElement.input.d = H2;
                    // console.log(watchHTMLElement);
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
            }
        };
        description.prototype.makeHTMLoutputVars = function (index) {
            document.getElementById('A4').innerHTML = index.value.outPutVars.initValue;
            //
            DESCRIP.watchHTMLElement.output.a = document.getElementById('A4');
        };
        description.prototype.checkValid = function (c) {
            switch (DESCRIP.watchHTMLElement.input.length) {
                case 2:
                    if (DESCRIP.watchHTMLElement.expression.value != '' && !isNaN(DESCRIP.watchHTMLElement.input.a.value) && !isNaN(DESCRIP.watchHTMLElement.input.b.value) && DESCRIP.watchHTMLElement.input.a.value != '' && DESCRIP.watchHTMLElement.input.b.value != '') {
                        console.log(2);
                        document.getElementById('start').removeAttribute('disabled');
                        State.isInputValidFlag = true;
                        c.checkValid();
                        console.log(c.currentState.name);
                    }
                    break;
                case 3:
                    if (DESCRIP.watchHTMLElement.expression.value != '' && !isNaN(DESCRIP.watchHTMLElement.input.a.value) && !isNaN(DESCRIP.watchHTMLElement.input.b.value) && !isNaN(DESCRIP.watchHTMLElement.input.c.value) && DESCRIP.watchHTMLElement.input.a.value != '' && DESCRIP.watchHTMLElement.input.b.value != '' && DESCRIP.watchHTMLElement.input.c.value != '') {
                        console.log(3);
                        document.getElementById('start').removeAttribute('disabled');
                    }
                    State.isInputValidFlag = true;
                    c.checkValid();
                    console.log(c.currentState.name);
                    break;
                case 4:
                    if (DESCRIP.watchHTMLElement.expression.value != '' && !isNaN(DESCRIP.watchHTMLElement.input.a.value) && !isNaN(DESCRIP.watchHTMLElement.input.b.value) && !isNaN(DESCRIP.watchHTMLElement.input.c.value) && !isNaN(DESCRIP.watchHTMLElement.input.d.value) && DESCRIP.watchHTMLElement.input.a.value != '' && DESCRIP.watchHTMLElement.input.b.value != '' && DESCRIP.watchHTMLElement.input.c.value != '' && DESCRIP.watchHTMLElement.input.d.value != '') {
                        console.log(4);
                        document.getElementById('start').removeAttribute('disabled');
                    }
                    State.isInputValidFlag = true;
                    c.checkValid();
                    console.log(c.currentState.name);
                    break;
            }
        };
        return description;
    })();
    DESCRIP.description = description;
})(DESCRIP || (DESCRIP = {}));
