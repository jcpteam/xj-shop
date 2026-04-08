<template>
	<view class="container">
		<view class="page-body">
			<scroll-view class="nav-left" scroll-y :style="'height:'+height+'px'">
				<view class="nav-left-item" @click="typeClickMain(item,index)" :key="index"
					:class="index==typeActive?'active':''" v-for="(item,index) in typeList">
					{{item.anotherName?item.anotherName:item.name}}
				</view>
			</scroll-view>

			<scroll-view class="nav-right" scroll-y :scroll-top="scrollTop" @scroll="scroll"
				@scrolltolower="scrollToBottom" :style="'height:'+height+'px'" scroll-with-animation>
				<view class="cart-item" v-for="(item,index) in subTypeList" :key="index">
					<view class="image-wrapper">
						<image class="multi-image2" mode="aspectFill" :src="parseGoodImgUrl(item)"
							@error="imageError(item)" @click="detail(item)"></image>
					</view>

					<view class="item-right">
						<text class="title">{{item.propertyName}}</text>
						<text class="attr">{{item.goodsIntroduce?item.goodsIntroduce:"暂无描述"}}</text>

						<view class="item-last">
							<view class="left-price">
								<view class="price">
									<text
										class="multi-subprice">{{getGoodSalePriceInfo(item).salePrice}}元/{{getGoodSalePriceInfo(item).saleUnitName}}</text>
									<text class="multi-mainprice">{{item.settingPrice}}元/{{item.mainUnitName}}</text>
								</view>

								<view v-if="item.saleNum && item.saleNum > 0" class="multi-standardweight-content">
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
									今日已售罄
								</view>
							</view>
							<view v-if="item.saleNum && item.saleNum > 0 && item.settingPrice && item.settingPrice > 0" class="right-add">
								<uni-number-box v-if="checkIfGoodInCart(item) && item.cartQuantity > 0"
									class="multi-addgoodnum" :min="0" :max="item.saleNum" :value="item.cartQuantity"
									:itemObj="item" @change="numberChange">
								</uni-number-box>
								<image v-else class="multi-addgood" :src="addImg" @click="addGood(item)"></image>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import uniNumberBox from '@/components/xjshop/cart/uni-number-box.vue'

	export default {
		name: 'tree-list',
		components: {
			uniNumberBox
		},
		props: {
			typeList: {
				type: Array,
				default: function() {
					return []
				}
			},
			ramdom: Number
		},
		data() {
			return {
				//默认的商品图片
				defaultGoodsImg: '/static/xj_logo.jpg',

				height: 0,
				typeActive: 0,
				scrollTop: 0,
				scrollHeight: 0,
				addImg: '/static/add.png',

				subTypeList: [], //商品列表

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},
				//当前分类类型
				curTypeItem: {},
			}
		},
		mounted() {
			this.height = uni.getSystemInfoSync().windowHeight;
		},

		watch: {
			typeList: {
				immediate: true,
				handler: function(newValue, oldValue) {
					console.log('watch typeList change....', newValue);
					if (newValue && newValue.length > 0) {
						this.subTypeList = [];
						this.pagination.total = 0;
						this.pagination.pageNo = 1;
						this.pagination.pageSize = 10;
						this.typeActive = 2;
						this.getGoodsListForType(newValue[2]);
					}
				}
			},

			ramdom(newValue, oldValue) {
				this.typeClickMain(this.curTypeItem, this.typeActive);
			}
		},

		methods: {
			//获取商品销售价格显示
			getGoodSalePriceInfo(item) {
				return this.getGoodSalePrice(item);
			},

			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},

			//分类点击
			typeClickMain(categroy, index) {
				this.typeActive = index;
				this.subTypeList = [];
				this.pagination.total = 0;
				this.pagination.pageNo = 1;
				this.pagination.pageSize = 10;
				this.getGoodsListForType(categroy);
			},
			//查看商品详情
			detail(item) {
				this.$jump('/pages/xjshop/order/gooddetail?goodsId=' + item.goodsId);
			},
			//添加商品到购物车
			addGood(item) {
				let self = this;
				//已经登录，添加到购物车
				item.cartQuantity = 1;
				self.saveGoodToCacheMap(item, 1);
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

			//获取商品列表
			getGoodsListForType(typeItem) {
				this.curTypeItem = typeItem;

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
				let url = '/shop/quotationGoods/appList?pageNum=' + self.pagination.pageNo + '&pageSize=' +
					self.pagination.pageSize + '&classifyId=' + typeItem.classifyId + '&status=1';
				// if (this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.quotationId) {
				// 	url += '&quotationId=' + this.userInfo.quotationInfo.quotationId;
				// };
				// 查询代下单的客户ID和对应区域的数据
				let daiclientid = uni.getStorageSync('userModifyMerchantId');;
				let customerArea = uni.getStorageSync('userModifyDeptId');
				if (daiclientid) {
					url += '&merchantId=' + daiclientid;
				}
				if (customerArea) {
					url += '&loginUserDeptId=' + customerArea;
				}
				//获取当前代下单客户的报价单id
				let quotationId = uni.getStorageSync('userModifyQuotationId');
				if(quotationId){
					url += '&quotationId=' + quotationId;
				}
				
				//默认10条数据
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.pagination.total = resp.data.total;

						//请求成功，更新下次请求的页码
						self.pagination.pageNo = self.pagination.pageNo + 1;

						//默认设置库存为10000
						resp.data.rows.forEach(function(e) {
							e.saleNum = e.settingQuanintiy;
						})

						//数据累加
						self.subTypeList = self.subTypeList.concat(resp.data.rows);
					}
					uni.hideLoading();
				}, function(error) {
					console.log('error = ' + error)
					uni.hideLoading();
				})
			},

			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.getGoodsListForType(this.curTypeItem);
			},

			//判断商品是否在购物车有数据
			checkIfGoodInCart(item) {
				let userModifyOrderGoodsMap = uni.getStorageSync('userModifyOrderGoodsMap') || {};
				if (userModifyOrderGoodsMap && userModifyOrderGoodsMap[item.goodsId]) {
					item.cartQuantity = userModifyOrderGoodsMap[item.goodsId].quantity;
					return true;
				} else {
					return false;
				}
			},

			//输入数量的改变
			numberChange(value, item) {
				let self = this;
				console.log("numberChange--->value:", value);
				item.cartQuantity = value;
				console.log("numberChange--->item.cartQuantity:", item.cartQuantity);
				//已经登录，添加到购物车
				self.saveGoodToCacheMap(item, item.cartQuantity);
			},

			//保存商品信息到缓存
			saveGoodToCacheMap(item, cartQuantity) {
				let self = this;
				var goodInfo = {};
				//获取商品销售价格信息
				let priceInfo = self.getGoodSalePrice(item);

				let tempMap = uni.getStorageSync('userModifyOrderGoodsMap') || {};
				if (tempMap[item.goodsId]) {
					//已经在缓存中，说明是点击数字加减更新商品的数量
					goodInfo = tempMap[item.goodsId];
					goodInfo.quantity = cartQuantity;
					goodInfo.price = priceInfo.finalPrice; //结算价格存入数据库
					goodInfo.amount = cartQuantity * priceInfo.salePrice; //销售总价
					goodInfo.unitId = priceInfo.saleUnitId; //销售单位id
					goodInfo.goodsImg = item.goodsImg;
				} else {
					let daiclientid = uni.getStorageSync('userModifyMerchantId');
					//新添加
					goodInfo = {
						userId: daiclientid, //子账号保存的购物车数据直接关联商户id
						goodsId: item.goodsId,
						unitId: priceInfo.saleUnitId,
						spuNo: item.goodsCode,
						quantity: 1,
						amount: priceInfo.salePrice,
						price: priceInfo.finalPrice, //结算价格存入数据库
						goodsImg: item.goodsImg,
						comment: item.memberComment || '',
					};
				}
				//保存商品信息到临时内存
				tempMap[item.goodsId] = goodInfo;
				console.log('saveGoodToCacheMap--->tempMap--->', tempMap);
				uni.setStorageSync('userModifyOrderGoodsMap', tempMap);

				self.$forceUpdate();
				self.$emit("cartChange");
			}
		},

	}
