<template>
	<view class="content">
		
		<!--
			{
				 "settingId":538,     //控价单编号
				 "deptId":"040601",   //所在中心编号
				 "deptName":"长沙直营部",//所在中心名称
				 "optDate":"2021-08-16", //控价日期
				 "status":"2",//审核状态：0-未生效   1-已生效
				 "goodsCount":5,//商品数量
				 "adjustCount":1,//报价单数量
				 "lastModifyTime":"2021-08-16 03:00:00",//最后修改时间
				 "submitStatus":"1"//提交状态：0-未提交  1-已提交
			 }
		 -->
		<empty v-if="!kongshuList"></empty>
		<!-- 控数列表 -->
        <view v-else>
            <view 
                v-for="(item,index) in kongshuList" :key="index"
                class="order-item"
                @click="toDetail(item)"
            >
                <view>
					<text class="gray_txt">控价单编号：</text>
					<text class="value">{{item.settingId}}</text>
                </view>
                <view>
					<text class="gray_txt">所属中心：</text>
					<text class="value">{{item.deptName}}</text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">商品数量：</text>
                        <text class="value">{{item.goodsCount}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt">报价单数量：</text>
                        <text class="value">{{item.adjustCount}}</text>
                    </text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">控价日期：</text>
                        <text class="value">{{item.optDate}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt">状态：</text>
                        <text class="value">{{getStatusName(item.submitStatus)}}</text>
                    </text>
                </view>
                <view v-if="item.submitStatus !== '1'">
                    <button type="default" @click="setCheck(item)" class="common-normal-btn">审核</button>
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
                        <uni-datetime-picker type="date" :value="searchStartDate" @change="searchStartDateChange"/>
                    </view>
                    <!-- <view class="common-flex-x" style="margin-top: 20upx;">
                        <text class="left">结束日期：</text>
                        <uni-datetime-picker type="date" :value="searchEndDate" />
                    </view> -->
                    <!-- <view class="common-flex-x" style="margin-top: 40upx;">
                        <text class="left">所属中心：</text>
                        <picker @change="searchLocationChange" :value="searchLocationIndex" :range="searchLocationArray" class="right">
                            <view class="uni-input">{{searchLocationArray[searchLocationIndex]}}</view>
                        </picker>
                    </view> -->
                    <button type="default" @click="clearSearch" class="sesarch-confirm-btn">清空</button>
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
                searchStartDate: '',
                searchEndDate: '',
                searchLocationArray: [],
                searchLocationIndex: 0,
				showRight: false,
				pageNum: 1,
				pageSize: 10,
				totalCount: 0,
				kongshuList:[],
                searchWidth: 280
			};
		},
		
		onLoad(options){
			this.reload_list()
		},
		
		watch:{

		},
		onReachBottom() {
			let totalPage = this.totalCount / this.pageSize
			
			if(this.pageNum < totalPage){
				this.pageNum++
				this.load_list();
			}
		}, 
		onPullDownRefresh() {

		},
        mounted() {
          var _this = this
          uni.getSystemInfo({
            success: function (res) {
              _this.searchWidth = res.windowWidth * 0.7
              console.log("_this.searchWidth = " + _this.searchWidth)
            }
          })  
        },
  		//点击导航栏 buttons 时触发
  		onNavigationBarButtonTap(e) {
  			console.log(e);
  			const index = e.index;
  			if (index === 0) {
                // 筛选
                this.showDrawer('showRight')
  			}
  		},
		methods: {
			searchStartDateChange(newvalue){
				console.log('newvalue = ' + newvalue)
				this.searchStartDate = newvalue
			},
			getStatusName(status) {
				if(status == '1'){
					return '已提交'
				}
				return '未提交'
			},
            cornformSearch() {
                this.initList();
				this.closeDrawer('showRight')
            },
			clearSearch(){
				this.searchStartDate = ''
				this.initList();
				this.closeDrawer('showRight')
			},
			initList(){
				this.pageNum = 1
				this.kongshuList=[];
				this.load_list();
			},
            searchLocationChange: function(e) {
                console.log('picker发送选择改变，携带值为', e.target.value)
                this.searchLocationIndex = e.target.value
            },
			// 打开窗口
			showDrawer(e) {
				this.$refs[e].open()
			},
			// 关闭窗口
			closeDrawer(e) {
				this.$refs[e].close()
			},
			reload_list(){
				this.kongshuList=[];
				this.load_list();
			},
			load_list(){
				/**
				 * 提交参数：
						pageNum: 1
						pageSize: 10
						deptId: 040601
						status: 0
						submit_status: 1
						optDate: 2021-08-16 
				 **/
				let vm = this
				let req = {
					pageNum: vm.pageNum,
					pageSize: vm.pageSize,
					status: 0,
					submit_status: 0
				}
				
				if(vm.searchStartDate){
					req.optDate = vm.searchStartDate
				}
				
				let url = '/shop/price/list'
				vm.sendRequest({'url': url, 'method': 'POST', 'data': req}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							for(let i = 0; i < resp.data.rows.length; i++){
								vm.kongshuList.push(resp.data.rows[i])
							}
						}
					}
					// console.log('vm.kongshuList = ' + JSON.stringify(vm.kongshuList))
					vm.totalCount = resp.data.total
				}, function(error){
					vm.$api.msg('获取列表失败 : ' + error.data.msg)
				})
			},
            setCheck(item){
				let vm = this
				let req = {
					priceId: item.settingId
				}
				
				let url = '/price/detail/editStatus'
				vm.sendRequest({'url': url, 'method': 'POST', 'data': req}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						vm.initList()
					}else{
                        vm.$api.msg(error.data.msg)
                    }
				}, function(error){
					vm.$api.msg('修改失败 : ' + error.data.msg)
				})
			}
            // toDetail(item){
            //     this.$navigateTo("/pages/kongjia/detail?priceid=" + item.settingId);
            // }
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
