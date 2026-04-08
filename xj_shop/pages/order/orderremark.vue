<template>
	<view class="content">
		<textarea class="input-bg" v-model="remark" />
		<button class="add-btn" @click="comfirm">确定</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				remark: ''
			}
		},

		onLoad(options) {
			this.remark = options.remark;
			if (!this.remark || this.remark == undefined) {
				this.remark = '';
			}
		},

		methods: {
			comfirm() {
				//将当前页面值返回给上一个页面
				uni.$emit("remarkChange", {
					remark: this.remark
				});
				
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			}
		}
	}
</script>

<style lang="scss">
	.input-bg {
		margin: 20upx;
		width: auto;
		padding: 10upx 20upx;
		background-color: #f6f6f6;
		height: 250upx;
		font-size: 24upx;
		border-radius: 10upx;
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
</style>
