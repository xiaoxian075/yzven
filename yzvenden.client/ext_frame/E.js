/**
 * Created by Administrator on 2016/11/28.
 */

if(typeof E == 'undefined') E = {
    /*框架信息配置*/
    basePath : '',//项目文件的根目录，具体到项目名称 如：http://127.0.0.1:83/frame/ 如果未定义，会自动定位到项目的ext_frame下
    _extendVTypes:undefined,//扩展Vtypes
    _rootReqModel : [],//请求地址配配置
    _request : 'ajax',//,默认ajax,请求方式,支持jsonp
    _extFolder : '',//ext框架的项目地址
    _reader : {//远程加载配置器
    	grid : {//普通表格返回的数据的根，例： data:[{},....]
    		rootProperty : 'data'
    	},
    	gridPaging : {//分页表格返回的数据
    		rootProperty : 'datas',
            totalProperty : 'totalCount'
    	}
    },
    _postAspect:undefined,
    _getAspect:undefined,
    _dataWrap : undefined,//数据包裹
    //以上配置都可以在EConfig配置文件中重写
    __xtypes : {
        'EAlert' : 'Ext.ux.E.Alert',
        'ECheckbox':'Ext.ux.E.Checkbox',
        'ECombo':'Ext.ux.E.Combo',
        'EForm' : 'Ext.ux.E.Form',
        'EGrid':'Ext.ux.E.Grid',
        'EImg':'Ext.ux.E.Img',
        'EMonth':'Ext.ux.E.Month',
        'ERadio':'Ext.ux.E.Radio',
        'EIFrameTabPanel':'Ext.ux.E.IFrameTabPanel',
        'EToolbar':'Ext.ux.E.Toolbar',
        'ETree':'Ext.ux.E.Tree',
        'EView':'Ext.ux.E.View',
        'EAccordion':'Ext.ux.EAccordion'
    },
    _required:{},
    vtypes : undefined,
    /**
     * 初始化完成
     * @param todo
     */
    onReady : function(todo){
        // jQuery.ajaxSettings.traditional = true;
        var _self = this;
        Ext.onReady(function () {
            var _config = EConfig || {};
            for(var k in _config){
                //属性重写
                _self[k] = _config[k];
            }
            var scriptEls = document.getElementsByTagName('script');
            var scriptElsLen = scriptEls.length;
            var path = scriptEls[--scriptElsLen].src;
            while(!path){
                path = scriptEls[--scriptElsLen].src;
                if(scriptElsLen <= 0) throw new Error("未找到有意义的脚本配置（src地址全为空！！）");
            }
            _self._extFolder = path.substring(0, path.lastIndexOf('/'));
            if(!_self.basePath){
                _self.basePath = _config.basePath || _self._extFolder;
            }
            /*Ext的基础配置*/
            Ext.QuickTips.init();// 初始化显示提示信息。没有它提示信息出不来。
            Ext.form.Field.prototype.msgTarget = "side";
            Ext.Loader.setConfig({enabled:true});//自动加载机制
            Ext.Loader.setPath('Ext.ux', _self._extFolder  + '/extjs/ux');//第三方ux
            //VTypes扩展
            var EV = _self._extendVTypes;
            if(typeof EV == 'object'){
                Ext.apply(Ext.form.field.VTypes,EV);
            }
            if(_config._rootReqModel){
                //配置了rootReq模式
                var model = _config._rootReqModel;
                var root = model['root'];
                if(root){
                    $.get(root,function(data){
                        if(data){
                            if(typeof data == "string"){
                                try{
                                    data = eval("(" + data + ")");
                                }catch(e){
                                    throw new Error(e);
                                }
                            }
                            E._rootReqModel = data;
                        }
                        if(todo) todo();
                    });
                }
            }else{
                if(todo) todo();
            }
        });
    },
    /**
     * 创建框架内容
     * 左侧的导航树
     * 右侧的tabPanel的主界面
     */
    createFrame : function(){

    },
    /**
     * 创建界面
     * @param cfg  object
     */
    createView : function(cfg){
        this.__create(cfg,'Ext.ux.E.View');
    },
    /**
     * 弹出框显示
     * @param domCfg object,弹出窗口上的组件的配置，必须的
     * @param width	 number,弹出窗口的宽，可缺省
     * @param height number,弹出窗口的高，可缺省
     * @param winCfg object,弹出窗口配置，可缺省
     */
    show : function(domCfg,width,height,winCfg){
        winCfg = winCfg || {width:500,height:500};
        if(typeof width == 'number'){
        	winCfg.width = width;
        	//如果设置了宽，则需要判断高
        	if(typeof height == 'number'){
        		winCfg.height = height;
        	}else if(height){//如果有设置高,但是高不是数字，则为对象
        		winCfg = height;
        	}
        }else if(width){//如果设置宽设置为对象
       		winCfg = width;
        }
        winCfg.closable = domCfg.closable == false ? false : winCfg.closable != false ;
        winCfg.title = domCfg.title || "";
        delete domCfg.title;
        //设置弹出窗口Id
        if(!winCfg.id) winCfg.id = E._randomId();
        if(domCfg.animateTarget){
            winCfg.animateTarget = domCfg.animateTarget;
        }
        if(domCfg.items && domCfg.items instanceof  Array){
            for(var key in domCfg.items){
                var cfg = domCfg.items[key];
                cfg.__fromShowId = winCfg.id;
                if(!cfg.id) cfg.id = E._randomId(cfg.xtype || '');
            }
        }else{
            domCfg.__fromShowId = winCfg.id;
            if(!domCfg.id) domCfg.id = E._randomId(domCfg.xtype || '');
        }
        winCfg.layout = 'fit';
       (winCfg.items = winCfg.items || []).push({
            xtype : 'panel',
            autoScroll : true,
            layout : 'fit',
            border : false,
            items : [domCfg]
        });
        var alert = this.__create(winCfg,'Ext.ux.E.Alert');
        return alert.show();
    },
    __create : function (cfg,xtype) {
        //配置说明：
        // if(!E._required[xtype]){
        //     //Ext.require([xtype]);
        //     // Ext.onReady(function(){
        //         E._required[xtype] = true;
        //         doCreate();
        //     // })
        // }else{
        //     doCreate();
        // }
        var body = Ext.getBody();
        var bodyWidth = body.getWidth() || 800;
        var bodyHeight = body.getHeight() || 600;
        if(cfg.width > bodyWidth){
            cfg.width = bodyWidth - 60;
        }
        if(cfg.height > bodyHeight){
            cfg.height = bodyHeight - 60;
        }
        cfg._returnDoms = function(doms){
            for(var id in doms){
                var dom = doms[id];
                E[id] = doms[id];
                if(dom && dom.__afterrender){
                    dom.__afterrender(dom,E);
                }
            }
        };
        return Ext.create(xtype,cfg);
    },

    /**
     * 弹出框
     * @param content   显示的内容
     * @param title     显示的标题
     * @param cb        确认的回调
     */
    alert : function(content,title,cb){
        if(typeof title != 'string'){
            cb = title;
            title = '提示';
        }
        Ext.MessageBox.alert(title || '提示',content,cb, this);
    },
    /**
     * 提示框
     * @param content
     * @param title
     * @param cb
     */
    prompt : function(content,title,cb){
        if(typeof title != 'string'){
            cb = title;
            title = '提示';
        }
        Ext.MessageBox.prompt(title,content,cb, this);
    },
    confirm : function(content,title,cb){
        if(typeof title != 'string'){
            cb = title;
            title = '确认？';
        }
        Ext.MessageBox.confirm(title,content,function(btn){
            cb(btn);
        });
    },
    ysc : function(content,title,cb){
        if(typeof title != 'string'){
            cb = title;
            title = '确认？';
        }
        Ext.MessageBox.show({
            title:title || '确认？',
            msg: content,
            buttons: Ext.MessageBox.YESNOCANCEL,
            scope: this,
            fn: cb,
            icon: Ext.MessageBox.QUESTION
        });
    },
    toast : function(content,align){
        var w = E._labelWidth(content,240);
        Ext.toast({
            html: content,
            closable: false,
            align: align || 'br',
            slideInDuration: 400,
            minWidth: w + 20
        });
    },
    post : function(url,data,success,type){
    	if(url && url.indexOf('http://')==-1){
            url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
    	}
    	if(typeof data == 'function'){
    		success = data;
    		data = undefined;
    	}
    	$.post(url,data,function(d){
    		var res = '';
    		try{
    			res = eval('(' + d + ')');
    		}catch(e){
    			res = d;
    		}
    		if(E._postAspect){
                if(E._postAspect(res)){
                    success(res);
                }
            }else{
                success(res);
            }
    	},type);
    },
    
    get : function(url,callback){
    	if(url && url.indexOf('http://')==-1){
    		url = E.basePath + url;
    	}
    	$.get(url,function(d){
    		var res = '';
    		try{
    			res = eval('(' + d + ')');
    		}catch(e){

    		}
    		if(E._getAspect){
                if(E._getAspect(res)){
                    callback(res);
                }
            }else{
                callback(res);
            }
    	});
    },

    /*封装类方法*/
    _labelWidth : function(text,minWidth){
        if(!text) return minWidth || 0;
        var width = text.replace(/[^\x00-\xff]/g, '__').length * 8;
        return minWidth ? width < minWidth ? minWidth : width : width;
    },
    _icon : function(icon){
        if(!icon) return '';
        return this._extFolder + '/images/'+icon+'.png';
    },
    _randomId : function(prefix){
    	if(!prefix) prefix = 'id_';
    	return prefix + Math.ceil(Math.random()*10) + "_" + (new Date()).getTime();

        // return Ext.id(prefix);
    }
};