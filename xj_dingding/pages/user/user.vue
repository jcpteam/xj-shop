<template>
	<view class="user">
		<!-- 头部 -->
		<view class="user-wrap">
			<!-- <view class="setting iconfont icon31shezhi"></view> -->
			<view class="info">
				<image class="avatar" mode="aspectFill" :src="getConst().defaultAvatar"></image>
				<view class="nickname">{{userInfo.userName || ''}}
					<picker @change="roleChange" :value="roleIndex" :range="roleArray" class="right">
						<text style="color: red;">{{userInfo.deptName + '_' + userInfo.roleName || ''}}</text>
					</picker>
				</view>

			</view>
		</view>


		<!-- 用户 -->
		<view class="com-item">
			<view class="com-wrap">
				<view class="cell" @click="$navigateTo('/pages/user/set')">
					<view class="cell-left">
						<image class="cell-icon" src="/static/img/user/icon-kefu.png" mode="aspectFill"></image>
						<view class="cell-text">个人信息</view>
					</view>
					<view class="iconfont iconmore1"></view>
				</view>
				<view class="cell" @click="logout">
					<view class="cell-left">
						<image class="cell-icon" src="/static/img/user/icon-kefu.png" mode="aspectFill"></image>
						<view class="cell-text">退出登录</view>
					</view>
					<view class="iconfont iconmore1"></view>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	/**
 * /system/user/profile/changeRoleAuthForApp  获取多个角色的，加个个接口
system/user/changeUserRoleAuth  {userId:userId,roleAuthId:roleAuthId}  切换角色 

{"total":4,"rows":[{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":23,"userId":"admin_gz","roleId":10001,"deptId":"040701","isDefault":"0","loginName":"admin_gz","userName":"admin_gz","deptName":"广州业务区","roleName":"区域经理"},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":26,"userId":"admin_gz","roleId":10006,"deptId":"040701","isDefault":"0","loginName":"admin_gz","userName":"admin_gz","deptName":"广州业务区","roleName":"系统管理员"},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":27,"userId":"admin_gz","roleId":10004,"deptId":"040701","isDefault":"0","loginName":"admin_gz","userName":"admin_gz","deptName":"广州业务区","roleName":"仓库主管"},{"searchValue":null,"createBy":null,"createTime":null,"updateBy":null,"updateTime":null,"remark":null,"params":{},"id":30,"userId":"admin_gz","roleId":10000,"deptId":"040701","isDefault":"0","loginName":"admin_gz","userName":"admin_gz","deptName":"广州业务区","roleName":"业务经理"}],"code":0,"msg":0}    
roleAuthId对应json里面的id 
 ***/
	import menu from '../mixins/menu.js'
	export default {
		mixins: [menu],
		data() {
			return {
				userInfo: {},

				roleIndex: 0, //当前选中的角色index
				roleArray: [], //角色名称显示array
				roleIdArray: [] //角色id列表array
			};
		},
		methods: {
			initMenu(successFunc) {
				// 初始化菜单权限
				// 个人权限菜单  /system/user/profile/menu/{userId}

				/* 
				订单审核 shop:order:edit    menu-order
				控数设置 shop:sale:edit     menu-kongshu
				控价管理 shop:price:list    menu-kongjia
				待下单  shop:order:add      menu-daixiadan 
				订单审核  shop:stock:view   menu-ordercheck
				入库管理  shop:stock:list   menu-rukuguanli
				控价审核 shop:price:audit   menu-kongjiashenhe
				客户列表  shop:member:view  menu-kehuliebiao
				*/

				let vm = this
				let userInfo = uni.getStorageSync('userInfo');
				if (userInfo && userInfo['userId']) {
					let url = '/system/user/profile/menu/' + userInfo['userId']
					vm.sendRequest({
						'url': url,
						'data': null,
						'method': 'POST'
					}, function(resp) {
						console.log('获取权限菜单 resp = ' + JSON.stringify(resp.data))

						if (resp.data) {
							vm.clearMenuStatus()
							let item
							for (let i = 0; i < resp.data.length; i++) {
								item = resp.data[i]
								if (item['perms'] === 'shop:order:edit') {
									uni.setStorageSync('menu-order', 1);
								} else if (item['perms'] === 'shop:sale:edit') {
									uni.setStorageSync('menu-kongshu', 1);
								} else if (item['perms'] === 'shop:price:list') {
									uni.setStorageSync('menu-kongjia', 1);
								} else if (item['perms'] === 'shop:stock:view') {
									uni.setStorageSync('menu-ordercheck', 1);
								} else if (item['perms'] === 'shop:stock:list') {
									uni.setStorageSync('menu-rukuguanli', 1);
								} else if (item['perms'] === 'shop:price:audit') {
									uni.setStorageSync('menu-kongjiashenhe', 1);
								} else if (item['perms'] === 'shop:member:view') {
									uni.setStorageSync('menu-kehuliebiao', 1);
								} else if (item['perms'] === 'shop:order:add') {
									uni.setStorageSync('menu-daixiadan', 1);
								}
							}

							if (successFunc) {
								successFunc();
							}
						}
					}, function(error) {
						vm.clearSession()
						vm.$api.msg('获取权限失败 : ' + JSON.stringify(error))
					})
				} else {
					vm.clearSession()
					vm.$api.msg('获取权限失败 : ' + resp.data.msg)
				}
			},
			roleChange: function(e) {
				console.log('picker发送选择改变 ', e.target)
				console.log('picker发送选择改变，携带值为', e.target.value)
				console.log('picker发送选择改变,roleId--> ', this.roleIdArray[e.target.value]);

				let userInfo = uni.getStorageSync('userInfo');

				let vm = this
				let req = {
					userId: userInfo['userId'],
					roleAuthId: this.roleIdArray[e.target.value]
				}
				let url = '/system/user/changeUserRoleAuth'
				vm.sendRequest({
					'url': url,
					'method': 'POST',
					'data': req,
					'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'
				}, function(resp) {
					if (resp.data.code === 0 || resp.data.code === '0') {
						// 切换角色成功,刷新用户信息
						vm.refreshUserInfo(function() {
							// 初始化菜单
							vm.initMenu(function() {
								uni.reLaunch({
									url: '/pages/user/user',
								});
							})
						})
					}
				}, function(error) {
					vm.$api.msg('切换角色成功失败 : ' + error.data.msg)
				})
			},

			refreshUserInfo(successFunc) {
				let vm = this
				let req = {}
				let url = '/system/user/profile/11'
				vm.sendRequest({
					'url': url,
					'method': 'POST',
					'contentType': 'application/x-www-form-urlencoded',
					'data': req
				}, function(resp) {
					if (resp.data.userId) {
						// 回调
						uni.setStorageSync('userInfo', resp.data);
						vm.userInfo = resp.data;
						successFunc()
					} else {
						vm.clearSession()
						vm.$api.msg('获取用户信息失败 : ' + resp.data.msg)
					}
				}, function(error) {
					vm.clearSession()
					vm.$api.msg('获取用户信息失败 : ' + error.data.msg)
				})
			},

			initRoleList() {
				let vm = this
				this.roleArray = []
				this.roleIdArray = []
				let url = '/system/user/profile/changeRoleAuthForApp'
				vm.sendRequest({
					'url': url,
					'method': 'GET',
					'data': null,
					'contentType': 'application/x-www-form-urlencoded; charset=UTF-8'
				}, function(resp) {
					if (resp.data.code === 0 || resp.data.code === '0') {
						if (resp.data.rows) {
							for (let i = 0; i < resp.data.rows.length; i++) {
								let item = resp.data.rows[i]
								//设置显示的角色列表
								vm.roleArray.push(item['deptName'] + '_' + item['roleName'])
								//设置角色id值列表
								vm.roleIdArray.push(item['id'])
							}
							console.log('initRoleList--->roleArray-->', vm.roleArray);
							console.log('initRoleList--->roleIdArray--->', vm.roleIdArray);
						}
					}
				}, function(error) {
					vm.$api.msg('获取列表失败 : ' + error.data.msg)
				})
			},
			getuser() {
				let storeUserInfo = this.getUserInfo()
				console.log('storeUserInfo = ' + JSON.stringify(storeUserInfo))
				if (storeUserInfo) {

					this.roleIndex = 0;
					this.roleArray = []
					this.roleIdArray = []

					this.userInfo = storeUserInfo;
					this.initRoleList()
				}
			},
			logout() {
				this.clearSession()
				this.$navigateTo("/pages/login/login")
			}
		},
		onLoad() {},
		onShow() {
			this.getuser();
		}
	};
