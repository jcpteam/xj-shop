<template>
	<view>
		<block>
			<!-- 轮播图 -->
			<swiper :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000">
				<swiper-item v-for='(item,index) in bannerList' :key='index'>
					<image class="swiper-image" :src="item.imgUrl === undefined?defaultAdImg:item.imgUrl"
						@error="imageError(item)"></image>
				</swiper-item>
			</swiper>

			<!-- 每日精选-->
			<list-mall :num='2' :banner='listMallBanner' :list='mallList'></list-mall>
		</block>
	</view>
</template>

<script>
	import listMall from '@/components/index/list-mall.vue'
	export default {
		components: {
			listMall,
		},

		data() {
			return {
				//默认的广告图片，加载失败则使用此图片
				defaultAdImg: '/static/index/banner.png',

				//顶部广告图片列表
				bannerList: [],

				listMallBanner: {
					title: '每日精选',
					// link: '更多',
					// id: 4,
				},

				//商品列表数据
				mallList: [],

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},
				
				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},
			}
		},
		onLoad() {
			this.refreshUserData();
		},

		onShow() {
			//重置参数
			this.mallList = [];
			this.pagination.total = 0;
			this.pagination.pageNo = 1;
			this.pagination.pageSize = 10;

			//调用接口获取数据
			this.getTopAdList();

			//获取用户购物车列表数据
			let self = this;
			this.initUserCartData(function(resp) {
				self.queryGoodsList();
			}, function(error) {
				self.queryGoodsList();
			});
		},

		onReachBottom() {
			console.log("监听主页滑到底部....");
			this.queryGoodsList();
		},

		methods: {
			//获取顶部广告图片
			getTopAdList() {
				let self = this;
				let url = '/shop/homepageAd/list';
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.bannerList = resp.data.rows;
					} else {
						//未取到广告图片，初始化一个默认的
						self.bannerList = [self.defaultAdImg];
					}
				}, function(error) {
					//未取到广告图片，初始化一个默认的
					self.bannerList = [self.defaultAdImg];
				})
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
					.pageSize+ '&status=1';
				if(this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.quotationId){
					url += '&quotationId=' + this.userInfo.quotationInfo.quotationId;
				}	
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

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultAdImg;
			},
			
			//刷新用户信息数据
			refreshUserData() {
				if (!this.userInfo || !this.userInfo.memberId) {
					console.log("用户未登录....userInfo-->", userInfo)
					return;
				}
				let url = '/shop/member/quotationWithMemberId?id=' + this.userInfo.memberId;
				console.log('sendUserQuotationRequest url = ' + url)
				let self = this;
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp))
					if (resp.data.code === 0 || resp.data.code === '0') {
						self.userInfo.quotationInfo = resp.data.data;
						uni.setStorageSync('userInfo', self.userInfo);
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	swiper {
		height: 320upx;

		.swiper-image {
			height: 320upx;
			width: 690upx;
			margin: 0 30upx;
			border-radius: 15upx;
		}
	}
</style>
