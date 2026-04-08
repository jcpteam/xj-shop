<template>
	<view class="cartinfo">
		<view class="adress">
			<view class="adress-top">
				<view>收货人：{{userInfo.userName}}</view>
				<view class="phone">{{userInfo.phonenumber}}</view>
			</view>
			<view class="adress-bot">{{userInfo.address}}</view>
		</view>
		<view class="goods">
			<view class="customer-name">供应商（{{userInfo.deptName}}）</view>
			<view v-if="isCartGoodsInvalid" class="godds-invalid-tip">{{goodsInvalidTip}}</view>
			<view class="image_contaner" @click="gotoCartItemDetail">
				<view>
					<image v-for="(val, key, i) in userCartMap" class="goods_image" v-if="userCartMap[key].quantity>0"
						:src="parseGoodImgUrl(userCartMap[key])" @error="imageError(userCartMap[key])"></image>
				</view>
				<view class="goods-num-content">
					<text class="goods_num">共{{goodCount}}种商品</text>
					<image class="arrow_image" src="/static/arrow.png"></image>
				</view>
			</view>
			<view class="time_contaner" @click="showtimePicker">
				<text>收货时间</text>
				<uni-datetime-picker class="delivery-date" ref="delivertime" v-model="deliveryDatetime" type="date"
					:border="false" />
				<!-- <view>
					<text>08-08 07:00~08-08 08:00</text>
					<image class="arrow_image" src="/static/arrow.png"></image>
				</view> -->
			</view>
			<view class="paytype_contaner">
				<text>支付方式</text>
				<text>到货现金支付</text>
			</view>
			<view class="trans_contaner">
				<text>运费</text>
				<text class="trans_amount">¥0.00</text>
			</view>
			<view class="remark_contaner" @click="addRemark">
				<text class="remark-tip">订单备注</text>
				<view class="remark-mid">
					<text class="remark">{{orderRemark}}</text>
					<image class="arrow_image_remark" src="/static/arrow.png"></image>
				</view>
			</view>

			<view class="amount_contaner">
				<text>共{{goodCount}}种</text>
				<view class="amount_total">
					<text>小计：</text>
					<text class="amount_total_value">¥{{goodsAmount}}</text>
				</view>
			</view>
		</view>

		<!-- 底部菜单栏 -->
		<view class="action-section">
			<view class="total-box">
				<text class="price-title">实付：</text>
				<text class="price-number">¥{{goodsAmount}}</text>
			</view>
			<button :disabled="submitDisable" class="confirm-btn" @click="goToAddGoods">提交下单</button>
		</view>
	</view>
</template>

