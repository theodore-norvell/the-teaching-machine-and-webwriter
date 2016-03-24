/// <reference path="../library/jquery.d.ts" />
/// <reference path="../src/JSTM.ts" />
/// <reference path="../state/State.ts" />


module quizBuilder
{
    
    //------------------------------
       //var tempGlobalvar:any;  
       //var tempIndex:any;
export  class fetchFile{
     //the url of the .json file; 
     url:any;
    //this represents the whole json file;
    jsonFile:any; 
    //this represents the whole json file in array;
    jsonFileArray=[];  
    //selected question
    selectedQuestion:any;
    //selected number
    selectedNumber:number;
    
    constructor(url,selectedNumber:number){
        this.url=url;
        this.selectedNumber=selectedNumber;
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
   public  AJAX_JSON_Req()
{
    var thisFetchFile=this;
    var dtd=$.Deferred();
    $.ajax({
            url : this.url,
            dataType:"json",
            type:"POST",
            async:false,
          })
    .done(function (data) { 
       thisFetchFile.jsonFile=data; 
       //thisFetchFile.tansferarray();
       //thisFetchFile.descript();
       dtd.resolve();
       
    })
    .fail(function () {dtd.reject(this); console.log("error! ");});
    
    
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
}

      //transfer ajax response to array.
   public  tansferarray(){
    //console.log(globalvar);
    for (var x in this.jsonFile[0]){
        this.jsonFileArray.push({'key':x,value:this.jsonFile[0][x]});
    }
    //Quiz.index=index;
    //console.log(this.jsonFileArray);
    /** 
    for (var i=0;i<index.length;i++){
        console.log(index[i].value);
    }
    **/
                                                }  

  public  descript(){
      var dtd=$.Deferred();
      this.selectedQuestion=this.jsonFileArray[this.selectedNumber].value;
       // console.log(tempIndex);
       //console.log(this.selectedQuestion.inputVars);
        dtd.resolve(this.selectedQuestion);
        return dtd;
                                    }
                                    
    //get question method
    public getselectedQuestion(){
        //console.log(this.selectedNumber);
        //this.AJAX_JSON_Req();
        this.tansferarray();
        this.descript();
        return this.selectedQuestion;
        
    }
      
      
      
  }
  
  
  //base class Question.
  class Questiion{
      
      
       //
       concreteJSTM:jstm.JSTM;
       
       category:string;
       name:string;
       text:string;
       language:string;
       inputVars:any;
      
       //selected question
       selectedQuestion:any;
       
       
       
      constructor(selectedQuestion:any){
      this.selectedQuestion=selectedQuestion;
                            }
                            
        public setCategory(category:string){
            this.category=category;
         }                          
  
        public setName(name:string){
            this.name=name;
        }
  
        public setLanguage(language:string){
             this.language=language;
        }
  
         public setText(text:string){
             this.text=text;
        }               
         
         public setInputVars(inputVars:any){
             this.inputVars=inputVars;
                                            }            
                     // set the JSTM reference to the local instance of FITEQuestion
         public setConcreteJSTM(ThisJSTM:jstm.JSTM){
              this.concreteJSTM=ThisJSTM;
                                                    }
        //get JSTM
        public getConcreteJSTM(){
             return this.concreteJSTM;
                                 }         
                            
                            
  }
 
    //fat product---->quiz
  export  class FITEQuestion extends Questiion
{

       
       code:string;
       outputVars:any;
       //
       controller:State.FITEController;


