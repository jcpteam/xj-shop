<template>
	<view class="order">
		<view class="tab">
			<uni-segmented-control :current="curSelectTab || 0" :values="tabArray" @clickItem="tabClick" styleType="text"
				activeColor='#3fb86b'></uni-segmented-control>
		</view>
		<scroll-view scroll-y :scroll-top="scrollTop" @scroll="scroll" @scrolltolower="scrollToBottom"
			:style="'height:'+height+'px'" scroll-with-animation>
			<view class="good-content">
				<view class="goods" v-for='(item,index) in orderList' :key='index' @click="gotoOrderDetail(item)">
					<view class="goods-customer">
						<view class="name">
							<view class="customer-name">{{userInfo.deptName}}</view>
							<image class="arrow_image_name" src="/static/arrow.png"></image>
						</view>
						<text v-if="item.status === '3'" class="status">待支付</text>
						<text v-else-if="item.status === '2'" class="status">待配送</text>
						<text v-else class="status">{{item.statusName}}</text>
					</view>

					<view class="image_contaner" @click="gotoOrderItemDetail">
						<view>
							<image v-for="(subItem,index) in item.goodsOrderItems" class="goods_image"
								:src="parseGoodImgUrl(subItem)" @error="imageError(subItem)"></image>
						</view>
						<view class="goods-num-content">
							<text class="goods_num">共{{item.goodsOrderItems.length}}种</text>
							<text v-if="item.status === '2' || item.status === '3' || item.status === '4'" class="goods_amount">¥{{item.sortingPrice}}</text>
							<text v-else class="goods_amount">¥{{item.price}}</text>
						</view>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">订单编号：</text>
						<text class="status_value">{{item.code}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">支付状态：</text>
						<text class="status_value">{{item.payStatusName}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">下单时间：</text>
						<text class="status_value">{{item.createTime}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">收货时间：</text>
						<text
							class="status_value">{{getOrderDeliverTime(item.deliveryDate)}}</text>
					</view>

					<view v-if="item.status === '1'" class="control-roder">
						<text class="modify-roder" @click.stop="modifyOrder(item,index)">修改订单</text>
						<text class="cancel-roder" @click.stop="cancelOrder(item,index)">取消订单</text>
					</view>
					<!-- <view v-if="item.status === '3' && item.receiptStatus === '1'" class="cancel-roder" @click.stop="payOrder(item,index)">
						<text>支付订单</text>
					</view> -->


					<!-- <button class="contact-btn">联系客服</button> -->
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import uniSegmentedControl from '@/components/common/uni-segmented-control.vue'

	export default {
		components: {
			uniSegmentedControl,
		},

		data() {
			return {
				hasLogin: uni.getStorageSync('hasLogin'), //判断是否登录

				//顶部tab数据
				curSelectTab: 0,
				tabArray: ['全部', '待审核', '待配送', '待支付', '已完成'],

				//默认的广告图片，加载失败则使用此图片
				defaultGoodsImg: '/static/xj_logo.jpg',
				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},

				orderList: [],

				//滚动条
				height: 0,
				scrollTop: 0,
				scrollHeight: 0,
			};
		},

		mounted() {
			this.height = uni.getSystemInfoSync().windowHeight;
		},

		onLoad(options) {},

		onShow() {
			this.init();
		},

		methods: {
			init() {
				this.hasLoginFun(this.hasLogin);
				if (!this.hasLogin) {
					//relaunch关闭所有页面跳转应用内某个页面，左上角返回按钮也没有了。
					this.$jump('/pages/login/login')
				}

				let jumpIndex = uni.getStorageSync('selectOrderTabIndex');
				console.log('jumpIndex--->', jumpIndex);
				if (jumpIndex >= 0) {
					this.curSelectTab = jumpIndex;
					uni.setStorageSync('selectOrderTabIndex', -1);
				}

				this.orderList = [];
				this.pagination.total = 0;
				this.pagination.pageNo = 1;
				this.pagination.pageSize = 10;
				this.getOrderList();
				
				//重置订单修改的缓存数据
				uni.setStorageSync('userModifyOrderId', '');
				uni.setStorageSync('userModifyOrderGoodsMap', {});
			},

			// 判断是否登录
			hasLoginFun(hasLogin) {
				if (hasLogin === true || hasLogin === 'true') {
					this.hasLogin = true
				} else {
					this.hasLogin = false
				}
			},

			tabClick(index) {
				this.curSelectTab = index;
				this.init();
			},

			// onReachBottom() {
			// 	console.log("监听滑到底部....");
			// 	this.getOrderList();
			// },

			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},

			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.getOrderList();
			},

			getOrderList() {
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

				let status = this.curSelectTab + '';
				if (status === '0') {
					status = '';
				}
				let self = this;
				let url = '/shop/order/appList?pageNum=' + self.pagination.pageNo + '&pageSize=' + self.pagination
					.pageSize + '&merchantId=' + this.userInfo.memberId + '&status=' + status +
					'&orderByColumn=create_time&isAsc=desc';
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log("orderList--->", resp);
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							self.pagination.total = resp.data.total;
							//请求成功，更新下次请求的页码
							self.pagination.pageNo = self.pagination.pageNo + 1;
							//数据累加
							self.orderList = self.orderList.concat(resp.data.rows);
						} else {
							self.orderList = [];
						}
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			//跳转订单详情页面
			gotoOrderDetail(orderItem) {
				this.$jump('/pages/order/orderdetail?orderId=' + orderItem.orderId);
			},

			cancelOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定取消订单吗？',
					success: function(res) {
						if (res.confirm) {

							uni.showLoading({
								title: '正在取消...'
							});

							let url = '/shop/order/appRemove?ids=' + orderItem.orderId;
							self.sendRequest({
								'url': url,
								'method': 'POST',
								'data': null
							}, function(resp) {
								uni.hideLoading();
								if (resp.data.code === 0) {
									self.orderList.splice(index, 1); //删除元素
									// self.getOrderList();
								}
							}, function(error) {
								uni.hideLoading();
							})
						}
					}
				});
			},
			
			modifyOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定修改订单吗？',
					success: function(res) {
						if (res.confirm) {
							self.$jump('/pages/order/ordermodify?orderId=' + orderItem.orderId + '&useCache=0');
						}
					}
				});
			},

			payOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '温馨提示',
					content: '确定支付订单吗？',
					success: function(res) {
						if (res.confirm) {

							uni.showLoading({
								title: '正在获取付款码...'
							});

							let url = '/shop/xjpay/qrcode/' + orderItem.orderId;
							self.sendRequest({
								'url': url,
								'method': 'GET',
								'data': null
							}, function(resp) {
								uni.hideLoading();
								if (resp.data.respCode === 'SUCCESS') {
									uni.showToast({
										icon: 'none',
										title: '获取付款码成功'
									});

									//跳转二维码显示页面
									self.$jump('/pages/order/orderpaycode?qrCode=' + resp.data.qrCode +
										'&orderNo=' + resp.data.orderNo + '&payMoney=' + resp.data
										.payMoney);
								} else {
									if (resp.data.returnCode && resp.data.returnCode === 'SUCCESS' &&
										resp.data.tradeState && resp.data.tradeState === 'S') {
										uni.showToast({
											icon: 'none',
											title: '订单支付成功，已扣除现金账户余额'
										});

										self.orderList = [];
										self.pagination.total = 0;
										self.pagination.pageNo = 1;
										self.pagination.pageSize = 10;
										self.getOrderList();
									} else {
										let msg = resp.data.message ? resp.data.message :
											'获取付款码失败，请稍后再试';
										uni.showToast({
											icon: 'none',
											title: msg
										});
									}
								}
							}, function(error) {
								uni.hideLoading();
								uni.showToast({
									icon: 'none',
									title: '获取付款码失败，请稍后再试'
								});
							})
						}
					}
				});
			},
			
			getOrderDeliverTime(deliverTime){
				if(deliverTime && deliverTime.length > 10){
					return deliverTime.slice(0,10);
				}else {
					return deliverTime;
				}
			},
			
			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

		},
	}
