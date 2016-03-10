/// <reference path="../library/jquery.d.ts" />

module quizBuilder
{
    
    //------------------------------
       //var tempGlobalvar:any;  
       //var tempIndex:any;
        
 
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
       
      static tempGlobalvar:any;
      static tempIndex:any; 
      static index=[];
       
       //expression:HTMLElement;
       
  constructor(category:string,name:string,language:string){
      this.category=category;
      this.name=name;
      this.language=language;
                                                            }
      //ajax get function with synchronous settings!!!!@false
   public static AJAX_JSON_Req( url )
{
    var AJAX_req = new XMLHttpRequest();
    //@false
    AJAX_req.open( "GET", url, false );
    AJAX_req.setRequestHeader("Content-type", "application/json");
    AJAX_req.send();
    var response = JSON.parse( AJAX_req.response );
    Quiz.tempGlobalvar=response;
    //return response;
}

      //transfer ajax response to array.
   public static tansferarray(index:Array<Object>){
    //console.log(globalvar);
    for (var x in Quiz.tempGlobalvar[0]){
        Quiz.index.push({'key':x,value:Quiz.tempGlobalvar[0][x]});
    }
    //Quiz.index=index;
    console.log(Quiz.index);
    /** 
    for (var i=0;i<index.length;i++){
        console.log(index[i].value);
    }
    **/
    
                                                }  

  public static descript(index){
      var dtd=$.Deferred();
        Quiz.tempIndex=index.value;
        document.getElementById('start').innerHTML='Start';
        document.getElementById('start').setAttribute('disabled','disabled');
       // console.log(tempIndex);
       console.log(Quiz.tempIndex.inputVars);
        dtd.resolve(Quiz.tempIndex);
        return dtd;
        
        
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
  export  class FITEQuizQuizBuilder extends QuizBuilder{
    constructor(category:string,name:string,language:string){
      super(category,name,language);
  }  

       
    public  buildText(){
        this.quiz.setText(Quiz.tempIndex.text);
    };
    public  buildCode(){
        this.quiz.setCode(Quiz.tempIndex.code);
    };
    public  buildInputVars(){
        //console.log(tempIndex.inputVars);
        this.quiz.setInputVars(Quiz.tempIndex.inputVars);
    };
    public  buildoutputVars(){
        //console.log()
        this.quiz.setOutputVars(Quiz.tempIndex.outPutVars);
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