       //
       Div:HTMLElement;
       innerDiv1:HTMLElement;
       P:HTMLElement;
       insertDiv1:HTMLElement;
       startButton:HTMLElement;
       innerDiv2:HTMLElement;
       fieldSet:HTMLElement;
       //goForwardButton:HTMLElement;
       //goBackButton:HTMLElement;
       //expressionDisplay:HTMLElement;
       aHref:HTMLElement;
       insertDiv2:HTMLElement;
       //
       span:HTMLElement;
      // newLine:HTMLElement;
       //expression:HTMLElement;
       
       
       //
       //<span> element for expression
       inputExpressionSpan:HTMLElement;
       //<input> elelement for expression
       inputExpressionValue:HTMLElement;
       //<span> element for variables
       inputVarsSpan = new Array<HTMLElement>();
       //<input> element for variables
       inputVarsValue = new Array<HTMLElement>();
       //
       //<span> element for watch variables 
       outputMirrorSpan = new Array<HTMLElement>();
       //<input> element for watch variables
       outputMirrorValue = new Array<HTMLElement>();
       //
       //outputVarsSpan = new Array<HTMLElement>();
       //outputVarsValue = new Array<HTMLElement>();
   
       
  constructor(selectedQuestion:any){
      super(selectedQuestion);
                            }
                            
                             
  public setCode(code:string){
      this.code=code;
                              }
                              
  
  public setOutputVars(outputVars:any){
      this.outputVars=outputVars;  
                                     }
    //setter a reference
  public setController(controller:State.FITEController){
      this.controller=controller;
  }
  
  //getter a reference
  public getController(){
      return this.controller;
                                 }   
  
  /////////////////////////////makeHTML
  
      //outer div <div id='question1'>
      public makeDiv(){
      this.Div = document.createElement('div');
      this.Div.setAttribute('id',this.name);
      return this.Div;
      }
      //innerDiv1 <div id='question1=area1'>      
      public makeInnerDiv1(){
      this.innerDiv1 = document.createElement('div');
      this.innerDiv1.setAttribute('id',this.name+'-area1');
      return this.innerDiv1;
      }
      //     
      public makeP(){
      this.P =document.createElement('p');
      this.P.setAttribute('id',this.name+'-questionDisplay');
      this.P.innerHTML=this.name+":"+this.text;
      return this.P;
      }
      //insertDiv1     
      public makeInsertDiv1(){
      this.insertDiv1 = document.createElement('div');
      this.insertDiv1.setAttribute('id',this.name+'-insertDiv1');
      return this.insertDiv1;

      }
      //      
      public makeStartButton(){
      this.startButton = document.createElement('button');
      this.startButton.setAttribute('id',this.name+'-start');
      this.startButton.setAttribute('disabled','disabled');
      this.startButton.setAttribute('style','display:none');
      this.startButton.innerHTML='start';
      return this.startButton;
      }
      //innerDiv2 <div id='question-area2'>      
      public makeInnerDiv2(){
      this.innerDiv2 = document.createElement('div');
      this.innerDiv2.setAttribute('id',this.name+'-area2');
      return this.innerDiv2;
      }
      //
      public makeFieldSet(){
      this.fieldSet =document.createElement('fieldSet');
      this.fieldSet.setAttribute('id',this.name+'panel');
      this.fieldSet.style.display='none';
      return this.fieldSet;
      }

      //      
      public makeAHref(){
      this.aHref = document.createElement('a');
      this.aHref.setAttribute('id',this.name+'-close');
      this.aHref.setAttribute('href','javascript:void(0)');
      this.aHref.setAttribute('aria-label','Close Account Info Modal Box');
      this.aHref.setAttribute('style','float:right;');
      this.aHref.innerHTML='&#x274C;';
      return this.aHref;
      }
      //#insertDiv2
      public makeInsertDiv2(){
      this.insertDiv2 = document.createElement('div');
      this.insertDiv2.setAttribute('id',this.name+'-insertDiv2');
      return this.insertDiv2;
      }
      //
      public makeSpan(){
          this.span=document.createElement('span');
          this.span.innerHTML='&nbsp';
          return this.span;
      }
      //

      /////////////////////make insertArea1 and 2
      public setinputExpressionSpan(){
      this.inputExpressionSpan=document.createElement('span');
      this.inputExpressionSpan.innerHTML='expression: ';
      
      return this.inputExpressionSpan;
      
                                        
                                        }
                                        
