<template>
	<view class="order">
		<view class="tab">
			<uni-segmented-control :current="curSelectTab || 0" :values="tabArray" @clickItem="tabClick"
				styleType="text" activeColor='#3fb86b'></uni-segmented-control>
		</view>
		<scroll-view scroll-y :scroll-top="scrollTop" @scroll="scroll" @scrolltolower="scrollToBottom"
			:style="'height:'+height+'px'" scroll-with-animation>
			<view class="good-content">
				<view class="search-box">
					<text-switch bg="#3fb86b" width="175" height="55" :checked="switchState" check_text="全部订单"
						checked_text="自己下单" @change="switchChange" />
					<mSearch class="mSearch-input-box" :mode="2" button="inside" :placeholder="defaultKeyword"
						@search="doSearch()" @confirm="doSearch()" @input="inputChange" v-model="inputKeyword">
					</mSearch>
				</view>

				<view class="goods" v-for='(item,index) in orderList' :key='index' @click="gotoOrderDetail(item)">
					<view class="goods-customer">
						<view class="name">
							<view class="customer-name">{{item.merchantName}}</view>
							<image class="arrow_image_name" src="/static/arrow.png"></image>
						</view>
						<text v-if="item.status === '3'" class="status">待支付</text>
						<text v-else-if="item.status === '2'" class="status">待配送</text>
						<text v-else class="status">{{item.statusName}}</text>
					</view>

					<view class="image_contaner">
						<view>
							<image v-for="(subItem,index) in item.goodsOrderItems" class="goods_image"
								:src="parseGoodImgUrl(subItem)" @error="imageError(subItem)"></image>
						</view>
						<view class="goods-num-content">
							<text class="goods_num">共{{item.goodsOrderItems.length}}种</text>
							<text v-if="item.status === '2' || item.status === '3' || item.status === '4'"
								class="goods_amount">¥{{item.sortingPrice}}</text>
							<text v-else class="goods_amount">¥{{item.price}}</text>
						</view>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">订单编号：</text>
						<text class="status_value">{{item.code}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">支付状态：</text>
						<text class="status_value">{{item.payStatusName}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">下单时间：</text>
						<text class="status_value">{{item.createTime}}</text>
					</view>
					<view class="paystatus_contaner">
						<text class="status_tip">收货时间：</text>
						<text class="status_value">{{getOrderDeliverTime(item.deliveryDate)}}</text>
					</view>

					<view v-if="item.status === '1'" class="control-roder">
						<text class="check-roder" @click.stop="checkOrder(item,index)">审核订单</text>
						<text class="modify-roder" @click.stop="modifyOrder(item,index)">修改订单</text>
						<text class="cancel-roder" @click.stop="cancelOrder(item,index)">作废订单</text>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import uniSegmentedControl from '@/components/xjshop/common/uni-segmented-control.vue'
	//引用mSearch组件，如不需要删除即可
	import mSearch from '@/components/mehaotian-search-revision/mehaotian-search-revision.vue';
	import textSwitch from '@/components/xjshop/common/text-switch.vue';

	export default {
		components: {
			uniSegmentedControl,
			mSearch,
			textSwitch
		},

		data() {
			return {
				//顶部tab数据
				curSelectTab: 0,
				tabArray: ['全部', '待审核', '待配送', '待支付', '已完成'],

				//默认的广告图片，加载失败则使用此图片
				defaultGoodsImg: '/static/xj_logo.jpg',
				//用户信息
				userInfo: {},

				//分页参数
				pagination: {
					total: 0,
					pageSize: 10,
					pageNo: 1
				},

				orderList: [],

				//滚动条
				height: 0,
				scrollTop: 0,
				scrollHeight: 0,

				defaultKeyword: "输入客户名称",
				inputKeyword: "",
				hadSearch: false,

				//开关选中状态
				switchState: false,
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
				let jumpIndex = uni.getStorageSync('selectOrderTabIndex');
				console.log('jumpIndex--->', jumpIndex);
				if (jumpIndex >= 0) {
					this.curSelectTab = jumpIndex;
					uni.setStorageSync('selectOrderTabIndex', -1);
				}

				this.reloadOrderList();
			},

			reloadOrderList() {
				this.orderList = [];
				this.pagination.total = 0;
				this.pagination.pageNo = 1;
				this.pagination.pageSize = 10;
				this.getOrderList();

				//重置订单修改的缓存数据
				uni.setStorageSync('userModifyOrderId', '');
				uni.setStorageSync('userModifyMerchantId', '');
				uni.setStorageSync('userModifyQuotationId', '');
				uni.setStorageSync('userModifyDeptId', '');
				uni.setStorageSync('userModifyOrderGoodsMap', {});
			},

			tabClick(index) {
				this.curSelectTab = index;
				this.init();
			},

			scroll(e) {
				this.scrollHeight = e.detail.scrollHeight;
			},

			//滑到底部监听
			scrollToBottom() {
				console.log('监听到滑动到底部....');
				this.getOrderList();
			},

			getOrderList() {
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

				let status = this.curSelectTab + '';
				if (status === '0') {
					status = '';
				}
				let self = this;
				let url = '/shop/order/appListForDingding?pageNum=' + self.pagination.pageNo + '&pageSize=' + self
					.pagination
					.pageSize + '&status=' + status + '&deliveryKey=' + self.inputKeyword +
					'&orderByColumn=create_time&isAsc=desc';
				if(this.switchState){
					let userInfo = uni.getStorageSync('userInfo');
					if(userInfo && userInfo.userId){
						url += '&creatorId=' + userInfo.userId;
					}
				}
				this.sendRequest({
					'url': url,
					'data': null
				}, function(resp) {
					console.log("orderList--->", resp);
					if (resp.data.code === 0) {
						if (resp.data.rows && resp.data.rows.length > 0) {
							self.pagination.total = resp.data.total;
							//请求成功，更新下次请求的页码
							self.pagination.pageNo = self.pagination.pageNo + 1;
							//数据累加
							self.orderList = self.orderList.concat(resp.data.rows);
						} else {
							self.orderList = [];
						}
					}
					uni.hideLoading();
				}, function(error) {
					uni.hideLoading();
				})
			},

			//跳转订单详情页面
			gotoOrderDetail(orderItem) {
				this.$jump('/pages/xjshop/order/orderdetail?orderId=' + orderItem.orderId);
			},

			cancelOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定作废订单吗？',
					success: function(res) {
						if (res.confirm) {
							uni.showLoading({
								title: '正在提交...'
							});

							let req = {
								'ids': orderItem.orderId
							}
							let url = '/shop/order/remove'
							self.sendRequest({
								'url': url,
								'method': 'POST',
								'contentType': 'application/x-www-form-urlencoded',
								'data': req
							}, function(resp) {
								uni.hideLoading();
								if (resp.data.code === 0) {
									self.orderList.splice(index, 1); //删除元素
								}
							}, function(error) {
								uni.hideLoading();
								self.$api.msg('作废订单失败 : ' + error.data.msg)
							})
						}
					}
				});
			},

			checkOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定审核订单吗？',
					success: function(res) {
						if (res.confirm) {
							uni.showLoading({
								title: '正在提交...'
							});

							// 订单审核
							let req = {
								'ids': orderItem.orderId
							}
							let url = '/shop/order/examine'
							self.sendRequest({
								'url': url,
								'method': 'POST',
								'contentType': 'application/x-www-form-urlencoded',
								'data': req
							}, function(resp) {
								uni.hideLoading();
								if (resp.data.code === 0) {
									//审核通过删除内存元素
									self.orderList.splice(index, 1); //删除元素
								}
							}, function(error) {
								uni.hideLoading();
								self.$api.msg('订单审核失败 : ' + error.data.msg)
							})
						}
					}
				});
			},

			modifyOrder(orderItem, index) {
				let self = this;
				uni.showModal({
					title: '警告',
					content: '确定修改订单吗？',
					success: function(res) {
						if (res.confirm) {
							self.$jump('/pages/xjshop/order/ordermodify?orderId=' + orderItem.orderId +
								'&useCache=0');
						}
					}
				});
			},

			getOrderDeliverTime(deliverTime) {
				if (deliverTime && deliverTime.length > 10) {
					return deliverTime.slice(0, 10);
				} else {
					return deliverTime;
				}
			},

			// 当载失败时使用默认图片
			imageError(item) {
				item.imgUrl = this.defaultGoodsImg;
			},

			//监听输入
			inputChange(event) {
				var k = event.detail ? event.detail.value : event;
				if (!k && this.hadSearch) {
					this.hadSearch = false;
					this.reloadOrderList();
				}
			},

			//监听输入
			doSearch() {
				this.hadSearch = true;
				this.reloadOrderList();
			},

			//开关状态改变
			switchChange(event){
				this.switchState = !event.checked;
				this.reloadOrderList();
			}
		},
	}
