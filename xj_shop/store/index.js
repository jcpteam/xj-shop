import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		/**
		 * 是否需要强制登录
		 */
		forcedLogin: true,
		//用户账号信息
		userLoginName:'',
		userLoginPwd:'',
		//是否已经登录标志
		hasLogin: uni.getStorageSync('hasLogin') == undefined || uni.getStorageSync('hasLogin') == false ? false : true,//true--登录，false--未登录
		//用户登录后返回的个人信息对象
		userInfo: {},
		//用户登录成功的token
		sessiontoken:'',
		//用户购物车对象
		userCartMap:{},   //用户购物车对象
		
		//选择的订单列表页面的tab index
		selectOrderTabIndex:0,
		
		//历史登录账号列表
		historyUserNameArr:[],
		historyUserLoginMap:[],
		
		//用户修改的订单商品对象
		userModifyOrderId:'',
		userModifyOrderGoodsMap:{},   
	},
	mutations: {
		login(state, userInfo) {
			state.hasLogin = true;
			// state.userInfo = userInfo;
			// state.user_id = userInfo.id;
			uni.setStorage({//将用户信息保存在本地
				key: 'hasLogin',
				data: state.hasLogin
			})
			// console.log(state.userInfo);
		},
		logout(state) {
			state.hasLogin = false;
			// state.userInfo = {};
			// state.user_id = '';
			uni.setStorage({//将用户信息保存在本地
				key: 'hasLogin',
				data: state.hasLogin
			})
		},
		setOpenid(state, openid) {
			state.openid = openid
		}
	},
	actions: {}

})

export default store
