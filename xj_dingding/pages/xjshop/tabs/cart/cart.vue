<template>
	<view class="container">
		<cart-info :ramdom="ramdomValue" :orderRemark="orderRemark" @refreshCart="refreshCart"></cart-info>
	</view>
</template>

<script>
	import cartInfo from '@/components/xjshop/cart/cart-info.vue'
	export default {
		components: {
			cartInfo
		},
		data() {
			return {
				cartList: [],
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

		onUnload() {    
		    // 移除监听事件    
		     uni.$off('remarkChange');    
		},

		onShow() {
			//判断购物车是否为空
			this.checkCartEmpty();

			//更新随机值，间接更新子组件数据
			this.ramdomValue = Math.random();
		},

		methods: {
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
