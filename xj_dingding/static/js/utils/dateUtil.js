function Core(){
	
}


Core.prototype.formatTime=function(time, fmt) {
	if(time == null) {
		return;
	}
	var fmt = fmt ? fmt : 'yyyy-MM-dd';
	var time = new Date(time);
	var z = {
		M: time.getMonth() + 1,
		d: time.getDate(),
		h: time.getHours(),
		m: time.getMinutes(),
		s: time.getSeconds()
	};
	fmt = fmt.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
		return((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1))).slice(-2);
	});
	return fmt.replace(/(y+)/g, function(v) {
		return time.getFullYear().toString().slice(-v.length);
	});
};

Core.prototype.add_o=function(i){
	return parseInt(i)<10?("0"+i):i;
}
Core.prototype.getDateFormat= function (date,fmt='yyyy-MM-dd hh:mm:ss') { //保证所有浏览器都能得到一个时间格式
    var y = date.getFullYear(), m = date.getMonth() + 1,d = date.getDate(), h =date.getHours(),mi=date.getMinutes(),s=date.getSeconds();
	m=dateUtil.add_o(m);d=dateUtil.add_o(d);h=dateUtil.add_o(h);mi=dateUtil.add_o(mi);s=dateUtil.add_o(s);
	let li=fmt.split(" ");
	if(!!li&&!!li[0]){
		let li3=li[0].split("-");
		let fmt_y=li3[0];let fmt_m=li3[1];let fmt_d=li3[2];
		if(fmt_m=="01")m="01";
		if(fmt_d=="01")d="01";
	}
	if(!!li&&!!li[1]){
		let li2=li[1].split(":");
		let fmt_h=li2[0];let fmt_mi=li2[1];let fmt_s=li2[2];
		if(fmt_h=="00")h="00";else if(fmt_h=="23")h="23";
		if(fmt_mi=="00")mi="00";else if(fmt_mi=="59")mi="59";
		if(fmt_s=="00")s="00";else if(fmt_s=="59")s="59";
	}
	if(!li[1]){
		h=null;mi=null;s=null;
	}
    var str=y + "-" + m + "-" + d;
    if(null!=h){
    	str=str+" "+h;
    	if(null!=mi){
    		str=str+":"+mi;
    		if(null!=s){
    			str=str+":"+s;
    		}		
    	}
    }
    return str;
}

Core.prototype.getDateObj=function(_date){//
	let date=null;
	let type=typeof _date;
	if("object"==type){
		date=_date;
	}else{
		if("number"==type){
			date=new Date(parseFloat(_date));
		}else if(type=="string"){
			if(_date.indexOf("/")>-1||_date.indexOf('-')>-1){//ios适配
				_date=_date.replace(/\-/g, '/').replace('.0', '');	
				date=new Date(_date);
			}
		}
	}	
	return date;
}

Core.prototype.getDateStr=function(_date,fmt='yyyy-MM-dd hh:mm:ss'){//在不确定后端返回时间 格式 到底是时间戳还是 时间字符串的时候调用此函数	
	let ret="";
	
	/* let date=null;
	let type=typeof _date;
	if("object"==type){
		date=_date;
	}else{
		if("number"==type){
			date=new Date(parseFloat(_date));
		}else if(type=="string"){
			if(_date.indexOf("/")>-1||_date.indexOf('-')>-1){
				_date=_date.replace(/\-/g, '/').replace('.0', '');	
				date=new Date(_date);
			}
		}
	} */	
	
	let date=dateUtil.getDateObj(_date);
	ret=dateUtil.getDateFormat(date,fmt);
	return ret;
}
Core.prototype.getYmd= function(d){
	/* let t_d=typeof d;
	let date
	if(t_d=="object"){
		date=d;
	}else{
		d=d.replace(/\-/g, '/').replace('.0', '');
		date=new Date(d);
	} */
	return dateUtil.getDateStr(d,'yyyy-MM-dd');
}
Core.prototype.getYmd2= function(d){
	/* let t_d=typeof d;
	let date
	if(t_d=="object"){
		date=d;
	}else{
		d=d.replace(/\-/g, '/').replace('.0', '');
		date=new Date(d);
	} */
	return dateUtil.getDateStr(d,'yyyy-MM-dd').replace(/-/g,'/');
}
Core.prototype.getWeekStartDate=function(time){ //获得本周的开端日期
   var now = new Date(time); //当前日期   
	var nowDayOfWeek = now.getDay(); //今天本周的第几天  
	var nowYear = now.getYear(); //当前年   
	var nowMonth = now.getMonth(); //月   
	var nowDay = now.getDate(); //日   
	var beginHour="09:00:00";  
	var endHour="23:59:59";  
	  
	 nowYear += (nowYear < 2000) ? 1900 : 0; //  
	 nowDayOfWeek = nowDayOfWeek==0?7:nowDayOfWeek; // 如果是周日，就变成周七  
	   
	   
	    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek+1);  
	    //return weekStartDate.format("yyyy-MM-dd hh:mm:ss"); 
		  return dateUtil.getDateStr(weekStartDate,"yyyy-MM-dd hh:mm:ss");	
}  
	   
