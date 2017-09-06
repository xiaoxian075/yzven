/**
 * 基础配置文件
 * 所有的项目都需要修改该文件！！！
 */
var EConfig = {
	//根目录配置 
	// basePath : 'http://192.168.1.215:83/frame/',
	basePath : 'http://192.168.1.84/ext_frame/',
	_dataWrap : 'data',//表示返回的数据会有数据包裹，而包裹需要的数据的字段名为'data'
	_postAspect:function(data){
		if(data && (data == 'noLogin' || data.data=='noLogin')){
    		E.alert("账号已退出登录，请重新登录",function(){
      			top.location.href = E.basePath + "index.html";
          	});
    		return false;
     	}
		return true;
	},

/*	_rootReqModel : {
		root : "http://192.168.1.215/req.htm?g=admin"
	},*/

	_extendVTypes : {
		ip : function(val,field){
			return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(val);
		},
		ipText : '请输入正确的IP地址',
		ipMask :  /[\d\.]/i
	}
};
