const	defaultuser = {
		"createAt":"2021-02-21 23:13:52",
		"id":0,
		"isValid":true,
		"lastOptAt":"2021-02-23 14:08:10",
		"mobile":"",
		"nickName":"",
		"wxHeadImg":"",
		"wxNickName":""
}
const state =Object.assign({},defaultuser)
const mutations = {
  update: (state, d) => {
	let tmp = Object.assign(state,d)
	state = tmp
  }
}

const actions = {
	    update({ commit,dispatch,state},d){	 
			commit("update",d)
			return new Promise((resolve,reject)=>{
				resolve(state)
			})
		}
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

//let user=this.$store.state.user;
//this.$store.state.user.id=-1
//this.$store.dispatch("user/update",user)