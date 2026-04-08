<template>
	<view class="">
		<tree-list :typeList="typeList" :ramdom="ramdomValue" @cartChange="cartChange"></tree-list>

		<!-- 底部菜单栏 -->
		<view class="action-section">
			<view class="total-box">
				<text class="price-title">合计：</text>
				<text class="price-number">¥{{totalPrice}}</text>
			</view>
			<navigator class="confirm-btn" @click="saveOrderModify()">去下单</navigator>
		</view>
	</view>
</template>

<script>
	import treeList from '@/components/xjshop/type/tree-list-order-modify.vue'
	export default {
		components: {
			treeList
		},
		data() {
			return {
				//分类列表
				typeList: [],
				ramdomValue: 0,
				
				//购物车商品总价格
				totalPrice:0,
			}
		},
		onLoad() {
			//调用接口获取数据
			this.getTypeList();
		},
		onShow() {
			this.calculateCartTotalPrice();
			//更新随机值，间接更新子组件数据
			this.ramdomValue = Math.random();
		},

		computed: {
		},

		watch: {

		},
		methods: {
			//获取商品分类列表
			getTypeList() {
				let self = this;
				let url = '/shop/classify/appList?pageNum=' + 1 + '&pageSize=' + 100;
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.typeList = resp.data.rows;
					}
				}, function(error) {
					console.log('error = ' + error)
				})
			},
			
			//计算购物车总价格
			calculateCartTotalPrice: function() {
				let tempMap = uni.getStorageSync('userModifyOrderGoodsMap') || {};
				let totalPrice = 0;
				if (tempMap) {
					for (var goodsId in tempMap) { //遍历对象的所有属性，包括原型链上的所有属性
						let item = tempMap[goodsId];
						totalPrice += Number(item.amount);
					}
				}
				if(totalPrice > 0 && String(totalPrice).indexOf('.') != -1){
					totalPrice = Number(totalPrice.toFixed(2));
				}
				this.totalPrice = totalPrice;
			},
			
			//购物车数据改变
			cartChange(){
				this.calculateCartTotalPrice();
			},
			
			//保存订单修改
			saveOrderModify(){
				let orderId = uni.getStorageSync('userModifyOrderId') || '';
				uni.redirectTo({
					url:'/pages/xjshop/order/ordermodify?orderId=' + orderId + '&useCache=1'
				});
			},
		},

	}
</script>

<style lang="scss">
	/* 底部栏 */
	.action-section {
		// /* #ifdef H5 */
		// margin-bottom: 100upx;
		// /* #endif */
		padding-bottom: var(--window-bottom);
		position: fixed;
		bottom: 0;
		z-index: 95;
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 750upx;
		height: 90upx;
		background: #FFFFFF;

		.total-box {
			display: flex;

			margin-left: 30upx;

			.price-title {
				font-size: 28upx;
				color: black;
			}

			.price-number {
				font-size: 32upx;
				color: $uni-xj-good-price;
				font-weight: bold;
			}


		}

		.confirm-btn {
			width: 200upx;
			height: 90upx;
			line-height: 90upx;
			text-align: center;
			margin-right: 20upx;
			font-size: 32upx;
			background: $theme-color;
			color: #FFFFFF;
			border-radius: 25px;
		}
	}
</style>