Core.prototype.getWeekEndDate=function(time){//获得本周的停止日期  
		var now = new Date(time); //当前日期   
	    var nowDayOfWeek = now.getDay(); //今天本周的第几天  
	    var nowYear = now.getYear(); //当前年   
	    var nowMonth = now.getMonth(); //月   
	    var nowDay = now.getDate(); //日   
	    var beginHour="09:00:00";  
	    var endHour="23:59:59";  
	  
	     nowYear += (nowYear < 2000) ? 1900 : 0; //  
	     nowDayOfWeek = nowDayOfWeek==0?7:nowDayOfWeek; // 如果是周日，就变成周七  
		
	    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek+1));  
	    //return weekEndDate.format("yyyy-MM-dd hh:mm:ss");  
		
		return dateUtil.getDateStr(weekEndDate,"yyyy-MM-dd hh:mm:ss");	
}
Core.prototype.getDateParam = function(c){
	/* let o={};
	let now=new Date();
	let nextmonth=new Date(now.getFullYear(),now.getMonth()+1,now.getDate());
	let nextyear=new Date(now.getFullYear()+1,now.getMonth(),now.getDate());
	
	o.todaybegin=dateUtil.getDateStr(now,"yyyy-MM-dd 00:00:00");
	o.todayend=dateUtil.getDateStr(now,"yyyy-MM-dd 23:59:59");
	o.monthbegin=dateUtil.getDateStr(now,"yyyy-MM-01 00:00:00");
	o.monthend=dateUtil.getDateStr(nextmonth,"yyyy-MM-01 00:00:00");
	o.yearbegin=dateUtil.getDateStr(now,"yyyy-01-01 00:00:00");	
	o.yearend=dateUtil.getDateStr(nextyear,"yyyy-01-01 00:00:00");
	
	
	o.weekbegin=dateUtil.getWeekStartDate(new Date().getTime());
	o.weekend=dateUtil.getWeekEndDate(new Date().getTime());
	
	return o; */
	
	let o={};
	let now=c||new Date();
	let nextmonth=new Date(now.getFullYear(),now.getMonth()+1,now.getDate());
	let nextyear=new Date(now.getFullYear()+1,now.getMonth(),now.getDate());
	
	o.todaybegin=dateUtil.getDateStr(now,"yyyy-MM-dd 00:00:00");
	o.todayend=dateUtil.getDateStr(now,"yyyy-MM-dd 23:59:59");
	o.monthbegin=dateUtil.getDateStr(now,"yyyy-MM-01 00:00:00");
	let __monthend=dateUtil.getDateStr(nextmonth,"yyyy-MM-01 00:00:00");
	
	
	let date=dateUtil.getDateObj(__monthend);
	o.monthend=dateUtil.getDateStr(date.getTime()-1000*60);
	
	o.yearbegin=dateUtil.getDateStr(now,"yyyy-01-01 00:00:00");	
	o.yearend=dateUtil.getDateStr(nextyear,"yyyy-01-01 00:00:00");
	
	
	o.weekbegin=dateUtil.getWeekStartDate(new Date().getTime());
	o.weekend=dateUtil.getWeekEndDate(new Date().getTime());
	
	return o;
}

var dateUtil=new Core();
export default dateUtil