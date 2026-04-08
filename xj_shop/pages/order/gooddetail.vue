<template>
	<view>
		<!-- 轮播图 -->
		<swiper v-if="goodsInfo && goodsInfo.imgs" :indicator-dots="true" :autoplay="true" :interval="3000"
			:duration="1000">
			<swiper-item v-for='(item,index) in goodsInfo.imgs' :key='index'>
				<image class="swiper-image" :src="item"></image>
			</swiper-item>
		</swiper>

		<view class="multi-description" v-if="goodsInfo">
			<text class="multi-title">{{goodsInfo.propertyName}}</text>
			<text class="multi-subtitle">{{goodsInfo.goodsIntroduce?goodsInfo.goodsIntroduce:"暂无描述"}}</text>

			<view class="price">
				<text
					class="multi-subprice">{{getGoodSalePriceInfo(goodsInfo).salePrice}}元/{{getGoodSalePriceInfo(goodsInfo).saleUnitName}}</text>
				<text class="multi-mainprice">{{goodsInfo.settingPrice}}元/{{goodsInfo.mainUnitName}}</text>
			</view>
		</view>

		<view class="rich-description" v-if="goodsInfo">
			<rich-text :nodes="goodsInfo.description"></rich-text>
		</view>

	</view>
</template>

<script>
	export default {
		components: {},

		data() {
			return {
				//默认的广告图片，加载失败则使用此图片
				defaultAdImg: '/static/index/banner.png',

				//商品id
				goodsId: '',

				//商品信息
				goodsInfo: {
					// imgs: ['/static/index/banner.png'],
				},
			}
		},

		onLoad(options) {
			this.goodsId = options.goodsId;
			if (!this.goodsId || this.goodsId == undefined) {
				this.goodsId = '';
			}

			this.queryGoodsList();
		},

		methods: {
			//获取商品销售价格显示
			getGoodSalePriceInfo(item) {
				return this.getGoodSalePrice(item);
			},

			//分页获取商品列表
			queryGoodsList() {
				if (!this.goodsId) {
					return;
				}

				uni.showLoading({
					title: '正在加载...'
				});

				let self = this;
				var goodsIdList = [this.goodsId];
				let url = '/shop/quotationGoods/appList?goodsIdList=' + goodsIdList;
				this.sendRequestWithDeptId({
					'url': url,
					'method': 'POST',
					'data': null
				}, function(resp) {
					if (resp.data.code === 0 && resp.data.rows && resp.data.rows.length > 0) {
						self.goodsInfo = resp.data.rows[0];
						self.goodsInfo.imgs = self.parseGoodImgUrlList(self.goodsInfo);
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			/**
			 * 转换富文本的图片最大为100%
			 * 转换行内样式的双引号问题
			 */
			formatRichText(html) { //控制小程序中图片大小
				let newContent = html.replace(/<img[^>]*>/gi, function(match, capture) {
					match = match.replace(/style="[^"]+"/gi, '').replace(/style='[^']+'/gi, '');
					match = match.replace(/width="[^"]+"/gi, '').replace(/width='[^']+'/gi, '');
					match = match.replace(/height="[^"]+"/gi, '').replace(/height='[^']+'/gi, '');
					return match;
				});

				newContent = newContent.replace(/style="[^"]+"/gi, function(match, capture) {
					match = match.replace(/width:[^;]+;/gi, 'max-width:100%;').replace(/width:[^;]+;/gi,
						'max-width:100%;');
					return match;
				});
				newContent = newContent.replace(/<br[^>]*\/>/gi, '');
				newContent = newContent.replace(/\<img/gi,
					'<img style="max-width:100%;height:auto;"');

				var stylePattern = /style="[^=>]*"[\s+\w+=|>]/g;
				var innerQuotePattern = /(?<!=)"(?!>)/g;
				return newContent.replace(stylePattern, function(matches) {
					return matches.replace(innerQuotePattern, '\'');
				});
				// return newContent;
			}

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

	.multi-description {
		display: flex;
		flex-direction: column;
		margin-top: 30upx;
		margin-left: 20upx;

		.multi-title {
			color: #1A1A1A;
			font-size: 40upx;
			// line-height: 34.9upx;
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
			font-size: 34upx;
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

		.price {
			display: flex;
			flex-direction: column;
		}

		.multi-subprice {
			color: #1A1A1A;
			opacity: 0.5;
			font-size: 25upx;
			line-height: 27.1upx;
			margin-left: 8upx;
			margin-top: 20upx;
		}

		.multi-mainprice {
			color: $uni-xj-good-price;
			font-size: 35upx;
			line-height: 27.1upx;
			margin-left: 8upx;
			margin-top: 8upx;
		}

	}

	.rich-description {
		margin-top: 30upx;
		margin-left: 20upx;
		margin-right: 20upx;
	}
</style>