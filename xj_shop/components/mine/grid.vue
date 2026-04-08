<template>
	<view>
		<view class="nav">
			<view class="nav-item" v-for="(item,index) in nav" :key='index' @click="goDetail(index,item.url)">
				<image :src="item.image" class="nav-image"></image>
				<text class="nav-text">{{item.name}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "grid",
		props: {
			nav: {
				type: Array,
				default: function() {
					return []
				}
			},

			hasLogin: {
				type: [Boolean, String],
				default: false
			}
		},
		data() {
			return {}
		},
		methods: {
			goDetail(index,url) {
				if (this.hasLogin) {
					if (url === '/pages/tabs/order/order') {
						uni.setStorageSync('selectOrderTabIndex',index + 1);
						uni.switchTab({
							url: url
						})
					} else {
						uni.navigateTo({
							url: url
						})
					}
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


			}
		}
	}
</script>

<style lang="scss">
	.nav {
		margin: 15upx;
		border-radius: 15upx;
		width: 720upx;
		display: flex;
		flex-wrap: wrap;
		background-color: #FFFFFF;

		.nav-item {
			width: 25%;
			height: 200upx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			box-sizing: border-box;

			.nav-image {
				width: 50upx;
				height: 50upx;
			}

			.nav-text {
				margin-top: 12upx;
				height: 40upx;
				line-height: 40upx;
				font-size: 28upx;
				text-align: center
			}
		}
	}
</style>
