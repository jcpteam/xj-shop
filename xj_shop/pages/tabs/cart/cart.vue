<template>
	<view class="container">
		<view v-if="hasLogin">
			<empty v-if='isCartEmpty' :ramdom="ramdomValue" @refreshCart="refreshCart"></empty>
			<cart-info v-else :ramdom="ramdomValue" :orderRemark="orderRemark" @refreshCart="refreshCart"></cart-info>
		</view>
		<!-- <view v-else>
			<empty v-if='cartList.length===0'></empty>
		</view> -->
	</view>
</template>

<script>
	import empty from '@/components/cart/empty.vue'
	import cartInfo from '@/components/cart/cart-info.vue'
	export default {
		components: {
			empty,
			cartInfo
		},
		data() {
			return {
				cartList: [],
				hasLogin: uni.getStorageSync('hasLogin'), //判断是否登录
				ramdomValue: 0,
				isCartEmpty: true,
				//订单备注
				orderRemark: '',
			};
		},

		onLoad() {
			uni.$on("remarkChange", res => {
				if (res) {
					this.orderRemark = res.remark;
					console.log("remarkChange--->", this.orderRemark);
				}
			})
		},

		onShow() {
			//判断购物车是否为空
			this.checkCartEmpty();
			// 第一次加载会触发，tabbar 切换就不会触发了
			this.hasLoginFun(this.hasLogin);
			if (!this.hasLogin) {
				//relaunch关闭所有页面跳转应用内某个页面，左上角返回按钮也没有了。
				this.$jump('/pages/login/login')
			}
			//更新随机值，间接更新子组件数据
			this.ramdomValue = Math.random();
		},

		methods: {
			// 判断是否登录
			hasLoginFun(hasLogin) {
				if (hasLogin === true || hasLogin === 'true') {
					this.hasLogin = true
				} else {
					this.hasLogin = false
				}
			},

			//检查购物车是否为空
			checkCartEmpty() {
				let userCartMap = uni.getStorageSync('userCartMap') || {};
				let totalQuality = 0;
				if (userCartMap) {
					for (var goodsId in userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
						let item = userCartMap[goodsId];
						totalQuality += item.quantity;
						if (totalQuality > 0) {
							break;
						}
					}
				}
				if (totalQuality > 0) {
					this.isCartEmpty = false;
				} else {
					this.isCartEmpty = true;
				}
			},

			refreshCart() {
				this.checkCartEmpty();
			}
		},
	}
</script>

<style lang='scss'>
	.container {
		padding-bottom: 134upx;

	}
</style>
