window.onload=function(){
    
    
var str = '$1$ blabh $1$balh b$2$lah';
console.log(str);


//regexp to replace the middle part of $
var a = 'world';
var str1 = str.replace(/\$.*?\$\s?/, a);
console.log(str1);    

var str2 = str1.replace(/\$.*?\$\s?/, a);
console.log(str2);    
    
var str3 = str2.replace(/\$.*?\$\s?/, a);    
console.log(str3);    
}