function Core(){
	
}

Core.prototype.isIdcard=function(str){
	var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	if(reg.test(str) === false) {  
	 return false; 
	}else{
		return true;
	}
	
}

Core.prototype.replaceSpace=function(str){
	 let _str=str.replace(/\s+/g,"");
	 return _str;
}
Core.prototype.isNull=function(e){
	 var ret=false;
	 if(typeof e=="undefined"||e==null||!e){
	  ret=true;
	 }
	 return ret;
};
export function isNull(e) {return strUtil.isNull(e);}

Core.prototype.notNull=function(e){
	var ret=strUtil.isNull(e);
	return !ret;
}




Core.prototype.isEmpty=function(e){
	let str=strUtil.replaceSpace(e);
	return strUtil.isNull(str);
}
Core.prototype.notEmpty=function(e){
	return !strUtil.isEmpty(e);
}

Core.prototype.copyTxtByH5=function(content) {
	let textarea = document.createElement("textarea")
	textarea.value = content
	textarea.readOnly = "readOnly"
	document.body.appendChild(textarea)
	textarea.select() // 选择对象
	textarea.setSelectionRange(0, content.length) //核心
	let result = document.execCommand("copy") // 执行浏览器复制命令
	textarea.remove();
	
	return result  
}
Core.prototype.copyTxtByApp=function(content) {
	uni.setClipboardData({
	   data: content,
	   success: function () {
		   
	   },
       fail(){
	   console.log('此功能不支持h5!');
	 }
	});
}


Core.prototype.endwith=function(str,length,char='...'){
	if(strUtil.isNull(str))return "";	
	let ret="";
	if(str.length>length){
		ret=str.substring(0,length)+char;
	}else{
		ret=str;
	}
	return ret;
}



var strUtil=new Core();
export default strUtil