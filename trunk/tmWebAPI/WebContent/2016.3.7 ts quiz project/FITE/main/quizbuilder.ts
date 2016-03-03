/// <reference path="../library/jquery.d.ts" />
/// <reference path="../singleton/singleton.ts" />

module quizBuilder
{
    
    //------------------------------
       var tempGlobalvar:any;  
       var tempIndex:any;
        
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
      var dtd=$.Deferred();
        tempIndex=index.value;
        document.getElementById('start').innerHTML='Start';
        document.getElementById('start').setAttribute('disabled','disabled');
       // console.log(tempIndex);
       console.log(tempIndex.inputVars);
        dtd.resolve(tempIndex);
        return dtd;
        
        
                                    }
 
    //fat product---->quiz
  export  class Quiz
{
       category:string;
       name:string;
       text:string;
       language:string;
       code:string;
       inputVars:any;
       outputVars:any;
       expression:HTMLElement;
       //
       inputExpressionSpanElement = new Array<HTMLElement>();
       inputExpressionInputElement:any = new Array<HTMLElement>();
       inputVarsSpanElement = new Array<HTMLElement>();
       inputVarsInputElement = new Array<HTMLElement>();
       //
       outputVarsSpanElement = new Array<HTMLElement>();
       outputVarsInputElement = new Array<HTMLElement>();
       outputVariableSpanElement = new Array<HTMLElement>();
       outputVariableinputElement = new Array<HTMLElement>();
       
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
  
  public setInputVars(inputVars:any){
      this.inputVars=inputVars;
                                    }
  
  public setOutputVars(outputVars:any){
      this.outputVars=outputVars;  
                                     }
  // i will receive the inputVars object and return inputVarsSpan;
  public setInputExpressionSpanElement(){
      this.inputExpressionSpanElement[0]=document.createElement('span');
      this.inputExpressionSpanElement[0].innerHTML='expression: ';
      
      return this.inputExpressionSpanElement[0];
      
                                        
                                        }
                                        
   public setinputExpressionInputElement(){
      this.inputExpressionInputElement[0]=document.createElement('input');
      this.inputExpressionInputElement[0].setAttribute('type','text');
      this.inputExpressionInputElement[0].setAttribute('value','');
      return this.inputExpressionInputElement[0];
      
                                            }
  
  
  public setinputVarsSpanElement(inputVars:any){
     // console.log(inputVars);
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
  
  
  public setoutputVarsSpanElement(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++){
        this.outputVarsSpanElement[i]=document.createElement('span');
        this.outputVarsSpanElement[i].innerHTML=inputVars[i].name;  
      }
      return this.outputVarsSpanElement;
                                                }
  
  public setoutputVarsInputElement(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++)
      {
          this.outputVarsInputElement[i]=document.createElement('input');
          this.outputVarsInputElement[i].setAttribute('type','text');
          this.outputVarsInputElement[i].setAttribute('value',inputVars[i].defaultInitValue);
      }
      return this.outputVarsInputElement;
                                                   }
  
  public setoutputVariableSpanElement(outPutVars:any){
      var count = Object.keys(outPutVars).length;
      for(var i=0;i<count;i++){
          this.outputVariableSpanElement[i]=document.createElement('span');
          this.outputVariableSpanElement[i].innerHTML = outPutVars[i].name;
       }
             return this.outputVariableSpanElement;
                                                  }
   public setoutputVariableinputElement(outPutVars:any){
       //console.log(outPutVars);
      var count = Object.keys(outPutVars).length;
      for(var i=0;i<count;i++)
      {
          this.outputVariableinputElement[i]=document.createElement('input');
          this.outputVariableinputElement[i].setAttribute('type','text');
          this.outputVariableinputElement[i].setAttribute('value',outPutVars[i].initValue);
      }
      return this.outputVariableinputElement;
       
       
                                                        }
  
  
   //constructDisplay area1!!!!
  public constructInertArea1(){
       $('#insert1').empty();
       $('#insert1').append(this.inputExpressionSpanElement);
       $('#insert1').append(this.inputExpressionInputElement);
       $('#insert1').append('<br></br>');
       for(var j=0; j<this.inputVarsInputElement.length;j++)
       {    
       $('#insert1').append(this.inputVarsSpanElement[j]);
       $('#insert1').append(this.inputVarsInputElement[j]);
       $('#insert1').append("<br></br>");
       }  
      
                            }   
  
  public constructInsertArea2(){
       $('#insert2').empty();
       for(var j=0; j<this.outputVarsInputElement.length;j++)
       {
       $('#insert2').append(this.outputVarsSpanElement[j]);
       $('#insert2').append(this.outputVarsInputElement[j]);
       $('#insert2').append("<br></br>");
       }
       for(var j=0;j<this.outputVariableinputElement.length;j++)
       {
       $('#insert2').append(this.outputVariableSpanElement[j]);
       $('#insert2').append(this.outputVariableinputElement[j]);
       $('#insert2').append("<br></br>");
           
       }  
                                }


    
//fat product
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
    public abstract buildoutputVars();
 
}
   //builder
  export  class FITEQuiz extends QuizBuilder{
    constructor(category:string,name:string,language:string){
      super(category,name,language);
  }  

       
    public  buildText(){
        this.quiz.setText(tempIndex.text);
    };
    public  buildCode(){
        this.quiz.setCode(tempIndex.code);
    };
    public  buildInputVars(){
        //console.log(tempIndex.inputVars);
        this.quiz.setInputVars(tempIndex.inputVars);
        this.quiz.setInputExpressionSpanElement();
        this.quiz.setinputExpressionInputElement();
        this.quiz.setinputVarsSpanElement(tempIndex.inputVars);
        this.quiz.setinputVarsinputElement(tempIndex.inputVars);
    };
    public  buildoutputVars(){
        //console.log()
        this.quiz.setOutputVars(tempIndex.outPutVars);
        this.quiz.setoutputVarsSpanElement(tempIndex.inputVars);
        this.quiz.setoutputVarsInputElement(tempIndex.inputVars);
        this.quiz.setoutputVariableSpanElement(tempIndex.outPutVars);
        this.quiz.setoutputVariableinputElement(tempIndex.outPutVars);
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
            this.quizbuilder.buildoutputVars();
            
        }
   }
   
   /** 

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
  // console.log(qz.category+"/"+qz.name+"/"+qz.language+"/"+qz.code+"/"+qz.inputVars +"/"+qz.outputVars+"/"+qz.text+"is completed and ready");
   console.log(qz.inputVarsSpanElement);
   console.log(qz.inputVarsInputElement);
   //console.log(qz.inputVars[0].name);
   //console.log(Object.keys(qz.inputVars).length);
   console.log(qz.outputVars);
   document.body.appendChild(qz.inputVarsSpanElement[0]);
   document.body.appendChild(qz.inputVarsInputElement[0]);
   

}
**/
}