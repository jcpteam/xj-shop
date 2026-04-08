<template>
	<view class="content">
		<view class="login-bg">
			<view class="login-card">
				<view class="login-head">用户名</view>
				<view class="login-input login-margin-b">
					<input type="text" v-model="username" placeholder="请输入用户名" />
				</view>
				<view class="login-input">
					<input type="password" v-model="password" placeholder="请输入密码(8-16位)" />
				</view>
				<view class="login-input login-margin-b" style="position: relative;">
					<input type="text" v-model="captcode" placeholder="请输入验证码" />
					<img :src="yzmUrl" ref="yzmImage" @click="refresh()"
						style="position: absolute; right: 0; bottom: 7px; height: 30px;" />
				</view>
			</view>
		</view>
		<view class="login-btn" @click="loginin">
			<button class="landing" type="primary">登陆</button>
		</view>
	</view>
</template>

<script>
	import menu from '../mixins/menu.js'
	export default {
		mixins: [menu],
		data() {
			return {
				title: 'Hello',
				yzmUrl: '',
				yzmData: null,
				"username": uni.getStorageSync('userLoginName') || '',
				"password": uni.getStorageSync('userLoginPwd') || '',
				"captcode": "",
				candidates: uni.getStorageSync('historyUserNameArr') || [],
			}
		},
		onLoad() {
			this.autoLogin();
		},
		created() {
			let sessiontoken = uni.getStorageSync('sessiontoken')

			this.refresh()
		},
		methods: {
			autoLogin() {
				let sessionToken = uni.getStorageSync('sessiontoken');
				let userInfo = uni.getStorageSync('userInfo');

				if (sessionToken && userInfo) {
					uni.setStorageSync('hasLogin', true);
					console.log('autoLogin sessionToken = ' + sessionToken)
					this.$navigateTo("/pages/index/index")
				}
			},
			loginin() {
				let vm = this
				if (!vm.username) {
					vm.$api.msg("请输入用户名")
					return
				}
				if (!vm.password) {
					vm.$api.msg("请输入密码")
					return
				}
				if (!vm.captcode) {
					vm.$api.msg("请输入验证码")
					return
				}

				let req = {
					'username': vm.username,
					'password': vm.password,
					'validateCode': vm.captcode,
					'rememberMe': false
				}
				// let url = '/login?username=' + vm.username + '&password=' + vm.password + '&validateCode=' + vm.captcode + '&rememberMe=false'
				console.log('loginurl = ' + url)
				let url = '/login'
				// let url = '/appLogin'
				vm.sendRequest({
					'url': url,
					'method': 'POST',
					'contentType': 'application/x-www-form-urlencoded',
					'data': req
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp.data.msg))
					if (resp.data.code === 0 || resp.data.code === '0') {
						if (resp.data && resp.data.msg) {
							uni.setStorageSync('sessiontoken', resp.data.msg);
							
							uni.setStorageSync('userLoginName', vm.username);
							uni.setStorageSync('userLoginPwd', vm.password);
							
							//登录成功，保存登录信息
							vm.saveHistoryLoginInfo(vm);
						}
						// uni.setStorageSync('userInfo', resp.data.data);

						// vm.$navigateTo("/pages/index/index")
						vm.getUserInfo(function() {
							// 获取用户信息后，基础逻辑处理完后，回调进入首页
							uni.setStorageSync('hasLogin', true)
							vm.$navigateTo("/pages/index/index")
						})
					} else {
						vm.$api.msg("登录失败")
					}
				}, function(error) {
					vm.$api.msg('登录失败 : ' + error.data.msg)
				})
			},

			getUserInfo(successFunc) {
				let vm = this
				let req = {}
				let url = '/system/user/profile/11'
				vm.sendRequest({
					'url': url,
					'method': 'POST',
					'contentType': 'application/x-www-form-urlencoded',
					'data': req
				}, function(resp) {
					if (resp.data.userId) {
						// 回调
						uni.setStorageSync('userInfo', resp.data);

						// 初始化菜单
						vm.initMenu(function() {
							successFunc()
						})
					} else {
						vm.clearSession()
						vm.$api.msg('获取用户信息失败 : ' + resp.data.msg)
					}
				}, function(error) {
					vm.clearSession()
					vm.$api.msg('获取用户信息失败 : ' + error.data.msg)
				})
			},
			initMenu(successFunc) {
				// 初始化菜单权限
				// 个人权限菜单  /system/user/profile/menu/{userId}

				/* 
				订单审核 shop:order:edit    menu-order
				控数设置 shop:sale:edit     menu-kongshu
				控价管理 shop:price:list    menu-kongjia
				待下单  shop:order:add      menu-daixiadan 
				订单审核  shop:stock:view   menu-ordercheck
				入库管理  shop:stock:list   menu-rukuguanli
				控价审核 shop:price:audit   menu-kongjiashenhe
				客户列表  shop:member:view  menu-kehuliebiao
				*/
				// uni.setTabBarItem({

				// })
				let vm = this
				let userInfo = uni.getStorageSync('userInfo');
				if (userInfo && userInfo['userId']) {
					let url = '/system/user/profile/menu/' + userInfo['userId']
					vm.sendRequest({
						'url': url,
						'data': null,
						'method': 'POST'
					}, function(resp) {
						console.log('获取权限菜单 resp = ' + JSON.stringify(resp.data))

						if (resp.data) {
							let item
							for (let i = 0; i < resp.data.length; i++) {
								item = resp.data[i]
								if (item['perms'] === 'shop:order:edit') {
									uni.setStorageSync('menu-order', 1);
								} else if (item['perms'] === 'shop:sale:edit') {
									uni.setStorageSync('menu-kongshu', 1);
								} else if (item['perms'] === 'shop:price:list') {
									uni.setStorageSync('menu-kongjia', 1);
								} else if (item['perms'] === 'shop:stock:view') {
									uni.setStorageSync('menu-ordercheck', 1);
								} else if (item['perms'] === 'shop:stock:list') {
									uni.setStorageSync('menu-rukuguanli', 1);
								} else if (item['perms'] === 'shop:price:audit') {
									uni.setStorageSync('menu-kongjiashenhe', 1);
								} else if (item['perms'] === 'shop:member:view') {
									uni.setStorageSync('menu-kehuliebiao', 1);
								} else if (item['perms'] === 'shop:order:add') {
									uni.setStorageSync('menu-daixiadan', 1);
								}
							}
						}

						successFunc()
					}, function(error) {
						vm.clearSession()
						vm.$api.msg('获取权限失败 : ' + JSON.stringify(error))
					})
				} else {
					vm.clearSession()
					vm.$api.msg('获取权限失败 : ' + resp.data.msg)
				}
			},
			toGetCheckKey() {
				let vm = this
				vm.sendRequest({
					'url': '/captcha/captchaImage?type=math',
					'data': null,
					'method': 'GET'
				}, function(resp) {
					vm.yzmData = resp
				}, function(error) {})
			},
			refresh: function() {
				let vm = this
				this.sendRequest({
					'url': '/captcha/captchaImage?type=math&mobile=1',
					'data': null,
					'method': 'GET'
				}, function(resp) {
					console.info(resp)
					vm.yzmUrl = resp.data
					uni.setStorageSync('sessiontoken', resp.header['access-token']);
				}, function(error) {})
			},
			
			//保存用户登录的历史账号
			saveHistoryLoginInfo(vm){
				let historyArr = uni.getStorageSync('historyUserNameArr') || [];
				console.log('saveHistoryLoginInfo---> historyArr:' ,historyArr);
				if(historyArr && historyArr.length > 0){
					//已经存在历史账号，将账号提到第一个
					var newArr = [];
					newArr.push(vm.account);
					for(var i = 0;i<historyArr.length;i++){
						if(vm.account === historyArr[i]){
						}else{
							newArr.push(historyArr[i]);
						}
					}
					console.log('saveHistoryLoginInfo---> newArr:' ,newArr);
					//保存账号名称
					uni.setStorageSync('historyUserNameArr', newArr);
					//保存账号对应的密码信息
					let historyMap = uni.getStorageSync('historyUserLoginMap') || {};
					if (vm.rememberPsw) {
						historyMap[vm.account] = {password:vm.password}
					}else{
						historyMap[vm.account] = {password:''}
					}
					uni.setStorageSync('historyUserLoginMap', historyMap);
				}else{
					var newArr = [];
					//保存账号名称
					newArr.push(vm.account);
					uni.setStorageSync('historyUserNameArr', newArr);
					console.log('saveHistoryLoginInfo---> newArr:' ,newArr);
					//保存账号对应的密码信息
					let historyMap = uni.getStorageSync('historyUserLoginMap') || {};
					if (vm.rememberPsw) {
						historyMap[vm.account] = {password:vm.password}
					}else{
						historyMap[vm.account] = {password:''}
					}
					uni.setStorageSync('historyUserLoginMap', historyMap);
				}
			}
		}
	}
