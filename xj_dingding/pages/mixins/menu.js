export default {
  data() {
    return {
    }
  },

  methods: {
	clearSession(){
		uni.setStorageSync('sessiontoken', null);
		uni.setStorageSync('userInfo', null);
		
		// 菜单初始化
		uni.setStorageSync('menu-order', 0);
		uni.setStorageSync('menu-kongshu', 0);
		uni.setStorageSync('menu-kongjia', 0);
		uni.setStorageSync('menu-ordercheck', 0);
		uni.setStorageSync('menu-rukuguanli', 0);
		uni.setStorageSync('menu-kongjiashenhe', 0);
		uni.setStorageSync('menu-kehuliebiao', 0);
		
	},
	clearMenuStatus(){
		// 菜单初始化
		uni.setStorageSync('menu-order', 0);
		uni.setStorageSync('menu-kongshu', 0);
		uni.setStorageSync('menu-kongjia', 0);
		uni.setStorageSync('menu-ordercheck', 0);
		uni.setStorageSync('menu-rukuguanli', 0);
		uni.setStorageSync('menu-kongjiashenhe', 0);
		uni.setStorageSync('menu-kehuliebiao', 0);
	},
	getUserInfo(){
		let userInfo = uni.getStorageSync('userInfo')
		
		return userInfo
	},
    // 检测是否有权限
    hasRight(menuParam) {
      let result = uni.getStorageSync('menu-order');

	  if (result === 1 || result === '1'){
		  return true
	  }
	  return false
    }
  }
}
