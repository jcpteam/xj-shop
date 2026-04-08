<template>
	<view class="page">
      <!-- 菜单列表 start -->  
	  <view class="center98 flex wrap" style="padding-bottom: 20upx; margin-top: 60upx;">
            <view class="block-head-line top-title">
				<view >工作台</view>
				<view class="top-message" @click="goMessageList()">消息</view>
			</view>
		  <view class="w25p flex-col m-center c-center" 
            v-for="(item,index) in menuList" :key="index"
            style="'height: 5rem; color: #000; margin-top: 40upx;" @click="toMenu(item)" 
			v-if="!item.rightparam || hasRight(item.rightparam)">		 
            <view>
                <image src="/static/img/default.png" style="width: 60px; height: 60px; margin-left: auto; margin-right: auto;" />
            </view>
			<text class="w80p h30p  flex m-center c555" style="margin-top: 4upx; font-size: 14px; ">{{item.name}}</text>	
		  </view>		  
	  </view>
      <!-- 菜单列表 end -->  
      <view class="blank-line">&nbsp;</view>
	  <!-- 柱状图 -->
      <view class="block-head-line">大数据分析</view>
	  <view class="qiun-charts" >
		<canvas canvas-id="canvasColumn" 
			id="canvasColumn" 
			class="charts" 
			disable-scroll=true 
			@touchstart="touchColumn"
			@touchmove="moveColumn" 
			@touchend="touchEndColumn"></canvas>
	  </view>
	  
	  <!-- 饼图start -->
      <view class="blank-line">&nbsp;</view>
      <view class="block-head-line">大数据分析</view>
	  <view class="qiun-charts" >
			<canvas 
				canvas-id="canvasPie" 
				id="canvasPie" 
				class="charts" 
				@touchstart="touchPie"></canvas>
	  </view>
	  <!-- 饼图end -->
	</view> 
</template>

<script> 
import uCharts from '@/components/u-charts/u-charts.js';
import  { isJSON } from '@/common/checker.js';
import menu from '../mixins/menu.js'
  
var _self;
var canvaColumn=null;
var canvaPie=null;
	
