/**
 * AutoToc.js can genereate a table of content according to the HTML document.
 * This is part of the VISual Regular Expression EDitor project.
 * $Id: AutoToc.js 515 2011-06-15 19:41:31Z xiaoyu $
 *
 * Thanks to OnReady() from Toby, http://tobyho.com
 */

(function(){
    var addLoadListener;
    var removeLoadListener;
    if (window.addEventListener){
        addLoadListener = function(func){
            window.addEventListener('DOMContentLoaded', func, false);
            window.addEventListener('load', func, false);
        };
        removeLoadListener = function(func){
            window.removeEventListener('DOMContentLoaded', func, false);
            window.removeEventListener('load', func, false);
        };
    }else if (document.attachEvent){
        addLoadListener = function(func){
            document.attachEvent('onreadystatechange', func);
            document.attachEvent('load', func);
        };
        removeLoadListener = function(func){
            document.detachEvent('onreadystatechange', func);
            document.detachEvent('load', func);
        };
    }
    
    var callbacks = null;
    var done = false;
    function __onReady(){
        done = true;
        removeLoadListener(__onReady);
        if (!callbacks) return;
        for (var i = 0; i < callbacks.length; i++){
            if(callbacks[i] != undefined){
                callbacks[i]();
            }
        }
        callbacks = null;
    }
    function OnReady(func){
        if (done){
            func();
            return;
        }
        if (!callbacks){
            callbacks = [];
            addLoadListener(__onReady);
        }
        callbacks.push(func);
    }
    window.OnReady = OnReady;
})();
 
var _AutoToc = {
    defaultTag: 'h3',
    defaultId: 'toc',
    defaultHandlerId: 'toc_handler',
    defaultShow: false,
    show: false,
    go: function(){
        var div = document.getElementById(this.defaultId);
        if(div == null){
            div = document.createElement('div');
            div.id = this.defaultId;
        } else {
            if (div.parent != null){
                div.parent.removeChild(div);
            }
        }
        div.innerHTML = '<h3>Table of Contents</h3>';
    
        var ul = document.createElement('ul');
        var nodes = document.getElementsByTagName(this.defaultTag);
        if(nodes.length > 0){
            // only generate a menu when content list is not empty
            for(var i = 0; i < nodes.length; i++){
                this.appendLi(ul, nodes[i].textContent, nodes[i].id);
            }
            
            div.appendChild(ul);
            if(this.defaultShow == false){
                div.style.display = 'none';
            }
            document.body.appendChild(div);
            
            var handler = document.createElement('a');
            handler.id = this.defaultHandlerId;
            handler.innerHTML = 'menu';
            handler.href = '#';
            handler.onclick = function(){
                var div = document.getElementById(_AutoToc.defaultId);
                _AutoToc.show = !_AutoToc.show;
                if(_AutoToc.show){
                    div.style.display = '';
                }
                else{
                    div.style.display = 'none';
                }
                return false;
            };
            document.body.appendChild(handler);
        }
    },
    appendLi: function(ul, text, anchor){
        var li = document.createElement('li');
        var a = document.createElement('a');
        a.href = '#' + anchor;
        a.textContent = text;
        li.appendChild(a);
        ul.appendChild(li);
    }
};

OnReady(_AutoToc.go());