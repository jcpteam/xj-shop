import {isNull} from "./strUtil.js";


function Core(){
	
}

Core.prototype.pushNoRepeat=function(li, item) {
			var isRepeat = false;
			for (var i in li) {
				var ite = li[i];
				if (item == ite) isRepeat = true
			}
			if (isRepeat == false) {
				li.push(item)
			}
			return li;
	
}
Core.prototype.delArrItem=function(arr, item) { //根据k-v对来删除数组元素
		let n_arr=[];
		for(var i in arr) {
			if(arr[i] != item) {
				n_arr.push(arr[i]);
			}
		}
		return n_arr;
}
Core.prototype.delArrItemByKv=function(arr, k, v) { //根据k-v对来删除数组元素
		let n_arr=[];
		for(var i in arr) {
			if(arr[i][k] != v) {
				n_arr.push(arr[i]);
			}
		}
		return n_arr;
}

Core.prototype.isMapStr=function(_str){
	if(isNull(_str))return false;
	if(_str[0]!='{')return false;
	return _str.indexOf(":")>-1&&_str.indexOf("{")>-1&&_str.indexOf("}")>-1;
}
Core.prototype.isLiStr=function(_str){
	if(isNull(_str))return false;
	if(_str[0]!='[')return false;
	return _str.indexOf("[")>-1&&_str.indexOf("]")>-1;
}

Core.prototype.isJSONString=function(str){
	if(isNull(str))return false;
	let ret=false;
	if(objUtil.isMapStr(str)||objUtil.isLiStr(str))ret=true;
	return ret;
}
Core.prototype.parseJSON=function(str){  //主要是为了解决JSON.parse 解析非JSON串的报错问题
	if(objUtil.isJSONString(str)){
		return JSON.parse(str);
	}else{
		return str;
	}
}
export function parseJSON(e) {return objUtil.parseJSON(e);}

Core.prototype.getLi=function(arr){
	let c_list=[];	
	let local_c_list=[];	
	if(typeof arr=="object")local_c_list=arr;
	if(typeof arr=="string")local_c_list=objUtil.parseJSON(arr);
		
	if(typeof local_c_list=="object"&&local_c_list.length>0){
		c_list=local_c_list
	}
	return c_list;
}

Core.prototype.getMap=function(obj){
	let c_obj={};
	let local_c_list={};
	if(typeof obj=="object")local_c_list=obj;
	if(typeof obj=="string")local_c_list=objUtil.parseJSON(obj);		
	if(typeof local_c_list=="object"&&typeof local_c_list!="undefined"){
		c_obj=local_c_list
	}
	return c_obj;
}

Core.prototype.copyObj =function(obj) {          //obj arr 对象的克隆（区分于指针赋值）
       /*  if(obj.constructor == Array) {
            var a = [];
            for(var i in obj) {
                a.push(obj[i]);
            }
            return a;
        } else {
            var o = {}
            for(var i in obj){
                o[i] = obj[i];
            }
            return o;
        } */
       let ret="";
	   ret=JSON.parse(JSON.stringify(obj));
	   return ret;
}

Core.prototype.combineObjectInList =function(arr, item, list) {   //数组去除重复，item为重复判定项，list为重复记录需要累加的值的数组
        var obj = {};            
        var a = [];
        for(var i in arr) {
            if(!obj[arr[i][item]]) {
                obj[arr[i][item]] = objUtil.copyObj(arr[i]);  //数组克隆
            } else if(!!obj[arr[i][item]]) {
                for(var j in list) {          
                    obj[arr[i][item]][list[j]] = obj[arr[i][item]][list[j]] + parseFloat(arr[i][list[j]]);
                }
            }
        }
        for(var k in obj) {
            a.push(obj[k]);
        }
        return a;
}


var objUtil=new Core();
export default objUtil