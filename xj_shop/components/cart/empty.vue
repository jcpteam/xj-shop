<template>
	<view class="empty">
		<scroll-view scroll-y :style="'height:'+height+'px'" @scrolltolower="scrollToBottom">
			<block>
				<image src="http://cdn.mzyun.tech/shop/emptyCart.jpg" mode="aspectFit"></image>
				<view class="empty-tips">
					{{tipContent}}
				</view>
				<!-- <navigator class="add-btn" url="/pages/tabs/type/type" open-type="switchTab">{{btnContent}}</navigator> -->
				<button class="add-btn" @click="goToAddGoods">{{btnContent}}</button>
				<list-mall :num='2' :banner='listMallBanner' :list='mallList' @cartChange="checkCartChange"></list-mall>
			</block>
		</scroll-view>
	</view>
</template>

<script>
	import listMall from '@/components/index/list-mall.vue'

	export default {
		name: 'empty',

		components: {
			listMall
		},

		props: {
			ramdom: Number
		},

		data() {
			return {
				tipContent: '购物车还没有商品，快去添加吧',
				btnContent: '去添加',

				height: 0,
				listMallBanner: {
					title: '每日精选',
					// link: '更多',
					// id: 4,
				},

				mallList: [],

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},

				isEmpty: true,
				
				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},
			};
		},

		watch: {
			ramdom(newValue, oldValue) {
				this.init();
			}
		},

		created() {
			this.init();
		},


		mounted() {
			this.height = uni.getSystemInfoSync().windowHeight;
		},

		methods: {
			init() {
				//重置参数
				this.mallList = [];
				this.pagination.total = 0;
				this.pagination.pageNo = 1;
				this.pagination.pageSize = 10;

				//调用接口获取数据
				this.queryGoodsList();
				this.checkCartChange();
			},

			updateTipContent(isEmpty) {
				this.isEmpty = isEmpty;
				if (isEmpty) {
					this.tipContent = '购物车还没有商品，快去添加吧';
					this.btnContent = '去添加';
				} else {
					this.tipContent = '当前购物车已有商品';
					this.btnContent = '刷新购物车';
				}
			},

			//购物车数据改变监听
			checkCartChange() {
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
					this.updateTipContent(false);
				} else {
					this.updateTipContent(true);
				}
			},

			//分页获取商品列表
			queryGoodsList() {
				//判断是否超过总数，超过则不再请求
				if (this.pagination.total > 0) {
					let pageTotal = this.$getTotalPageNo(this.pagination.total, this.pagination.pageSize);
					console.log("总页数-->", pageTotal);
					console.log("当前请求页--->", this.pagination.pageNo);
					if (this.pagination.pageNo > pageTotal) {
						return;
					}
				}

				uni.showLoading({
					title: '正在加载...'
				});

				let self = this;
				let url = '/shop/quotationGoods/appList?pageNum=' + self.pagination.pageNo + '&pageSize=' + self.pagination
					.pageSize + '&status=1';
				if(this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.quotationId){
					url += '&quotationId=' + this.userInfo.quotationInfo.quotationId;
				};	
				if(this.userInfo && this.userInfo.memberId){
					url += '&merchantId=' + this.userInfo.memberId;
				};	
				this.sendRequestWithDeptId({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.pagination.total = resp.data.total;
						//请求成功，更新下次请求的页码
						self.pagination.pageNo = self.pagination.pageNo + 1;

						//默认设置库存为10000
						resp.data.rows.forEach(function(e){
							e.saleNum = e.settingQuanintiy;
						})

						//数据累加
						self.mallList = self.mallList.concat(resp.data.rows);
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.queryGoodsList();
			},

			goToAddGoods() {
				if (this.isEmpty) {
					uni.switchTab({
						url: '/pages/tabs/type/type'
					})
				} else {
					this.$emit("refreshCart");
				}
			},
		}
	}
</script>

<style lang="scss">
	.empty {
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;
		height: 100vh;
		padding-bottom: 100upx;
		display: flex;
		// justify-content: center;
		flex-direction: column;
		align-items: center;
		background: #fff;

		image {
			width: 240upx;
			height: 160upx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 120upx auto 30upx auto;
		}

		.empty-tips {
			display: flex;
			font-size: 26upx;
			color: gray;
			align-items: center;
			justify-content: center;

			.navigator {
				color: $uni-color-primary;
				margin-left: 16upx;
			}
		}

		.add-btn {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 560upx;
			height: 80upx;
			margin: 60upx auto;
			font-size: 32upx;
			color: #fff;
			background-color: $theme-color;
			border-radius: 45upx;
		}
	}
</style>
