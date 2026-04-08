function Core(){
	
}


Core.prototype.getRandom=function(n,m){			
	return Math.floor(Math.random()*(m-n+1)+n)
}
Core.prototype.round2pos=function(num){
	var ret= Math.round(num*100)/100;
	return ret;
}

Core.prototype.getDuration=function(time){ //入参为毫秒

	if(!time){
		return "";
	}
	let n=parseFloat(time);
	if(n<1000){
		return time+"ms";
	}else if(n>(1000-1)&&n<1000*60){
		return numUtil.round2pos(n/1000)+"s";
	}else if(n>(1000*60-1)&&n<1000*60*60){
		return numUtil.round2pos(n/(1000*60))+"分钟";
	}else {
		return numUtil.round2pos(n/(1000*60*60))+"小时";
	}	
}

Core.prototype.rangeNum=function(floor,ceil,val){			
	let b1=parseFloat(val)>parseFloat(floor);
	let b2=parseFloat(val)<parseFloat(ceil);
	

	if(b1&&b2){
		return true;
	}else {
		return false;
	}
}
Core.prototype.rangeCeil=function(floor,ceil,val){			

	if(parseFloat(val)==parseFloat(ceil)){
		return true;
	}
	
	return numUtil.rangeNum(floor,ceil,val);
}

Core.prototype.rangeFloor=function(floor,ceil,val){			
	if(parseFloat(val)==parseFloat(floor)){
		return true;
	}
	
	return numUtil.rangeNum(floor,ceil,val);
}


export function rangeCeil(e,b,c) {return numUtil.rangeCeil(e,b,c);}


var numUtil=new Core();
export default numUtil



