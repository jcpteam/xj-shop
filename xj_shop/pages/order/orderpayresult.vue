<template>
	<view style="display: grid;">
		<view class="title">
			<icon :type="iconSuccess" size="26" />
			<text style="margin-left: 10px;">您的订单支付完成</text>
		</view>

		<button v-if="timeCount > 0" class="add-btn1">{{timeCount}}秒后查看订单</button>
		<button v-else class="add-btn" @click="toBillmanager">返回查看订单</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				sub_mch_id: '', //特约商户号
				out_trade_no: '', //商户订单号
				orderInfo: {},

				iconSuccess: 'success_no_circle',
				
				timeCount:10,
				timer: null
			}
		},

		onLoad(option) {
			if (option) {
				if (option.sub_mch_id) {
					this.sub_mch_id = option.sub_mch_id; //特约商户号
				}
				if (option.out_trade_no) {
					this.out_trade_no = option.out_trade_no; //商户订单号
				}
				
				this.startCoutDownTimer();
			}
		},
		
		onUnload() {
			if (this.timer) {
				clearTimeout(this.timer);
				this.timer = null;
			}
		},

		onReady() {
			let mchData = {
				action: 'onIframeReady',
				displayStyle: 'SHOW_CUSTOM_PAGE'
			};
			let postData = JSON.stringify(mchData);
			parent.postMessage(postData, 'https://payapp.weixin.qq.com');
		},

		mounted() {
			//引入微信上交小票在线js，否则会受到处罚
			var element = document.createElement("script");
			element.src = "https://http://wx.gtimg.com/pay_h5/goldplan/js/jgoldplan-1.0.0.js";
			document.body.appendChild(element);
		},

		methods: {
			toBillmanager() {
				var mchData = {
					action: 'jumpOut',
					jumpOutUrl: 'http://csds.xiangjiamuye.com/xjshop/#/pages/order/billmanager' //跳转的页面
				}
				var pData = JSON.stringify(mchData);
				parent.postMessage(pData, 'https://payapp.weixin.qq.com')
			},
			
			//执行倒计时
			startCoutDownTimer() {
				let self = this;
				this.timer = setInterval(() => {
					self.timeCount--;
				}, 1000)
			},
		}
	}
</script>

<style lang="scss">
	.title {
		display: flex;
		align-items: center;
		justify-content: center;
		text-align: center;
		margin: 100rpx;
	}

	.add-btn {
		width: auto;
		height: 80upx;
		margin: 30upx 20upx;
		font-size: 32upx;
		color: #fff;
		background-color: $theme-color;
		border-radius: 45upx;
	}
	
	.add-btn1 {
		width: auto;
		height: 80upx;
		margin: 30upx 20upx;
		font-size: 32upx;
		color: #fff;
		background-color: $uni-text-color-disable;
		border-radius: 45upx;
	}
</style>
