<template>
	<view>
		<view class="user-card">
			<view class="usercard-top">
				<view class="avatar">
					<!-- 如果刚登录还没修改过头像将使用默认头像，否则就是用修改过的头像 -->
					<image :src="'/static/xj_logo.jpg'"></image>
					<view v-if="hasLogin" class="user-info">
						<view class="user-name">
							<text>{{userinfo.userName}}</text>
						</view>
						<view class="dept-name">
							<text>{{userinfo.deptName}}</text>
						</view>
					</view>
					<view v-else class="user-info">
						<view class="user-name" @click="gologin">
							<text>登录</text>
						</view>
						<view class="dept-name">
							<text>请先登录</text>
						</view>
					</view>
				</view>

				<view class="logout" v-if="hasLogin" @click="logout">退出></view>
			</view>

			<block v-if="expand.length>0">
				<view class="usercard-bottom">
					<block v-for="(item,index) in expand" :key='index'>
						<block v-if="index>0">
							<view class="line"></view>
						</block>
						<view class="usercard-bottom-item" @click="gocardbag(item,index)">
							<view class="item-top">
								<text>{{item.number}}</text>
							</view>
							<view class="item-bottom">
								<text>{{item.name}}</text>
							</view>
						</view>
					</block>
				</view>
			</block>
		</view>

	</view>
</template>

<script>
	export default {
		name: "user-card",
		props: {
			userinfo: {},
			expand: {
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
		computed: {
			has_Login() {
				return this.hasLogin
			},
		},
		methods: {
			// 跳到登录页面事件
			gologin() {
				this.$jump('/pages/login/login')
			},

			// 进入卡包
			gocardbag(item, index) {
				// if (this.hasLogin) {
				// 	this.$jump('/pages/tabs/mine/minecardbag?type=' + item.type + '&num=' + item.number)
				// } else {
				// 	let _this = this;
				// 	uni.showModal({
				// 		title: '提示',
				// 		content: '您尚未登录，立即前往登录页面？',
				// 		success: function(res) {
				// 			if (res.confirm) {
				// 				_this.$jump('/pages/login/login');
				// 			}
				// 		}
				// 	});
				// }
			},

			//登出
			logout() {
				let self = this;
				uni.showModal({
					title: '提示',
					content: '确定退出登录吗？',
					success: function(res) {
						if (res.confirm) {
							self.clearCacheLogout();
							self.$jump('/pages/login/login');
						}
					}
				});
			}
		},

		watch: {

		}
	}
</script>

<style lang="scss">
	.user-card {
		margin: 5% 5%;
		width: 90%;
		display: flex;
		flex-direction: column;
		height: 320upx;
		border-radius: 15upx;
		background: $theme-color;
	}

	.usercard-top {
		width: 100%;

		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.logout {
		width: 120upx;
		margin-right: 20upx;
		color: #ffffff;
		font-size: 28upx;
	}

	.avatar {
		width: 100%;
		display: flex;
		justify-content: space-around;
		align-items: center;
		align-content: center;
		margin-top: 50upx;
	}

	.avatar-noexpand {
		width: 100%;
		display: flex;
		flex-direction: column;
		justify-content: space-around;
		align-items: center;
		text-align: right;
	}

	.avatar image {
		border-radius: 70upx;
		width: 100upx;
		height: 100upx;
		margin: 0upx 32upx 0upx 50upx;
	}

	.avatar-noexpand image {
		border-radius: 70upx;
		width: 100upx;
		height: 100upx;
	}

	.user-info {
		width: 70%;
		display: flex;
		flex-direction: column;

		.user-name {
			font-size: 36upx;
			color: #ffffff;
			font-weight: bold;
		}

		.dept-name {
			margin-top: 10upx;
			font-size: 30upx;
			color: #ffffff;
		}
	}

	.name {
		flex: 1;
	}

	.name .dispflex {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.name .dispflex text {
		font-size: 30upx;
		color: #5B441A;
	}

	.go_login {
		margin-right: 40upx;
		height: 56upx;
		line-height: 56upx;
		width: 160upx;
		border-radius: 8upx;
		background: #F8F8F8;
		text-align: center;
	}

	.usercard-bottom {
		width: 100%;
		height: 160upx;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.usercard-bottom-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 25%;
	}

	.item-top text {
		font-size: 34upx;
		text-align: center;
		color: #ffffff
	}

	.item-bottom text {
		font-size: 22upx;
		text-align: center;
		color: #ffffff
	}

	.line {
		height: 50upx;
		width: 3upx;
		margin: 0 20upx;
		background-color: rgba(0, 0, 0, 0.2);
	}
</style>