      public setinputExpressionValue(){
      this.inputExpressionValue=document.createElement('input');
      this.inputExpressionValue.setAttribute('type','text');
      this.inputExpressionValue.setAttribute('value','');
      return this.inputExpressionValue;
      
                                            }
  
  
      public setinputVarsSpan(inputVars:any){
     // console.log(inputVars);
      var count = Object.keys(inputVars).length;
       for(var i=0;i<count;i++)
        {
            this.inputVarsSpan[i]=document.createElement('span');
            //this.inputVarsElement[i].setAttribute('id','Z1');
            this.inputVarsSpan[i].innerHTML="Initial Value of "+inputVars[i].name;                                        
        }
          
          return this.inputVarsSpan;
                                                }
  //i will receive the inputVars object and return inputVarsInputElement;
      public setinputVarsValue(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++)
      {
          this.inputVarsValue[i]=document.createElement('input');
          //this.inputVarsSpanElement[i].setAttribute('id','Z2');
          this.inputVarsValue[i].setAttribute('type','text');
          this.inputVarsValue[i].setAttribute('value',inputVars[i].defaultInitValue);
          //console.log(inputVars[i].defaultInitValue);
      }
      return this.inputVarsValue;
                                                  }
  
  
     public setoutputMirrorSpan(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++){
        this.outputMirrorSpan[i]=document.createElement('span');
        this.outputMirrorSpan[i].innerHTML=inputVars[i].name;  
      }
      return this.outputMirrorSpan;
                                                }
  
     public setoutputMirrorValue(inputVars:any){
      var count = Object.keys(inputVars).length;
      for(var i=0;i<count;i++)
      {
          this.outputMirrorValue[i]=document.createElement('input');
          this.outputMirrorValue[i].setAttribute('type','text');
          this.outputMirrorValue[i].setAttribute('value',inputVars[i].defaultInitValue);
      }
      return this.outputMirrorValue;
                                                   }
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
  public constructInsertArea1(){
       var fn=this.insertDiv1.firstChild;
       while(fn){
           this.insertDiv1.removeChild(fn);
           fn=this.insertDiv1.firstChild;
       }
       this.insertDiv1.appendChild(this.inputExpressionSpan);
       this.insertDiv1.appendChild(this.inputExpressionValue);
       this.insertDiv1.appendChild(document.createElement('br'));
       for(var j=0; j<this.inputVarsValue.length;j++)
       {    
       this.insertDiv1.appendChild(this.inputVarsSpan[j]);
       this.insertDiv1.appendChild(this.inputVarsValue[j]);
       this.insertDiv1.appendChild(document.createElement('br'));
       }  
      
                            }   
  
  public constructInsertArea2(){
       var fn=this.insertDiv2.firstChild;
       while(fn){
           this.insertDiv2.removeChild(fn);
           fn=this.insertDiv2.firstChild;
       }
       //make the VariableWatcher by calling concreteJSTM's method
       var VariableWatcher:HTMLElement=this.concreteJSTM.makeVariableWatcher(this.outputVars[0].name,this.outputVars[0].initValue);
       
       for(var j=0; j<this.outputMirrorValue.length;j++)
       {
       this.insertDiv2.appendChild(this.outputMirrorSpan[j]);
       this.insertDiv2.appendChild(this.outputMirrorValue[j]);
       this.insertDiv2.appendChild(document.createElement('br'));
       }
       //this.insertDiv2.appendChild(this.outputVarsSpan[0]);
       //
       this.insertDiv2.appendChild(VariableWatcher);
       //this.insertDiv2.appendChild(document.createElement('br'));
                                }                                                   
                                                        

