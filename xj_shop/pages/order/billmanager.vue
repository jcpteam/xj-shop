<template>
	<view class="content">
		<view class="top-back">
			<view @click="goBack()">&nbsp;<&nbsp;返回</view>
		</view>
		
		<scroll-view scroll-y :scroll-top="scrollTop" @scroll="scroll" @scrolltolower="scrollToBottom"
			:style="'height:'+height+'px'" scroll-with-animation>

			<view v-if="billList && billList.length > 0" class="bill-content">
				<view class="bills" v-for='(item,index) in billList' :key='index' @click="goOrderList(item)">
					<view class="goods-customer">
						<view class="name">
							<view class="customer-name">{{item.deptName}}</view>
							<image class="arrow_image_name" src="/static/arrow.png"></image>
						</view>
						<text v-if="item.payStatus === '0'" class="status">未付款</text>
						<text v-else-if="item.payStatus === '1'" class="status">已付款</text>
					</view>

					<view class="paystatus_contaner">
						<text class="status_tip">收款单号：</text>
						<text class="status_value">{{item.code}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">结算客户：</text>
						<text class="status_value">{{item.merchantName}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">结算金额：</text>
						<text class="status_value goods_amount">{{item.amount}}</text>
					</view>

					<view v-if="item.payStatus === '0'" class="control-roder">
						<text class="modify-roder" @click.stop="directPayBill(item,index)">直接支付</text>
						<text class="cancel-roder" @click.stop="payBill(item,index)">扫码支付</text>
					</view>
				</view>
			</view>
			<view v-else class="empty">暂无数据</view>

		</scroll-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//滚动条
				height: 0,
				scrollTop: 0,
				scrollHeight: 0,

				//账单列表数据
				billList: [],

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},
			}
		},

		mounted() {
			this.height = uni.getSystemInfoSync().windowHeight;
		},

		onLoad(options) {},

		onShow() {
			//重置参数
			this.billList = [];
			this.pagination.total = 0;
			this.pagination.pageNo = 1;
			this.pagination.pageSize = 10;

			this.queryBillList();
		},

		methods: {
			goBack(){
				let pages = getCurrentPages()
				if (pages.length > 1) {
					// 关闭当前页面 返回详细信息页面
					uni.navigateBack()
				}else{
					uni.reLaunch({
						url: '/pages/tabs/index/index',
					});
				}
			},
			
			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},
			
			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.queryBillList();
			},
			
			//分页获取账单列表
			queryBillList() {
				var userInfo = uni.getStorageSync('userInfo') || {};
				if (!userInfo || !userInfo.memberId) {
					console.log("用户未登录....userInfo-->", userInfo)
					return;
				}

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
				let url = '/shop/receipt/appList?pageNum=' + self.pagination.pageNo +
					'&pageSize=' + self.pagination.pageSize +
					'&orderByColumn=create_time&isAsc=desc' +
					'&merchantId=' + userInfo.memberId + 
					'&payStatus=' + '0';
				this.sendRequestWithDeptId({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.pagination.total = resp.data.total;

						//请求成功，更新下次请求的页码
						self.pagination.pageNo = self.pagination.pageNo + 1;

						//数据累加
						self.billList = self.billList.concat(resp.data.rows);
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			//支付账单
			payBill(payItem, index) {
				let self = this;
				uni.showModal({
					title: '温馨提示',
					content: '确定支付账单吗？',
					success: function(res) {
						if (res.confirm) {

							uni.showLoading({
								title: '正在获取付款码...'
							});

							let url = '/shop/xjpay/qrcode/' + payItem.receiptId;
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
										'&receiptId=' + payItem.receiptId +
										'&payOrderNo=' + resp.data.payOrderNo +
										'&merchantName=' + payItem.merchantName +
										'&payMoney=' + resp.data.payMoney);
								} else {
									if (resp.data.returnCode && resp.data.returnCode === 'SUCCESS' &&
										resp.data.tradeState && resp.data.tradeState === 'S') {
										uni.showToast({
											icon: 'none',
											title: '订单支付成功，已扣除现金账户余额'
										});

										self.billList = [];
										self.pagination.total = 0;
										self.pagination.pageNo = 1;
										self.pagination.pageSize = 10;
										self.queryBillList();
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

			//支付账单
			directPayBill(payItem, index) {
				let self = this;
				uni.showModal({
					title: '温馨提示',
					content: '确定支付账单吗？',
					success: function(res) {
						if (res.confirm) {
							self.getWxAuthCode(payItem);
						}
					}
				});
			},

			//获取微信授权code
			getWxAuthCode(payItem) {
				let redirectUrl = encodeURIComponent('http://csds.xiangjiamuye.com/xjshop/#/pages/order/orderpaymid')
				let appid = 'wxcee79d26c3eda7fd';
				window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?' +
					'appid=' + appid +
					'&redirect_uri=' + redirectUrl +
					'&response_type=code&scope=snsapi_base' +
					'&state=' + payItem.receiptId +
					'#wechat_redirect';
			},
			
			goOrderList(payItem){
				if(payItem && payItem.orderIds){
					this.$jump('/pages/order/orderforbill?orderIds=' + payItem.orderIds);
				}else{
					uni.showToast({
						icon: 'none',
						title: '订单为空'
					});
				}
			}
		}
	}
</script>

<style lang="scss">
	.content {
		.top-back {
			padding: 20upx 0upx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 28upx;
			color: #000000;
			margin-left: 20upx;
			margin-right: 20upx;
			border-bottom: solid 1px #E0E0E0;
		}
		
		.empty {
			margin-top: 100upx;
			text-align: center;
		}

		.bill-content {

			.bills {
				padding: 20upx 0upx 20upx 0upx;
				color: #000000;
				// margin-top: 100upx;
				border-bottom: solid 1px #E0E0E0;

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

					.goods_amount {
						color: $uni-xj-good-price;
					}
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
	}
</style>
