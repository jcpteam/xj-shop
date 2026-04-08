<template>
	<view>
		<view class="cart-list">
			<view class="cart-list-top">
				<view class="cancel-text" @click="goBack()">取消</view>
				<view class="title-text" >订单:{{orderDetail.code}}</view>
				<view class="submit-btn" :disabled="submitDisable" @click="submitModify">保存</view>
			</view>

			<view class="cart-item" v-for="(item,index) in orderGoodList" :key="index">

				<view class="cart-item-top">
					<view class="image-wrapper">
						<image class="multi-image2" mode="aspectFill" :src="parseGoodImgUrl(item)"
							@error="imageError(item)" @click="detail(item)"></image>
					</view>

					<view class="item-right">
						<text class="title">{{item.propertyName}}</text>

						<view class="item-last">
							<view class="left-price">
								<view class="price">
									<text
										class="multi-subprice">{{getGoodSalePrice(item).salePrice}}元/{{getGoodSalePrice(item).saleUnitName}}</text>
									<text class="multi-mainprice">{{item.settingPrice}}元/{{item.mainUnitName}}</text>
								</view>
								<view v-if="item.saleNum && item.saleNum > 0">
									<text class="multi-standardweight" v-if="item.saleNum > 300">
										库存充足
									</text>
									<text class="multi-standardweight" v-if="item.saleNum > 100 && item.saleNum<=300">
										库存正常
									</text>
									<text class="multi-standardweight" v-if="item.saleNum > 0 && item.saleNum<=100">
										库存余{{item.saleNum}}
									</text>
								</view>
								<view class="sale-finish" v-else>
									今日已售罄，请删除商品再下单
								</view>
							</view>
							<!-- <view v-if="item.saleNum && item.saleNum > 0" class="right-add"> -->
							<view class="right-add">
								<uni-number-box v-if="item.quantity > 0" class="multi-addgoodnum" :min="0"
									:max="item.maxQuantity" :value="item.quantity" :itemObj="item" @change="numberChange">
								</uni-number-box>
								<text class="sale-num-tip" v-if="item.quantity > item.maxQuantity">库存不足，先修改数量再下单</text>
							</view>
						</view>
					</view>
				</view>

				<view class="remark_contaner" @click="addGoodRemark(item)">
					<text class="remark-tip">商品备注</text>
					<view class="remark-mid">
						<text class="remark">{{item.comment}}</text>
						<image class="arrow_image_remark" src="/static/arrow.png"></image>
					</view>
				</view>

			</view>

			<view class="bottom-total">
				<view class="add-new-good" @click="addNewGoods()">新增商品</view>
				
				<view class="total-box">
					<text class="price-title">商品小计：</text>
					<text class="price-number">¥{{totalPrice}}</text>
				</view>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	import uniNumberBox from '@/components/xjshop/cart/uni-number-box.vue'
	export default {
		name: 'order-modify',
		components: {
			uniNumberBox
		},

		data() {
			return {
				//订单id
				orderId: '',
				useCache:'0',
				//订单详情
				orderDetail: {
					code: ''
				},
				//订单商品列表
				orderGoodList: [],
				//订单商品总价格
				totalPrice: 0,
				//默认的商品图片
				defaultGoodsImg: '/static/xj_logo.jpg',

				//是否禁用提交按钮
				submitDisable: false,
			}
		},
		onLoad(options) {
			//获取页面传进来的订单id
			this.orderId = options.orderId;
			if (!this.orderId || this.orderId == undefined) {
				// 关闭当前页面 返回列表页面
				uni.navigateBack()
				return;
			}
			this.useCache = options.useCache;
			console.log('ordermodify usecache--->',this.useCache);

			this.init();

			//监听备注页面返回
			let self = this;
			uni.$on("goodCommentChange", res => {
				if (res && res.goodsId) {
					//更新内存值并显示
					self.orderGoodList.forEach(function(e) {
						if (e.goodsId == res.goodsId) {
							e.comment = res.comment;
						}
					});
					self.$forceUpdate();
				}
			})
		},

		onUnload() {
			// 移除监听事件    
			uni.$off('goodCommentChange');
		},

		methods: {
			init() {
				this.getOrderDetail();
			},
			
			//获取订单详情数据
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
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							self.orderDetail = resp.data.rows[0];
							//遍历点单中的商品数据，拿到goodsId
							self.queryOrderGoodsList(self);
						}else{
							uni.hideLoading();
						}
					}else{
						uni.hideLoading();
					}
				}, function(error) {
					uni.hideLoading();
					uni.showToast({
						title: "获取订单详情失败，请退出重试！",
						icon: 'none',
					});
				})
			},

			//获取订单中的商品列表
			queryOrderGoodsList(vm) {
				//商品id对应商品map
				let goodsId2InfoMap = {};
				//商品id列表
				let goodsIdList = [];
				//商品总金额
				let totalPrice = 0;
				
				console.log('test usecache--->',vm.useCache);
				if(vm.useCache === '1'){
					let tempMap = uni.getStorageSync('userModifyOrderGoodsMap') || {};
					for (var goodsId in tempMap) { //遍历对象的所有属性，包括原型链上的所有属性
						let cartItem = tempMap[goodsId];
						if (cartItem.quantity > 0) {
							goodsIdList.push(cartItem.goodsId);
							goodsId2InfoMap[cartItem.goodsId] = cartItem;
							totalPrice += Number(cartItem.amount);
						}
					}
				}else{
					if (vm.orderDetail.goodsOrderItems) {
						vm.orderDetail.goodsOrderItems.forEach(function(e) {
							if (e.goodsId) {
								goodsIdList.push(e.goodsId);
								goodsId2InfoMap[e.goodsId] = e;
								totalPrice += Number(e.amount);
							}
						});
					}
				}
				
				if (totalPrice > 0 && String(totalPrice).indexOf('.') != -1) {
					totalPrice = Number(totalPrice.toFixed(2));
				}

				//筛选购物车商品id列表
				if (!goodsIdList || goodsIdList.length <= 0) {
					return;
				}

				uni.showLoading({
					title: '正在加载...'
				});

				//查询报价单商品详情
				let self = vm;
				let url = '/shop/quotationGoods/appList?pageNum=1' + '&pageSize=100' + '&goodsIdList=' + goodsIdList;
				let daiclientid = uni.getStorageSync('daiclientid');
				if(vm.orderDetail.merchantId){
					url += '&merchantId=' + vm.orderDetail.merchantId;
				};
				if(vm.orderDetail.deptId){
					url += '&loginUserDeptId=' + vm.orderDetail.deptId;
				};
				self.sendRequest({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						//默认设置库存为10000
						resp.data.rows.forEach(function(e) {
							e.saleNum = e.settingQuanintiy; //设置商品库存为spu总库存
							//为报价单商品填充下单数量信息
							if (goodsId2InfoMap && goodsId2InfoMap[e.goodsId]) {
								var pInfo = self.getGoodSalePrice(e);
								e.price = pInfo.finalPrice; //结算价格存入数据库
								e.unitId = pInfo.saleUnitId;
								e.quantity = goodsId2InfoMap[e.goodsId].quantity;
								e.comment = goodsId2InfoMap[e.goodsId].comment;
								e.itemId = goodsId2InfoMap[e.goodsId].itemId;
								e.deptId = vm.orderDetail.deptId;
								e.amount = goodsId2InfoMap[e.goodsId].amount;
								e.maxQuantity = (e.saleNum + e.quantity);
							}
						})
						self.orderGoodList = resp.data.rows;
						self.totalPrice = totalPrice;
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
					uni.showToast({
						title: "获取报价单商品失败，请退出重试！",
						icon: 'none',
					});
				})
			},

			//输入数量的改变
			numberChange(value, item) {
				console.log("numberChange--->value:", value);
				//更新商品的总数
				item.quantity = value;
				console.log("tempData--->itemId:", item.itemId);
				//延迟执行，解决numberbox组件bug
				setTimeout(()=>{
					//获取商品销售价格信息，重新计算更新商品金额
					var priceInfo = this.getGoodSalePrice(item);
					item.amount = item.quantity * priceInfo.salePrice; //销售总价
					//如果数量为0，则从商品列表移除该商品
					let self = this;
					if (item.quantity <= 0) {
						let tempData = [];
						for (var i = 0; i < self.orderGoodList.length; i++) {
							if (item.itemId != self.orderGoodList[i].itemId) {
								tempData.push(self.orderGoodList[i]);
							}
						}
						console.log("tempData--->value:", tempData);
						self.orderGoodList = tempData;
						self.$forceUpdate();
					}
					//重新计算商品总金额
					let totalPrice = 0;
					for (var i = 0; i < this.orderGoodList.length; i++) {
						let im = this.orderGoodList[i];
						totalPrice += Number(this.orderGoodList[i].amount);
					}
					if (totalPrice > 0 && String(totalPrice).indexOf('.') != -1) {
						totalPrice = Number(totalPrice.toFixed(2));
					}
					this.totalPrice = totalPrice;
				},200);
				
			},

			submitModify() {
				this.submitDisable = true;
				uni.showLoading({
					title: '正在保存...'
				});
				let self = this;
				//查询一下商户对应的用户id
				let req1 = {
					pageNum: 1,
					pageSize: 10,
					memberId: this.orderDetail.merchantId
				}
				let url1 = '/system/user/list'
				this.sendRequest({'url': url1, 'method': 'POST', 'data': req1, 'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							let clientObj = resp.data.rows[0];
							self.submitModifyWithUserId(self,clientObj.userId);
						}
					}
				})
			},
			
			submitModifyWithUserId(vm,userId){
				let orderStatus = vm.orderDetail.status;
				if (vm.orderGoodList && vm.orderGoodList.length > 0) {} else {
					orderStatus = '5'; //如果订单里面没有商品则 废除订单
				}
				//更新订单请求参数
				let orderParams = {
					orderId: vm.orderDetail.orderId,
					deptId: vm.orderDetail.deptId,
					status: orderStatus,
					merchantId: vm.orderDetail.merchantId,
					price: vm.totalPrice,
					itemList: vm.orderGoodList
				}
				orderParams.userId = userId;
				console.log("submitModify orderParams--->", orderParams);
				
				let self = vm;
				let url = '/shop/order/appEdit';
				vm.sendRequest({
					'url': url,
					'data': orderParams
				}, function(resp) {
					console.log("submit modify order--->", resp);
					if (resp.data.code === 0) {
						uni.hideLoading();
						// 关闭当前页面 返回列表页面
						self.goBack();
					} else {
						uni.hideLoading();
						let msg = "保存订单失败！" + resp.data.msg;
						uni.showToast({
							title: msg,
							icon: 'none',
						});
						self.submitDisable = false;
					}
				}, function(error) {
					uni.hideLoading();
					uni.showToast({
						title: "保存订单失败！",
						icon: 'none',
					});
					self.submitDisable = false;
				})
			},
			
			//添加商品备注页面
			addGoodRemark(item) {
				let goodsId = item.goodsId;
				let comment = item.comment || '';
				let cartItemId = item.itemId;
				this.$jump('/pages/order/cartgoodremark?goodsId=' + goodsId + "&cartItemId=" + cartItemId + "&comment=" + comment);
			},

			goBack() {
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},
			
			//跳转新增新的商品分类页面
			addNewGoods(){
				//先获取跳转商户的报价单id
				//先获取当前商户的用户信息保存
				let vm = this
				let req = {
					pageNum: 1,
					pageSize: 10,
					id: vm.orderDetail.merchantId
				}
				let url = '/shop/member/list'
				vm.sendRequest({'url': url, 'method': 'POST', 'data': req, 'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							vm.addNewGoodsAfterGetQuotationId(resp.data.rows[0].quotationId);
						}else{
							vm.addNewGoodsAfterGetQuotationId('');
						}
					}else{
						vm.addNewGoodsAfterGetQuotationId('');
					}
				}, function(error){
					vm.addNewGoodsAfterGetQuotationId('');
				})
			},
			
			addNewGoodsAfterGetQuotationId(quotationId){
				//先缓存编辑的订单商品数据
				if(this.orderGoodList){
					uni.setStorageSync('userModifyQuotationId', quotationId);
					uni.setStorageSync('userModifyOrderId', this.orderId);
					uni.setStorageSync('userModifyMerchantId', this.orderDetail.merchantId);
					uni.setStorageSync('userModifyDeptId', this.orderDetail.deptId);
					let tempGoodsMap = {};
					for (var i = 0; i < this.orderGoodList.length; i++) {
						tempGoodsMap[this.orderGoodList[i].goodsId] = this.orderGoodList[i];
					}
					console.log('addNewGoods--->goodsMap--->',tempGoodsMap);
					uni.setStorageSync('userModifyOrderGoodsMap', tempGoodsMap);
				}else{
					uni.setStorageSync('userModifyQuotationId', '');
					uni.setStorageSync('userModifyOrderId', '');
					uni.setStorageSync('userModifyMerchantId', '');
					uni.setStorageSync('userModifyDeptId', '');
					uni.setStorageSync('userModifyOrderGoodsMap', {});
				}
				
				uni.redirectTo({
					url:'/pages/xjshop/order/ordermodifyadd'
				});
				// this.$jump('/pages/order/ordermodifyadd');
			}
		},
		mounted() {}

	}
