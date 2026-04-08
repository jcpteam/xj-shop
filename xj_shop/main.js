import Vue from 'vue'
import App from './App'
import store from './store'
import util from './common/util.js'
import Mock from './common/mock.js'

Vue.prototype.$root = util.root
Vue.prototype.$get = util.get
Vue.prototype.$prePage = util.prePage
Vue.prototype.$msg = util.msg
Vue.prototype.$jump = util.jump
Vue.prototype.$isLogin = util.isLogin
Vue.prototype.$store = store
Vue.prototype.$mock = Mock
Vue.prototype.$getTotalPageNo = util.getTotalPageNo
Vue.prototype.$compareCurTime = util.compareCurTime

// 环境地址  http://csds.xiangjiamuye.com/
Vue.prototype.httpUrl = 'http://192.168.0.9:9999';
// Vue.prototype.httpUrl = 'http://csds.xiangjiamuye.com';

//发送接口请求带用户登录区域id
Vue.prototype.sendRequestWithDeptId = function(param, successCal, failedCal) {
	var userInfo = uni.getStorageSync('userInfo') || {};
	if (userInfo && userInfo.deptId && param) {
		param.url = param.url + '&loginUserDeptId=' + userInfo.deptId;
	}
	this.sendRequest(param, successCal, failedCal);
}

//发送接口请求
Vue.prototype.sendRequest = function(param, successCal, failedCal) {
	var serverUrl = Vue.prototype.httpUrl

	var token = uni.getStorageSync('sessiontoken') || '';
	var timestamp = Date.parse(new Date()); //时间戳
	var _self = this,
		url = param.url,
		method = param.method,
		data = param.data || {},
		header = {
			'content-type': param.contentType || "application/json",
			'Access-Token': token
		},
		hideLoading = param.hideLoading || false;

	data._t = timestamp / 1000

	//拼接完整请求地址
	var requestUrl = url
	if (url.indexOf('http') != 0) {
		requestUrl = serverUrl + url;
	}

	//请求方式:GET或POST(POST需配置header: {'content-type' : "application/x-www-form-urlencoded"},)
	if (method) {
		method = method.toUpperCase(); //小写改为大写
	} else {
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
			this.checkTokenExpress(res.data);
			typeof successCal == "function" && successCal(res);
		},
		fail: (e) => {
			console.log("网络请求fail:" + JSON.stringify(e));
			this.checkTokenExpress(e.data);
			// uni.showToast({
			//     'title': "网络请求fail:" + JSON.stringify(e)
			// });
			typeof failedCal == "function" && failedCal(e.data);
		},
		complete: () => {}
	});
}

//清空用户缓存信息，退出登录
Vue.prototype.clearCacheLogout = function() {
	// 清空缓存token
	uni.setStorageSync('hasLogin', false)
	uni.setStorageSync('sessiontoken', '');
	uni.setStorageSync('userInfo', {});
	uni.setStorageSync('userCartMap', {});
}

//检查token是否已经过期，过期清空用户数据，跳转登录页面
Vue.prototype.tokenExpress = false;
Vue.prototype.checkTokenExpress = function(data) {
	//api错误
	if (!this.tokenExpress && data.status && data.status == 500 && data.message && data.message.indexOf(
			'There is no session with id') != -1) {
		this.tokenExpress = true;
		// TOKEN 失效 - 重新登陆
		this.clearCacheLogout();

		uni.showToast({
			title: "登录过期，请重新登录！",
			icon: 'none',
		});

		setTimeout(() => {
			this.$jump('/pages/login/login')
		}, 2000);
	}
}