<script>
	import uniDatetimePicker from '@/components/common/uni-datetime-picker/uni-datetime-picker.vue'

	export default {
		name: 'cart-info',

		components: {
			uniDatetimePicker,
		},

		props: {
			ramdom: Number,
			orderRemark: String
		},

		data() {
			return {
				//默认的广告图片，加载失败则使用此图片
				defaultGoodsImg: '/static/xj_logo.jpg',
				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},
				//购物车信息
				userCartMap: uni.getStorageSync('userCartMap') || {},
				//商品数量
				goodCount: 0,
				//总费用
				goodsAmount: 0,
				//收货日期
				deliveryDatetime: '',
				//订单备注
				// orderRemark:''

				//是否禁用提交按钮
				submitDisable: false,
				
				//是否购物车商品失效
				isCartGoodsInvalid:false,
				goodsInvalidTip:'商品已被下架或删除，请先删除失效商品再下单',
			}
		},

		created() {
			this.init();
		},

		watch: {
			ramdom(newValue, oldValue) {
				this.init();
			},

			orderRemark(newValue, oldValue) {
				this.orderRemark = newValue;
			}
		},

		methods: {
			showtimePicker() {
				this.$refs.delivertime.show();
			},

			init() {
				let myDate = new Date();
				let hour = myDate.getHours(); // 时
				if (hour < 9) {
					//默认规则是：2021-10-08 09:00:00  到  2021-10-09 09:00:00  这段时间下单的交货日期就是2021-09-09
					//即当前小时小于9就是当天日期
				} else {
					myDate.setDate(myDate.getDate() + 1);
				}
				let endDate = myDate.toISOString().slice(0, 10)
				// this.deliveryDatetime = endDate + ' 07:30:00';
				this.deliveryDatetime = endDate;
				console.log('初始化收货时间--->', this.deliveryDatetime);

				this.userCartMap = uni.getStorageSync('userCartMap') || {};
				if (this.userCartMap) {
					this.goodCount = 0;
					this.goodsAmount = 0;
					for (var goodsId in this.userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
						if (this.userCartMap[goodsId].quantity > 0) {
							this.goodCount += 1;
							this.goodsAmount += Number(this.userCartMap[goodsId].amount);
						}
					}
					if (this.goodsAmount > 0 && String(this.goodsAmount).indexOf('.') != -1) {
						this.goodsAmount = Number(this.goodsAmount.toFixed(2));
					}
					// this.goodsAmount = this.goodsAmount.toFixed(2);
				}
				console.log("goodCount--->", this.goodCount);
				console.log("goodsAmount>", this.goodsAmount);
				
				this.checkCartGoodsListInvalid();
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

			//跳转已选商品列表页面
			gotoCartItemDetail() {
				console.log("time--->", this.deliveryDatetime);
				this.$jump('/pages/order/cartitem')
			},

			//添加备注页面
			addRemark() {
				this.$jump('/pages/order/orderremark?remark=' + this.orderRemark)
			},

			//添加订单
			goToAddGoods() {
				//检测购物车商品数据
				this.userCartMap = uni.getStorageSync('userCartMap') || {};
				if (!this.userCartMap) {
					uni.showToast({
						title: "购物车商品为空！",
						icon: 'none',
					});
					return;
				}

				if (this.userInfo.quotationInfo) {
					if (!this.$compareCurTime(this.userInfo.quotationInfo.operateStartTime) && this.$compareCurTime(this
							.userInfo.quotationInfo.operateEndTime)) {
						//在下单时间内
					} else {
						uni.showToast({
							title: "请在商品供应时间内提交下单",
							icon: 'none',
						});
						return;
					}
				}
				
				if(this.isCartGoodsInvalid){
					uni.showToast({
						title: this.goodsInvalidTip,
						icon: 'none',
					});
					return;
				}

				this.submitDisable = true;
				uni.showLoading({
					title: '正在提交...'
				});
				
				this.checkTodayOrderBeforeSubmit();
			},
			
			//提交前先检查当天是否有订单
			checkTodayOrderBeforeSubmit(){
				//检查当前是否已有订单
				let status = '';
				let self = this;
				let url = '/shop/order/checkTodayOrder?merchantId=' + this.userInfo.memberId;
				let curDate = this.getNowFormatDate();
				url += '&createBeginDate=' + curDate + ' 00:00:00';
				url += '&createEndDate=' + curDate + ' 59:59:59';
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log("orderList--->", resp);
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							uni.showModal({
								title: '温馨提示',
								content: '今日已有订单，确定提交新的订单吗？',
								success: function(res) {
									if (res.confirm) {
										self.submitOrderAfterCheck();
									}else{
										self.submitDisable = false;
										uni.hideLoading();
									}
								}
							});
						}else{
							self.submitOrderAfterCheck();
						}
					}else{
						self.submitOrderAfterCheck();
					}
				}, function(error) {
					self.submitOrderAfterCheck();
				});
			},
			
			submitOrderAfterCheck(){
				//构建订单参数
				let itemData = [];
				let priceTotal = 0;
				for (var goodsId in this.userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
					let cartItem = this.userCartMap[goodsId];
					if (cartItem.quantity > 0) {
						let goodItem = cartItem;
						goodItem.itemId = null;
						priceTotal += Number(goodItem.amount);
						goodItem.deptId = this.userInfo.deptId;
						itemData.push(goodItem);
					}
				}
				if (priceTotal > 0 && String(priceTotal).indexOf('.') != -1) {
					priceTotal = Number(priceTotal.toFixed(2));
				}
				
				if (itemData && itemData.length > 0) {
					let orderParams = {
						// itemData: JSON.stringify(itemData),
						itemList: itemData,
						price: priceTotal
					};
					orderParams.userId = this.userInfo.userId;
					orderParams.merchantId = this.userInfo.memberId;
					if (this.deliveryDatetime && this.deliveryDatetime.length < 11) {
						orderParams.deliveryDate = this.deliveryDatetime + " 00:00:00";
					} else {
						orderParams.deliveryDate = this.deliveryDatetime;
					}
					orderParams.comment = this.orderRemark;
					orderParams.status = '1';
					orderParams.deptId = this.userInfo.deptId;
					console.log("orderParams--->", orderParams);
				
					let self = this;
					let url = '/shop/order/appAdd';
					this.sendRequest({
						'url': url,
						'data': orderParams
					}, function(resp) {
						console.log("submit order--->", resp);
						if (resp.data.code === 0) {
							//删除成功，重新初始化购物车数据
							self.initUserCartData(function(resp) {
								self.refreshCartData(self);
								self.submitDisable = false;
							}, function(error) {
								self.refreshCartData(self);
								self.submitDisable = false;
							});
							uni.hideLoading();
						} else {
							uni.hideLoading();
							let msg = "订单提交失败！" + resp.data.msg;
							uni.showToast({
								title: msg,
								icon: 'none',
							});
							self.submitDisable = false;
						}
					}, function(error) {
						uni.hideLoading();
						uni.showToast({
							title: "订单提交失败！",
							icon: 'none',
						});
						self.submitDisable = false;
					})
				} else {
					uni.hideLoading();
					uni.showToast({
						title: "购物车商品为空！",
						icon: 'none',
					});
					self.submitDisable = false;
				}
			},

			refreshCartData(vm) {
				uni.showToast({
					title: "订单提交成功！",
					icon: 'none',
				});
				setTimeout(() => {
					vm.$emit("refreshCart");
				}, 1000)
			},

			//获取今天日期，格式YYYY-MM-DD
			getNowFormatDate() {
				var date = new Date();
				var seperator1 = "-";
				var year = date.getFullYear();
				var month = date.getMonth() + 1;
				var strDate = date.getDate();
				if (month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if (strDate >= 0 && strDate <= 9) {
					strDate = "0" + strDate;
				}
				var currentdate = year + seperator1 + month + seperator1 + strDate;
				return currentdate;
			},
			
			//检查购物车商品是否有效
			checkCartGoodsListInvalid() {
				this.submitDisable = true;
				this.isCartGoodsInvalid = false;
				//筛选购物车商品id列表
				let goodsIdList = [];
				if(this.userCartMap){
					for (var goodsId in this.userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
						if (this.userCartMap[goodsId].quantity > 0) {
							goodsIdList.push(goodsId);
						}
					}
				}
				if (!goodsIdList || goodsIdList.length <= 0) {
					this.isCartGoodsInvalid = false;
					this.submitDisable = false;
					return;
				}
				
				uni.showLoading({
					title: '正在加载...'
				});
			
				let self = this;
				let url = '/shop/quotationGoods/appList?pageNum=1' + '&pageSize=100' + '&goodsIdList=' + goodsIdList;
				if(this.userInfo && this.userInfo.memberId){
					url += '&merchantId=' + this.userInfo.memberId;
				};
				this.sendRequestWithDeptId({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						for(var i =0;i<resp.data.rows.length;i++){
							let e = resp.data.rows[i];
							//只要有商品状态不是1，说明已下架或者删除
							if(e.status != '1'){
								self.isCartGoodsInvalid = true;
								console.log('checkCartGoodsListInvalid--->invalid:',self.isCartGoodsInvalid);
								break;
							}
						}
					}
					uni.hideLoading();
					self.submitDisable = false;
				}, function(error) {
					uni.hideLoading();
					self.submitDisable = false;
				})
			},
		}
	}
