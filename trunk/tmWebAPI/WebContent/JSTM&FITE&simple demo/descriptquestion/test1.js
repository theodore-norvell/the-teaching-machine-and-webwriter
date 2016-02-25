///<reference path="description.ts" />
///<reference path="../state/State.ts" />
///<reference path="../state/concreteJSTM.ts" />
window.onload = function go() {
    /** --------------------------------i am now in the initialize state -------------------------------------  **/
    /** --------------------------------i am now in the initialize state -------------------------------------  **/
    //fite is the object for my FITE
    var description = new DESCRIP.description();
    //call ajax function to read the whole json file and then store in a var called globalvar;
    //1
    description.AJAX_JSON_Req('description.json');
    //2 push to index
    var index = [];
    index = description.tansferarray(index);
    //3 
    var select = document.getElementById('questionslist');
    select.addEventListener('change', selec, false);
    function selec() {
        var i = this.selectedIndex;
        //console.log(index[i].value.inputVars.length);
        console.log(index[i]);
        description.descript(index[i]);
    }
    //after select, all the initial vars are ok!.
    var concrete = new jstm.concreteJSTM('i will be send to FITE');
    var fite = new State.FITE(concrete);
    var check = document.getElementById('checkValid');
    //
    check.addEventListener('click', checkValid, false);
    function checkValid() {
        description.checkValid(fite);
    }
    /** --------------------------------i am now in the initialize state -------------------------------------  **/
    /** --------------------------------i am now in the initialize state -------------------------------------  **/
    /** --------------------------------i am now in the startable state -------------------------------------  **/
    /** --------------------------------i am now in the startable state -------------------------------------  **/
    var start = document.getElementById('start');
    start.addEventListener('click', clickStartAndWait, false);
    //after i click the start button, change to the wait state
    //use asyn callback to make sure the program cascading function is first excuted and return program.
    function clickStartAndWait() {
        fite.clickStart(fite);
    }
    var goForwardButton = document.getElementById('goFoward');
    var goBackButton = document.getElementById('goBack');
    /** --------------------------------i am now in the start state -------------------------------------  **/
    /** --------------------------------i am now in the start state -------------------------------------  **/
    goForwardButton.addEventListener('click', fite.goForward, false);
    goBackButton.addEventListener('click', fite.goBack, false);
    var closebtn = document.getElementById('closebtn');
    closebtn.addEventListener('click', closepanel, false);
    function closepanel() {
        //console.log();
        document.getElementById('panel').style.display = 'none';
    }
};
