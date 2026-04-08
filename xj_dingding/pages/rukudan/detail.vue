<template>
	<view class="content">
		
		<empty v-if="!rukulist"></empty>
		<!-- 入库单列表 -->
        <view v-else>
			<view
			    v-for="(item,index) in rukulist" :key="index"
			    class="order-item"
			    @click="torukudetail(item)"
			>
			    <view class="oi-top common-flex-x">
			        <text class="left" style="width: 80%;">
			            <!-- <text class="border-txt" v-if="item.source === '2'">代</text> -->
						<view class="title" style="font-weight: bold;">{{item.merchantName}}</view>
			        </text>
			        <text class="right" style="width: 20%;">{{item.statusName}}</text>
			    </view>
			    <view class="i-top b-b">
			        <view><text class="gray_txt">入库单编号：</text>{{item.stockNo}}</view>
			        <view><text class="gray_txt">所属仓库：</text>{{item.warehouseName}}</view>
			        <view><text class="gray_txt">仓库所属区域：</text>{{item.deptName}}</view>
			        <view><text class="gray_txt">仓库类别：</text>{{item.stockTypeName}}</view>
			        <view><text class="gray_txt">入库时间：</text>{{item.createTime}}</view>
					<view style="position: relative;">
						<text class="gray_txt">入库时间：&nbsp;&nbsp; </text>{{item.createTime}}
						<view class="right" style="position: absolute; bottom: 0; right: 25upx;">
							<text v-if="item.auditStatus === '1'">审核通过</text>
							<text v-else 
							style="background-color: #1AAD19; color:#fff; padding: 2px 5px; border-radius: 5px;" 
							@click.stop="checkItem(item)">审核</text>
						</view>
					</view> 
			    </view>
			</view>
        </view>
		<view style="margin-top: 50upx;height: 100upx;">&nbsp;</view>
        <!-- 搜索条件弹出框 start -->
        <view class="example-body">
            <uni-drawer ref="showRight" mode="right" :mask-click="true" :width="searchWidth">
                <view style="padding: 30% 15px; font-size: 30upx;">
                    <view class="common-flex-x">
                        <text class="left">开始日期：</text>
                        <uni-datetime-picker type="date" :value="searchStartDate" />
                    </view>
                    <view class="common-flex-x" style="margin-top: 20upx;">
                        <text class="left">结束日期：</text>
                        <uni-datetime-picker type="date" :value="searchEndDate" />
                    </view>
                    <!-- <view class="common-flex-x" style="margin-top: 40upx;">
                        <text class="left">所属中心：</text>
                        <picker @change="searchLocationChange" :value="searchLocationIndex" :range="searchLocationArray" class="right">
                            <view class="uni-input">{{searchLocationArray[searchLocationIndex]}}</view>
                        </picker>
                    </view> -->
                    <button type="default" @click="cornformSearch" class="sesarch-confirm-btn">确定</button>
                </view>
            </uni-drawer>
        </view>
        <!-- 搜索条件弹出框 end -->
	</view>
</template> 

<script>
import uniDatetimePicker from '@/components/uni-datetime-picker/uni-datetime-picker.vue';
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
import empty from "@/components/empty/empty"; 
	
	export default {
		components: {
			uniLoadMore,
			empty,
            uniDatetimePicker
		},
		data() {
			return {
				currentId: 0,
                searchWidth: 280
			};
		},
		
		onLoad(options){
			if(options.id){
				this.currentId = options.id
				this.loadDetail()
			}
		},
		
		watch:{

		},
        mounted() {
        },
		methods: {
			loadDetail () {
				let vm = this
				// vm.sendRequest({'url': '/shop/stock/list?pageNum=' + vm.pageNum + '&pageSize=10&orderByColumn=last_modify_time&isAsc=desc', 'contentType': 'x-www-form-urlencoded', 'method': 'POST', 'data': null}, function(resp){
				// 	console.log('resp = ' + JSON.stringify(resp))
				// 	if(resp.data.rows){
				// 		for (let i=0; i<resp.data.rows.length; i++){
				// 			vm.rukulist.push(resp.data.rows[i])
				// 		}
				// 	}
				// }, function(error){
				// 	vm.$api.msg(error.data.msg)
				// })
				
				
				vm.sendRequest({'url': '/shop/stock/detail/' + vm.currentId, 'contentType': 'x-www-form-urlencoded', 'method': 'POST', 'data': null}, function(resp){
					console.log('resp = ' + JSON.stringify(resp))
					if(resp.data.rows){
					}
				}, function(error){
					vm.$api.msg(error.data.msg)
				})
				
				
			},
			checkItem (item) {
				let vm = this
				vm.sendRequest({'url': '/shop/stock/audit/stock?ids=' + item. stockId+ '&auditStatus=1', 'contentType': 'x-www-form-urlencoded', 'method': 'POST', 'data': null}, function(resp){
					vm.reload_list()
				}, function(error){
					vm.$api.msg(error.data.msg)
				})
			}
		},
	}
</script>

<style lang="scss">
	page, .content{
		background: #f8f8f8;
		height: 100%;
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
	}
	.order-item{
		padding-left: 30upx;
		padding: 20upx 30upx;
		background: #fff;
		margin-top: 16upx;
		font-size: 28upx;
		line-height: 50upx;
		
        .title {
            color: #4A94FF;
        }
        .to_dxd_btn {
            position: absolute;
            right: 0;
            top: 20upx;
            width: 140upx;
            height: 60upx;
            line-height: 60upx;
            text-align: center;
            border: solid 4upx #0DBACB;
            border-radius: 26upx;
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
    
    .common-flex-x {
    	display: flex;
    	flex-direction: row;
    	flex: 1;
    	.left {
    		width: 50%;
    		text-align: left;
    	}
    	.right {
    		width: 50%;
    		text-align: left;
    	}
    }
    .uni-drawer-info {
    		background-color: #ffffff;
    		padding: 15px;
    		padding-top: 5px;
    		color: #3b4144;
            font-size: 28upx;
    	}
    
    	.uni-padding-wrap {
    		padding: 0 15px;
    		line-height: 1.8;
    	}
    
    	.input {
    		flex: 1;
    		padding: 0 5px;
    		height: 24px;
    		line-height: 24px;
    		font-size: $uni-font-size-base;
    		background-color: transparent;
    	}
    
    	.close {
    		padding: 15px;
    	}
    
    	.example-body {
    		/* #ifndef APP-NVUE */
    		display: flex;
    		/* #endif */
    		flex-direction: row;
    		padding: 0;
    	}
    
    	.draw-cotrol-btn {
    		flex: 1;
    	}
    
    	.info {
    		padding: 15px;
    		color: #666;
    	}
    
    	.info-text {
    		font-size: 14px;
    		color: #666;
    	}
    	.info-content {
    		padding: 5px 15px;
    	}
        
        .sesarch-confirm-btn {
            margin-top: 50upx;
            height: 60upx; line-height: 60upx; 
            color: #fff; background: #1AAD19;
        }
</style>