export default {  
	components:{   
		 
	},
	mixins: [menu],
	data() {
		return {
			menuColors: ['#25BD97', '#2451FF', '#3BB4ED', '#B35AFF', '#001779', '#0A49A4', '#050B21', '#25BD97', '#2451FF', '#3BB4ED', '#B35AFF', '#001779', '#0A49A4', '#050B21'],
			menuList:[
				{
					id: 0,
					name: '订单审核',
					path: '/pages/xjshop/tabs/order/order',
                    rightparam: 'menu-order'
				}, {
					id: 1,
					name: '控数设置',
					path: '/pages/kongshu/index',
                    rightparam: 'menu-kongshu'
				}, {
					id: 2,
					name: '控价设置',
					path: '/pages/kongjia/index',
                    rightparam: 'menu-kongjia'
				}, {
					id: 3,
					name: '代下单',
					path: '/pages/clienter/index',
                    rightparam: 'menu-daixiadan'
				}, {
					id: 4,
					name: '统计分析',
					path: '/pages/statistics/index'
				}, {
					id: 5,
					name: '入库审核',
					path: '/pages/rukudan/index',
                    rightparam: 'menu-rukuguanli'
				}, {
					id: 6,
					name: '控价审核',
					path: '/pages/kongjia/index?type=1',
                    rightparam: 'menu-kongjiashenhe'
				}
			],
			cWidth:'',
			cHeight:'',
			pixelRatio:1
		};
	},
	onShow() {
	},
	onLoad() {
		_self = this
		this.cWidth=uni.upx2px(750);
		this.cHeight=uni.upx2px(500);
		this.getServerData();
	},
	methods: {
		toMenu(menuItem){
			if(menuItem.path){
				this.$navigateTo(menuItem.path);
			}
		},
		getServerData(){
			// 初始化柱状图
			let LineA0 = {
			  "categories": ["2012", "2013", "2014", "2015", "2016", "2017"],
			  "series": [{
				"name": "成交量A",
				"data": [35, 8, 25, 37, 4, 20]
			  }, {
				"name": "成交量B",
				"data": [70, 40, 65, 100, 44, 68]
			  }, {
				"name": "成交量C",
				"data": [100, 80, 95, 150, 112, 132]
			  }]
			}
			let Column={categories:[],series:[]};
			//这里我后台返回的是数组，所以用等于，如果您后台返回的是单条数据，需要push进去
			Column.categories=LineA0.categories;
			Column.series=LineA0.series;
			_self.showColumn("canvasColumn",Column);
			
			// 初始化饼图
			let Pie={series:[]};
			//这里我后台返回的是数组，所以用等于，如果您后台返回的是单条数据，需要push进去
			let pieSeries = [{"name": "中心一",
								"data": 50
							  }, {"name": "中心2",
								"data": 30
							  }, {"name": "中心3",
								"data": 20
							  }, {"name": "中心4",
								"data": 18
							  }, {"name": "中心5",
								"data": 8
							  }]
			Pie.series = pieSeries;
			_self.showPie("canvasPie",Pie);
		},
		showColumn(canvasId,chartData){
			canvaColumn=new uCharts({
				$this:_self,
				canvasId: canvasId,
				type: 'column',
				fontSize:11,
				padding:[5,15,15,15],
				legend:{
					show:true,
					position:'top',
					float:'center',
					itemGap:30,
					padding:5,
					margin:5,
					//backgroundColor:'rgba(41,198,90,0.2)',
					//borderColor :'rgba(41,198,90,0.5)',
					borderWidth :1
				},
				dataLabel:true,
				dataPointShape:true,
				background:'#FFFFFF',
				pixelRatio:_self.pixelRatio,
				categories: chartData.categories,
				series: chartData.series,
				animation: true,
				enableScroll: true,
				xAxis: {
					disableGrid:false,
					type:'grid',
					gridType:'dash',
					itemCount:4,
					scrollShow:true,
					scrollAlign:'left',
				},
				yAxis: {
					//disabled:true
					gridType:'dash',
					splitNumber:4,
					min:10,
					max:180,
					format:(val)=>{return val.toFixed(0)+'元'}//如不写此方法，Y轴刻度默认保留两位小数
				},
				width: _self.cWidth*_self.pixelRatio,
				height: _self.cHeight*_self.pixelRatio,
				extra: {
					column: {
						type:'group',
						width: _self.cWidth*_self.pixelRatio*0.45/chartData.categories.length
					}
				},
			});
		},
		showPie(canvasId,chartData){
			canvaPie=new uCharts({
				$this:_self,
				canvasId: canvasId,
				type: 'pie',
				fontSize:11,
				padding:[15,15,0,15],
				legend:{
					show:true,
					padding:5,
					lineHeight:11,
					margin:0,
				},
				background:'#FFFFFF',
				pixelRatio:_self.pixelRatio,
				series: chartData.series,
				animation: true,
				width: _self.cWidth*_self.pixelRatio,
				height: _self.cHeight*_self.pixelRatio,
				dataLabel: true,
				extra: {
					pie: {
		  border:true,
		  borderColor:'#FFFFFF',
		  borderWidth:3
					}
				},
			});
		},
		touchColumn(e){
			canvaColumn.scrollStart(e);
		},
		moveColumn(e) {
			canvaColumn.scroll(e);
		},
		touchEndColumn(e) {
			canvaColumn.scrollEnd(e);
			canvaColumn.touchLegend(e, {
				animation:true,
			});
			canvaColumn.showToolTip(e, {
				format: function (item, category) {
					return category + ' ' + item.name + ':' + item.data 
				}
			});
		},
		touchPie(e){
			canvaPie.showToolTip(e, {
				format: function (item) {
					return item.name + ':' + item.data 
				}
			});
			canvaPie.touchLegend(e,{animation:true});
		},
		goMessageList(){
			this.$jump('/pages/index/message');
		},
	}
}
</script>

<style lang="scss" scoped>
	.charts {
		width: 100%;
		height: 500upx;
		background-color: #FFFFFF;
	}
	.qiun-charts {
		margin: 15px;
		overflow: hidden;
	}
    .block-head-line {
        width: 100%; border-bottom: solid 2px #E9E9E9;
        height: 60upx;
        padding: 12upx 20upx;
    }
	.top-title{
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.top-message{
		font-size: 28upx;
		color:#dd524d;
	}
    .blank-line {
        height: 30upx;
        background-color: #F3F2F8;
    }
</style>
