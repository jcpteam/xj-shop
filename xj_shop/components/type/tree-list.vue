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
				<text class="multi-time">{{getQuotationTime()}}</text>
				
				<view class="cart-item" v-for="(item,index) in subTypeList" :key="index">
					<view class="image-wrapper">
						<image class="multi-image2" mode="aspectFill"
							:src="parseGoodImgUrl(item)"
							@error="imageError(item)" @click="detail(item)"></image>
					</view>

					<view class="item-right">
						<text class="title">{{item.propertyName}}</text>
						<text class="attr">{{item.goodsIntroduce?item.goodsIntroduce:"暂无描述"}}</text>

						<view class="item-last">
							<view class="left-price">
								<view v-if="checkLogin()" class="price">
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
									<!-- <text class="multi-standardweight"
										v-if="item.unitPriceList && item.unitPriceList.length > 1">
										1{{item.unitPriceList[0].unitName}}/{{item.unitPriceList[1].unitName}}
									</text> -->
									<text class="multi-standardweight" v-if="item.saleNum > 300">
										库存充足
									</text>
									<text class="multi-standardweight" v-if="item.saleNum > 100 && item.saleNum<=300">
										库存正常
									</text>
									<text class="multi-standardweight" v-if="item.saleNum > 0 && item.saleNum<=100">
										库存余{{item.saleNum}}
									</text>
									<!-- <text class="multi-standardweight" v-else>
										库存{{item.saleNum}}
									</text> -->
								</view>
								<view class="sale-finish" v-else>
									今日已售罄
								</view>
							</view>
							<view v-if="item.saleNum && item.saleNum > 0 && item.settingPrice && item.settingPrice > 0" class="right-add">
								<!-- <image class="multi-addgood" :src="addImg" @click="addGood(item)"></image> -->
								<uni-number-box v-if="checkIfGoodInCart(item) && item.cartQuantity > 0"
									class="multi-addgoodnum" :min="0" :max="item.saleNum" :value="item.cartQuantity" :itemObj="item"
									@change="numberChange">
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
	import uniNumberBox from '@/components/cart/uni-number-box.vue'
	
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
			ramdom:Number
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
				
				//用户信息
				userInfo: uni.getStorageSync('userInfo') || {},
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
						// this.typeActive = 0;
						// this.getGoodsListForType(newValue[0]);
						this.typeActive = 2;
						this.getGoodsListForType(newValue[2]);
					}
				}
			},
			
			ramdom(newValue,oldValue){
				this.typeClickMain(this.curTypeItem,this.typeActive);
			}
		},

		methods: {
			checkLogin(){
				let hasLogin = uni.getStorageSync('hasLogin');
				if (hasLogin === true || hasLogin === 'true') {
					return true;
				} else {
					return false
				}
			},
			
			//获取商品销售价格显示
			getGoodSalePriceInfo(item) {
				return this.getGoodSalePrice(item);
			},
			
			//获取报价单运营时间
			getQuotationTime() {
				if (this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.operateStartTime &&
					this.userInfo.quotationInfo.operateStartTime && this.userInfo.quotationInfo.operateEndTime) {
					if (this.userInfo.quotationInfo.operateStartTime.length > 5) {
						return ('供应时间: ' + this.userInfo.quotationInfo.operateStartTime.slice(0,5) + '~' + this.userInfo.quotationInfo
							.operateEndTime.slice(0,5));
					} else {
						return ('供应时间: ' + this.userInfo.quotationInfo.operateStartTime + '~' + this.userInfo.quotationInfo
							.operateEndTime);
					}
				} else {
					return '';
				}
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
				// let index = 0;
				// let imgList = [!item.goodsImg ? this.defaultGoodsImg : item.goodsImg];
				// uni.previewImage({
				// 	current: index, //预览图片的下标
				// 	urls: imgList //预览图片的地址，必须要数组形式，如果不是数组形式就转换成数组形式就可以
				// })
				
				this.$jump('/pages/order/gooddetail?goodsId=' + item.goodsId);
			},
			//添加商品到购物车
			addGood(item) {
				let self = this;
				let hasLogin = uni.getStorageSync('hasLogin');
				if (hasLogin) {
					if(this.userInfo.quotationInfo){
						if (!this.$compareCurTime(this.userInfo.quotationInfo.operateStartTime) && this.$compareCurTime(this.userInfo.quotationInfo.operateEndTime)) {
							//在下单时间内
						}else{
							uni.showToast({
								title: "请在商品供应时间内下单",
								icon: 'none',
							});
							return;
						}
					}else{
						uni.showToast({
							title: "没有配置报价单，请联系管理员",
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
				if(this.userInfo && this.userInfo.quotationInfo && this.userInfo.quotationInfo.quotationId){
					url += '&quotationId=' + this.userInfo.quotationInfo.quotationId;
				};	
				if(this.userInfo && this.userInfo.memberId){
					url += '&merchantId=' + this.userInfo.memberId;
				};
				//默认10条数据
				this.sendRequestWithDeptId({
					'url': url,
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
						icon:'none',
					});
				});
			},
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
						
						.multi-standardweight-content{
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
