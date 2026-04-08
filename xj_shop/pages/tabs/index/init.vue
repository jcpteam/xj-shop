<template>
	<view class="content">
		<!-- 入口页 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
			}
		},
		onLoad(){
			this.loadExecution()
		},
		methods: {
			loadExecution: function(){
				uni.switchTab({
				    url: '/pages/tabs/index/index'
				});
			}
		}
	}
</script>

<style>
	
</style>
