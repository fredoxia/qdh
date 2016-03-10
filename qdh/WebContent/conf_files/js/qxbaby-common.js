var IN_USE = "1";
var SUCCESS = "2";
var FAIL = "3";
var NO_AUTHORITY = "4";
var ERROR = "5";
var WARNING = "6";
var ALL_RECORD = -1;

var ACTION_ADD = 1;
var ACTION_UPDATE = 2;
var ACTION_DELETE = 3;

var NORMAL_PRICE = 1;
var SPECIAL_PRICE = 2;

var SALES_ORDER_TITLE = "新建零售单";

/**
     * 时间格式化 返回格式化的时间
     * @param date {object}  可选参数，要格式化的data对象，没有则为当前时间
     * @param fomat {string} 格式化字符串，例如：'YYYY年MM月DD日 hh时mm分ss秒 星期' 'YYYY/MM/DD week' (中文为星期，英文为week)
     * @return {string} 返回格式化的字符串
     * 
     * 例子:
     * formatDate(new Date("january 01,2012"));
     * formatDate(new Date());
     * formatDate('YYYY年MM月DD日 hh时mm分ss秒 星期 YYYY-MM-DD week');
     * formatDate(new Date("january 01,2012"),'YYYY年MM月DD日 hh时mm分ss秒 星期 YYYY/MM/DD week');
     * 
     * 格式：   
     *    YYYY：4位年,如1993
　　 *　　YY：2位年,如93
　　 *　　MM：月份
　　 *　　DD：日期
　　 *　　hh：小时
　　 *　　mm：分钟
　　 *　　ss：秒钟
　　 *　　星期：星期，返回如 星期二
　　 *　　周：返回如 周二
　　 *　　week：英文星期全称，返回如 Saturday
　　 *　　www：三位英文星期，返回如 Sat
     */
    function formatDate(date, format) {
        if (arguments.length < 2 && !date.getTime) {
            format = date;
            date = new Date();
        }
        typeof format != 'string' && (format = 'YYYY年MM月DD日 hh时mm分ss秒');
        var week = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', '日', '一', '二', '三', '四', '五', '六'];
        return format.replace(/YYYY|YY|MM|DD|hh|mm|ss|星期|周|www|week/g, function(a) {
            switch (a) {
            case "YYYY": return date.getFullYear();
            case "YY": return (date.getFullYear()+"").slice(2);
            case "MM": return date.getMonth() + 1;
            case "DD": return date.getDate();
            case "hh": return date.getHours();
            case "mm": return date.getMinutes();
            case "ss": return date.getSeconds();
            case "星期": return "星期" + week[date.getDay() + 7];
            case "周": return "周" +  week[date.getDay() + 7];
            case "week": return week[date.getDay()];
            case "www": return week[date.getDay()].slice(0,3);
            }
        });
    }
function validateLoginForm(){
    var error = "";
	if (document.getElementById("userName").value == ""){
		error += "用户名 - 必须输入!\n";
	}
	if (document.getElementById("password").value == ""){
		error += "密码 - 必须输入!\n";
	}
	if (error != ""){
		alert(error);
		return false;
	} else
		return true;
}

/**
 * 判断是否为integer
 * @param e
 * @returns {Boolean}
 */
function is_number(e){
	var char_code = e.charCode ? e.charCode : e.keyCode;
	if(char_code<48 || char_code >57){
		alert("只允许输入数字");	
		return false;
	}else{
		return true;	
	}
}

function isValidInteger(s){
    var num= /^-?[1-9][0-9]{0,6}/; 
    if(!num.test(s))
	    return false;
    else {
    	return true;
    }
}
function isValidPositiveInteger(s){
    var num= /^[1-9][0-9]{0,6}/; 
    if(!num.test(s))
	    return false;
    else {
    	return true;
    }
}
function  isValidDate(str)   {  
	 var reg =/\d{4}-\d{1,2}-\d{1,2}/ ;  
	 if (reg.test(str))
       return true;  
	 else
       return false;  
 }  

/**
 * 判断是否为正的double类型数字
 * @param e
 * @returns {Boolean}
 */
