<template>
	<view class="content">
		<view class="title">账单付款码</view>
		<view class="qrcode-box">
			<uqrcode ref="uqrcode" />
		</view>
		<view v-if="showMoney" class="time-tip">付款码有效时长：60分钟</view>

		<view v-if="showMoney" class="paystatus_contaner">
			<text class="status_tip">结算客户：</text>
			<text class="status_value">{{merchantName}}</text>
		</view>
		<view v-if="showMoney" class="paystatus_contaner">
			<text class="status_tip">支付金额：</text>
			<text class="color-price">{{payMoney}}</text>
		</view>

		<view v-if="showMoney" class="btn-row">
			<button type="primary" class="close-btn" @tap="close">完成</button>
		</view>
	</view>
</template>

<script>
	import uqrcode from '@/components/common/uqrcode/uqrcode.vue'

	export default {
		components: {
			uqrcode,
		},

		data() {
			return {
				showMoney: false, //是否显示金额

				qrCode: '',
				receiptId: '',
				merchantName: '',
				payMoney: '',
				payOrderNo: '',

				timer: null
			}
		},

		onLoad(options) {
			this.qrCode = options.qrCode;
			this.receiptId = options.receiptId;
			this.payOrderNo = options.payOrderNo;
			this.merchantName = options.merchantName;
			this.payMoney = options.payMoney;
			if (this.qrCode) {
				//有付款码信息
			} else {
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			}
		},

		onUnload() {
			if (this.timer) {
				clearTimeout(this.timer);
				this.timer = null;
			}
		},

		onReady() {
			this.showQrCode();
		},

		methods: {
			showQrCode() {
				uni.showLoading({
					mask: true,
					title: '付款码生成中,请稍后...'
				});

				let self = this;
				this.$refs
					.uqrcode
					.make({
						size: 354,
						text: this.qrCode,
						enableDelay: true // 启用延迟绘制
					})
					.then(res => {
						// 返回的res与uni.canvasToTempFilePath返回一致
						console.log('二维码信息--->', res)

						self.showMoney = true;

						if (self.payOrderNo) {
							self.sendQueryPayResult();
						}
					})
					.finally(() => {
						uni.hideLoading()
					})
			},

			close() {
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			},

			//执行网络请求
			sendQueryPayResult() {
				let self = this;
				this.timer = setInterval(() => {
					let url = '/shop/xjpay/query/' + self.payOrderNo;
					self.sendRequest({
						'url': url,
						'method': 'GET',
						'data': null
					}, function(resp) {
						console.log('test resp--->',resp);
					})
				}, 10000)
			},
		}
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
		// justify-content: center;
		// align-items: center;
		// margin-top: 40upx;
	}

	.title {
		flex-direction: column;
		align-items: center;
		text-align: center;
		margin-top: 20upx;
		font-size: 36upx;
		color: #666666;
	}

	.time-tip {
		flex-direction: column;
		align-items: center;
		text-align: center;
		margin-top: 10upx;
		margin-bottom: 30upx;
		font-size: 26upx;
		color: #666666;
	}

	.qrcode-box {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		margin-top: 30upx;
		margin-bottom: 30upx;
	}

	.text {
		margin-top: 12rpx;
		font-size: 34rpx;
	}

	.paystatus_contaner {
		margin-top: 10upx;
		padding: 10upx 20upx 10upx 20upx;
		display: flex;
		align-items: center;
		justify-content: flex-start;
		font-size: 30upx;

		.status_tip {
			color: #666;
		}

		.status_value {
			color: #000000;
		}
	}

	.color-price {
		color: $uni-xj-good-price;
	}

	.btn-row {
		padding: 50upx 30upx 30upx 30upx;
	}

	.close-btn {
		background-color: #3fb86b;
		border-radius: 45upx;
	}
</style>