//初始化用户购物车数据
Vue.prototype.initUserCartData = function(successCal, failedCal) {
	var hasLogin = uni.getStorageSync('hasLogin');
	var userInfo = uni.getStorageSync('userInfo') || {};
	if (!hasLogin || !userInfo || !userInfo.memberId) {
		console.log("用户未登录....userInfo-->", userInfo)
		typeof failedCal == "function" && failedCal('-1');
		return;
	}
	var params = {
		// userId: userInfo.userId
		   userId: userInfo.memberId
	};
	var self = this;
	var url = '/shop/cartItem/list';
	this.sendRequest({
		'url': url,
		'method': 'POST',
		'data': params
	}, function(resp) {
		console.log(resp)
		if (resp.data.code === 0) {
			let userCartMap = {};
			if (resp.data.rows && resp.data.rows.length > 0) {
				//请求成功,初始化购物车数据
				resp.data.rows.forEach(item => {
					var finalPrice = item.price;
					var finalAmount = item.amount;
					var saleUnitId = item.unitId;
					if(item.storeGoodsQuotationGoods){
						var priceInfo = self.getGoodSalePrice(item.storeGoodsQuotationGoods);
						console.log('test--->',priceInfo);
						if(priceInfo && priceInfo.finalPrice && priceInfo.salePrice){
							finalPrice = priceInfo.finalPrice;
							finalAmount = item.quantity * priceInfo.salePrice; //销售总价
							saleUnitId = priceInfo.saleUnitId;
						}
					}
					
					//默认初始化数量为1的商品数据
					var goodInfo = {
						itemId: item.itemId,
						userId: item.userId,
						goodsId: item.goodsId,
						unitId: saleUnitId,
						spuNo: item.spuNo,
						quantity: item.quantity,
						amount: finalAmount,
						price: finalPrice,
						goodsImg: item.goodsImg,
						comment: item.comment,
					};
					userCartMap[item.goodsId] = goodInfo;
				});
			}
			uni.setStorageSync('userCartMap', userCartMap);
			console.log('initUserCartData--->userCartMap-->', userCartMap);
		}
		typeof successCal == "function" && successCal(resp);
	}, function(error) {
		typeof failedCal == "function" && failedCal(error.data);
	})
}

//添加商品到购物车
Vue.prototype.addGoodToCart = function(item, successCal, failedCal) {
	var userInfo = uni.getStorageSync('userInfo') || {};
	if (!userInfo || !userInfo.memberId) {
		console.log("添加到购物车失败，商户id为空")
		return;
	}

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
		// goodInfo.price = priceInfo.salePrice; 
		goodInfo.price = priceInfo.finalPrice; //结算价格存入数据库
		goodInfo.amount = item.cartQuantity * priceInfo.salePrice; //销售总价
		goodInfo.unitId = priceInfo.saleUnitId; //销售单位id
		goodInfo.goodsImg = item.goodsImg;
	} else {
		//如果缓存中没有，说明是点击加号按钮添加的，这里默认是加1个
		goodInfo = {
			// userId: userInfo.userId,
			userId: userInfo.memberId,   //子账号保存的购物车数据直接关联商户id
			goodsId: item.goodsId,
			unitId: priceInfo.saleUnitId,
			spuNo: item.goodsCode,
			quantity: 1,
			amount: priceInfo.salePrice,
			price: priceInfo.finalPrice,   //结算价格存入数据库
			goodsImg: item.goodsImg,
			comment:item.memberComment || '',
		};
	}
	console.log("addGoodToCart--->goodInfo-->", goodInfo);

	var self = this;
	var url = '/shop/cartItem/add';;
	this.sendRequest({
		'url': url,
		'method': 'POST',
		'data': goodInfo
	}, function(resp) {
		if (resp.data.code === 0) {
			//请求成功，更新内存数据
			if (resp.data.data) {
				goodInfo.itemId = resp.data.data.itemId;
			}
			//如果数量为0，则删除改商品缓存
			if (goodInfo.quantity <= 0) {
				goodInfo.itemId = null;
			} else {
				userCartMap[item.goodsId] = goodInfo;
			}
			uni.setStorageSync('userCartMap', userCartMap);
			console.log("addGoodToCart--->userCartMap-->", userCartMap);
			typeof successCal == "function" && successCal(resp);
		} else {
			typeof failedCal == "function" && failedCal(resp);
		}
		uni.hideLoading();
	}, function(error) {
		typeof failedCal == "function" && failedCal(error.data);
		uni.hideLoading();
	})
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
			if(ret.salePrice > 0 && String(ret.salePrice).indexOf('.') != -1){
				ret.salePrice = Number(ret.salePrice.toFixed(2));
			}
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

// http://csds.xiangjiamuye.com
Vue.prototype.parseGoodImgUrl = function(item) {
	// !item.goodsImg || item.goodsImg === undefined?defaultGoodsImg:item.goodsImg
	if (item && item.goodsImg) {
		if (item.goodsImg.indexOf(',') != -1) {
			var img = item.goodsImg.split(',')[0];
			return Vue.prototype.httpUrl + img;
			//return 'http://csds.xiangjiamuye.com' + img;
		} else {
			return Vue.prototype.httpUrl + item.goodsImg;
			//return 'http://csds.xiangjiamuye.com' + item.goodsImg;
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

Vue.config.productionTip = false
App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
