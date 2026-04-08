<template>
	<view class="content">
		
		<!-- 
		 quotationName : "石门报价单"  --所属报价单   unitIds ==mainUnitId ？mainUnitName : subUnitName  单位  saleNum : 12  控数数量
		 salePercent : 60  控数比例
		 deptName : "石门大客户部"  所属单位
		 -->
		<empty v-if="!kongshuList"></empty>
		<!-- 控数列表 -->
        <view v-else>
            <view 
                v-for="(item,index) in kongshuList" :key="index"
                class="order-item"
            >
				<view style="position: relative;">
					<text class="gray_txt">所属中心：</text>
					<text class="value">{{item.deptName}}</text>
					<text class="classifyName">{{item.classifyName}}</text>
				</view>
				<view>
					<text class="gray_txt">所属报价单：</text>
					<text class="value">{{item.quotationName}}</text>
				</view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">控数单位：</text>
                        <text class="value">{{item.unitIds === item.mainUnitId ? item.mainUnitName : item.subUnitName}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt">数量比例 ：</text>
                        <text class="value">{{item.salePercent}}</text>
                    </text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">控数数量：</text>
                        <text class="value">{{item.saleNum}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt"></text>
                        <text class="value"></text>
                    </text>
                </view>
                <view class="common-flex-x">
                    <button type="default" @click="openUpdateDialog(item)" class="common-normal-btn">修改控数</button>
                </view>
            </view>
        </view>
		<view style="margin-top: 50upx;height: 100upx;">&nbsp;</view>
		<uni-popup ref="popup" type="dialog">
		    <div style="width: 80%; margin-left: auto; margin-right: auto; margin-top: 100px; background: #fff; padding: 30px; line-height: 30px;">
				数量比例： <input type="text" v-model="setsalePercent" style="height: 30px; line-height: 30px; border: solid 1px #ddd; width: 100px; display: inline-block; padding: 0 15px;"/> %&nbsp;&nbsp;
				<br><br>
                <button type="default" @click="updateKongshushuliang" class="common-normal-btn">修改</button>
			</div>
		</uni-popup>
	</view>
</template> 

<script>
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
import empty from "@/components/empty/empty"; 
	
	export default {
		components: {
			uniLoadMore,
			empty
		},
		data() {
			return {
				kongshuList:[],
				settingId: 0,
				spuNo: '',
				deptId: 0,
				createTime: null,
				goodsId: 0,
				setkongshushuliang: 0,
				setsalePercent: 0,
				settingId: 0
			};
		},
		
		onLoad(options){
			if(options.settingId){
				this.settingId = options.settingId
			}
			if(options.spuNo){
				this.spuNo = options.spuNo
			}
			if(options.deptId){
				this.deptId = options.deptId
			}
			if(options.createTime){
				this.createTime = options.createTime
			}
			
			this.reload_list()
		},
		
		watch:{

		},
		onReachBottom() {
			// this.load_list();
		}, 
		onPullDownRefresh() {

		},
		methods: {
			openUpdateDialog(item) {
				this.setkongshushuliang = item.saleNum
				this.setsalePercent = item.salePercent
				this.goodsId = item.goodsId
				
				this.$refs.popup.open()
			},
			closeUpdateDialog() {
				this.$refs.popup.close()
			},
			updateKongshushuliang(){
				/**
				 * /shop/sale/edit  修改详情里面的控数的  json
{"settingId":1220,"goodsList":[{"goodsId":52,"salePercent":"60","settingId":1220},{"goodsId":53,"salePercent":30,"settingId":1220},{"goodsId":57,"salePercent":5,"settingId":1220},{"goodsId":94,"salePercent":"1","settingId":1220}]} 
				 **/
				let vm = this
				//  遍历各个商品比例，总比例不能超过 100
				if (vm.setsalePercent > 100) {
					vm.$api.msg('控数总比例不能超过100!')
					return
				}
				
				let total = parseInt(vm.setsalePercent)
				let goodsList = []
				goodsList.push({"goodsId":vm.goodsId,"salePercent": "" + vm.setsalePercent,"settingId": vm.settingId})
				for(let i=0; i<vm.kongshuList.length; i++){
					if(vm.goodsId !== vm.kongshuList[i]['goodsId']){
						total += parseInt(vm.kongshuList[i]['salePercent'])
						goodsList.push({"goodsId": vm.kongshuList[i]['goodsId'],"salePercent": "" + vm.kongshuList[i]['salePercent'],"settingId": vm.settingId})
					}
				}
				if(total > 100){
					vm.$api.msg('控数总比例不能超过100!')
					return
				}
				let req = {
					settingId: vm.settingId,
					goodsList: goodsList
				}
				let url = '/shop/sale/edit'
				vm.sendRequest({'url': url, 'method': 'POST', data: req}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
                        vm.closeUpdateDialog()
						vm.reload_list()
					}
				}, function(error){
					vm.$api.msg('修改失败 : ' + error.data.msg)
				})
			},
			reload_list(){
				this.kongshuList=[];
				this.load_list();
			},
			load_list(){
				this.kongshuList = []
				let vm = this
				// let req = {
				// 	'settingId': vm.settingId,
				// 	'spuNo': vm.spuNo
				// }
				
				let url = '/shop/sale/quotationGoods?pageNum=1&pageSize=100&spuNo=' + vm.spuNo + '&deptId=' + vm.deptId + '&createTime=' + vm.createTime
				vm.sendRequest({'url': url, 'method': 'POST'}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						vm.kongshuList = resp.data.rows
					}
					// console.log('vm.kongshuList = ' + JSON.stringify(vm.kongshuList))
					vm.totalCount = resp.data.total
				}, function(error){
					vm.$api.msg('获取列表失败 : ' + error.data.msg)
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
		font-size: 32upx;
		line-height: 50upx;
		
        .title {
            color: #4A4B50;
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
    .common-normal-btn {
        margin-top: 30upx;
        background: #4A94FF;
        background-color: #4A94FF;
        color: #fff;
    }
	.classifyName {
		position: absolute;
		right: -15px;
		top: -10px;
		display: block;
		background: #1AAD19;
		color: #fff;
		padding: 0px 1px;
		font-size: 8px;
	}
</style>
