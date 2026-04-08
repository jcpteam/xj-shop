import CONFIG from "../data/constant.js"


import {data_local} from "./cacheUtil.js"

import {post} from "./requestUtil.js";
import {parseJSON} from "./objUtil.js";

function Core(){
	
}



/* ===============================number */	


/* ===============================string */


/* ===============================object */



/* ===============================datetime */

var getImgPathFromLocal=function(e){
	return "../../static/img/"+e;  //打包app时 从本地读取
}
var getImgPathFromLocalDeep3=function(e){
	return "../../../static/img/"+e;  //打包app时 从本地读取
}
var getImgPathFromOss=function(e){
	return "http://hwhypordstatic.oss-cn-hangzhou.aliyuncs.com/img/"+e;    //打包小程序时，删除本地的static 里的图片；从oss获取图片
	
}
Core.prototype.getImgPath=function(e){
	//return getImgPathFromLocal(e);  //打包app时 从本地读取
	return getImgPathFromOss(e)    //打包小程序时，删除本地的static 里的图片；从oss获取图片	
}
Core.prototype.getImgPathFromLocalDeep3=getImgPathFromLocalDeep3;
Core.prototype.getImgPathFromLocal=getImgPathFromLocal;
Core.prototype.getImgPathFromOss=getImgPathFromOss;


Core.prototype.getPlat=function(){
	let ret={};
	//ret.platform=uni.getSystemInfoSync().platform;
	
	// #ifdef H5 
	ret.plat="H5";
	// #endif 
	
	// #ifdef MP-WEIXIN 
	ret.plat="MP-WEIXIN";
	// #endif
	// #ifdef MP-ALIPAY
	ret.plat="MP-ALIPAY";
	// #endif
	// #ifdef MP-BAIDU
	ret.plat="MP-BAIDU";
	// #endif
	// #ifdef MP-TOUTIAO
	ret.plat="MP-TOUTIAO";
	// #endif
	// #ifdef MP-QQ
	ret.plat="MP-QQ";
	// #endif
	// #ifdef MP-360
	ret.plat="MP-360";
	// #endif
	
	// #ifdef APP-PLUS
	ret.plat="APP-PLUS";
	// #endif
	// #ifdef APP-PLUS-NVUE
	ret.plat="APP-PLUS-NVUE";
	// #endif 
		
	return ret.plat;
}

Core.prototype.isWeixin=function(){
	return (typeof navigator!="undefined" 
	&& typeof navigator.userAgent!="undefined"
	&& navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == "micromessenger"
	)
}
export function isWeixin(e) {return mainUtil.isWeixin(e);}



Core.prototype.isApp=function(){
	let b1= "APP-PLUS"==mainUtil.getPlat();
	let b2= "APP-PLUS-NVUE"==mainUtil.getPlat();
	return b1||b2;
};
export function isApp(e) {return mainUtil.isApp(e);}

Core.prototype.isH5=function(){
	return "H5"==mainUtil.getPlat();	
};
export function isH5(e) {return mainUtil.isH5(e);}

Core.prototype.getSysInfo=function(){
    let o=uni.getSystemInfoSync();	
	return o;
};

/* ===============================platinfo */



Core.prototype.getQuery = function(url){
	    if(!mainUtil.isH5())return {};
	
	    var obj = {};
		if(typeof url=="undefined")url= location.href;
		var query = url.substr(url.indexOf("?"));
		query=query.substr(1);
		var reg = /([^=&\s]+)[=\s]*([^=&\s]*)/g;
		while(reg.exec(query)){
			obj[RegExp.$1] = decodeURI(RegExp.$2);
		}	
	    return obj;
}
export function getQuery(e) {return mainUtil.getQuery(e);}


Core.prototype.openImage=function(o){
	data_local("show_img_param",o);
	mainUtil.navigateTo("/pages/common/show-img")
}
Core.prototype.jump_to_exter_page=function(src,title){
	if(mainUtil.isWeixin()){
		location.href=src;
	}else{
		let o={};
		o.src=src;o.title=title;
		data_local("page_url",o)
		mainUtil.navigateTo("/pages/common/web-page");
	}		
}

