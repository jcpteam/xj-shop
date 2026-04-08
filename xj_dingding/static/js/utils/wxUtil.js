




import {post} from "./requestUtil.js";
import {isNull} from "./strUtil.js";
import {data_local} from "./cacheUtil.js"

import {isWeixin,isH5,isApp,getUser,setUser,getQuery,getConfig,rmLoginMsg,navigateTo} from './util.js'

function Core() {

}



var get_xjapp_User=function(func,param,g_prepage){
			var userInfo = getUser(); 					
			if(!!userInfo && !!userInfo.wxOpenId){
				if(!!func&&typeof func=="function"){func({"data":userInfo});}	
				return; 
			}
			
			if(!isWeixin()){return }
			
			var url = location.href.split("#")[0];
			var query = getQuery();
			if(!query["code"]){
				if(!!param) data_local("wxreg_param",param);
				if(!!g_prepage) data_local("pre_guider_page",g_prepage);
				
			    post("wxapi/authorize",{"dsturl":encodeURIComponent(url)},function(res){
					location.replace(res.data)	
			    })
			}else{
				var o={};
				o.code=query["code"];				
				let wxreg_param=data_local("wxreg_param");
				if(!!wxreg_param){
					for(let i in wxreg_param){
						let item=wxreg_param[i];
						if(!isNull(item))o[i]=item;					
					}
					data_local("wxreg_param",null);
				}
							
			    post("wxapi/userinfo",o,(resp)=>{ 
					 if(!!func&&typeof func=="function"){func(resp);}	
			    },function(r){})
			}
}

var __getwxuser__=function(func,pre_guider_page){
	let query=getQuery();
	let o={};
	for(var key in query){
		o[key]=query[key];
	}

	get_xjapp_User((res)=>{
		
		setUser(res.data);
		if(typeof func=="function"){func(res); }
		if(res.status==200){
	
			let page=data_local("pre_guider_page");
			if(!!page){
				data_local("pre_guider_page",null);
				if(isWeixin()){
					location.replace(page);
				}else{
					navigateTo(page);
				}			
			}
		}else{
			
		}			
	},o,pre_guider_page)
}
var __getwxuser=function(func,pre_guider_page){
	let _CONFIG=getConfig();
	let val=_CONFIG.xjapp_refresh_stat||'0';
	let local_version=data_local("xjapp_refresh_stat")||"0";
	if(local_version!=val){
		rmLoginMsg();		
		data_local("xjapp_refresh_stat",val)
	}
	__getwxuser__(func,pre_guider_page);
	
}




/* =================================h5 */




var ____appgetwxuser=function(func){
	let _CONFIG=getConfig();
	let val=_CONFIG.wxapp_refresh_stat||'0';
	let local_version=data_local("wxapp_refresh_stat")||"0";
	if(local_version!=val){
		rmLoginMsg();		
		data_local("wxapp_refresh_stat",val)
	}
	____appgetwxuser___();
}

var ____appgetwxuser___=function(func){
	let user=getUser();
	if(!!user&&!!user.wxAppOpenId){
		if(typeof func=="function"){func({"data":user}); }
		return;
	}
	uni.login({
	  provider: 'weixin',
	  success: function (loginRes) {
		  let auth=loginRes.authResult;
		  let o={"token":auth.access_token,"openid":auth.openid};
		  post("wxapi/userinfo_app",o,res=>{
			  if(typeof func=="function"){func(res); }
			 setUser(res.data);
		  },err=>{
			 
		  })
	  }
	});	
}



/* ===========================app */


var ____mpgetwxuser=function(func,param){
	
	let _CONFIG=getConfig();
	let val=_CONFIG.wxmp_refresh_stat||'0';
	let local_version=data_local("wxmp_refresh_stat")||"0";
	if(local_version!=val){
		rmLoginMsg();		
		data_local("wxmp_refresh_stat",val)
	}
	____mpgetwxuser___(func,param);
}

var ____mpgetwxuser___=function(func,param){
	let user=getUser();
	if(!!user&&!!user.wxMpOpenId){
		if(typeof func=="function"){func({"data":user}); }
		return;
	}
	
	uni.login({
	  provider: 'weixin',
	  success: function (loginRes) {
		  let o={"code":loginRes.code};
		  if(!!param){ for(let p_i in param){o[p_i]=param[p_i];}}
		  post("wxapi/userinfo_mp",o,res=>{
			  if(typeof func=="function"){func(res); }
			 setUser(res.data);		 
		  },err=>{
			 
		  })
	  }
	});	
}



Core.prototype.wxmpGetUser=function(e){  //小程序按钮点击获取用户信息
	let wx_user=e.detail.userInfo;
	let data=getUser();
	data.wxNickName=wx_user.nickName;
	data.wxHeadImg=wx_user.avatarUrl;
	post("user/update",data,res2=>{
		setUser(res2.data);
	})	
}
/* ========================================mp */


Core.prototype.wxLogin=function(func,pre_guider_page,mp_param){
	if(isH5()){
		__getwxuser(func,pre_guider_page);
	}else if(isApp()){
		____appgetwxuser(func);
	}else {
		____mpgetwxuser(func,mp_param);
	}
}

Core.prototype.updateWxLogin=function(func,pre_guider_page,mp_param){
	rmLoginMsg();
	return wxwxLogin(func,pre_guider_page,mp_param);

}




var wxUtil = new Core();
export default wxUtil
