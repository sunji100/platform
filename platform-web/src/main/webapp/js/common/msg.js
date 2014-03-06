MessageBox = {
	getMessage : function(key) {
		if (undefined == promptMsg[key] || "" == promptMsg[key]) {
			return key;
		} else {
			return promptMsg[key];
		}
	},
	sucess : function(key,title) {
		$.ligerDialog.success(this.getMessage(key),this.getMessage(title));
	},
	warn : function(key,title) {
		$.ligerDialog.warn(this.getMessage(key),this.getMessage(title));
	},
	confirm:function(key,title,callback){
		$.ligerDialog.confirm(this.getMessage(key),this.getMessage(title),callback);
	}
}