</script>

<style lang="scss">
	.order {
		.tab {
			margin-top: 0upx;
		}

		.good-content {
			margin-top: 90upx;

			.goods {
				padding: 20upx 0upx 20upx 0upx;
				color: #000000;
				// margin-top: 100upx;
				border-bottom: solid 1px #E0E0E0;

				.image_contaner {
					padding: 15upx 20upx 15upx 20upx;
					display: flex;
					align-items: center;
					justify-content: space-between;

					.goods_image {
						width: 120upx;
						height: 120upx;
						border-radius: 8upx;
						margin-right: 15upx;
					}

					.goods-num-content {
						width: 230upx;
						display: flex;
						align-items: center;
						justify-content: flex-end;
						font-size: 28upx;

						.goods_num {
							color: #666;
						}

						.goods_amount {
							margin-left: 20upx;
							color: $uni-xj-good-price;
						}
					}

				}

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

						.arrow_image_name {
							width: 16upx;
							height: 24upx;
							vertical-align: middle;
						}

					}

					.status {
						color: #000000;
						display: flex;
						align-items: flex-end;
						justify-content: flex-end;
						flex: 4;
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

				.contact-btn {
					display: flex;
					align-items: center;
					justify-content: center;
					width: 180upx;
					height: 60upx;
					font-size: 28upx;
					color: #000000;
					margin-left: auto;
					margin-right: 20upx;
					background-color: #ffffff;
					border-radius: 20px;
					border: 1upx solid #bbb;
				}

				.control-roder {
					font-size: 28upx;
					display: flex;
					margin-right: 20upx;
					align-items: flex-end;
					justify-content: flex-end;
				}

				.check-roder {
					font-size: 28upx;
					color: #006400;
					margin-right: 40upx;
				}

				.cancel-roder {
					font-size: 28upx;
					color: #dd524d;
				}

				.modify-roder {
					font-size: 28upx;
					color: #4A4AFF;
					margin-right: 40upx;
				}
			}

		}



		.arrow_image {
			width: 16upx;
			height: 24upx;
			margin-left: 10upx;
			vertical-align: middle;
		}

	}

	.search-box {
		width: 95%;
		background-color: rgb(242, 242, 242);
		padding: 15upx 2.5%;
		display: flex;
		justify-content: space-between;
	}

	.search-box .mSearch-input-box {
		margin-left: 15upx;
		width: 100%;
	}

	.search-box .input-box {
		width: 85%;
		flex-shrink: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.search-box .search-btn {
		width: 15%;
		margin: 0 0 0 2%;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0;
		font-size: 28upx;
		color: #fff;
		background: linear-gradient(to right, #ff9801, #ff570a);
		border-radius: 60upx;
	}

	.search-box .input-box>input {
		width: 100%;
		height: 60upx;
		font-size: 32upx;
		border: 0;
		border-radius: 60upx;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		padding: 0 3%;
		margin: 0;
		background-color: #ffffff;
	}
</style>
