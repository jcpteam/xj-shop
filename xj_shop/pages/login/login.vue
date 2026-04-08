<template>
	<view class="content">
		<image class="top-logo" :src="'/static/xj_logo.jpg'"></image>

		<view class="wrapinput">
			<view class="input-group">
				<view class="input-row border">
					<!-- <m-input class="m-input" type="text" clearable focus v-model="account" placeholder="请输入用户名">
					</m-input> -->
					<uni-combox class="m-input" label="" :candidates="candidates" placeholder="请输入用户名" emptyTips="" v-model="account" @input="userNameInput()"></uni-combox>
				</view>
				<view class="input-row border">
					<m-input type="password" displayable v-model="password" placeholder="请输入密码"></m-input>
				</view>

				<view class="input-row border" style="position: relative;" v-if="showCaptcode">
					<input type="text" v-model="captcode" placeholder="请输入验证码" />
					<img :src="yzmUrl" ref="yzmImage" @click="refreshCaptCode()"
						style="position: absolute; right: 0; bottom: 7px; height: 30px;" />
				</view>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="login-btn" @tap="bindLogin">登录</button>
		</view>
		<view class="action-row">
			<view>
				<label>
					<checkbox value="1" checked="rememberPsw" @tap="rememberPsw = !rememberPsw" color="#3fb86b"
						style="transform:scale(0.7)">记住密码</checkbox>
				</label>
			</view>
		</view>
	</view>
</template>

<script>
	import service from '@/service.js';
	import {
		mapState,
		mapMutations
	} from 'vuex'
	import mInput from '@/components/logininput/m-input.vue'

	export default {
		components: {
			mInput
		},

		data() {
			return {
				showCaptcode: false,
				yzmUrl: '',
				account: uni.getStorageSync('userLoginName') || '',
				password: uni.getStorageSync('userLoginPwd') || '',
				candidates: uni.getStorageSync('historyUserNameArr') || [],
				captcode: '',

				rememberPsw: true
			}
		},

		onBackPress(options) {
			// 监听页面返回，返回 event = {from:backbutton、 navigateBack} ，backbutton 表示来源是左上角返回按钮或 android 返回键；navigateBack表示来源是 uni.navigateBack
			this.back();
			console.log('from:' + options.from)
			return true; //加true才执行
		},
		created() {
			// this.refreshCaptCode(false)
		},
		
		methods: {
			...mapMutations(['login']),
			back() {
				uni.reLaunch({
					url: '/pages/tabs/index/index',
				});
			},
			
			bindLogin() {
				/*** 客户端对账号信息进行一些必要的校验。实际开发中，根据业务需要进行处理，这里仅做示例。*/
				if (!this.account) {
					uni.showToast({
						icon: 'none',
						title: '账号不能为空'
					});
					return;
				}
				if (!this.password) {
					uni.showToast({
						icon: 'none',
						title: '密码不能为空'
					});
					return;
				}
				if (this.showCaptcode && !this.captcode) {
					uni.showToast({
						icon: 'none',
						title: '验证码不能为空'
					});
					return;
				}

				uni.showLoading({
					title: '正在登录...'
				});
				// let sessiontoken = uni.getStorageSync('sessiontoken')
				// if (sessiontoken) {
				this.sendLoginRequest()
				// } else {
				// 	this.refreshCaptCode(true)
				// }
			},

			sendLoginRequest() {
				let url = '/appLogin?username=' + this.account + '&password=' + this.password + '&rememberMe=false'
				// console.log('loginurl = ' + url)
				let self = this;
				this.sendRequest({
					'url': url,
					'method': 'POST',
					'contentType': 'x-www-form-urlencoded',
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp.data.msg))
					if (resp.data.code === 0 || resp.data.code === '0') {
						self.tokenExpress = false;
						uni.setStorageSync('userLoginName', self.account);
						if (self.rememberPsw) {
							uni.setStorageSync('userLoginPwd', self.password);
						} else {
							uni.removeStorageSync('userLoginPwd');
						}
						uni.setStorageSync('hasLogin', 'true')
						uni.setStorageSync('sessiontoken', resp.data.msg);
						uni.setStorageSync('userInfo', resp.data.data);
						
						//登录成功，保存登录信息
						self.saveHistoryLoginInfo(self);
						
						//登录成功，获取用户对应报价单信息
						if(resp.data.data && resp.data.data.memberId){
							self.sendUserQuotationRequest(self,resp.data.data.memberId);
							self.sendUserCashRequest(self,resp.data.data.memberId);
						}else{
							uni.hideLoading();
							uni.reLaunch({
								url: '/pages/tabs/index/index',
							});
						}
						
					} else {
						uni.hideLoading();
						if (resp.data.msg) {
							uni.showToast({
								icon: 'none',
								title: resp.data.msg
							});
						} else {
							uni.showToast({
								icon: 'none',
								title: '登录失败'
							});
						}
					}
				}, function(error) {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '登录失败 : ' + error.data.msg
					});
				})
			},
			
			sendUserQuotationRequest(vm,memberId) {
				let url = '/shop/member/quotationWithMemberId?id=' + memberId;
				console.log('sendUserQuotationRequest url = ' + url)
				let self = vm;
				vm.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp))
					if (resp.data.code === 0 || resp.data.code === '0') {
						var userInfo = uni.getStorageSync('userInfo') || {};
						userInfo.quotationInfo = resp.data.data;
						uni.setStorageSync('userInfo', userInfo);
						console.log('userinfo--->',userInfo);
						
						uni.hideLoading();
						uni.reLaunch({
							url: '/pages/tabs/index/index',
						});
					} else {
						uni.hideLoading();
						uni.reLaunch({
							url: '/pages/tabs/index/index',
						});
					}
				}, function(error) {
					uni.hideLoading();
					uni.reLaunch({
						url: '/pages/tabs/index/index',
					});
				})
			},

			/**
			 * 刷新验证码
			 */
			async refreshCaptCode(withLogin) {
				let vm = this
				await this.sendRequest({
					'url': '/captcha/captchaImage?type=math&mobile=1',
					'data': null,
					'method': 'GET'
				}, function(resp) {
					console.info(resp)
					vm.yzmUrl = resp.data
					uni.setStorageSync('sessiontoken', resp.header['access-token']);

					if (withLogin) {
						vm.sendLoginRequest();
					}
				}, function(error) {
					uni.hideLoading();
				})
			},
			
			//发送获取用户账户余额信息
			sendUserCashRequest(vm,memberId) {
				let url = '/shop/account/appList?memberId=' + memberId;
				console.log('sendUserCashRequest url = ' + url)
				let self = vm;
				vm.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp))
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						var userInfo = uni.getStorageSync('userInfo') || {};
						var item = resp.data.rows[0];
						userInfo.cashInfo = {
							cash:item.cash,
							bondAccount:item.bondAccount,
							rebateAccount:item.rebateAccount
						};
						uni.setStorageSync('userInfo', userInfo);
						console.log('userinfo--->',userInfo);
					} 
				});
			},
			
			//用户名数据
			userNameInput(){
				let historyArr = uni.getStorageSync('historyUserNameArr') || [];
				console.log("userNameInput--->historyArr:",historyArr);
				let historyMap = uni.getStorageSync('historyUserLoginMap') || {};
				if(historyMap){
					let historyUser = historyMap[this.account];
					console.log("userNameInput--->historyUser:",historyUser);
					if(historyUser && historyUser.password){
						this.password = historyUser.password;
					}else{
						this.password = '';
					}
				}else{
					this.password = '';
				}
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
		},
		onReady() {
			// this.initPosition();
			// this.initProvider();
		}
	}
