<template>
	<view class="content">
		<view class="wrapinput">
			<view class="input-group">
				<view class="input-row border">
					<m-input type="password" displayable v-model="newPwd" placeholder="请输入新密码"></m-input>
				</view>
				<view class="input-row border">
					<m-input type="password" displayable v-model="newPwd1" placeholder="请再次确认新密码"></m-input>
				</view>
			</view>
		</view>
		<view class="btn-row">
			<button type="primary" class="login-btn" @tap="changePwd()">确认修改</button>
		</view>
	</view>
</template>

<script>
	import mInput from '@/components/logininput/m-input.vue'

	export default {
		components: {
			mInput
		},

		data() {
			return {
				newPwd: '',
				newPwd1: '',
			}
		},


		created() {

		},

		methods: {
			changePwd() {
				if (!this.newPwd) {
					uni.showToast({
						icon: 'none',
						title: '请输入新密码！'
					});
					return;
				}

				if (!this.newPwd1) {
					uni.showToast({
						icon: 'none',
						title: '请再次确认新密码！'
					});
					return;
				}

				if (this.newPwd == this.newPwd1) {
					this.sendChangePwdRequest();
				} else {
					uni.showToast({
						icon: 'none',
						title: '两次输入的新密码不一致！'
					});
				}
			},
			
			sendChangePwdRequest(){
				uni.showLoading({
					title: '正在提交...'
				});
				
				var userInfo = uni.getStorageSync('userInfo') || {};
				let url = '/system/user/resetPwdForApp?userId=' + userInfo.userId + '&loginName=' + userInfo.loginName + '&password=' + this.newPwd
				let self = this;
				this.sendRequest({
					'url': url,
					'method': 'POST',
					'contentType': 'x-www-form-urlencoded',
					'data': null
				}, function(resp) {
					console.log('resp = ' + JSON.stringify(resp.data.msg))
					if (resp.data.code === 0 || resp.data.code === '0') {
						var oldPwd = uni.getStorageSync('userLoginPwd');
						if(oldPwd){
							uni.setStorageSync('userLoginPwd', self.newPwd);
						}
						
						uni.showToast({
							icon: 'none',
							title: '修改成功'
						});
						
						setTimeout(()=>{
							// 关闭当前页面 返回详细信息页面
							uni.navigateBack()
						},800);
					} else {
						uni.hideLoading();
						if (resp.data.msg) {
							uni.showToast({
								icon: 'none',
								title: resp.data.msg
							});
						} else {
							uni.showToast({
								icon: 'none',
								title: '修改失败'
							});
						}
					}
				}, function(error) {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '修改失败 : ' + error.data.msg
					});
				})
			}
		},

	}
</script>

<style>
	.content {
		background: #ffffff;
		height: calc(100vh - var(--window-top));
	}

	.top-logo {
		width: 200upx;
		height: 200upx;
		margin: 20upx 30upx;
		display: block;
		margin-left: auto;
		margin-right: auto;
	}

	.wrapinput {
		padding: 20upx 30upx;
	}

	.input-group {
		background: #FFFFFF;
		padding: 10upx;
	}

	.input-row {
		/* display: flex; */
		flex-direction: row;
		align-items: center;
		padding: 38upx 10upx;
	}

	.input-row.border {
		border-bottom: 1upx solid #CCCCCC;
	}

	.btn-row {
		padding: 100upx 30upx 30upx 30upx;
	}

	.login-btn {
		background-color: #3fb86b;
		border-radius: 45upx;
	}

	.action-row {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 10upx 30upx;
	}

	.action-row navigator {
		color: #007aff;
		padding: 0 20upx;
	}

	.actiontext {
		color: $uni-text-color;
		padding: 0 20upx;
		display: inline-block;
		vertical-align: middle;
	}

	.oauth-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
	}

	.oauth-image {
		width: 100upx;
		height: 100upx;
		border: 1upx solid #dddddd;
		border-radius: 100upx;
		margin: 0 40upx;
		background-color: #ffffff;
	}

	.oauth-image image {
		width: 60upx;
		height: 60upx;
		margin: 20upx;
	}
</style>
