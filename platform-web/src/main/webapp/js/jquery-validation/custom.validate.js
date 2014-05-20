$(function(){
	$.validator.addMethod(
            "isIdCardNo",
            function (value, element, params)
            {
            	return this.optional(element) || isIdCardNo(value);
            },
            "请正确输入身份证号码"
    );
	$.validator.addMethod(
            "compareStartDate",
            function (value, element, params)
            {
            	return this.optional(element) || compareStartDate($(params).val(),value);
            },
            "结束日期应大于或等于开始日期"
    );
});

function compareStartDate(start,end){
	var startDate = StringToDate(start);
	if(startDate.DateDiff("d", end) < 0){
		return false;
	}
	return true;
}
/**
 * 匹配身份证号码
 */
function isIdCardNo(num) {
	// if (isNaN(num)) {alert("输入的不是数字!"); return false;}
	var len = num.length, re;
	if (len == 15)
		re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{2})(\w)$/);
	else if (len == 18)
		re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\w)$/);
	else {
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
			return false;
		}
	}
	if (!re.test(num)) {
		return false;
	}
	return true;
} 