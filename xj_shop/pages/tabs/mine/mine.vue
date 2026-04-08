<template>
	<view>
		<user-card :userinfo='userinfo' :expand="expand" :hasLogin="hasLogin"></user-card>
		<grid :nav='nav' :hasLogin="hasLogin"></grid>
		<view class="user-tool">常用工具</view>
		<grid :nav='tools' :hasLogin="hasLogin"></grid>
	</view>
</template>

<script>
	import userCard from '@/components/mine/user-card.vue'
	import grid from '@/components/mine/grid.vue'
	export default {
		components: {
			userCard,
			grid,
		},
		data() {
			return {
				//用户信息对象
				userinfo: {},

				//默认的数据
				defaultExpand: [{
					number: 0,
					name: '现金账户(元)',
					type: 'money'
				}, {
					number: 0,
					name: '保证金账户(元)',
					type: 'money'
				}, {
					number: 0,
					name: '返点账户(元)',
					type: 'money'
				}, ],

				//扩展数据
				expand: [],

				nav: [
				// 	{
				// 	image: '/static/mine/order1.png',
				// 	name: '全部订单',
				// 	url: '/pages/tabs/order/order'
				// }, 
				{
					image: '/static/mine/order4.png',
					name: '待审核',
					url: '/pages/tabs/order/order'
				}, {
					image: '/static/mine/order2.png',
					name: '待分拣',
					url: '/pages/tabs/order/order'
				}, {
					image: '/static/mine/order3.png',
					name: '待支付',
					url: '/pages/tabs/order/order'
				}, {
					image: '/static/mine/order5.png',
					name: '已完成',
					url: '/pages/tabs/order/order'
				}, ],

				tools: [{
					image: '/static/mine/order2.png',
					name: '账单支付',
					url: '/pages/order/billmanager'
				},{
					image: '/static/mine/order4.png',
					name: '修改密码',
					url: '/pages/tabs/mine/modifypwd'
				}, 
				// {
				// 	image: '/static/mine/order4.png',
				// 	name: '客服与帮助',
				// 	url: '/pages/order/order?index=3'
				// }, 
				],

				hasLogin: uni.getStorageSync('hasLogin'), //判断是否登录
			}
		},
		onLoad() {
			// 第一次加载会触发，tabbar 切换就不会触发了
			this.hasLoginFun(this.hasLogin)
			//获取用户信息
			if (this.hasLogin) {
				this.userinfo = uni.getStorageSync('userInfo') || {};
			}
			console.log("userinfo--->", this.userinfo);
			//初始化用户余额数据
			if (this.userinfo) {
				this.initCash();
			} else {
				this.expand = this.defaultExpand;
			}
		},
		onShow() {
			this.sendUserCashRequest();
		},

		methods: {
			initCash(){
				if(this.userinfo.cashInfo){
					this.expand = [{
						number: this.userinfo.cashInfo.cash,
						name: '现金账户(元)',
						type: 'money'
					}, {
						number: this.userinfo.cashInfo.bondAccount,
						name: '保证金账户(元)',
						type: 'money'
					}, {
						number: this.userinfo.cashInfo.rebateAccount,
						name: '返点账户(元)',
						type: 'money'
					}, ];
				}else{
					this.expand = [{
						number: 0,
						name: '现金账户(元)',
						type: 'money'
					}, {
						number: 0,
						name: '保证金账户(元)',
						type: 'money'
					}, {
						number: 0,
						name: '返点账户(元)',
						type: 'money'
					}, ];
				}
			},
			
			// 判断是否登录
			hasLoginFun(hasLogin) {
				if (hasLogin === true || hasLogin === 'true') {
					this.hasLogin = true
				} else {
					this.hasLogin = false
				}
			},
			
			//发送获取用户账户余额信息
			sendUserCashRequest() {
				if (!this.userinfo || !this.userinfo.memberId) {
					console.log("用户未登录....userInfo-->", this.userinfo)
					return;
				}
				
				let url = '/shop/account/appList?memberId=' + this.userinfo.memberId;
				console.log('sendUserCashRequest url = ' + url)
				let self = this;
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp))
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						var item = resp.data.rows[0];
						self.userinfo.cashInfo = {
							cash:item.cash,
							bondAccount:item.bondAccount,
							rebateAccount:item.rebateAccount
						};
						uni.setStorageSync('userInfo', self.userinfo);
						console.log('userinfo--->',self.userinfo);
						
						self.initCash();
					} 
				});
			},

		},
	}
</script>

<style>
	.user-tool {
		margin: 0upx 30upx;
		padding: 20upx 0upx;
		border-bottom: 1upx solid #CCCCCC;
	}
</style>