</script>

<style lang="scss">
	.order {
		.tab {
			margin-top: 0upx;
		}

		.good-content {
			margin-top: 90upx;

			.goods {
				padding: 20upx 0upx 20upx 0upx;
				color: #000000;
				// margin-top: 100upx;
				border-bottom: solid 1px #E0E0E0;

				.image_contaner {
					padding: 15upx 20upx 15upx 20upx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					.goods_image {
						width: 120upx;
						height: 120upx;
						border-radius: 8upx;
						margin-right: 15upx;
					}

					.goods-num-content {
						width: 230upx;
						display: flex;
						align-items: center;
						justify-content: flex-end;
						font-size: 28upx;

						.goods_num {
							color: #666;
						}

						.goods_amount {
							margin-left: 20upx;
							color: $uni-xj-good-price;
						}
					}

				}

				.goods-customer {
					padding: 15upx 20upx 15upx 20upx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					font-size: 28upx;
					color: #000000;

					.name {
						flex: 10;
						display: flex;
						align-items: center;
						justify-content: flex-start;

						.customer-name {
							// flex: 7;
							font-size: 32upx;
							// padding-left: 20upx;
							padding-right: 20upx;
							color: #000000;
							font-weight: bold;

							//设置文本单行显示，超出显示省略号
							display: -webkit-box;
							overflow: hidden;
							text-overflow: ellipsis;
							word-wrap: break-word;
							white-space: normal !important;
							-webkit-line-clamp: 1; //设置行数
							-webkit-box-orient: vertical;
						}

						.arrow_image_name {
							width: 16upx;
							height: 24upx;
							vertical-align: middle;
						}

					}

					.status {
						color: #000000;
						display: flex;
						align-items: flex-end;
						justify-content: flex-end;
						flex: 4;
					}
				}

				.paystatus_contaner {
					padding: 10upx 20upx 10upx 20upx;
					display: flex;
					align-items: center;
					justify-content: flex-start;
					font-size: 28upx;

					.status_tip {
						color: #666;
					}

					.status_value {
						color: #000000;
					}
				}

				.contact-btn {
					display: flex;
					align-items: center;
					justify-content: center;
					width: 180upx;
					height: 60upx;
					font-size: 28upx;
					color: #000000;
					margin-left: auto;
					margin-right: 20upx;
					background-color: #ffffff;
					border-radius: 20px;
					border: 1upx solid #bbb;
				}

				.control-roder {
					font-size: 28upx;
					display: flex;
					margin-right: 20upx;
					align-items: flex-end;
					justify-content: flex-end;
				}

				.cancel-roder {
					font-size: 28upx;
					color: #dd524d;
				}
				
				.modify-roder {
					font-size: 28upx;
					color: #4A4AFF;
					margin-right: 40upx;
				}
			}

		}



		.arrow_image {
			width: 16upx;
			height: 24upx;
			margin-left: 10upx;
			vertical-align: middle;
		}

	}
</style>
