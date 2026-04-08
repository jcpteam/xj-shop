<template>
	<view class="content">

		<view class="search-box">
			<mSearch class="mSearch-input-box" :mode="2" button="inside" :placeholder="defaultKeyword"
				@search="doSearch()" @confirm="doSearch()" @input="inputChange" v-model="inputKeyword"></mSearch>
		</view>

		<empty v-if="!clientList"></empty>
		<!-- 客户列表 -->
		<view v-else>
			<view v-for="(item,index) in clientList" :key="index" class="order-item">
				<view class="oi-top b-b">
					<view>
						<text class="title">{{item.nickname}}</text>
					</view>
					<view><text class="gray_txt">客户编号：</text>{{item.customerNo}}</view>
					<view v-if="item.address"
						style="border-top: solid 1px #f8f8f8; margin-top: 20upx; padding: 10upx 0;">
						&nbsp;&nbsp;
						<text class="gray_txt mark-des-txt">{{item.address}}</text>
					</view>
					<view v-else>
						&nbsp;
					</view>
					<view class="to_dxd_btn" @click="toDaixiadan(item)">代下单</view>
				</view>
			</view>
		</view>
		<view style="margin-top: 50upx;height: 100upx;">&nbsp;</view>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import empty from "@/components/empty/empty";
	//引用mSearch组件，如不需要删除即可
	import mSearch from '@/components/mehaotian-search-revision/mehaotian-search-revision.vue';

	export default {
		components: {
			uniLoadMore,
			empty,
			mSearch
		},
		data() {
			return {
				clientList: [],
				pageNum: 1,
				pageSize: 10,
				totalCount: 0,

				defaultKeyword: "输入客户名称",
				inputKeyword: "",
				hadSearch: false
			};
		},

		onLoad(options) {
			this.reload_list()
		},

		watch: {

		},
		onReachBottom() {
			let totalPage = this.totalCount / this.pageSize

			if (this.pageNum < totalPage) {
				this.pageNum++
				this.load_list();
			}
		},
		onPullDownRefresh() {

		},
		methods: {
			//监听输入
			inputChange(event) {
				var k = event.detail ? event.detail.value : event;
				if (!k && this.hadSearch) {
					this.hadSearch = false;
					this.reload_list();
				}
			},

			//监听输入
			doSearch() {
				this.hadSearch = true;
				this.reload_list();
			},

			reload_list() {
				this.pageNum = 1
				this.clientList = [];
				this.load_list();
			},
			load_list() {
				let vm = this
				let req = {
					pageNum: vm.pageNum,
					pageSize: vm.pageSize,
				}
				let url = '/shop/member/list?nickname=' + vm.inputKeyword;
				vm.sendRequest({
					'url': url,
					'method': 'POST',
					'data': req,
					'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'
				}, function(resp) {
					if (resp.data.code === 0 || resp.data.code === '0') {
						if (resp.data.rows) {
							for (let i = 0; i < resp.data.rows.length; i++) {
								vm.clientList.push(resp.data.rows[i])
							}
						}
					}
					vm.totalCount = resp.data.total
				}, function(error) {
					vm.$api.msg('获取列表失败 : ' + error.data.msg)
				})
			},
			
			toDaixiadan(memberItem) {
				this.$navigateTo("/pages/xjshop/tabs/type/type?clientid=" + memberItem.id + '&customerArea=' + memberItem
					.customerArea);
			}
		},
	}
</script>

<style lang="scss">
	page,
	.content {
		background: #f8f8f8;
		height: 100%;
	}

	.head-search {
		position: sticky;
		/* #ifdef H5 */
		top: 44px;
		/* #endif */
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		z-index: 999;
		flex: 1;
		flex-direction: column;
		overflow: hidden;
		background-color: #ffffff;
	}

	.gray_txt {
		color: #A1A2A6;
	}

	.border-txt {
		display: inline;
		color: #4A94FF;
		paddding: 2upx 20upx;
		border: solid 3upx #4A94FF;
		text-align: center;
		margin-left: 20upx;
		font-size: 24upx;
	}

	.order-item {
		padding-left: 30upx;
		padding: 20upx 30upx;
		background: #fff;
		margin-top: 16upx;
		font-size: 32upx;
		line-height: 50upx;

		.oi-top {
			.title {
				color: #4A4B50;
			}

			position: relative;

			.to_dxd_btn {
				position: absolute;
				right: 0;
				top: 10px;
				width: 140upx;
				height: 60upx;
				line-height: 60upx;
				text-align: center;
				border: solid 4upx #2FC25B;
				border-radius: 10upx;
				background-color: #2FC25B;
				color: #fff;
			}
		}
	}

	/* load-more */
	.uni-load-more {
		display: flex;
		flex-direction: row;
		height: 80upx;
		align-items: center;
		justify-content: center
	}

	.uni-load-more__text {
		font-size: 28upx;
		color: #999
	}

	.uni-load-more__img {
		height: 24px;
		width: 24px;
		margin-right: 10px
	}

	.uni-load-more__img>view {
		position: absolute
	}

	.uni-load-more__img>view view {
		width: 6px;
		height: 2px;
		border-top-left-radius: 1px;
		border-bottom-left-radius: 1px;
		background: #999;
		position: absolute;
		opacity: .2;
		transform-origin: 50%;
		animation: load 1.56s ease infinite
	}

	.uni-load-more__img>view view:nth-child(1) {
		transform: rotate(90deg);
		top: 2px;
		left: 9px
	}

	.uni-load-more__img>view view:nth-child(2) {
		transform: rotate(180deg);
		top: 11px;
		right: 0
	}

	.uni-load-more__img>view view:nth-child(3) {
		transform: rotate(270deg);
		bottom: 2px;
		left: 9px
	}

	.uni-load-more__img>view view:nth-child(4) {
		top: 11px;
		left: 0
	}

	.load1,
	.load2,
	.load3 {
		height: 24px;
		width: 24px
	}

	.load2 {
		transform: rotate(30deg)
	}

	.load3 {
		transform: rotate(60deg)
	}

	.load1 view:nth-child(1) {
		animation-delay: 0s
	}

	.load2 view:nth-child(1) {
		animation-delay: .13s
	}

	.load3 view:nth-child(1) {
		animation-delay: .26s
	}

	.load1 view:nth-child(2) {
		animation-delay: .39s
	}

	.load2 view:nth-child(2) {
		animation-delay: .52s
	}

	.load3 view:nth-child(2) {
		animation-delay: .65s
	}

	.load1 view:nth-child(3) {
		animation-delay: .78s
	}

	.load2 view:nth-child(3) {
		animation-delay: .91s
	}

	.load3 view:nth-child(3) {
		animation-delay: 1.04s
	}

	.load1 view:nth-child(4) {
		animation-delay: 1.17s
	}

	.load2 view:nth-child(4) {
		animation-delay: 1.3s
	}

	.load3 view:nth-child(4) {
		animation-delay: 1.43s
	}

	@-webkit-keyframes load {
		0% {
			opacity: 1
		}

		100% {
			opacity: .2
		}
	}

	.mark-des-txt {
		font-size: 24upx;
	}

	.search-box {
		width: 95%;
		background-color: rgb(242, 242, 242);
		padding: 15upx 2.5%;
		display: flex;
		justify-content: space-between;
	}

	.search-box .mSearch-input-box {
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
