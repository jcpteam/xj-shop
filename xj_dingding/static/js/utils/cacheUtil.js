function Core(){
	
}


Core.prototype.data_session=function(k,d){
	
	if(typeof d=="undefined"){
		var o = sessionStorage.getItem(k);
		if(o==null){
			return null;
		}else{
			o = JSON.parse(o)
			return o.data||o;
		}		
		
	}else if(null==d){
		return sessionStorage.removeItem(k)
	}else{
		var m = {};
		m.type = "string";
		m.data = d;
		sessionStorage.setItem(k, JSON.stringify(m));
	}
}

Core.prototype.data_local=function(k,d){
	if(typeof d=="undefined"){
		var o = uni.getStorageSync(k)
		//console.log(o==null||!o)
		if(o==null||!o){
			return null;
		}else{
			o = JSON.parse(o);
			return o;
		}						
	}else if(null==d){
		return uni.removeStorageSync(k)
	}else{
		return uni.setStorageSync(k,JSON.stringify(d))
	}
}
export function data_local(a,b) {return cacheUtil.data_local(a,b);}

var cacheUtil=new Core();
export default cacheUtil