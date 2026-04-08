var CONFIG={
	host:"http://localhost:8086/", 
	host:"http://mall.51gang.top/api/",
	projectName:"xjapp/#/",
	WS:"ws://192.168.1.6:8090/websocket/", 
}
CONFIG.API=CONFIG.host; //config+"api"

CONFIG.uploadUrl=CONFIG.API+"attach/upload";
//CONFIG.verifyUrl=CONFIG.API+"open/verify";

CONFIG.defaultAvatar="http://ydytxx.oss-cn-beijing.aliyuncs.com/avatar.jpg";
CONFIG.defaultImg="http://ydytxx.oss-cn-beijing.aliyuncs.com/avatar.jpg";

CONFIG.localKey={
	"show_img_param":"show_img_param" ,//show-img页面传值
	"page_url":"page_url",             //web-page页面传值
	
	
	"user_info":"user_info",
	"token":"token",
	"password":"password",
	
	"pre_reg_page":"pre_reg_page",           //跳转到注册页面之前的非登陆页面
	"pre_guider_page":"pre_guider_page",
	"wxreg_param":"wxreg_param",  //xjapp注册参数
	"wxapp_refresh_stat":"wxapp_refresh_stat",
	"xjapp_refresh_stat":"xjapp_refresh_stat",
	"wxmp_refresh_stat":"wxmp_refresh_stat",
	
	/* ============================$dataLocal */
	
	"launchFlag":"launchFlag",  //guider页面
	"is_agree":"is_agree",  //注册隐私政策页面   
	
}



CONFIG.pickerObj={
	"Educateion":{
		"list":["幼儿","小学","中学","大学","暂无"]
	},
	
	"gender":{
		"list":["男","女"],"map":{ 0:{"name":"男","code":0}, 1:{"name":"女","code":1} }
	}
	
}



export default CONFIG;