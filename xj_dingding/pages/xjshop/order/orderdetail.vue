<template>
	<view class="orderdetail">
		<view class="top-status" v-if="orderDetail">
			<view v-if="orderDetail.status==='1'">等待分拣</view>
			<view v-if="orderDetail.status==='2' || orderDetail.status==='3'">正在配送</view>
			<view v-if="orderDetail.status==='4'">配送完成</view>
		</view>
		<view v-else class="top-status">等待分拣</view>
		<view class="adress">
			<view class="adress-top font-bold">
				<view>收货人：{{orderDetail.merchantName}}</view>
				<view class="phone font-bold">{{orderDetail.contactPhone}}</view>
			</view>
			<view class="adress-bot">{{orderDetail.contactAddress}}</view>
		</view>

		<view class="mid-content" v-if="orderDetail">
			<view class="paystatus_contaner">
				<text class="status_tip">订单编号：</text>
				<text class="status_value font-bold">{{orderDetail.code}}</text>
			</view>
			<view class="paystatus_contaner">
				<text class="status_tip">下单时间：</text>
				<text class="status_value">{{orderDetail.createTime}}</text>
			</view>
			<view class="paystatus_contaner">
				<text class="status_tip">收货时间：</text>
				<text class="status_value">{{getOrderDeliverTime(orderDetail.deliveryDate)}}</text>
			</view>
			<view class="paystatus_contaner">
				<text class="status_tip">商户信息：</text>
				<text class="status_value">{{orderDetail.merchantName}}</text>
			</view>
			<!-- <view class="paystatus_contaner">
				<text class="status_tip">签收方式：</text>
				<text class="status_value">-</text>
			</view> -->
			<view class="paystatus_contaner">
				<text class="status_tip">订单备注：</text>
				<text class="status_value">{{orderDetail.remark?orderDetail.remark:'-'}}</text>
			</view>
		</view>

		<view class="mid-amount-content">
			<view class="mid-amount">
				<text>下单金额</text>
				<text>¥{{orderDetail.price}}</text>
			</view>
			<view class="mid-amount">
				<text>出库金额</text>
				<text v-if="orderDetail.status!='3' && orderDetail.status!='4'">商品正在称重中，出库后更新金额</text>
				<text v-else>{{orderDetail.sortingPrice?orderDetail.sortingPrice:'-'}}</text>
			</view>
			<!-- <view class="mid-amount">
				<text>运费</text>
				<text class="color-price">¥0.00</text>
			</view> -->
			<view class="bottom-line"></view>
			<view class="mid-amount">
				<text>合计金额</text>
				<text class="color-price font-bold"
					v-if="orderDetail.status!='3' && orderDetail.status!='4'">¥{{orderDetail.price}}</text>
				<text class="color-price font-bold"
					v-else>¥{{orderDetail.sortingPrice?orderDetail.sortingPrice:orderDetail.price}}</text>
			</view>
			<!-- <view class="mid-amount">
				<text>已支付金额</text>
				<text
					v-if="orderDetail.payStatus==='1'">¥{{orderDetail.sortingPrice?orderDetail.sortingPrice:'0.00'}}</text>
				<text v-else>¥0.00</text>
			</view> -->
		</view>

		<view class="good-list" v-if="orderDetail.goodsOrderItems && orderDetail.goodsOrderItems.length>0"
			v-for="(item,index) in orderDetail.goodsOrderItems" :key="index">
			<view class="image-wrapper">
				<image class="multi-image2" mode="aspectFill"
					:src="parseGoodImgUrl(item)"
					@error="imageError(item)"></image>
			</view>

			<view class="item-right">
				<view class="item-row">
					<text class="title">{{item.goodName}}</text>
					<text class="out-price">（出库单价：{{item.price}}元/公斤）</text>
				</view>
				<view class="item-row">
					<text class="order-count">下单数：{{item.quantity}}{{item.unitName}}</text>
					<text class="real-count" v-if="item.sortingWeight">出库数：{{item.sortingWeight}}公斤</text>
					<text class="real-count" v-else>出库数：-</text>
				</view>
				<view class="item-row">
					<view>
						<text class="price">下单金额：</text>
						<text class="price font-bold">¥{{item.amount}}元</text>
					</view>
					<view class="real-price">
						<text class="price">出库金额：</text>
						<text class="color-price font-bold" v-if="item.sortingPrice">¥{{item.sortingPrice}}元</text>
						<text class="price" v-else>-</text>
					</view>

				</view>
				<view class="item-row">
					<view>
						<text class="price">备注：</text>
						<text class="price" v-if="item.comment">{{item.comment}}</text>
						<text class="price" v-else>-</text>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		components: {},

		data() {
			return {
				//默认的商品图片
				defaultGoodsImg: '/static/xj_logo.jpg',

				//当前订单id
				orderId: '',
				//订单详情对象
				orderDetail: {},
			};
		},

		onLoad(options) {
			console.log('orderdetail--->', options);
			if (options && options.orderId) {
				this.orderId = options.orderId;
			}
		},

		onShow() {
			this.init();
		},

		methods: {
			init() {
				this.getOrderDetail();
			},

			getOrderDetail() {
				uni.showLoading({
					title: '正在加载...'
				});

				let self = this;
				let url = '/shop/order/appListForDingding?pageNum=' + 1 + '&pageSize=' + 10 + '&orderId=' + this.orderId;
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log("orderList--->", resp);
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							//数据累加
							self.orderDetail = resp.data.rows[0];
						} else {
							self.orderDetail = {};
						}
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
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
	.orderdetail {
		.top-status {
			padding-top: 20upx;
			padding-bottom: 20upx;
			background-color: $theme-color;
			font-size: 28upx;
			color: #ffffff;
			text-align: center;
		}

		.adress {
			padding: 20upx 20upx 20upx 80upx;
			font-size: 28upx;
			border-bottom: 20upx solid rgba(0, 0, 0, 0.05);
			color: #000000;
			background: url(../../../static/location.png) no-repeat left center;
			background-size: 40upx 40upx;
			background-position-x: 20upx;

			.adress-top {
				display: flex;
				align-items: center;
				justify-content: space-between;

				.phone {
					font-size: 30upx;
					color: #000000;
					margin-right: 20upx;
				}
			}

			.adress-bot {
				margin-top: 20upx;
				font-size: 32upx;
				color: #000000;
				// font-weight: bold;
			}
		}

		.mid-content {
			padding: 10upx 0upx 10upx 0upx;
			border-bottom: 20upx solid rgba(0, 0, 0, 0.05);
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

		.font-bold {
			font-weight: bold;
		}

		.color-price {
			color: $uni-xj-good-price;
		}

		.bottom-line {
			border-bottom: solid 1px #E0E0E0;
			margin: 10upx 20upx 10upx 20upx;
		}

		.mid-amount-content {
			padding: 10upx 0upx 10upx 0upx;
			border-bottom: 20upx solid rgba(0, 0, 0, 0.05);
		}

		.mid-amount {
			padding: 10upx 20upx 10upx 20upx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			font-size: 28upx;
			color: #000000;
		}

		.good-list {
			display: flex;
			position: relative;
			align-content: center;
			align-items: center;
			padding: 30upx 0upx;
			margin-left: 20upx;
			margin-right: 20upx;
			border-bottom: solid 1px #E0E0E0;

			.image-wrapper {
				width: 140upx;
				height: 140upx;
				flex-shrink: 0;
				position: relative;

				image {
					width: 140upx;
					height: 140upx;
					border-radius: 8upx;
				}
			}

			.item-right {
				display: flex;
				flex-direction: column;
				flex: 1;
				overflow: hidden;
				position: relative;
				padding-left: 15upx;
				// padding-bottom: 5upx;
				font-size: 26upx;

				.item-row {
					margin-top: 5upx;
					margin-bottom: 5upx;

					display: flex;
					flex-direction: row;

					// align-items: center;
					// justify-content: space-between;
				}

				.title {
					color: #1A1A1A;
				}

				.price {
					// margin-left: 10upx;
					color: #666;
				}
				
				.out-price {
					// margin-left: 10upx;
					font-size: 20upx;
					color: #666;
				}

				.order-count {
					color: #666;
				}

				.real-count {
					margin-left: 120upx;
					color: #666;
				}

				.real-price {
					margin-left: 35upx;
				}
			}
		}


	}
</style>