</script>

<style lang="scss">
	.cart-list {
		.cart-list-top {
			padding: 20upx 0upx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-size: 36upx;
			color: #000000;
			margin-left: 20upx;
			margin-right: 20upx;
			border-bottom: solid 1px #E0E0E0;
			
			.cancel-text{
				width: 120upx;
				text-align: center;
				background: #999999;
				color: #FFFFFF;
				border-radius: 4px;
			}
			
			.title-text{
				margin-left: 20upx;
				margin-right: 20upx;
				font-size: 26upx;	
			}
			
			.submit-btn{
				width: 120upx;
				text-align: center;
				background: $theme-color;
				color: #FFFFFF;
				border-radius: 4px;
			}

			.top-title {
				//设置文本单行显示，超出显示省略号
				display: -webkit-box;
				overflow: hidden;
				text-overflow: ellipsis;
				word-wrap: break-word;
				white-space: normal !important;
				-webkit-line-clamp: 2; //设置行数
				-webkit-box-orient: vertical;
			}
		}

		/* 购物车列表项 */
		.cart-item {
			.cart-item-top {
				display: flex;
				position: relative;
				align-content: center;
				align-items: center;
				padding: 30upx 0upx 0upx 10upx;
				margin-left: 20upx;
				margin-right: 20upx;
				// border-bottom: solid 1px #E0E0E0;

				.cart-check {
					width: 30upx;
					height: 30upx;
				}

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
					padding-bottom: 5upx;

					.title {
						color: #1A1A1A;
						font-size: 30upx;
						line-height: 34.9upx;

						//设置文本单行显示，超出显示省略号
						display: -webkit-box;
						overflow: hidden;
						text-overflow: ellipsis;
						word-wrap: break-word;
						white-space: normal !important;
						-webkit-line-clamp: 2; //设置行数
						-webkit-box-orient: vertical;
					}


					.attr {
						color: #1A1A1A;
						opacity: 0.5;
						font-size: 24upx;
						margin-top: 3upx;

						//设置文本单行显示，超出显示省略号
						display: -webkit-box;
						overflow: hidden;
						text-overflow: ellipsis;
						word-wrap: break-word;
						white-space: normal !important;
						-webkit-line-clamp: 1; //设置行数
						-webkit-box-orient: vertical;
					}



					.item-last {
						display: flex;
						align-items: center;
						justify-content: space-between;

						.left-price {
							display: flex;
							flex: 4;
							flex-direction: column;

							.multi-subprice {
								color: #1A1A1A;
								opacity: 0.5;
								font-size: 18upx;
								line-height: 27.1upx;
								margin-left: 8upx;
								margin-top: 10upx;
							}

							.multi-mainprice {
								color: #FF8C00;
								font-size: 30upx;
								line-height: 27.1upx;
								margin-left: 8upx;
								margin-top: 8upx;
							}

							.price {
								display: flex;
								flex-direction: column;
							}

							.multi-standardweight {
								padding: 2upx 10upx;
								text-align: center;
								color: $uni-xj-good-price;
								font-size: 20upx;
								line-height: 27.1upx;
								margin-left: 8upx;
								margin-top: 8upx;
								border: 1px solid $uni-xj-good-price;
								border-radius: 4px;
							}

							.sale-finish {
								padding: 2upx 5upx;
								text-align: left;
								color: $uni-xj-good-price;
								font-size: 24upx;
								line-height: 27.1upx;
								margin-right: 20upx;
								margin-top: 8upx;
							}
						}

						.right-add {
							display: flex;
							flex: 2;
							flex-direction: column;
							align-items: flex-end;
							justify-content: flex-end;

							.multi-addgood {
								width: 60upx;
								height: 60upx;
								margin-top: 10upx;
								margin-right: 20upx;
								display: block;
								margin-left: auto;
							}

							.multi-addgoodnum {
								display: flex;
								flex-direction: row;
								width: 160upx;
								height: 60upx;
								margin-top: 10upx;
								margin-right: 20upx;
								margin-left: auto;
							}

							.min-weight {
								// color: #1A1A1A;
								opacity: 0.5;
								font-size: 18upx;
								line-height: 27.1upx;
								margin-left: 8upx;
								margin-top: 10upx;
							}

							.sale-num-tip {
								margin-top: 4upx;
								font-size: 16upx;
								color: $uni-xj-good-sale-finish
							}
						}
					}
				}
			}

			.remark_contaner {
				padding: 15upx 20upx 15upx 20upx;
				display: flex;
				align-items: center;
				justify-content: space-between;
				font-size: 28upx;
				color: #000000;
				background-color: #fffef9;
				border-bottom: solid 1px #E0E0E0;

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


		}

		.bottom-total{
			display: flex;
			justify-content: space-between;
			align-items: center;
			
			.add-new-good{
				margin-left: 20upx;
				font-size: 32upx;
				color: black;
				padding-top: 5upx;
				padding-bottom: 5upx;
				width: 180upx;
				text-align: center;
				background: $theme-color;
				color: #FFFFFF;
				border-radius: 4px;
			}
			
			.total-box {
				display: flex;
				justify-content: flex-end;
				align-items: center;
				margin-right: 20upx;
				margin-top: 30upx;
				margin-bottom: 30upx;
			
				.price-title {
					font-size: 32upx;
					color: black;
				}
			
				.price-number {
					font-size: 32upx;
					color: $uni-xj-good-price;
				}
			}
		}
	}
</style>