</script>

<style>
	.landing {
		height: 84upx;
		line-height: 84upx;
		border-radius: 44upx;
		font-size: 32upx;
		background: linear-gradient(left, #4A94FF, #4A94FF);
	}

	.login-btn {
		padding: 10upx 20upx;
		margin-top: 350upx;
	}

	.login-function {
		overflow: auto;
		padding: 20upx 20upx 30upx 20upx;
	}

	.login-forget {
		float: left;
		font-size: 26upx;
		color: #999;
	}

	.login-register {
		color: #666;
		float: right;
		font-size: 26upx;

	}

	.login-input input {
		background: #F2F5F6;
		font-size: 28upx;
		padding: 10upx 25upx;
		height: 62upx;
		line-height: 62upx;
		border-radius: 8upx;
	}

	.login-margin-b {
		margin-bottom: 25upx;
	}

	.login-input {
		padding: 10upx 20upx;
	}

	.login-head {
		font-size: 34upx;
		text-align: center;
		padding: 25upx 10upx 55upx 10upx;
	}

	.login-card {
		background: #fff;
		border-radius: 12upx;
		padding: 10upx 25upx;
		box-shadow: 0 6upx 18upx rgba(0, 0, 0, 0.12);
		position: relative;
		margin-top: 120upx;
	}

	.login-bg {
		height: 260upx;
		padding: 25upx;
		background: linear-gradient(#4A94FF, #4A94FF);
	}
</style>
