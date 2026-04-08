import Vue from 'vue'
import App from './App'

import store from "./store/index.js";


Vue.config.productionTip = false
App.mpType = 'app'


import util from "./static/js/utils/util.js"
import commonutil from './common/util.js'

Vue.prototype.getPlat = util.getPlat;
Vue.prototype.isWeixin = util.isWeixin;
Vue.prototype.isApp = util.isApp;
Vue.prototype.isH5 = util.isH5;
Vue.prototype.getSysInfo = util.getSysInfo;

Vue.prototype.getQuery = util.getQuery;
Vue.prototype.$naviExter = util.jump_to_exter_page;

Vue.prototype.$openImage = util.openImage;
Vue.prototype.$navigateTo = util.navigateTo;
Vue.prototype.$redirectTo = util.redirectTo;
Vue.prototype.$switchTab = util.switchTab;
Vue.prototype.$reLaunch = util.reLaunch;

Vue.prototype.$toast = util.toast;
Vue.prototype.$comfirm = util.comfirm;

Vue.prototype.initConfig =util.initConfig
Vue.prototype.getConfig=util.getConfig;
Vue.prototype.getConst=util.getConst;
Vue.prototype.getUser = util.getUser;
Vue.prototype.setUser = util.setUser;
Vue.prototype.$refreshUser = util.refreshUser

Vue.prototype.getImgPath = util.getImgPath;
Vue.prototype.$rmLoginMsg = util.rmLoginMsg;

Vue.prototype.$updateAppVersion=util.updateAppVersion;

Vue.prototype.$getTotalPageNo = commonutil.getTotalPageNo
Vue.prototype.$compareCurTime = commonutil.compareCurTime

Vue.prototype.isChecking=function(k,type){
	let cf=util.getConfig();
	if(1==cf.mp_check_stat){
		return true;
	}else{
		return false;
	}
}



import numUtil from "./static/js/utils/numUtil.js"
Vue.prototype.$getRandom = numUtil.getRandom;
Vue.prototype.$round2pos = numUtil.round2pos;

import strUtil from "./static/js/utils/strUtil.js"
Vue.prototype.$isNull = strUtil.isNull;
Vue.prototype.$notNull = strUtil.notNull;
Vue.prototype.$endwith = strUtil.endwith;
Vue.prototype.$copyTxt = function(ct){ if(util.isH5()){ strUtil.copyTxtByH5(ct);}else{strUtil.copyTxtByApp(ct); }}


import objUtil from "./static/js/utils/objUtil.js"
Vue.prototype.$pushNoRepeat = objUtil.pushNoRepeat;
Vue.prototype.$delArrItem = objUtil.delArrItem;
Vue.prototype.$delArrItemByKv = objUtil.delArrItemByKv;
Vue.prototype.getLi = objUtil.getLi;
Vue.prototype.getMap = objUtil.getMap;
Vue.prototype.$copyObj = objUtil.copyObj;
Vue.prototype.$combineObjectInList = objUtil.combineObjectInList;

import cacheUtil from "./static/js/utils/cacheUtil.js"
Vue.prototype.$dataLocal = cacheUtil.data_local;
Vue.prototype.$dataSession = cacheUtil.data_session;

import dateUtil from "./static/js/utils/dateUtil.js"
Vue.prototype.$getDateStr = dateUtil.getDateStr;
Vue.prototype.$getYmd = dateUtil.getYmd;
Vue.prototype.$getDateParam = dateUtil.getDateParam;

import requestUtil from "./static/js/utils/requestUtil.js"
Vue.prototype.$post = requestUtil.post;
Vue.prototype.$upload = requestUtil.upLoad;
Vue.prototype.$chooseImageUpload = requestUtil.choose_image_upload;

import wxUtil from "./static/js/utils/wxUtil.js"
Vue.prototype.wxLogin = wxUtil.wxLogin;
Vue.prototype.updateWxLogin = wxUtil.updateWxLogin;
Vue.prototype.wxmpGetUser= wxUtil.wxmpGetUser;//已经弃用,使用mp-auth获取微信头像
 


import wsUtil from "./static/js/utils/wsUtil.js"
Vue.prototype.INIT_WS = wsUtil.INIT_WS;
Vue.prototype.SEND_WS_MSG = wsUtil.SEND_WS_MSG;
Vue.prototype.CLOSE_WS= wsUtil.CLOSE_WS;

