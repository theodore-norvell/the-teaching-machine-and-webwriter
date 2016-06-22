/// <reference path="JSTM.ts" />
var jstm;
(function (jstm) {
    function tmAdder(tm) {
        return function (element) {
            var name = element.getAttribute("name");
            var newElem = null;
            var varName = null;
            switch (name) {
                case "tm-expEngineCanvas":
                    newElem = tm.makeExpressionDisplay();
                    break;
                case "tm-goForward":
                    newElem = tm.makeGoForwardButton();
                    break;
                case "tm-goBack":
                    newElem = tm.makeGoBackButton();
                    break;
                case "tm-var-watcher":
                    varName = element.getAttribute("data-var-name");
                    newElem = tm.makeVariableWatcher(varName);
                    break;
                case "tm-canvas":
                	newElem = getCanvasElement();
                	break;
            }
            if (newElem != null)
                replace(element, newElem);
        };
    }
    function replace(oldElem, newElem) {
        oldElem.parentNode.replaceChild(newElem, oldElem);
    }
    function walk(node, operation) {
        // Apply the operation
        operation(node);
        // Copy the children that are Elements.
        var children = node.childNodes;
        var childArray = new Array();
        var i;
        var j = 0;
        for (i = 0; i < children.length; ++i) {
            if (children.item(i).nodeType == document.ELEMENT_NODE) {
                childArray[j++] = children.item(i);
            }
        }
        // Recurse on the children
        for (i = 0; i < childArray.length; ++i) {
            walk(childArray[i], operation);
        }
    }
    function addTM(tm, root) {
        walk(root, tmAdder(tm));
    }
    jstm.addTM = addTM;
})(jstm || (jstm = {}));
// # sourceMappingURL=addTM.js.map
