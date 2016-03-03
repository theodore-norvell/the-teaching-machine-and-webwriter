module Singleton{
    
   export class singleton {
        
        private quiz:any = null;
        
        private selectquiz:any = null;
        
        private  progromText:string = null;
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        private static instance:singleton = null;
        
        
        constructor(){
            if (singleton.instance){
                throw new Error('Error:Intantiation failed: Use singleton.getSingleton instead of new.');
            }
            singleton.instance=this;
        }
        
        public static getSingleton():singleton{
            if (singleton.instance === null)
            {
                singleton.instance = new singleton();
            }
            return singleton.instance;
           
                                                }
                                                
                                                
                                                
                                                
        public getquiz(){
            return this.quiz;
        }
        public setquiz(quiz:any){
            this.quiz=quiz;
            
        }
        
        public getselectquiz(){
            return this.selectquiz;
        }
        public setselectquiz(selectquiz:any){
            this.selectquiz=selectquiz;
        }
        
        
                                                
        
        public getProgramText(){
            return this.progromText;
        }
        public setProgramText(prog:string){
            
            this.progromText=prog;
        }
                                                
                                                
                                                
                                                
                                                
                                                
                            }
    
    
    
    
    
    
    
}