Vue.prototype.$jump = commonutil.jump



/**
 *  因工具函数属于公司资产, 所以直接在Vue实例挂载几个常用的函数
 *  所有测试用数据均存放于根目录json.js
 *  
 *  css部分使用了App.vue下的全局样式和iconfont图标，有需要图标库的可以留言。
 *  示例使用了uni.scss下的变量, 除变量外已尽量移除特有语法,可直接替换为其他预处理器使用
 */
const msg = (title, duration=1500, mask=false, icon='none')=>{
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
const json = type=>{
	//模拟异步请求数据
	return new Promise(resolve=>{
		setTimeout(()=>{
			resolve(Json[type]);
		}, 500)
	})
}

const prePage = ()=>{
	let pages = getCurrentPages();
	let prePage = pages[pages.length - 2];
	// #ifdef H5
	return prePage;
	// #endif
	return prePage.$vm;
}


// 环境地址
// Vue.prototype.httpUrl = 'http://192.168.0.10:9999';
Vue.prototype.httpUrl = 'http://csds.xiangjiamuye.com';

Vue.prototype.sendRequest = function(param, successCal, failedCal){
    var serverUrl = Vue.prototype.httpUrl
	
    var token = uni.getStorageSync('sessiontoken') || 'unknown';
    var timestamp = Date.parse(new Date());//时间戳
    
    var _self = this, 
        url = param.url,
        method = param.method,
        data = param.data || {}, 
        header = {
            'content-type' : param.contentType || "application/json", 
            'Access-Token': token
        },
        hideLoading = param.hideLoading || false;
        
        data._t = timestamp / 1000
        
    //拼接完整请求地址
    var requestUrl = url
    if(url.indexOf('http') != 0){
        requestUrl = serverUrl + url;
    }
    
    //请求方式:GET或POST(POST需配置header: {'content-type' : "application/x-www-form-urlencoded"},)
    if(method){
        method = method.toUpperCase();//小写改为大写
    }else{
        method = "POST";
    }
    
    //用户交互:加载圈
    //网络请求
    uni.request({
        url: requestUrl,
        method: method,
        header: header,
        data: data,
        success: res => {
			if(res && res.data && res.data.status == 500 && (res.data.message && res.data.message.indexOf('There is no session with id') > -1)){
			    // TOKEN 失效 - 重新登陆
			    // 清空缓存token
			    uni.setStorageSync('sessiontoken', '');
			    uni.setStorageSync('user_info', {});
			    uni.showToast({
			        'title': "请重新登录"
			    });
			    uni.reLaunch({
			        url: '/pages/login/login'
			    });
			    return
			}
			
			if (res.data && typeof(res.data)=='string' && res.data.indexOf('<html>') > -1) {
				// TOKEN 失效 - 重新登陆
				// 清空缓存token
				uni.setStorageSync('sessiontoken', '');
				uni.setStorageSync('user_info', {});
				uni.showToast({
				    'title': "请重新登录"
				});
				uni.reLaunch({
				    url: '/pages/login/login'
				});
				return
			}
    //         if (res.data && res.data.code != 0) {
				// // api错误
    //             // uni.showToast({
    //             //     'title': "" + res.data.msg
    //             // });
    //             typeof failedCal == "function" && failedCal(res);
    //             return
    //         }
            typeof successCal == "function" && successCal(res);
        },
        fail: (e) => {
            console.log("网络请求fail:" + JSON.stringify(e));
            // uni.showToast({
            //     'title': "网络请求fail:" + JSON.stringify(e)
            // });
            typeof failedCal == "function" && failedCal(e.data);
        },
        complete: () => {
        }
    });
}

// 从商城过来的逻辑 start============================================================
// http://csds.xiangjiamuye.com
Vue.prototype.parseGoodImgUrl = function(item) {
	// !item.goodsImg || item.goodsImg === undefined?defaultGoodsImg:item.goodsImg
	if (item && item.goodsImg) {
		if (item.goodsImg.indexOf(',') != -1) {
			var img = item.goodsImg.split(',')[0];
			return Vue.prototype.httpUrl + img;
		} else {
			return Vue.prototype.httpUrl + item.goodsImg;
		}
	} else {
		return '/static/xj_logo.jpg';
	}
}

Vue.prototype.parseGoodImgUrlList = function(item) {
	var imgs = [];
	if (item && item.goodsImg) {
		if (item.goodsImg.indexOf(',') != -1) {
			var img = item.goodsImg.split(',');
			if (img && img.length > 0) {
				for (var i = 0; i < img.length; i++) {
					imgs.push(Vue.prototype.httpUrl + img[i]);
				}
			}
		} else {
			imgs.push(Vue.prototype.httpUrl + item.goodsImg);
		}
	}

	if (imgs && imgs.length > 0) {

	} else {
		imgs.push('/static/xj_logo.jpg');
	}
	return imgs;
}


//获取商品的价格单位信息
Vue.prototype.getGoodSalePrice = function(item) {
	//返回对象：包含价格，单位id，单位名称
	var ret = {};
	if (item.unitIds) {
		var saleUnitId = item.unitIds;
		//判断是否有多个销售单位
		if (item.unitIds.indexOf(',') != -1) {
			//有多个单位默认取第一个
			saleUnitId = item.unitIds.split(',')[0];
		}

		if (saleUnitId === item.mainUnitId.toString()) {
			//销售单位与主单位相同，直接使用主单位控价价格
			ret.salePrice = item.settingPrice;
			ret.saleUnitId = saleUnitId;
			ret.saleUnitName = item.mainUnitName;
		} else if (item.subUnitId && saleUnitId === item.subUnitId.toString() && item.subCaseMain) {
			//销售单位与副单位相同，且有转换比例，使用转换后的价格显示
			ret.salePrice = item.settingPrice * item.subCaseMain;
			ret.saleUnitId = saleUnitId;
			ret.saleUnitName = item.subUnitName;
		} else {
			//其他还是使用主单位价格显示
			ret.salePrice = item.settingPrice;
			ret.saleUnitId = item.mainUnitId;
			ret.saleUnitName = item.mainUnitName;
		}
	} else {
		//没有销售单位id，直接使用控价价格
		ret.salePrice = item.settingPrice;
		ret.saleUnitId = item.mainUnitId;
		ret.saleUnitName = item.mainUnitName;
	}
	
	//结算价格，结算价格就是主单位价格，不需要转换，后台订单会进行转换
	ret.finalPrice = item.settingPrice;  
	
	return ret;
}

//添加商品到购物车
Vue.prototype.addGoodToCart = function(item, successCal, failedCal) {
	//先发送接口请求保存数据
	uni.showLoading({
		title: '正在保存...'
	});
	//获取商品销售价格信息
	var priceInfo = this.getGoodSalePrice(item);

	var goodInfo = {};
	var userCartMap = uni.getStorageSync('userCartMap') || {};
	//如果已经存在缓存中，则先取出来，需要用到里面的itemId
	if (userCartMap[item.goodsId]) {
		//缓存中有的话，说明是点击数字加减更新商品的数量
		goodInfo = userCartMap[item.goodsId];
		goodInfo.quantity = item.cartQuantity;
		goodInfo.price = priceInfo.salePrice; //销售价格
		goodInfo.amount = item.cartQuantity * priceInfo.salePrice; //销售总价
		goodInfo.unitId = priceInfo.saleUnitId; //销售单位id
		goodInfo.goodsImg = item.goodsImg;
	} else {
		//如果缓存中没有，说明是点击加号按钮添加的，这里默认是加1个
		goodInfo = {
			userId: item.userId,
			goodsId: item.goodsId,
			unitId: priceInfo.saleUnitId,
			spuNo: item.goodsCode,
			quantity: 1,
			amount: priceInfo.salePrice,
			price: priceInfo.salePrice,
			goodsImg: item.goodsImg,
			comment:item.memberComment || '',
		};
	}
	console.log("addGoodToCart--->goodInfo-->", goodInfo);
	userCartMap[item.goodsId] = goodInfo;
	uni.setStorageSync('userCartMap', userCartMap);
	uni.hideLoading();
	typeof successCal == "function" && successCal(goodInfo);
}
// ==============================================================






Vue.config.productionTip = false
Vue.prototype.$fire = new Vue();
Vue.prototype.$store = store;
Vue.prototype.$api = {msg, json, prePage};
Vue.prototype.$api = {msg, json, prePage};

App.mpType = 'app'

const app = new Vue({
    ...App, store
})
app.$mount()