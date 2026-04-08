var defaultConf={
	"tplreq":false,   //是否已经点击模板获取(用于微信小程序)
}

const state =Object.assign({},defaultConf)
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
		},
	    tplreq({ commit,dispatch,state}){	 
			state.tplreq=true
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