</script>

<style>
	.content {
		background: #ffffff;
		height: calc(100vh - var(--window-top));
	}

	.top-logo {
		width: 200upx;
		height: 200upx;
		margin: 20upx 30upx;
		display: block;
		margin-left: auto;
		margin-right: auto;
	}

	.wrapinput {
		padding: 20upx 30upx;
	}

	.input-group {
		background: #FFFFFF;
		padding: 10upx;
	}

	.input-row {
		/* display: flex; */
		flex-direction: row;
		align-items: center;
		padding: 38upx 10upx;
	}

	.input-row.border {
		border-bottom: 1upx solid #CCCCCC;
	}

	.btn-row {
		padding: 100upx 30upx 30upx 30upx;
	}

	.login-btn {
		background-color: #3fb86b;
		border-radius: 45upx;
	}

	.action-row {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 10upx 30upx;
	}

	.action-row navigator {
		color: #007aff;
		padding: 0 20upx;
	}

	.actiontext {
		color: $uni-text-color;
		padding: 0 20upx;
		display: inline-block;
		vertical-align: middle;
	}

	.oauth-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
	}

	.oauth-image {
		width: 100upx;
		height: 100upx;
		border: 1upx solid #dddddd;
		border-radius: 100upx;
		margin: 0 40upx;
		background-color: #ffffff;
	}

	.oauth-image image {
		width: 60upx;
		height: 60upx;
		margin: 20upx;
	}
</style>