Core.prototype.navigateTo=function (url) {  
   let _url=url;if(url.indexOf("/pages")==-1)_url="/pages/"+url;
	uni.navigateTo({
		"url":_url,
		"complete":function(res){
			if("navigateTo:fail can not navigateTo a tabbar page"==res.errMsg){
				mainUtil.switchTab(_url);
			}
		}
	})
}
export function navigateTo(e) {return mainUtil.navigateTo(e);}

Core.prototype.redirectTo=function (url) {  
   let _url=url;if(url.indexOf("/pages")==-1)_url="/pages/"+url;
	uni.redirectTo({
		"url":_url,
		"complete":function(res){
			if(res.errMsg.indexOf("fail")>-1){
				mainUtil.switchTab(_url);
			} 
		}
	})
}
Core.prototype.reLaunch=function (url) {  
   let _url=url;if(url.indexOf("/pages")==-1)_url="/pages/"+url;
	uni.reLaunch({
	    url: _url,
		"complete":function(res){
			if("navigateTo:fail can not navigateTo a tabbar page"==res.errMsg){
				mainUtil.switchTab(_url);
			}
		}
	});
}
Core.prototype.switchTab=function (url) {  
   let _url=url;if(url.indexOf("/pages")==-1)_url="/pages/"+url;
	uni.switchTab({
		"url":_url
	})
}

/* ===============================navigetr */



/* ===============================datachace */


Core.prototype.comfirm = (_content='content',func,_title='提示')=>{
	uni.showModal({
		title: _title,
		content: _content,
		success: function (res) {
			if(res.confirm==true){
				func();
			}		
		}
	});	
}
Core.prototype.toast = (title, duration=1500, mask=false, icon='none')=>{
	//统一提示方便全局修改
	if(Boolean(title) === false){
		return;
	}
	uni.showToast({
		title,
		duration,
		mask,
		icon
	});
}

/* ===============================interactive */



/*=============================== xhr */





Core.prototype.getConst=function(){	
	return CONFIG;
}
export function getConst(e) {return mainUtil.getConst(e);}

Core.prototype.getConfig =function(){	
	let data=data_local("config_findall")||'{}';
	let li=data.rows;
	let CONFIG={}
	for(let  i in li){
		let item=li[i];
		CONFIG[item.name]=parseJSON(item.value);
	}
	delete CONFIG.cfg_weixin_menu;delete CONFIG.cfg_access_token;delete CONFIG.cfg_agt_fee;delete CONFIG.cfg_js_token;delete CONFIG.cfg_order_finish_day;
	delete CONFIG.cfg_verify_token;delete CONFIG.cfg_wd_point;delete CONFIG.join_for_mem;delete CONFIG.rebuy_for_mem;
	
	return CONFIG;
}
export function getConfig(e) {return mainUtil.getConfig(e);}

Core.prototype.getRsrc =function(){	
	let data=data_local("resource_findall")||'{}';
	let li=data.rows;
	let RSRC={}
	for(let  i in li){
		let item=li[i];
		RSRC[item.name]=parseJSON(item.value);
	}
	return RSRC;
}
export function getRsrc(e) {return mainUtil.getRsrc(e);}

Core.prototype.initConfig =function(func){  //程序入口函数，在调用其他函数之前调用;
	post("config/findall",{},res=>{
		let li=res.rows;
		let CONFIG={}
		for(let  i in li){
			let item=li[i];
			//let _val=item.value;let val=_val;if(objUtil.isLiStr(_val)){val=objUtil.getLi(_val);}else if(objUtil.isMapStr(_val)){val=objUtil.getMap(_val);}					
			CONFIG[item.name]=parseJSON(item.value);
		}
		delete CONFIG.cfg_weixin_menu;delete CONFIG.cfg_access_token;delete CONFIG.cfg_agt_fee;delete CONFIG.cfg_js_token;delete CONFIG.cfg_order_finish_day;
		delete CONFIG.cfg_verify_token;delete CONFIG.cfg_wd_point;delete CONFIG.join_for_mem;delete CONFIG.rebuy_for_mem;	
	
		post("resource/findall",{},res2=>{
			let li2=res2.rows;
			let RSRC={}
			for(let  i in li2){
				let item2=li2[i];
				/* let _val2=item2.value;let val2=_val2;if(objUtil.isLiStr(_val2)){val2=objUtil.getLi(_val2);}else if(objUtil.isMapStr(_val2)){val2=objUtil.getMap(_val2);}	RSRC[item2.name]=val2; */
				RSRC[item2.name]=parseJSON(item2.value);
			}		
			if(!!func&&typeof func=="function")func(CONFIG,RSRC);
		},null,true)
	},null,true)
}