</script>

<style lang="scss">
	page {
		background-color: white
	}

	.page-body {
		display: flex;
	}

	.nav-left {
		width: 187.5upx;

		.nav-left-item {
			height: 100upx;
			border-right: solid 1px #E0E0E0;
			font-size: 30upx;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.active {
			color: $theme-color;
		}
	}

	.nav-right {
		width: 562.5upx;
		padding-bottom: 70upx;

		.multi-time {
			margin-left: 20upx;
			align-self: center;
			color: $uni-xj-good-price;
			font-size: 24upx;
		}

		.nav-right-item {
			width: 100%;
			// height: 220upx;
			float: left;
			text-align: center;
			font-size: 28upx;

			.addw {
				width: 150upx;
				margin: 0 auto;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}

			.addmargin {
				margin: 10upx 0;
			}

			.addbj {
				width: 150upx;
				height: 150upx;
				margin: 0 auto;
				background: #E0E0E0;

				image {
					width: 150upx;
					height: 150upx;
				}
			}
		}

		/* 购物车列表项 */
		.cart-item {
			display: flex;
			position: relative;
			align-content: center;
			align-items: center;
			padding: 30upx 0upx;
			margin-left: 20upx;
			margin-right: 20upx;
			border-bottom: solid 1px #E0E0E0;

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

						.multi-standardweight-content {
							margin-top: 10upx;
						}

						.multi-standardweight {
							padding: 2upx 10upx;
							text-align: center;
							color: $uni-xj-good-price;
							font-size: 20upx;
							line-height: 27.1upx;
							margin-left: 8upx;
							// margin-top: 8upx;
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
					}
				}
			}

		}
	}
</style>
