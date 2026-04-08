<template>
	<view>
		<view class="cart-list">
			<view class="cart-list-top">
				<view @click="goBack()">&nbsp;<&nbsp;返回&nbsp;&nbsp;&nbsp;{{userInfo.deptName}}</view>
				<view @click="delCartGoods">删除</view>
			</view>

			<view class="cart-item" v-for="(item,index) in cartGoodList" :key="index"
				v-if="checkIfGoodInCart(item) && item.cartQuantity > 0">
				
				<view class="cart-item-top">
					<view class="image-wrapper">
						<image class="multi-image2" mode="aspectFill"
							:src="parseGoodImgUrl(item)"
							@error="imageError(item)" @click="detail(item)"></image>
					</view>
					
					<view class="item-right">
						<text class="title">{{item.propertyName}}</text>
					
						<view class="item-last">
							<view class="left-price">
								<view class="price">
									<text class="multi-subprice">{{getGoodSalePriceInfo(item).salePrice}}元/{{getGoodSalePriceInfo(item).saleUnitName}}</text>
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
							<view class="right-add">
								<uni-number-box v-if="item.cartQuantity > 0" class="multi-addgoodnum" :min="0"
									:max="item.saleNum" :value="item.cartQuantity" :itemObj="item" @change="numberChange">
								</uni-number-box>
								<text class="sale-num-tip" v-if="item.cartQuantity > item.saleNum">库存不足，先修改数量再下单</text>
							</view>
						</view>
					</view>
				</view>
				
				<view class="remark_contaner" @click="addGoodRemark(item)">
					<text class="remark-tip">商品备注</text>
					<view class="remark-mid">
						<text class="remark">{{getGoodComment(item.goodsId)}}</text>
						<image class="arrow_image_remark" src="/static/arrow.png"></image>
					</view>
				</view>
				
			</view>

			<view class="total-box">
				<text class="price-title">商品小计：</text>
				<text class="price-number">¥{{totalPrice}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	import uniNumberBox from '@/components/xjshop/cart/uni-number-box.vue'
	export default {
		name: 'cart-item',
		components: {
			uniNumberBox
		},
		props: {
			cartList: {
				type: Array,
				default: function() {
					return []
				}
			},
		},
		data() {
			return {
				//默认的商品图片
				defaultGoodsImg: '/static/xj_logo.jpg',
				//用户信息
				userInfo: {},
				//购物车信息
				userCartMap: uni.getStorageSync('userCartMap') || {},
				//购物车商品列表
				cartGoodList: [],
				//购物车商品总价格
				totalPrice: 0,
				
				//客户信息
				clientObj: {},
				clientUserId:'',
			}
		},
		onLoad() {
			let clientUserObj = uni.getStorageSync('daiClientUserObj');
			if(clientUserObj && clientUserObj.userId){
				this.userInfo = clientUserObj;
			}
			
			this.init();
			
			uni.$on("goodCommentChange", res => {
				if (res && res.goodsId) {
					//更新内存值并显示
					if (this.userCartMap && this.userCartMap[res.goodsId]) {
						this.userCartMap[res.goodsId].comment = res.comment;
						uni.setStorageSync('userCartMap', this.userCartMap);
						this.$forceUpdate();
					}
				}
			})
		},
		
		onUnload() {    
		    // 移除监听事件    
		     uni.$off('goodCommentChange');    
		},
		   
		methods: {
			init() {
				this.queryCartGoodsList();
			},

			//获取商品销售价格显示
			getGoodSalePriceInfo(item) {
				return this.getGoodSalePrice(item);
			},

			//获取购物车商品id列表
			getGoodsIdList() {
				//筛选购物车商品id列表
				let goodsIdList = [];
				if (this.userCartMap) {
					for (var goodsId in this.userCartMap) { //遍历对象的所有属性，包括原型链上的所有属性
						if (this.userCartMap[goodsId].quantity > 0) {
							goodsIdList.push(goodsId);
						}
					}
				}
				return goodsIdList;
				console.log("getGoodsIdList--->goodsIdList--->", goodsIdList);
			},
			
			//获取商品备注
			getGoodComment(goodsId){
				if (this.userCartMap && this.userCartMap[goodsId]) {
					return this.userCartMap[goodsId].comment;
				}else{
					return '';
				}
			},

			//分页获取商品列表
			queryCartGoodsList() {
				this.cartGoodList = [];
				//筛选购物车商品id列表
				let goodsIdList = this.getGoodsIdList();
				if (!goodsIdList || goodsIdList.length <= 0) {
					// 关闭当前页面 返回详细信息页面
					uni.navigateBack()
					return;
				}

				uni.showLoading({
					title: '正在加载...'
				});

				let self = this;
				let url = '/shop/quotationGoods/appList?pageNum=1' + '&pageSize=100' + '&goodsIdList=' + goodsIdList;
				// 查询代下单的客户ID
				let daiclientid = uni.getStorageSync('daiclientid');
				if(daiclientid){
					url += '&merchantId=' + daiclientid;
				};
				let customerArea = uni.getStorageSync('customerArea');
				if (customerArea) {
					url += '&loginUserDeptId=' + customerArea;
				}
				
				this.sendRequest({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						//默认设置库存为10000
						resp.data.rows.forEach(function(e){
							e.saleNum = e.settingQuanintiy;
						})
						
						self.cartGoodList = resp.data.rows;
						self.calculateCartTotalPrice();
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

			//判断商品是否在购物车有数据
			checkIfGoodInCart(item) {
				if (this.userCartMap[item.goodsId]) {
					item.cartQuantity = this.userCartMap[item.goodsId].quantity;
					return true;
				} else {
					return false;
				}
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

			//输入数量的改变
			numberChange(value, item) {
				let self = this;
				console.log("numberChange--->value:", value);
				item.cartQuantity = value;
				console.log("numberChange--->item.cartQuantity:", item.cartQuantity);

				//已经登录，添加到购物车
				this.addGoodToCart(item, function(resp) {
					console.log('addGoodToCart--->success...', resp);
					//删除了一个商品，需要刷新列表
					self.userCartMap = uni.getStorageSync('userCartMap') || {};
					//如果减到0，判断一下是否还有商品
					if (value <= 0) {
						let getGoodsIdList = self.getGoodsIdList();
						if (!getGoodsIdList || getGoodsIdList.length <= 0) {
							uni.showToast({
								title: "商品已清空！",
								icon: 'none',
							});
							
							setTimeout(()=>{
								// 关闭当前页面 跳转客户列表
								self.$navigateTo("/pages/clienter/index");
							},600)
							
							return;
						}
					}
					self.$forceUpdate();
					self.calculateCartTotalPrice();
				}, function(error) {
					console.log('addGoodToCart--->fail...', error);
					uni.showToast({
						title: "保存失败，请重新添加",
						icon: 'none',
					});
				});
			},
			
			delCartGoods(){
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定删除购物车全部商品吗？',
					success: function(res) {
						if (res.confirm) {
							
							uni.showLoading({
								title: '正在删除...'
							});
							
							uni.setStorageSync('userCartMap', {});
							
							uni.hideLoading();
							
							uni.showToast({
								title: "删除成功！",
								icon: 'none',
							});
							
							setTimeout(()=>{
								// 关闭当前页面 跳转客户列表
								self.$navigateTo("/pages/clienter/index");
							},600)
						}
					}
				});
			},
			
			//添加商品备注页面
			addGoodRemark(item) {
				let goodsId = item.goodsId;
				let comment = '';
				if (this.userCartMap && this.userCartMap[goodsId]) {
					comment = this.userCartMap[goodsId].comment || '';
				}
				this.$jump('/pages/xjshop/order/cartgoodremark?goodsId=' + item.goodsId + "&comment=" + comment);
			},
			
			goBack(){
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			},
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
			font-size: 28upx;
			color: #000000;
			margin-left: 20upx;
			margin-right: 20upx;
			border-bottom: solid 1px #E0E0E0;
		}

		/* 购物车列表项 */
		.cart-item {
			.cart-item-top{
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
							
							.sale-num-tip{
								margin-top: 4upx;
								font-size: 16upx;
								color:$uni-xj-good-sale-finish
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
</style>
