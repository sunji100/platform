/*****************************************************************
                  表单校验工具类  (linjq)       
*****************************************************************/
validation = {
/**
 * 判断整数num是否等于0
 * 
 * @param num
 * @return
 * @author jiqinlin
 */
		isIntEqZero:function(num){ 
     return num==0;
},

/**
 * 判断整数num是否大于0
 * 
 * @param num
 * @return
 * @author jiqinlin
 */
isIntGtZero:function(num){ 
    return num>0;
},

/**
 * 判断整数num是否大于或等于0
 * 
 * @param num
 * @return
 * @author jiqinlin
 */
isIntGteZero:function(num){ 
    return num>=0;
},

/**
 * 判断浮点数num是否等于0
 * 
 * @param num 浮点数
 * @return
 * @author jiqinlin
 */
isFloatEqZero:function(num){ 
    return num==0;
},

/**
 * 判断浮点数num是否大于0
 * 
 * @param num 浮点数
 * @return
 * @author jiqinlin
 */
isFloatGtZero:function(num){ 
    return num>0;
},

/**
 * 判断浮点数num是否大于或等于0
 * 
 * @param num 浮点数
 * @return
 * @author jiqinlin
 */
isFloatGteZero:function(num){ 
    return num>=0;
},

/**
 * 匹配Email地址
 */
isEmail:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
    if(result==null)return false;
    return true;
},

/**
 * 判断数值类型，包括整数和浮点数
 */
isNumber:function(str){
    if(isDouble(str) || isInteger(str)) return true;
    return false;
},     

/**
 * 只能输入数字[0-9]
 */
isDigits:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^\d+$/);
    if(result==null)return false;
    return true;
},     

/**
 * 匹配money
 */
isMoney:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^(([1-9]\d*)|(([0-9]{1}|[1-9]+)\.[0-9]{1,2}))$/);
    if(result==null)return false;
    return true;
},
    
/**
 * 匹配phone
 */
isPhone:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配mobile
 */
isMobile:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^((\(\d{2,3}\))|(\d{3}\-))?((13\d{9})|(15\d{9})|(18\d{9}))$/);
    if(result==null)return false;
    return true;
},     

/**
 * 联系电话(手机/电话皆可)验证   
 */
isTel:function(text){
    if(isMobile(text)||isPhone(text)) return true;
    return false;
},

/**
 * 匹配qq
 */
isQq:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[1-9]\d{4,12}$/);
    if(result==null)return false;
    return true;
},     

/**
 * 匹配english
 */
isEnglish:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[A-Za-z]+$/);
    if(result==null)return false;
    return true;
},     

/**
 * 匹配integer
 */
isInteger:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[-\+]?\d+$/);
    if(result==null)return false;
    return true;
},     

/**
 * 匹配double或float
 */
isDouble:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[-\+]?\d+(\.\d+)?$/);
    if(result==null)return false;
    return true;
},     


/**
 * 匹配邮政编码
 */
isZipCode:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[0-9]{6}$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配URL
 */
isUrl:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"])*$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
 */
isPwd:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[a-zA-Z]\\w{6,12}$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 判断是否为合法字符(a-zA-Z0-9-_)
 */
isRightfulString:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[A-Za-z0-9_-]+$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配english
 */
isEnglish:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[A-Za-z]+$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配身份证号码
 */
isIdCardNo:function(num) {
	// if (isNaN(num)) {alert("输入的不是数字!"); return false;}
	var len = num.length, re;
	if (len == 15)
		re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/);
	else if (len == 18)
		re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/);
	else {
		alert("输入的数字位数不对。");
		return false;
	}
	var a = num.match(re);
	if (a != null) {
		if (len == 15) {
			var D = new Date("19" + a[3] + "/" + a[4] + "/" + a[5]);
			var B = D.getYear() == a[3] && (D.getMonth() + 1) == a[4]
					&& D.getDate() == a[5];
		} else {
			var D = new Date(a[3] + "/" + a[4] + "/" + a[5]);
			var B = D.getFullYear() == a[3] && (D.getMonth() + 1) == a[4]
					&& D.getDate() == a[5];
		}
		if (!B) {
			alert("输入的身份证号 " + a[0] + " 里出生日期不对。");
			return false;
		}
	}
	if (!re.test(num)) {
		alert("身份证最后一位只能是数字和字母。");
		return false;
	}
	return true;
}, 

/**
 * 匹配汉字
 */
isChinese:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[\u4e00-\u9fa5]+$/);
    if(result==null)return false;
    return true;
}, 

/**
 * 匹配中文(包括汉字和字符)
 */
isChineseChar:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[\u0391-\uFFE5]+$/);
    if(result==null)return false;
    return true;
},     

/**
 * 字符验证，只能包含中文、英文、数字、下划线等字符。
 */
stringCheck:function(str){
    if(str==null||str=="") return false;
    var result=str.match(/^[a-zA-Z0-9\u4e00-\u9fa5-_]+$/);
    if(result==null)return false;
    return true;
},     

/**
 * 过滤中英文特殊字符，除英文"-_"字符外
 */
stringFilter:function(str){
    var pattern = new RegExp("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
    var rs = "";
    for (var i = 0; i < str.length; i++) {
        rs = rs + str.substr(i, 1).replace(pattern, '');
    }
    return rs;
}, 

/**
 * 判断是否包含中英文特殊字符，除英文"-_"字符外
 */
isContainsSpecialChar:function(str){
    if(str==null||str=="") return false;
    var reg = RegExp(/[(\ )(\`)(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\+)(\=)(\|)(\{)(\})(\')(\:)(\;)(\')(',)(\[)(\])(\.)(\<)(\>)(\/)(\?)(\~)(\！)(\@)(\#)(\￥)(\%)(\…)(\&)(\*)(\（)(\）)(\—)(\+)(\|)(\{)(\})(\【)(\】)(\‘)(\；)(\：)(\”)(\“)(\’)(\。)(\，)(\、)(\？)]+/);   
    return reg.test(str);    
},

isNull:function(val){
	var s = new String(val);
	s = s.replace(/\s/g, "");
	if(s=="undefined" || s=="" || s.length==0){
		return true;
	}
	return false;
}
}