<template>
	<view class="uni-numbox">
		<view @click="_calcValue('minus')" class="uni-numbox__minus uni-cursor-point">
			<text class="uni-numbox--text" :class="{ 'uni-numbox--disabled': inputValue <= min || disabled }">-</text>
		</view>
		<input :disabled="disabled" @focus="_onFocus" @blur="_onBlur" class="uni-numbox__value" type="number"
			v-model="inputValue" @keyup="_inputChange" />
		<view @click="_calcValue('plus')" class="uni-numbox__plus uni-cursor-point">
			<text class="uni-numbox--text" :class="{ 'uni-numbox--disabled': inputValue >= max || disabled }">+</text>
		</view>
	</view>
</template>
<script>
	/**
	 * NumberBox 数字输入框
	 * @description 带加减按钮的数字输入框
	 * @tutorial https://ext.dcloud.net.cn/plugin?id=31
	 * @property {Number} value 输入框当前值
	 * @property {Number} min 最小值
	 * @property {Number} max 最大值
	 * @property {Number} step 每次点击改变的间隔大小
	 * @property {Boolean} disabled = [true|false] 是否为禁用状态
	 * @event {Function} change 输入框值改变时触发的事件，参数为输入框当前的 value
	 */

	export default {
		name: "UniNumberBox",
		emits: ['change', 'input', 'update:modelValue', 'blur', 'focus'],
		props: {
			value: {
				type: [Number, String],
				default: 1
			},
			modelValue: {
				type: [Number, String],
				default: 1
			},
			min: {
				type: Number,
				default: 0
			},
			max: {
				type: Number,
				default: 100
			},
			step: {
				type: Number,
				default: 1
			},
			disabled: {
				type: Boolean,
				default: false
			},
			itemObj: {
				type: Object,
				default: {}
			}
		},
		data() {
			return {
				inputValue: 0,
				beforeFocusvalue:0   //获取焦点前的值
			};
		},
		watch: {
			value(val) {
				this.inputValue = +val;
			},
			modelValue(val) {
				this.inputValue = +val;
			}
		},
		created() {
			if (this.value === 1) {
				this.inputValue = +this.modelValue;
			}
			if (this.modelValue === 1) {
				this.inputValue = +this.value;
			}
		},
		methods: {
			_inputChange() { //输入框值改变
				if (this.inputValue) {
					this.inputValue = this.inputValue.replace(/[^\d]/g, '')
				} else {
					// this.inputValue = 0;
				}
			},

			_calcValue(type) {
				if (this.disabled) {
					return;
				}
				
				//用户信息
				let userInfo = uni.getStorageSync('userInfo') || {};
				if(userInfo.quotationInfo){
					if(userInfo && userInfo.quotationInfo && !this.$compareCurTime(userInfo.quotationInfo.operateStartTime) && this.$compareCurTime(userInfo.quotationInfo.operateEndTime)){
						//在下单时间内
					}else{
						uni.showToast({
							title: "请在商品供应时间内下单",
							icon: 'none',
						});
						return;
					}
				}else{
					uni.showToast({
						title: "没有配置报价单，请联系管理员",
						icon: 'none',
					});
					return;
				}
				
				const scale = this._getDecimalScale();
				let value = this.inputValue * scale;
				let step = this.step * scale;
				if (type === "minus") {
					value -= step;
					if (value < (this.min * scale)) {
						return;
					}
					if (value > (this.max * scale)) {
						value = this.max * scale
					}
				}

				if (type === "plus") {
					value += step;
					if (value > (this.max * scale)) {
						return;
					}
					if (value < (this.min * scale)) {
						value = this.min * scale
					}
				}

				this.inputValue = (value / scale).toFixed(String(scale).length - 1);
				this.$emit("change", +this.inputValue, this.itemObj);
				// TODO vue2 兼容
				this.$emit("input", +this.inputValue, this.itemObj);
				// TODO vue3 兼容
				this.$emit("update:modelValue", +this.inputValue, this.itemObj);
			},
			_getDecimalScale() {

				let scale = 1;
				// 浮点型
				if (~~this.step !== this.step) {
					scale = Math.pow(10, String(this.step).split(".")[1].length);
				}
				return scale;
			},
			_onBlur(event) {
				this.$emit('blur', event, this.itemObj)
				let value = event.detail.value;
				if (!value) {
					// this.inputValue = 0;
					// return;
					console.log("reset before focus value--->",this.beforeFocusvalue);
					this.inputValue = this.beforeFocusvalue;
					value = this.inputValue;
				}
				value = +value;
				if (value > this.max) {
					value = this.max;
				} else if (value < this.min) {
					value = this.min;
				}
				const scale = this._getDecimalScale();
				this.inputValue = value.toFixed(String(scale).length - 1);
				this.$emit("change", +this.inputValue, this.itemObj);
				this.$emit("input", +this.inputValue, this.itemObj);
			},
			_onFocus(event) {
				this.beforeFocusvalue = event.detail.value;
				console.log('before onFocus value--->',value);
				this.$emit('focus', event, this.itemObj)
			}
		}
	};
</script>
<style lang="scss" scoped>
	$box-height: 25px;
	/* #ifdef APP-NVUE */
	$box-line-height: 35px;
	/* #endif */
	$box-line-height: 26px;
	$box-width: 35px;

	.uni-numbox {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		height: $box-height;
		line-height: $box-height;
		width: 120px;
	}

	.uni-cursor-point {
		/* #ifdef H5 */
		cursor: pointer;
		/* #endif */
	}

	.uni-numbox__value {
		background-color: $uni-bg-color;
		width: 50px;
		height: $box-height;
		text-align: center;
		font-size: $uni-font-size-lg;
		border-width: 1rpx;
		border-style: solid;
		border-color: $uni-border-color;
		border-left-width: 0;
		border-right-width: 0;
	}

	.uni-numbox__minus {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		width: $box-width;
		height: $box-height;
		// line-height: $box-line-height;
		// text-align: center;
		font-size: 20px;
		color: $uni-text-color;
		background-color: $uni-bg-color-grey;
		border-width: 1rpx;
		border-style: solid;
		border-color: $uni-border-color;
		border-top-left-radius: $uni-border-radius-base;
		border-bottom-left-radius: $uni-border-radius-base;
		border-right-width: 0;
	}

	.uni-numbox__plus {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
		justify-content: center;
		width: $box-width;
		height: $box-height;
		border-width: 1rpx;
		border-style: solid;
		border-color: $uni-border-color;
		border-top-right-radius: $uni-border-radius-base;
		border-bottom-right-radius: $uni-border-radius-base;
		background-color: $uni-bg-color-grey;
		border-left-width: 0;
	}

	.uni-numbox--text {
		font-size: 20px;
		color: $uni-text-color;
	}

	.uni-numbox--disabled {
		color: $uni-text-color-disable;
		/* #ifdef H5 */
		cursor: not-allowed;
		/* #endif */
	}
</style>
