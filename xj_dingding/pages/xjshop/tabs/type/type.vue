<template>
	<view class="">
		<tree-list :typeList="typeList" :ramdom="ramdomValue" @cartChange="cartChange"></tree-list>

		<!-- 底部菜单栏 -->
		<view class="action-section">
			<view class="total-box">
				<text class="price-title">合计：</text>
				<text class="price-number">¥{{totalPrice}}</text>
			</view>
			<navigator class="confirm-btn" url="/pages/xjshop/tabs/cart/cart">去下单</navigator>
		</view>
	</view>
</template>

<script>
	import treeList from '@/components/xjshop/type/tree-list.vue'
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
				// 客户ID
				clientid: 0
			}
		},
		onLoad(options){
			if(options.clientid){
				this.clientid = options.clientid
				uni.setStorageSync('daiclientid', options.clientid);
				uni.setStorageSync('customerArea', options.customerArea);
				uni.setStorageSync('userCartMap', {});
				
				//缓存代下单客户的信息，然后获取类型数据
				this.cacheDaiClientMemberInfo();
				
				//缓存代下单用户信息
				this.cacheDaiClientUserInfo();
				
				//调用接口获取数据
				// this.getTypeList();
			}else{
				
			}
		},
		onShow() {
			this.calculateCartTotalPrice();
			//更新随机值，间接更新子组件数据
			this.ramdomValue = Math.random();
		},

		created() {
		},

		computed: {
		},

		watch: {

		},
		methods: {
			//缓存代下单用户信息
			cacheDaiClientMemberInfo(){
				//先获取当前商户的用户信息保存
				let vm = this
				let req = {
					pageNum: 1,
					pageSize: 10,
					id: this.clientid
				}
				let url = '/shop/member/list'
				vm.sendRequest({'url': url, 'method': 'POST', 'data': req, 'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							uni.setStorageSync('daiClientObj', resp.data.rows[0]);
							vm.getTypeList();
						}else{
							uni.setStorageSync('daiClientObj', {});
							
							uni.showToast({
								title: "代下单客户报价单为空",
								icon: 'none',
							});
						}
					}else{
						uni.setStorageSync('daiClientObj', {});
						
						uni.showToast({
							title: "获取代下单客户报价单失败1",
							icon: 'none',
						});
					}
				}, function(error){
					uni.setStorageSync('daiClientObj', {});
					
					uni.showToast({
						title: "获取代下单客户报价单失败2",
						icon: 'none',
					});
				})
			},
			
			cacheDaiClientUserInfo(){
				//查询一下商户对应的用户id
				let req1 = {
					pageNum: 1,
					pageSize: 10,
					memberId: this.clientid
				}
				let url1 = '/system/user/list'
				vm.sendRequest({'url': url1, 'method': 'POST', 'data': req1, 'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							uni.setStorageSync('daiClientUserObj', resp.data.rows[0]);
						}else{
							uni.setStorageSync('daiClientUserObj', {});
						}
					}else{
						uni.setStorageSync('daiClientUserObj', {});
					}
				})
			},
			
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
				let userCartMap = uni.getStorageSync('userCartMap') || {};
				let totalPrice = 0;
				if (userCartMap) {
					for (var goodsId in userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
						let item = userCartMap[goodsId];
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
