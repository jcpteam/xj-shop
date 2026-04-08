<template>
	<view class="message">
		<scroll-view scroll-y :scroll-top="scrollTop" @scroll="scroll" @scrolltolower="scrollToBottom"
			:style="'height:'+height+'px'" scroll-with-animation>
			<view class="good-content">
				<view class="goods" v-for='(item,index) in messageList' :key='index'>
					<view class="goods-customer">
						<view class="name">
							<view class="customer-name">{{item.title}}</view>
						</view>
					</view>

					<view class="paystatus_contaner">
						<text class="status_tip">预警内容：</text>
						<text class="status_value">{{item.content}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">发送时间：</text>
						<text class="status_value">{{item.createTime}}</text>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	export default {
		components: {},

		data() {
			return {
				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},

				messageList: [],

				//滚动条
				height: 0,
				scrollTop: 0,
				scrollHeight: 0,
			};
		},

		mounted() {
			this.height = uni.getSystemInfoSync().windowHeight;
		},

		onLoad(options) {},

		onShow() {
			this.init();
		},

		methods: {
			init() {
				this.reloadMessageList();
			},

			reloadMessageList() {
				this.orderList = [];
				this.pagination.total = 0;
				this.pagination.pageNo = 1;
				this.pagination.pageSize = 10;
				this.getMessageList();
			},

			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},

			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.getMessageList();
			},

			getMessageList() {
				//判断是否超过总数，超过则不再请求
				if (this.pagination.total > 0) {
					let pageTotal = this.$getTotalPageNo(this.pagination.total, this.pagination.pageSize);
					console.log("总页数-->", pageTotal);
					console.log("当前请求页--->", this.pagination.pageNo);
					if (this.pagination.pageNo > pageTotal) {
						return;
					}
				}

				uni.showLoading({
					title: '正在加载...'
				});

				let self = this;
				let url = '/shop/storeMessage/list?pageNum=' + self.pagination.pageNo + '&pageSize=' + self.pagination
					.pageSize;
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log("messageList>", resp);
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							self.pagination.total = resp.data.total;
							//请求成功，更新下次请求的页码
							self.pagination.pageNo = self.pagination.pageNo + 1;
							//数据累加
							self.messageList = self.messageList.concat(resp.data.rows);
						} else {
							self.messageList = [];
						}
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},
		},
	}
</script>

<style lang="scss">
	.message {
		.good-content {
			.goods {
				padding: 20upx 0upx 20upx 0upx;
				color: #000000;
				border-bottom: solid 1px #E0E0E0;

				.goods-customer {
					padding: 15upx 20upx 15upx 20upx;
					display: flex;
					align-items: center;
					justify-content: space-between;
					font-size: 28upx;
					color: #000000;

					.name {
						flex: 10;
						display: flex;
						align-items: center;
						justify-content: flex-start;

						.customer-name {
							// flex: 7;
							font-size: 32upx;
							// padding-left: 20upx;
							padding-right: 20upx;
							color: #000000;
							font-weight: bold;

							//设置文本单行显示，超出显示省略号
							display: -webkit-box;
							overflow: hidden;
							text-overflow: ellipsis;
							word-wrap: break-word;
							white-space: normal !important;
							-webkit-line-clamp: 1; //设置行数
							-webkit-box-orient: vertical;
						}
					}
				}

				.paystatus_contaner {
					padding: 10upx 20upx 10upx 20upx;
					display: flex;
					align-items: center;
					justify-content: flex-start;
					font-size: 28upx;

					.status_tip {
						color: #666;
					}

					.status_value {
						color: #000000;
					}
				}

			}
		}
	}
</style>
