var Singleton;
(function (Singleton) {
    var singleton = (function () {
        function singleton() {
            this.quiz = null;
            this.selectquiz = null;
            this.progromText = null;
            if (singleton.instance) {
                throw new Error('Error:Intantiation failed: Use singleton.getSingleton instead of new.');
            }
            singleton.instance = this;
        }
        singleton.getSingleton = function () {
            if (singleton.instance === null) {
                singleton.instance = new singleton();
            }
            return singleton.instance;
        };
        singleton.prototype.getquiz = function () {
            return this.quiz;
        };
        singleton.prototype.setquiz = function (quiz) {
            this.quiz = quiz;
        };
        singleton.prototype.getselectquiz = function () {
            return this.selectquiz;
        };
        singleton.prototype.setselectquiz = function (selectquiz) {
            this.selectquiz = selectquiz;
        };
        singleton.prototype.getProgramText = function () {
            return this.progromText;
        };
        singleton.prototype.setProgramText = function (prog) {
            this.progromText = prog;
        };
        singleton.instance = null;
        return singleton;
    })();
    Singleton.singleton = singleton;
})(Singleton || (Singleton = {}));
