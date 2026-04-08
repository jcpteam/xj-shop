<template>
	<view class="container " style="width: 100%;">
		<view class="list-cell b-b "  hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">姓名：</text>
			<input class="input" type="text" style="text-align: right;" v-model="userInfo.userName"  placeholder="" placeholder-class="placeholder" disabled="false"/>
		</view>
		<view class="list-cell b-b "  hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">手机：</text>
			<input class="input" type="text" style="text-align: right;" v-model="userInfo.phonenumber"  placeholder="" placeholder-class="placeholder" disabled="false" />
		</view>
		<view class="list-cell b-b "  hover-class="cell-hover" :hover-stay-time="50">
			<text class="cell-tit">所属中心：</text>
			<input class="input" type="text" style="text-align: right;" v-model="userInfo.deptName"  placeholder="" placeholder-class="placeholder" disabled="false" />
		</view>
		<!-- <picker  :value="parseInt(user.gender||0)" :range="getConst().pickerObj.gender.list" @change="bindPickerChange">
			<view class="list-cell b-b "  hover-class="cell-hover" :hover-stay-time="50" >		
				<text class="cell-tit">性别：</text>
			    <view class="uni-input">{{getConst().pickerObj.gender.map[parseInt(user.gender||0)].name}}</view>
		    </view>
		 </picker> -->

		<!-- <button class="list-cell log-out-btn center90" style="background-color: #4A94FF; color: #ffffff;" @click="confirm()">
			<text class="cell-tit" style="color: #ffffff;">保存</text>
		</button> -->

	</view>
</template>

<script>
import menu from '../mixins/menu.js'
	 
	export default {
		mixins: [menu],
		components: {
		    
		    
		},
		data() {

			return {
				userInfo: {}
			};
		},
		onLoad(options) {			
		},
		onShow() {
			let userInfo = this.getUserInfo()
			if(userInfo){
				this.userInfo = userInfo;
			}
		},
		
		methods:{
			bindPickerChange(e){
				
				this.user.gender=e.detail.value;
				this.user=JSON.parse(JSON.stringify(this.user));
				console.log(this.user);
			},
			confirm(){
				var o=this.user;			
				let _this=this;
				this.$post("user/update",o,function(res){
					_this.setUser(res.data);
					setTimeout(()=>{
						uni.navigateBack()
					}, 800)
				})			
			},
		}
	}
</script>

<style lang='scss'> 
	
	page{
		overflow-x: hidden;
		background: #ffffff;
	}
	
		.row{
		display: flex;
		align-items: center;
		position: relative;
		padding:0 30upx;
		height: 110upx;
		background: #fff;
		
		.tit{
			flex-shrink: 0;
			width: 120upx;
			font-size: 30upx;
			color: #303133;
		}
		.input{
			flex: 1;
			font-size: 30upx;
			color: #303133;
		}
		.icon-shouhuodizhi{
			font-size: 36upx;
			color: #909399;
		}
	}
	.list-cell{
		display:flex;
		align-items:baseline;
		padding: 20upx 0.9rem;
		line-height:60upx;
		position:relative;
		background: #fff;
		justify-content: center;
		&.log-out-btn{
			margin-top: 40upx;
			.cell-tit{
				color: $uni-color-primary;
				text-align: center;
				margin-right: 0;
			}
		}
		&.cell-hover{
			background:#fafafa;
		}
		&.b-b:after{
			left: 30upx;
		}
		&.m-t{
			margin-top: 16upx; 
		}
		.cell-more{
			align-self: baseline;
			font-size:0.8rem;
			color:#909399;
			margin-left:10upx;
		}
		.cell-tit{
			flex: 1;
			font-size: 0.72rem;
			color: #303133;
			margin-right:10upx;
		}
		.cell-tip{
			font-size: 0.9rem;
			color: #909399;
		}
		switch{
			transform: translateX(16upx) scale(.84);
		}
	}
</style>
