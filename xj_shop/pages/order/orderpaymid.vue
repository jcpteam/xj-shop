<template>
	<view class="content">
		<view class="title">订单支付</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				requestParams: {},
			}
		},

		onLoad() {
			this.requestParams = this.getUrlRequestParams();
			if (this.requestParams && this.requestParams.code && this.requestParams.state) {
				uni.showLoading({
					title: '正在生成订单支付信息...'
				});
				this.getWxpayData();
			} else {
				this.exitPage('支付失败：未获取到订单支付信息', 1000);
			}
		},

		onUnload() {},

		onShow() {},

		methods: {
			//退出改页面
			exitPage(tips, delay) {
				uni.showToast({
					icon: 'none',
					title: tips
				});
				setTimeout(() => {
					// 关闭当前页面 返回详细信息页面
					history.back();
				}, delay)
			},

			//获取url地址中的参数
			getUrlRequestParams() {
				let url = location.href;
				let requestParams = {};
				if (url.indexOf("?") !== -1) {
					let str = url.substr(url.indexOf("?") + 1); //截取?后面的内容作为字符串
					let strs = str.split("&"); //将字符串内容以&分隔为一个数组
					for (let i = 0; i < strs.length; i++) {
						requestParams[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
						// 将数组元素中'='左边的内容作为对象的属性名，'='右边的内容作为对象对应属性的属性值
					}
				}
				console.log('getUrlRequestParams--->', requestParams);
				return requestParams;
			},

			//获取支付的微信openid
			getWxpayData() {
				let url = '/toPlayerInfo?code=' + this.requestParams.code + "&receiptId=" + this.requestParams.state;
				let self = this;
				self.sendRequest({
					'url': url,
					'method': 'GET',
					'data': null
				}, function(resp) {
					console.log('getWxpayData---->', resp);
					uni.hideLoading();
					if (resp && resp.data && resp.data.payData) {
						self.processJsPay(self, resp.data.payData);
					} else {
						self.exitPage('支付失败：获取支付信息失败', 1000);
					}
				}, function(error) {
					uni.hideLoading();
					self.exitPage('支付失败：获取支付信息失败1', 1000);
				})
			},

			processJsPay(vm, payData) {
				if (typeof WeixinJSBridge == "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', vm.onBridgeReady(vm, payData), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', vm.onBridgeReady(vm, payData));
						document.attachEvent('onWeixinJSBridgeReady', vm.onBridgeReady(vm, payData));
					}
				} else {
					vm.onBridgeReady(vm, payData);
				}
			},

			onBridgeReady(vm, payData) {
				let payObj = JSON.parse(payData);
				console.log('onBridgeReady--->',payObj);
				
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": payObj.appId, //公众号ID，由商户传入     
						"timeStamp": payObj.timeStamp, //时间戳，自1970年以来的秒数     
						"nonceStr": payObj.nonceStr, //随机串     
						"package": payObj.package,
						"signType": payObj.signType, //微信签名方式：     
						"paySign": payObj.paySign //微信签名 
					},
			
					function(res) {
						if (res.err_msg == "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							vm.exitPage('支付完成', 1000);
						}else{
							vm.exitPage('支付失败...', 1000);
						}
					});
			},
		}
	}
</script>

<style lang="scss">
	.content {
		display: flex;
		flex-direction: column;
	}

	.title {
		flex-direction: column;
		align-items: center;
		text-align: center;
		margin-top: 20upx;
		font-size: 36upx;
		color: #666666;
	}
</style>
