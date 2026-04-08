<template>
	<view>
		<view class="switch_box row-b-c"
			:style="{backgroundColor: checked?bg:'',width: width + 'rpx',height: height + 'rpx',borderRadius: height/2 + 'rpx'}"
			@click="changeSwitch">
			<view class="switch_text c-white" :class="checked?['to_left']:['_right','c-black']"
				:style="{fontSize: size + 'rpx'}">{{checked?checked_text:check_text}}
			</view>
			<view class="round" :style="{left: checked?'100%':0,
		marginLeft:checked?'-' + height + 'rpx':'',width: + height + 'rpx',
		height: + height + 'rpx',
		borderRadius: height/2 + 'rpx'}">
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			bg: {
				type: String,
				default: '#ff4500'
			},
			checked: {
				type: Boolean,
				default: true
			},
			width: {
				type: String,
				default: '130'
			},
			height: {
				type: String,
				default: '50'
			},
			check_text: {
				type: String,
				default: ''
			},
			checked_text: {
				type: String,
				default: ''
			},
			size: {
				type: String,
				default: '24'
			}
		},
		data() {
			return {

			}
		},
		methods: {
			changeSwitch: function(e) {
				this.$emit('change', {
					checked: this.checked
				})
			}
		}
	}
</script>

<style>
	.row-c-c {
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.c-white {
		color: #FFFFFF;
	}

	.c-black {
		color: #666666;
	}


	/* ---------- */
	.switch_box {
		position: relative;
		border: 1rpx solid #EEEEEE;
		background-color: #F6F6F6;
		transition: all 0.4s;
	}

	.round {
		position: absolute;
		background-color: #FFFFFF;
		transition: all 0.4s;
	}

	.to_left {
		margin-top: 10rpx;
		left: 0;
		margin-left: 0;
	}

	.to_right {
		left: 100%;
		margin-left: -50rpx;
	}

	.switch_text {
		position: absolute;
		padding: 0 20rpx;
	}

	._right {
		margin-top: 10rpx;
		right: 0;
	}
</style>
