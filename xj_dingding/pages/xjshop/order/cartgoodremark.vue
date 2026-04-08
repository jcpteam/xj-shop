<template>
	<view class="content">
		<textarea class="input-bg" v-model="comment" />
		<button class="add-btn" @click="comfirm">确定</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsId:'',
				comment: ''
			}
		},

		onLoad(options) {
			this.goodsId = options.goodsId;
			this.comment = options.comment;
			if (!this.comment || this.comment == undefined) {
				this.comment = '';
			}
		},

		methods: {
			comfirm() {
				let params = {
					itemId:this.cartItemId,
					comment:this.comment
				};
				let self = this;
				self.goBack();
			},
			
			goBack(){
				//将当前页面值返回给上一个页面
				uni.$emit("goodCommentChange", {
					goodsId:this.goodsId,
					comment: this.comment
				});
				
				// 关闭当前页面 返回详细信息页面
				uni.navigateBack()
			},
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
		font-size: 30upx;
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
