<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/* 黑粗字体 灰度1 灰度2 灰度3 底线 全屏遮罩 根节点样式 底部占位 边框阴影 四周边框 单行省略 多行省略 主色调 字体1 字体2 字体3 字体4 字体5 */
	
	@import url("./static/font/iconfont.css");
	@import url("./static/css/common/box.css");
	@import url("./static/css/common/common.css"); 
	@import url("./static/css/common/custom.css"); 
    
    
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
</style>