</script>

<style lang="scss">
	//@import "../../static/css/iconfont/iconfont.css";

	page {
		background: #f2f2f2;
	}

	.btn-hover {
		background: #f2f2f2 !important;
	}

	.user {
		.user-wrap {
			display: flex;
			justify-content: center;
			align-items: center;
			height: 50vw;
			padding: 30rpx;
			z-index: 9;
			border-radius: 0 0 20% 20%;
			background: url('https://handsel.oss-cn-shenzhen.aliyuncs.com/1588938371592.jpg') no-repeat;
			background-size: cover;

			.setting {
				color: #fff;
				position: absolute;
				top: 60rpx;
				left: 60rpx;
				font-size: 50rpx;
			}

			.info {
				position: absolute;
				text-align: center;



				/* #ifndef H5 */
				top: 10%;

				/* #endif */


				.avatar {
					width: 150rpx;
					height: 150rpx;
					border-radius: 50%;
				}

				.nickname {
					color: #fff;
					font-size: 28rpx;
				}
			}
		}

		.order-status {
			padding: 0 20rpx;
			margin-top: -10vw;

			.status-wrap {
				border-radius: 25rpx;
				overflow: hidden;

				.status-list {
					display: flex;
					justify-content: space-evenly;
					align-items: center;
					background: #fff;
					padding-top: 30rpx;
					padding-bottom: 30rpx;

					.status-item {
						flex: 1;
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;

						.item-icon {
							line-height: 1;
							font-size: 65rpx;
							color: #bbb;
						}

						.item-text {
							font-size: 28rpx;
							color: #666;
							margin-top: 5rpx;
						}
				}
					}
			}
		}

		.com-item {
			padding-left: 20rpx;
			padding-right: 20rpx;
			margin-top: 20rpx;

			.com-wrap {
				border-radius: 25rpx;
				overflow: hidden;
			}
		}

		.cell {
			height: 80rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			background: #fff;
			border-bottom: 1px solid #f8f8f8;

			&:active {
				background: #f2f2f2;
			}

			&:last-child {
				border-bottom: none !important;
			}

			.cell-left {
				display: flex;
				align-items: center;

				.cell-icon {
					width: 50rpx;
					height: 50rpx;
				}

				.cell-text {
					color: #666;
					font-size: 28rpx;
					margin-left: 20rpx;
				}
			}

			.iconfont {
				font-size: 40rpx;
				color: #999;
			}
		}
	}
</style>