function isPositiveDouble(s){
    var num= /^[0-9]+.?[0-9]*$/; 
    if(!num.test(s))
	    return false;
    else {
    	if (parseFloat(s) == 0)
    		return false;
    	else 
	        return true;
    }
}

function isEmpty(string){
	if ($.trim(string) == "")
		return true;
	else
		return false;
}

function isValidBarcode(barcode){
	var barcodeTrim = $.trim(barcode);
	var barcodeReg = /\d{12}/;
	return barcodeReg.test(barcodeTrim);
}

function isValidDiscount(e){
	var src_id = event.srcElement.id;
	var discount = document.getElementById(src_id);
	if (isNaN(discount) || discount<=0 || discount>1){
		alert("折扣 - 必须为非0，切小于等于1的数字.");	
		return false;
	}else{
		return true;	
	}
}

function parsePFormt(string){
	return (parseFloat(string)).toFixed(N_PRINT);
}

function formatDay(year, month, day){
	year = 1900 + year;
	month = month +1;
	if (month <10)
		month = "0" + month;
	if (day <10)
		day = "0" + day;
	return year +"-" + month + "-" +day;
}
function parseValue(value){
	if (value == undefined)
		return "-";
	else 
		return value;
}

function parseNumberValue(value){
	if (value == undefined || isNaN(value))
		return "-";
	else if (value == 0){
		return "-";
	} else 
		return (value).toFixed(2);
}

function parseQuantity(value){
	if (value == undefined || isNaN(value))
		return "-";
	else if (value == 0){
		return "-";
	} else 
		return value;
}

/**
 * 在页面添加tab
 * @param params
 */
function addTab(params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
	var t = $('#indexTab');
	var opts = {
		title : params.title,
		closable : true,
		content : iframe,
		border : false,
		fit : true
	};

	t.tabs('add', opts);
}

/**
 * 在tab的iframe中再次打开,功能和addTab一样
 * @param url
 * @param title
 */
function addTab2(url, title) {
	var iframe = '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
	var t = $('#indexTab');
	var opts = {
		title : title,
		closable : true,
		content : iframe,
		border : false,
		fit : true
	};
	t.tabs('add', opts);
}
/**
 * 在tab的iframe中再次打开一个
 * 无论之前有没有相同title,都再打开一个新的
 * @param url
 * @param title
 */
function addTab3(url, title) {
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
	
	parent.addTab2(url, title);
}

/**
 * 在tab的iframe中再次打开,功能和addTab一样
 * 如果之前有个相同的title的tab，那就打开以前那个
 * @param url
 * @param title
 */
function addTab6(url, title) {
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
	
	var opts = {
		title : title,
		url : url
	};
	
	parent.addTab4(opts);
}
/**
 * 在页面添加tab， 检查重复
 * @param params
 */
function addTab4(params) {
	var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
	var t = $('#indexTab');
	var opts = {
		title : params.title,
		closable : true,
		content : iframe,
		border : false,
		fit : true
	};

 	if (t.tabs('exists', opts.title) ) {
		t.tabs('select', opts.title);
		parent.$.messager.progress('close');
	} else {
		t.tabs('add', opts);
 	}
}

function updateTabName(newName){
	var tab = $("#indexTab").tabs('getSelected');
	var options = tab.panel("options");
    var tab2 = options.tab;
	var title = tab2.find("span.tabs-title");
	title.html(newName);
}

$.modalDialog = function(options) {

	if ($.modalDialog.handler != undefined) {
		var dialogA = $.modalDialog.handler;
		dialogA.dialog('close');
	}
	var opts = $.extend({
		title : '',
		width : 840,
		height : 680,
		modal : true,
		onClose : function() {
			$.modalDialog.handler = undefined;
			$(this).dialog('destroy');
		}
	}, options);

	//opts.modal = options.modalA;// 强制此dialog为模式化，无视传递过来的modal参数
	return $.modalDialog.handler = $('<div/>').dialog(opts);

};



$.serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

/**
 * 增加formatString功能
 * 
 * 使用方法：$.formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
$.formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

String.prototype.startWith=function(s){ 
	if(s==null||s==""||this.length==0||s.length>this.length) 
		return false; 
	if(this.substr(0,s.length)==s) 
		return true; 
	else 
		return false; 
	return true; 
} 