</script>

<style lang="scss">
	.cartinfo {
		.adress {
			padding: 20upx 20upx 20upx 80upx;
			font-size: 28upx;
			border-bottom: 20upx solid rgba(0, 0, 0, 0.05);
			color: #000000;
			background: url(../../static/location.png) no-repeat left center;
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
				font-weight: bold;
			}
		}

		.goods {
			padding: 20upx;
			color: #000000;

			.customer-name {
				font-size: 32upx;
				padding-left: 20upx;
				padding-right: 20upx;
				color: #000000;
				font-weight: bold;
			}
			
			.godds-invalid-tip {
				margin-top: 10upx;
				font-size: 26upx;
				padding-left: 20upx;
				padding-right: 20upx;
				color: $uni-color-error;
			}

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
					width: 180upx;
					display: flex;
					align-items: center;
					justify-content: flex-end;

					.goods_num {
						flex: 5;
						font-size: 28upx;
						color: #666;
					}
				}

			}

			.time_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28upx;
				color: #000000;
			}

			.paytype_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28upx;
				color: #000000;
			}

			.trans_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28upx;
				color: #000000;

				.trans_amount {
					color: $uni-xj-good-price;
				}
			}

			.remark_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28upx;
				color: #000000;

				.remark-tip {
					flex: 2;
					width: 200upx;
				}

				.remark-mid {
					flex: 6;
					display: flex;
					align-items: center;
					justify-content: flex-end;

					.remark {
						color: #666;
						flex: 7;

						text-align: right;

						//设置文本单行显示，超出显示省略号
						display: -webkit-box;
						overflow: hidden;
						text-overflow: ellipsis;
						word-wrap: break-word;
						white-space: normal !important;
						-webkit-line-clamp: 1; //设置行数
						-webkit-box-orient: vertical;
					}

					.arrow_image_remark {
						width: 16upx;
						height: 24upx;
						margin-left: 10upx;
						vertical-align: middle;
					}
				}
			}

			.amount_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: flex-end;
				font-size: 28upx;
				color: #000000;

				.amount_total {
					margin-left: 30upx;
					font-size: 32upx;

					.amount_total_value {
						color: $uni-xj-good-price;
					}
				}
			}
		}

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

		.arrow_image {
			width: 16upx;
			height: 24upx;
			margin-left: 10upx;
			vertical-align: middle;
		}
	}
</style>
