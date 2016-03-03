/// <reference path="../library/jquery.d.ts" />

module uizBuilder
{
    
    //------------------------------
    export var tempGlobalvar;  
    export var tempIndex;
        
    //ajax get function with synchronous settings!!!!@false
   export function AJAX_JSON_Req( url )
{
    var AJAX_req = new XMLHttpRequest();
    //@false
    AJAX_req.open( "GET", url, false );
    AJAX_req.setRequestHeader("Content-type", "application/json");
    AJAX_req.send();
    var response = JSON.parse( AJAX_req.response );
    tempGlobalvar=response;
    //return response;
}
        
      //transfer ajax response to array.
   export function tansferarray(index:Array<Object>){
    //console.log(globalvar);
    for (var x in tempGlobalvar[0]){
        index.push({'key':x,value:tempGlobalvar[0][x]});
    }
    return index;
    /** 
    for (var i=0;i<index.length;i++){
        console.log(index[i].value);
    }
    **/
  }  
  
  export function descript(index){
        tempIndex=index;
        this.defaultXY(index);
        this.makeHTMLinputVars(index);
        this.makeHTMLoutputVars(index); 
        document.getElementById('start').innerHTML='Start';
        document.getElementById('start').setAttribute('disabled','disabled');
 }
    //-----------------------------
    var quizobject2=
     {
     "category":"fillInTheExpression",
     "name":"Question 2",
     "language":"c++",
     "text":"write an to calculate x+y+z",
     "code":"int void main(){\n double x=$x$; \n double y=$y$; \n double z=$z$; \n $exp$; \n return 0; \n }",
     "inputVars":[{"name":"x","defaultInitValue":21.0},
                  {"name":"y","defaultInitValue":20.0},
                  {"name":"z","defaultInitValue":30.0}],
     "outPutVars":[{"name":"a","initValue":0.0},
                   {"name":"b","initValue":1.0} ]
      }
    //product
  export  class Quiz
{
       category:string;
       name:string;
       text:string;
       language:string;
       code:string;
       inputVars:any;
       expression:HTMLElement;
       inputVarsSpanElement = new Array<HTMLElement>();
       inputVarsInputElement = new Array<HTMLElement>();
       outputVars:any;
  
  constructor(category:string,name:string,language:string){
      this.category=category;
      this.name=name;
      this.language=language;
  }
  
  public setText(text:string){
      this.text=text;
  }
  
  public setCode(code:string){
      this.code=code;
  }
  // i will receive the inputVars object and return inputVarsSpan;
  public setinputVarsSpanElement(inputVars:any){
      var count = Object.keys(inputVars).length;
       for(var i=0;i<count;i++)
        {
            this.inputVarsSpanElement[i]=document.createElement('span');
            //this.inputVarsElement[i].setAttribute('id','Z1');
            this.inputVarsSpanElement[i].innerHTML="Initial Value of "+inputVars[i].name;                                        
        }
          
          return this.inputVarsSpanElement;
      }
  //i will receive the inputVars object and return inputVarsInputElement;
  public setinputVarsinputElement(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++)
      {
          this.inputVarsInputElement[i]=document.createElement('input');
          //this.inputVarsSpanElement[i].setAttribute('id','Z2');
          this.inputVarsInputElement[i].setAttribute('type','text');
          this.inputVarsInputElement[i].setAttribute('value',inputVars[i].defaultInitValue);
          //console.log(inputVars[i].defaultInitValue);
      }
      return this.inputVarsInputElement;
  }
  
  public setExpression(){
        document.getElementById('E1').innerHTML='expression:';
        //cast to html element(<HTML>)
        document.getElementById('E2').setAttribute('value','');
  }
      
      
      public setOutputVars(outputVars:any){
      this.outputVars=outputVars;  
      
      
    }

}
  

//anstract builder
   abstract class QuizBuilder
{
      quiz:Quiz;
      category:string;
      name:string;
      language:string;
      
    constructor(category?:string,name?:string,language?:string){
            this.category=category;
            this.name=name;
            this.language=language;
  }  
      
      
    public getQuiz():Quiz {
        return this.quiz;
    }
    
    public createNewQuiz(){
       this.quiz = new Quiz(this.category,this.name,this.language); 
     
    }
    
    public abstract buildText();
    public abstract buildCode();
    public abstract buildInputVars();
    public abstract buildoutPutVars();
 
}
   //builder
  export  class FITEQuiz extends QuizBuilder{
    constructor(category:string,name:string,language:string){
      super(category,name,language);
  }  

       
    public  buildText(){
        this.quiz.setText(DESCRIP.tempIndex.text);
    };
    public  buildCode(){
        this.quiz.setCode(DESCRIP.tempIndex.code);
    };
    public  buildInputVars(){
        this.quiz.setinputVarsSpanElement(DESCRIP.tempIndex.inputVars);
        this.quiz.setinputVarsinputElement(DESCRIP.tempIndex.inputVars);
        this.quiz.setExpression();
    };
    public  buildoutPutVars(){
        this.quiz.setOutputVars(DESCRIP.tempIndex.outPutVars);
    };
   }
   
   //director
  export  class QuizDirector{
        quizbuilder:QuizBuilder;
        public setQuizBuilder(qb:QuizBuilder){
            this.quizbuilder=qb;
        }
        public getQuiz(){
            return this.quizbuilder.getQuiz();
        }
        public constructQuiz(){
            this.quizbuilder.createNewQuiz();
            this.quizbuilder.buildText();
            this.quizbuilder.buildCode();
            this.quizbuilder.buildInputVars();
            this.quizbuilder.buildoutPutVars();
            
        }
   }
   
   

   window.onload=function go(){
    //instantiate director
   var qd:QuizDirector = new QuizDirector();
   //instantiate concrete builder FITEQuiz
   var fitequiz:FITEQuiz = new FITEQuiz(quizobject2.category,quizobject2.name,quizobject2.language);
   //desclar quiz product
   var qz:Quiz
   //
   qd.setQuizBuilder(fitequiz);
   qd.constructQuiz();
   
   qz = qd.getQuiz();
  // console.log(qz);
  console.log(qz.inputVarsInputElement.length);
  
   for(var i=0; i<qz.inputVarsInputElement.length;i++){
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
}


     




   }