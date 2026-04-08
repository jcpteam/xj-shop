<template>
	<view class="multi-container">
		<view class="multi-head" v-if="banner !== undefined">
			<view class="multi-head-left">
				<text class="multi-topic">{{banner.title}}</text>
				<text class="multi-time">{{getQuotationTime()}}</text>
			</view>
		</view>
		<view class="multi-list" v-if="num === 2">
			<!-- 这里需要用户自行定义商品点击事件 -->
			<view v-for='(item,index) in list' :key='index' class="multi-product2">
				<image class="multi-image2"
					:src="parseGoodImgUrl(item)"
					@error="imageError(item)" @click="detail(item)"></image>
				<view class="multi-description">
					<text class="multi-title">{{item.propertyName}}</text>
					<text class="multi-subtitle">{{item.goodsIntroduce?item.goodsIntroduce:"暂无描述"}}</text>

					<view class="price">
						<text class="multi-subprice">{{getGoodSalePriceInfo(item).salePrice}}元/{{getGoodSalePriceInfo(item).saleUnitName}}</text>
						<text class="multi-mainprice">{{item.settingPrice}}元/{{item.mainUnitName}}</text>
					</view>
					<!-- <view v-if="item.unitPriceList && item.unitPriceList.length > 1" class="price">
						<text
							class="multi-subprice">{{item.unitPriceList[0].price}}元/{{item.unitPriceList[0].unitName}}</text>
						<text
							class="multi-mainprice">{{item.unitPriceList[1].price}}元/{{item.unitPriceList[1].unitName}}</text>
					</view>
					<view v-else="item.unitPriceList && item.unitPriceList === 1" class="price">
						<text class="multi-subprice"></text>
						<text
							class="multi-mainprice">{{item.unitPriceList[0].price}}元/{{item.unitPriceList[0].unitName}}</text>
					</view> -->

					<view v-if="item.saleNum && item.saleNum > 0" class="multi-standardweight-content">
						<!-- <text class="multi-standardweight" v-if="item.unitPriceList && item.unitPriceList.length > 1">
							1{{item.unitPriceList[0].unitName}}/{{item.unitPriceList[1].unitName}}
						</text> -->
						<text class="multi-standardweight" v-if="item.saleNum > 100">
							库存充足
						</text>
						<text class="multi-standardweight" v-if="item.saleNum > 50 && item.saleNum<=100">
							库存较充足
						</text>
						<text class="multi-standardweight" v-if="item.saleNum >= 11 && item.saleNum<=50">
							库存紧张
						</text>
						<text class="multi-standardweight" v-if="item.saleNum > 0 && item.saleNum<=10">
							库存紧缺
						</text>
						<!-- <text class="multi-standardweight" v-else>
							库存{{item.saleNum}}
						</text> -->

						<view v-if="item.settingPrice && item.settingPrice > 0">
							<uni-number-box v-if="checkIfGoodInCart(item) && item.cartQuantity > 0"
								class="multi-addgoodnum" :min="0" :max="item.saleNum" :value="item.cartQuantity"
								:itemObj="item" @change="numberChange">
							</uni-number-box>
							<image v-else class="multi-addgood" :src="addImg" @click="addGood(item)"></image>
						</view>
					</view>
					<view class="sale-finish" v-else>
						今日已售罄
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import uniNumberBox from '@/components/xjshop/cart/uni-number-box.vue'

	export default {
		name: 'list-mall',
		components: {
			uniNumberBox
		},
		data() {
			return {
				//默认的广告图片，加载失败则使用此图片
				defaultGoodsImg: '/static/xj_logo.jpg',
				//新增商品图标
				addImg: '/static/add.png',

				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},
			}
		},
		props: {
			banner: {},
			num: '',
			list: {
				type: Array,
				deault: function() {
					return []
				}
			}
		},

		methods: {
			//获取商品销售价格显示
			getGoodSalePriceInfo(item) {
				return this.getGoodSalePrice(item);
			},

			//获取报价单运营时间
			getQuotationTime() {
					return '';
				// if (this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.operateStartTime &&
				// 	this.userInfo.quotationInfo.operateStartTime && this.userInfo.quotationInfo.operateEndTime) {
				// 	if (this.userInfo.quotationInfo.operateStartTime.length > 5) {
				// 		return ('供应时间: ' + this.userInfo.quotationInfo.operateStartTime.slice(0, 5) + '~' + this.userInfo
				// 			.quotationInfo
				// 			.operateEndTime.slice(0, 5));
				// 	} else {
				// 		return ('供应时间: ' + this.userInfo.quotationInfo.operateStartTime + '~' + this.userInfo.quotationInfo
				// 			.operateEndTime);
				// 	}
				// } else {
				// 	return '';
				// }
			},

			//点击商品图片查看详情
			detail(item) {
				// let index = 0;
				// let imgList = [!item.goodsImg ? this.defaultGoodsImg : item.goodsImg];
				// uni.previewImage({
				// 	current: index, //预览图片的下标
				// 	urls: imgList //预览图片的地址，必须要数组形式，如果不是数组形式就转换成数组形式就可以
				// })
				
				this.$jump('/pages/xjshop/order/gooddetail?goodsId=' + item.goodsId);
			},

			//添加商品到购物车
			addGood(item) {
				let self = this;
				let hasLogin = uni.getStorageSync('hasLogin');
				if (hasLogin) {
					if (!this.$compareCurTime(this.userInfo.quotationInfo.operateStartTime) && this.$compareCurTime(this.userInfo.quotationInfo.operateEndTime)) {
						//在下单时间内
					}else{
						uni.showToast({
							title: "请在商品供应时间内下单",
							icon: 'none',
						});
						return;
					}

					//已经登录，添加到购物车
					item.cartQuantity = 1;
					this.addGoodToCart(item, function(resp) {
						if (resp.data.code === 0 && resp.data.data) {
							item.itemId = resp.data.itemId;
						}
						self.$forceUpdate();
						self.$emit("cartChange");
						console.log('addGoodToCart--->success...', resp);
					}, function(error) {
						console.log('addGoodToCart--->fail...', error);
						uni.showToast({
							title: "保存失败，请重新添加",
							icon: 'none',
						});
					});
				} else {
					let _this = this;
					uni.showModal({
						title: '提示',
						content: '您尚未登录，立即前往登录页面？',
						success: function(res) {
							if (res.confirm) {
								_this.$jump('/pages/login/login');
							}
						}
					});
				}
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

			//判断商品是否在购物车有数据
			checkIfGoodInCart(item) {
				let userCartMap = uni.getStorageSync('userCartMap') || {};
				if (userCartMap[item.goodsId]) {
					item.cartQuantity = userCartMap[item.goodsId].quantity;
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
				this.addGoodToCart(item, function(resp) {
					console.log('addGoodToCart--->success...', resp);
					self.$forceUpdate();
					self.$emit("cartChange");
				}, function(error) {
					console.log('addGoodToCart--->fail...', error);
					uni.showToast({
						title: "保存失败，请重新添加",
						icon: 'none',
					});
				});
			},
		}
	}
</script>

<style lang="scss">
	.multi-container {
		display: flex;
		flex-direction: column;
		width: 750upx;

		.multi-head {
			display: flex;
			justify-content: space-between;
			align-content: center;
			align-items: center;
			margin: 34.3upx 30upx 33.3upx 30upx;

			.multi-head-left {
				display: flex;
				align-content: flex-end;

				.multi-topic {
					color: $theme-color;
					font-size: 35.3upx;
					line-height: 40.6upx;
					margin-right: 30upx;
					align-self: center;
				}

				.multi-time {
					align-self: center;
					color: $uni-xj-good-price;
					font-size: 20upx;
				}
			}



			.multi-head-right {
				display: flex;
				align-content: center;
				align-items: flex-end;

				.multi-link {
					color: #1A1A1A;
					opacity: 0.5;
					font-size: 27upx;
					margin-right: 14upx;
					align-self: center;
				}

				.multi-arrow {
					width: 16upx;
					height: 28upx;
					align-self: center;
				}
			}

		}
	}


	.multi-list {
		display: flex;
		border-color: #0000ff;
		flex-wrap: wrap;
		margin-left: 25upx;
		margin-right: 25upx;

		.multi-product2 {
			display: flex;
			flex-direction: column;
			width: 50%;
			margin-bottom: 39upx;

			.multi-image2 {
				border-top-left-radius: 8upx;
				border-top-right-radius: 8upx;
				width: 333.3upx;
				height: 333.3upx;
				margin-bottom: 8upx;
			}
		}

		.multi-description {
			display: flex;
			flex-direction: column;
			margin-top: 30upx;

			.price {
				display: flex;
				flex-direction: column;
			}

			.multi-title {
				color: #1A1A1A;
				font-size: 30upx;
				line-height: 34.9upx;
				margin-left: 8upx;

				//设置文本单行显示，超出显示省略号
				display: -webkit-box;
				overflow: hidden;
				text-overflow: ellipsis;
				word-wrap: break-word;
				white-space: normal !important;
				-webkit-line-clamp: 2; //设置行数
				-webkit-box-orient: vertical;
			}

			.multi-subtitle {
				color: #1A1A1A;
				opacity: 0.5;
				font-size: 24upx;
				// line-height: 27.1upx;
				margin-left: 8upx;
				margin-top: 8upx;

				//设置文本单行显示，超出显示省略号
				display: -webkit-box;
				overflow: hidden;
				text-overflow: ellipsis;
				word-wrap: break-word;
				white-space: normal !important;
				-webkit-line-clamp: 1; //设置行数
				-webkit-box-orient: vertical;
			}

			.multi-subprice {
				color: #1A1A1A;
				opacity: 0.5;
				font-size: 18upx;
				line-height: 27.1upx;
				margin-left: 8upx;
				margin-top: 20upx;
			}

			.multi-mainprice {
				color: $uni-xj-good-price;
				font-size: 30upx;
				line-height: 27.1upx;
				margin-left: 8upx;
				margin-top: 8upx;
			}

			.multi-standardweight-content {
				margin-top: 10upx;
			}

			.multi-standardweight {
				padding: 2upx 10upx;
				text-align: center;
				color: $uni-xj-good-price;
				font-size: 24upx;
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
		}
	}
</style>
