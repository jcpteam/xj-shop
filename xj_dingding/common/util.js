const root = 'https://zhuangxiu.mzyun.tech/api' //API根路径
//简化uni.request
const get = (url, data = '', method = 'GET') => {
	return new Promise(resolve => {
		uni.request({
			url: root + url,
			method: method,
			data: data,
			success(res) {
				resolve(res.data)
			},
			fail(res) {
				console.log(res.data)
			}
		})
	})
}
//获取上一页实例
const prePage = () => {
	let pages = getCurrentPages();
	let prePage = pages[pages.length - 2];
	// #ifdef H5
	return prePage;
	// #endif
	return prePage.$vm;
}
//简化uni.showtoast
const msg = (title, duration = 1500, mask = false, icon = 'none') => {
	//统一提示方便全局修改
	if (Boolean(title) === false) {
		return;
	}
	uni.showToast({
		title,
		duration,
		mask,
		icon
	});
}
//简化uni.navigateTo
const jump = (url) => {
	uni.navigateTo({
		url: url
	})
}
//判断是否登录，否则跳转到登录页
const isLogin = (hasLogin) => {
	if (!hasLogin) {
		uni.showModal({
			content: '请先登陆',
			confirmText: '去登陆',
			success(res) {
				res.confirm && uni.navigateTo({
					url: '/pages/login/login'
				})
			}
		})
		return;
	}
}
//格式化时间
const formatTime = (timeStamp) => {
	let currentTime = Date.parse(new Date()) / 1000;
	if (timeStamp > currentTime) {
		let time = timeStamp - currentTime;
		let day = parseInt(time / 86400);
		time = time % 86400;
		let hour = parseInt(time / 3600);
		time = time % 3600;
		let minute = parseInt(time / 60);
		time = time % 60;
		let second = time;
		return {
			day,
			hour,
			minute,
			second
		}
	} else {
		console.log('error')
	}

}
const rad = e => {
	let PI = Math.PI;
	return e * PI / 180
}

const getDistance = (lat1, lng1, lat2, lng2) => {
	let radLat1 = rad(lat1);
	let radLat2 = rad(lat2);
	let a = radLat1 - radLat2;
	let b = rad(lng1) - rad(lng2);
	let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(
		Math.sin(
			b / 2), 2)));
	s = s * 6378.137;
	// EARTH_RADIUS;
	s = Math.round(s * 10000) / 10000;
	return s;
};

const getTotalPageNo = (total, perPageSize) => {
	let totalPage = 0;
	if (total > 0 && perPageSize > 0) {
		if (total % perPageSize != 0) {
			totalPage = parseInt(total / perPageSize) + 1;
		} else {
			totalPage = parseInt(total / perPageSize);
		}
	}
	return totalPage;
}

// 传入的时间大于当前时间 返回true  否则返回false
const compareCurTime = (time) => {
	console.log('compareCurTime--->time:',time);
	var date = new Date();//实例一个时间对象；
	var hour = date.getHours();//获取系统时间
	var minute = date.getMinutes(); //分
	var second = date.getSeconds();//秒
	var curTime = hour +':' + minute + ':' + second;
	console.log('compareCurTime--->curTime:',curTime);
	if (time_to_sec(time) - time_to_sec(curTime) > 0) {
		console.log('compareCurTime--->ret: true');
		return true;
	}
	console.log('compareCurTime--->ret: false');
	return false;
}

//将时分秒转为时间戳
const time_to_sec = (time) => {
	if (time !== null) {
		var s = "";
		var hour = time.split(":")[0];
		var min = time.split(":")[1];
		var sec = time.split(":")[2];
		s = Number(hour * 3600) + Number(min * 60) + Number(sec);
		return s;
	}
}

export default {
	root,
	get,
	prePage,
	msg,
	jump,
	isLogin,
	formatTime,
	getDistance,
	getTotalPageNo,
	compareCurTime,
}
