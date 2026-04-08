<template>
	<view class="page">
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
  
var _self;
var canvaColumn=null;
var canvaPie=null;
	
export default {  
	components:{   
		 
	},
	data() {
		return {
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
		}
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
    .blank-line {
        height: 30upx;
        background-color: #F3F2F8;
    }
</style>
