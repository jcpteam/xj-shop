import CONFIG from "../data/constant.js"
function Core(){
	
}

Core.prototype.INIT_WS=function(e,handle,open,err){
	 uni.connectSocket({
		  url: CONFIG.WS+e,
		  success:(cpl)=>{
				console.log("suc====================")  
		  },
		  complete:(cpl)=>{
			  
		  },
		  fail:(fai)=>{
			    console.log("fai===================");			  
				wsUtil.INIT_WS(e,handle,open,err); 
		  }
	 });
	
	 uni.onSocketOpen((res)=>{
		 console.log("opem====================");
		 if(typeof open=="function")open(res); 
	 });
	 
	 uni.onSocketError((res)=>{
		 
	 }); 
	 uni.onSocketMessage((res)=>{
		 if(typeof handle=="function")handle(res); 
	 });
	 
	 uni.onSocketClose((res)=>{
	 		 
	 });	
}

Core.prototype.SEND_WS_MSG=function(msg){
	 uni.sendSocketMessage({
	 	data:msg,
	 	success:(cpl)=>{
	 		console.log("send_cpl========"+JSON.stringify(cpl))
	 	},
	 	fail:(fai)=>{
	 		console.log("send_fail========"+JSON.stringify(fai))
	 	}
	 });
}

Core.prototype.CLOSE_WS=function(msg){
	 uni.closeSocket();
}

/* ================================================= */

Core.prototype.createWebSocket=function(e,func,err){
	 return uni.connectSocket({
	   url: CONFIG.WS+e,
	   success:(cpl)=>{
		   if(typeof func=="function")func(cpl);
	   },
	   complete:(cpl)=>{},
	   fail:(fai)=>{
		   if(typeof err=="function")err(fai);
	   }
	 });
}
Core.prototype.onSocketOpen=function(func){
    uni.onSocketOpen((res)=>{if(typeof func=="function")func(res);});
}
Core.prototype.onSocketError=function(func){
	uni.onSocketError((res)=>{func(res);});
}
Core.prototype.sendSocketMessage=function(msg){
    uni.sendSocketMessage({ 
		data:msg,
		success:(cpl)=>{
			console.log("send_cpl========"+JSON.stringify(cpl))
		},
		fail:(fai)=>{
			console.log("send_fail========"+JSON.stringify(fai))
		}
	});
}
Core.prototype.onSocketMessage=function(func){
    uni.onSocketMessage((res)=>{if(typeof func=="function")func(res); });
}
Core.prototype.onSocketClose=function(func){
    uni.onSocketClose((res)=>{console.log("suc"); if(typeof func=="function")func(res);});
}
Core.prototype.closeSocket=function(){
    uni.closeSocket();
}

var wsUtil=new Core();
export default wsUtil