/*=============================== other */




Core.prototype.getUser=function(){
	return data_local("user_info")||{};
};
export function getUser(e) {return mainUtil.getUser(e);}

Core.prototype.setUser=function(u){
	data_local("user_info",u);
};
export function setUser(e) {return mainUtil.setUser(e);}

Core.prototype.refreshUser = function (userid,func) {  //刷新local中的user缓存 
   post("user/find",{id:userid},function(res){
	   let b=!!res&&!!res.data;
	   if(b){
		   mainUtil.setUser(res.data);
		   if(!!func&&typeof func=="function"){func(res);}	
	   }    
	 },function(err){}) 
}

Core.prototype.rmLoginMsg=function(){
	mainUtil.setUser(null);
	data_local("token",null);
	data_local("password",null);
	data_local("pre_guider_page",null);
	data_local("pre_reg_page",null);
	
};
export function rmLoginMsg(e) {return mainUtil.rmLoginMsg(e);}


/* ========================================================= */




Core.prototype.updateAppVersion=function(){ //要在initconfig后面调用
	if(!mainUtil.isApp())return;
	let os_plat=mainUtil.getSysInfo().platform;
	var parseVersion=function(v){
		let v_li=v.split(".");
		let ret=0;		
		for(let i=0;i<v_li.length;i++){
			let a=parseFloat(v_li[i]);			
			let posi=v_li.length-1-i;
			let b=Math.pow(10,posi);		
			let item=a*b;		
			ret=ret+item;
		}
		return ret;	
	}
	if ("ios"==os_plat) {
		
	} else {
		//post("config/find",{"name":"cfg_android_version"},res=>{
				//let data=objUtil.getMap(res.data.value);
				//let v_code=parseVersion(data.version);
				
				let __version_conf=mainUtil.getConfig().cfg_android_version; //数据库中的版本号
				let version_conf=parseVersion(__version_conf);
				
				
				var __version_code=mainUtil.getConst().version_code; //代码中的版本号
				var version_code=parseVersion(__version_code);
										
				if(parseFloat(version_conf)>parseFloat(version_code)){				
					var installWgt=function(path, force) {
						if( force ) plus.nativeUI.showWaiting('正在安装');
						plus.runtime.install(path,{},function() {
								if( force ){
									plus.nativeUI.closeWaiting();
									plus.nativeUI.alert('应用资源更新完成！', function() {								
										plus.runtime.restart();
									});
								}
							},
							function(e) {
								if( force ){
									plus.nativeUI.closeWaiting();
									plus.nativeUI.alert('资源更新安装失败[' + e.code + ']：' + e.message);
								}
							}
						);
					}
					var downloadUpdate=function (sourceurl, force) {										
						if( force ) plus.nativeUI.showWaiting('正在下载');
						plus.downloader.createDownload(sourceurl, { filename: '_doc/update/' }, function(d, status) {
								if( force ) plus.nativeUI.closeWaiting();
								if (status == 200) {
									installWgt(d.filename, force); //安装更新包
								} else {
									if( force ) plus.nativeUI.alert('下载资源更新包失败！');
								}
							}).start();
					}				
					uni.showModal({
						title: '版本更新',
						content: '您有一个新版本需要更新。点击确定开始更新！',
						success: re => {
							if (re.confirm) {
								downloadUpdate(data.url, true);
							} else {
							   // plus.runtime.quit();
							}
						}
					});				
				}							
			//})						    
		}	
}










var mainUtil=new Core();
export default mainUtil