      public makeHTML(){
          //make the sturcture
          this.makeDiv();
          this.makeInnerDiv1();
          this.makeP();
          this.makeInsertDiv1();
          this.makeStartButton();
          this.makeInnerDiv2();
          this.makeFieldSet();
          var goForwardButton:HTMLElement =this.concreteJSTM.makeGoForwardButton();
          var goBackButton:HTMLElement  =this.concreteJSTM.makeGoBackButton();
          var expressionDisplay:HTMLElement =this.concreteJSTM.makeExpressionDisplay();
          this.makeAHref();
          this.makeInsertDiv2();
          this.makeSpan();
          //this.makeBr();
          
          //the span/input element in the insertDiv1 and insertDiv2
          this.setinputExpressionSpan();
          this.setinputExpressionValue();
          this.setinputVarsSpan(this.selectedQuestion.inputVars);
          this.setinputVarsValue(this.selectedQuestion.inputVars);
          this.setoutputMirrorSpan(this.selectedQuestion.inputVars);
          this.setoutputMirrorValue(this.selectedQuestion.inputVars);
          //this.setoutputVarsSpan(this.selectedQuestion.outPutVars);
          //this.setoutputVarsValue(this.selectedQuestion.outPutVars)
         //
         this.Div.appendChild(this.innerDiv1);
         this.Div.appendChild(this.innerDiv2);
         
         this.innerDiv1.appendChild(this.P);
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
         this.constructInsertArea2();
        
      }
      public getHTML(){
          return this.Div;
          
      }
      
      
      
      
      
       public addeventListener(){
            //lambda expression to hold the context this/controller
            var handler1 = (e:Event)=>{this.controller.ValidWatch();}
            this.inputExpressionValue.addEventListener('input',handler1,false);
            for(var i=0;i<this.inputVarsValue.length;i++){
            this.inputVarsValue[i].addEventListener('input',handler1,false);
                                                                    }  
                                                                     
            //lambda expression to hold the context this/controller                                                       
            var handler2 = (e:Event)=>{this.controller.goForward();}                                                        
            this.concreteJSTM.goForwardButton.addEventListener('click',handler2,false);
            
            //lambda expression to hold the context this/controller
            var handler3 = (e:Event)=>{this.controller.goBack();} 
            this.concreteJSTM.goBackButton.addEventListener('click',handler3,false);
            
            //lambda expression to hold the context this/controller
            var handler4 = (e:Event)=>{this.controller.close();} 
            this.aHref.addEventListener('click',handler4,false); 
            
            //lambda expression to hold the context this/controller
            var handler5 = (e:Event)=>{this.controller.start();} 
            this.startButton.addEventListener('click',handler5,false);                                                        
                                                                             
          }
      
        
//fat product
}
  

//anstract builder
   abstract class QuizBuilder
{
      quiz:FITEQuestion;
      category:string;
      name:string;
      language:string;
      //selected question
      selectedQuestion:any;

      
    constructor(selectedQuestion:any){
      this.selectedQuestion=selectedQuestion;

  }  
      
      
    public getQuiz():FITEQuestion {
        return this.quiz;
    }
    
    public createNewQuiz(){
       this.quiz = new FITEQuestion(this.selectedQuestion); 
     
    }
    public abstract buildCategory();
    public abstract buildName();
    public abstract buildLanguage();
    public abstract buildText();
    public abstract buildCode();
    public abstract buildInputVars();
    public abstract buildoutputVars();
 
}
   //builder
  export  class FITEQuizQuizBuilder extends QuizBuilder{
      
    constructor(selectedQuestion){
      super(selectedQuestion);
  }  
    public buildCategory(){
       this.quiz.setCategory(this.selectedQuestion.category);
   }
    public buildName(){
       this.quiz.setName(this.selectedQuestion.name);
    }
    public buildLanguage(){
       this.quiz.setLanguage(this.selectedQuestion.language);
    }
       
    public  buildText(){
        this.quiz.setText(this.selectedQuestion.text);
    }
    public  buildCode(){
        this.quiz.setCode(this.selectedQuestion.code);
    }
    public  buildInputVars(){
        //console.log(tempIndex.inputVars);
        this.quiz.setInputVars(this.selectedQuestion.inputVars);
    }
    public  buildoutputVars(){
        //console.log()
        this.quiz.setOutputVars(this.selectedQuestion.outPutVars);
    }
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
        //take a selectedQuestion as parameter,which is an json object
        public constructQuiz(){
            this.quizbuilder.createNewQuiz();
            this.quizbuilder.buildCategory();
            this.quizbuilder.buildName();
            this.quizbuilder.buildLanguage();
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