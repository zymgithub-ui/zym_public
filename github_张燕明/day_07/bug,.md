$('#id')为 jquery 对象,

$('#id')[0]为 js 原生对象  $('#id')[0] = document.getElementById("id") ;



$():就是获取当前html页面所有的dom对象然后组成一个数组,只有一个对象时,也是存放在集合[dom对象],如果有两个[dom对象1,dom对象2];

$()[0] : 获取当前集合的第一个元素,获取dom对象

