<template>
	<view class="content">
		
		<!--
		 {
			 "settingId":2405,  //控价详情ID，也就是每个销售商品每天具体控价ID
			 "priceId":538,//控价单ID，对应控价单的settingID
			 "quotationId":19,//报价单ID
			 "spuNo":"050401010001",//SPU编码
			 "spuId":null,
			 "goodsId":"90",//销售商品ID
			 "deptId":"040601",//所属中心ID
			 "spuName":"土著黄公鸡（未开膛）",//SPUm名称
			 "deptName":"长沙直营部", //部门名称
			 "propertyName":"土著黄公鸡（公斤）",//商品销售名称
			 "quotationName":"长沙测试报价单",//报价单名称
			 "unitName":"公斤", //结算单位
			 "unitSellName":"公斤",//销售单位
			 "sellPrice":12,//销售价格
			 "subCaseMain":1,//销售单位/结算单位的壁纸
			 "unitId":"1", //结算单位ID
			 "price":12,//结算价格
			 "settingDate":"2021-08-16",//设置日期
			 "status":null,
			 "lastModifyTime":null,
			 "goodsIdList":null
		 }
		 -->
		<empty v-if="!datalist"></empty>
		<!-- 控价列表 -->
        <view v-else>
            <view 
                v-for="(item,index) in datalist" :key="index"
                class="order-item"
            >

                <view>
                    <text class="gray_txt">商品名称：</text>
                    <text class="value">{{item.propertyName}}</text>
                </view>
                <view>
                        <text class="gray_txt">SPU名称：</text>
                        <text class="value">{{item.spuName}}</text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">所属中心：</text>
                        <text class="value">{{item.deptName}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt"></text>
                        <text class="value"></text>
                    </text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">结算单位：</text>
                        <text class="value">{{item.unitName}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt">结算价格 ：</text>
                        <text class="value">{{item.price}}</text>
                    </text>
                </view>
                <view class="common-flex-x">
                    <text class="left">
                        <text class="gray_txt">销售单位：</text>
                        <text class="value">{{item.unitSellName}}</text>
                    </text>
                    <text class="right">
                        <text class="gray_txt">销售价格 ：</text>
                        <text class="value">{{item.sellPrice}}</text>
                    </text>
                </view>
                
                <view class="common-flex-x">
                    <button type="default" @click="openUpdateDialog(item)" class="common-normal-btn">修改结算价格</button>
                </view>
            </view>
        </view>
		<view style="margin-top: 50upx;height: 100upx;">&nbsp;</view>
		
		<uni-popup ref="popup" type="dialog">
		    <div style="width: 80%; margin-left: auto; margin-right: auto; margin-top: 100px; background: #fff; padding: 30px; line-height: 30px;">
				价格： <input type="text" v-model="setprice" style="height: 30px; line-height: 30px; border: solid 1px #ddd; width: 100px; display: inline-block; padding: 0 15px;"/> &nbsp;&nbsp;元
				<br><br>
                <button type="default" @click="updatePrice" class="common-normal-btn">修改</button>
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
				priceId: 0,
				datalist:[],
				setprice: '',
				settingId: 0,
				priceId: 0
			};
		},
		
		onLoad(options){
			console.log('options.priceid = ' + options.priceid)
			if(options.priceid){
				this.priceId = options.priceid
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
				this.setprice = item.price
				this.settingId = item.settingId
				this.priceId = item.priceId
				this.$refs.popup.open()
			},
			closeUpdateDialog() {
				this.$refs.popup.close()
			},
			reload_list(){
				this.datalist=[];
				this.load_list();
			},
			load_list(){
				let vm = this
				let req = {
					pageNum: 1,
					pageSize: 100,
					priceId: parseInt(vm.priceId)
				}
				
				let url = '/price/detail/list'
				vm.sendRequest({'url': url, 'method': 'POST', 'contentType': 'application/x-www-form-urlencoded', 'data': req}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
						if(resp.data.rows){
							vm.datalist = resp.data.rows
						}
					}
				}, function(error){
					vm.$api.msg('获取列表失败 : ' + error.data.msg)
				})
			},
			updatePrice(){
				let vm = this
				let req = {
					settingId: vm.settingId,
					priceId: vm.priceId,
					price: vm.setprice
				}
				
				let url = '/price/detail/edit'
				vm.sendRequest({'url': url, 'method': 'POST', 'data': req}, function(resp){
					if (resp.data.code === 0 || resp.data.code === '0') {
                        vm.closeUpdateDialog()
						vm.reload_list()
					}else{
                        vm.closeUpdateDialog()
                        vm.$api.msg(error.data.msg)
                    }
				}, function(error){
					vm.$api.msg('修改失败 : ' + error.data.msg)
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
        width: 240upx;
    }
</style>
