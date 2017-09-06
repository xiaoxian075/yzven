/**
 * Created by Administrator on 2016/11/28.
 */
Ext.define('Ext.ux.E.Form',{
    extend : 'Ext.form.Panel',
    xtype : 'EForm',
    requires : [
        'Ext.ux.E.Img',
        'Ext.ux.E.Month'
    ],

    __parseItems : function(items){
        var _self = this,
            res=[],
            minLabelWidth = _self._labelWidth || 60;
            _width = _self._itemWidth || '100%';
        if(!(items instanceof Array)) throw new Error('配置错误，需要数组');
        for(var i=0,l=items.length;i<l;i++){
            var item = items[i],cfg;
            minLabelWidth = E._labelWidth(item.text,minLabelWidth);
            var cfg = {
                _name : item.name,
                xtype : 'textfield',
                fieldLabel : item.text || '',
                colspan : item.col || 1,
                rowspan : item.row || 1,
                labelAlign : 'right',
                width : _width,
                allowBlank : item.blankText ? false : item.allowBlank == false ? false : true,
                labelWidth : minLabelWidth
            };
            for(var k in item)
                cfg[k] = item[k];//这里写入了name属性
            if(cfg.id){
            	_self.__childItems.push(cfg.id);
            }
            if(cfg.readOnly){
            	//只读
            	cfg.xtype = 'displayfield';
                cfg.submitValue = true;
                delete cfg.readOnly;
            }
            if(item.render || item.renderer){
            	if(!cfg.listeners){
            		cfg.listeners = {};
            	}
            	cfg.__render = item.render || item.renderer;
            	delete cfg['render'];
            	delete cfg['renderer'];
            	cfg.listeners.afterrender = function(_field){
            		var val = _field.__render(_field.value,_field);
            		if(val)  _field.setValue(val);
            	}
            }
            if(_self._data){//重写value
            	var dataVal = _self._data[cfg._name];
                if(cfg.ipFormat){
                    if(!dataVal) cfg.value = '';
                    else{
                        var ip = Number(dataVal);
                        cfg.value = (ip>>>24) + '.' + ((ip&0x00FFFFFF)>>>16) + '.' + ((ip&0x0000FFFF)>>>8) + '.' + (ip&0x000000FF);
                    }
                }else{
                    cfg.value =  dataVal == '0' ? 0 : (dataVal || (cfg.value == '0' ? 0 : (cfg.value  || '')));
                }
                if(cfg.xtype == 'filefield'){
                    //文件，则value不同
                    cfg.emptyText = cfg.value;
                }
                if(cfg.dateFormat && !cfg.format && cfg.xtype != 'datefield'){//是个时间
                	cfg._format = cfg.dateFormat;
                	if(!cfg.listeners)
                		cfg.listeners = {};
                	cfg.listeners.afterrender = function(_field){ 
                        var format = _field._format;
                        var val = Number(_field.value);
                        if(!(val instanceof Date)){
                        	var date;
                            if(9999999999 > val){
                                date = new Date(val * 1000);
                            }else{
                                date = new Date(val);
                            }
                            _field.setValue(Ext.Date.format(date,format));
                        }
                    }
                }
            }
            if(cfg.combo){
                cfg.xtype = 'ECombo';
                //cfg._store = cfg.combo;
            }else if(cfg.checkbox){
                cfg.xtype = 'ECheckbox';
                cfg._group = cfg.checkbox;
            }else if(cfg.radio){
                cfg.xtype = 'ERadio';
                cfg._group = cfg.radio;
            }else if(cfg.hasOwnProperty('img')){
                delete cfg.name;//不参与数据读取，只做显示
                cfg.xtype = 'fieldcontainer';
                var imgCfg;
                if(typeof cfg.img == 'string'){
                    imgCfg = {
                        _url : cfg.img
                    };
                }else{
                    imgCfg = cfg.img;
                }
                imgCfg.xtype = 'EImg';
                imgCfg._url = cfg.value;
                var img = Ext.create('Ext.ux.E.Img',imgCfg);
                cfg._img = img;
                cfg.setSrc = function(url){
                    this._img.setSrc(url);
                };
                cfg.items = [img];
            }else if(cfg.hasOwnProperty('imgfile')){//图片上传组件
                delete cfg.name;//移除name属性
                cfg.xtype = 'fieldcontainer';
                cfg.items = [{
                    xtype : 'EImg',
                    _url : cfg.value,
                    _filefield : cfg._name
                }];
            }else if(cfg.hasOwnProperty('format')){
                //日期选择器
                cfg.xtype = /^y.*m$/i.test(cfg.format) ? 'EMonth' : 'datefield';
            }
            res.push(cfg);
        }
        return res;
    },

    __submit : function(form,btn){
        var fn = btn._okFn;
        if(fn && fn != 'submit'){
            fn(btn);
            if(form._submitAfter) form._submitAfter(form);
        }else{
            if(form && form.isValid()){
                if(btn._valid && !btn._valid()){//自定义验证
                    return ;
                }
                //开始提交表单
                form.mask('数据提交中...');
                var doms = [];
                if(btn._bind){
                    if(typeof btn._bind == 'string')
                        btn._bind = [btn._bind];
                    for(var i=0,l=btn._bind.length;i<l;i++){
                        var id = btn._bind[i];
                        var dom = E[id] || Ext.getCmp(id);
                        if(dom){
                            doms.push(dom);
                            dom.mask();
                        }
                    }
                }
                var url = btn._submit.url;
                if(url.indexOf('http://') == -1){
                    url = E._rootReqModel[url] ? E._rootReqModel[url] : E.basePath + url;
                }
                var finish = btn._submit.finish,
                    method = btn._submit.method || 'POST';
                var submitCfg = {
                    url : url,
                    method : method,
                    _doms : doms,
                    _finish : finish,
                    _form : form,
                    params : btn._submit.params,
                    __unmask : function(){
                        this._form.unmask();
                        var thisdoms = this._doms;
                        if(thisdoms) {
                            for (var j = 0, jl = thisdoms.length; j < jl; j++)
                                thisdoms[j].unmask();
                        }
                    },
                    success : function(thisF,act){
                        act.__unmask();
                        var val = act.response.responseText;
                        if(E._postAspect && !E._postAspect(val)) return ;
                        if(E._getAspect && !E._getAspect(val)) return ;
                        if(act._finish){
                            var res = '';
                            try{
                                res = eval('(' + val + ')')
                            }catch(e){
                                res = val;
                            }
                            act._finish(res,act,thisF);
                        }
                        if(form._submitAfter) form._submitAfter(form);
                    },
                    failure : function(thisF,act){
                        act.__unmask();
                        var val = act.response.responseText;
                        if(E._postAspect && !E._postAspect(val)) return ;
                        if(E._getAspect && !E._getAspect(val)) return ;
                        var state = act.response.status;
                        if(state && state != 200){
                            E.alert('请求失败state:' + act.response.status + "("+act.response.statusText+")",act.failureType);
                        }else{
                            if(act._finish){
                                var res = '';
                                try{
                                    res = eval('(' + val + ')')
                                }catch(e){
                                    res = val;
                                }
                                act._finish(res,act,thisF);
                            }
                        }
                        if(form._submitAfter) form._submitAfter(form);
                    },
                };
                form.submit(submitCfg);
            }
        }
    },

    loadData : function(data){
        if(!data){
            return ;
        }
        var form = this.getForm();
        var fields = form.getFields();
        var items = fields.items;
        for(var k in data){
            l:for(var i in items){
                var item  = items[i];
                if(item._name && item._name == k){
                    if(item.__render){
                        if(item._setValue){
                            item._setValue(item.__render(data[k]));
                        }else{
                            item.setValue(item.__render(data[k]));
                        }
                    }else{
                        if(item._setValue){
                            item._setValue(data[k]);
                        }else{
                            item.setValue(data[k]);
                        }
                    }
                    break l;
                }
            }
        }

    },

    constructor : function(){
        var init = {
            border : false,
            bodyPadding : 10,
            /**
             * 表单提交
             *   url : 提交地址
             *   param : object 每次提交都会带上这些参数
             *          如： param : ｛id:1｝
             *   #v1.2新属性 同时移除了success,fail属性
             *   finish : function(data)
             */
            _submit : undefined,//表单提交地址
            _submitBefore : undefined,//提交前调用
            _submitAfter : undefined,//返回结果时调用
            _submitConfirm : undefined,//提交前确认
            /**
             * 当表单自身验证通过时，会调用该检验方法，
             * 如果返回false则会中断表单提交。
             */
            _valid : undefined,//自定义检验
            /**
             * 提交按钮的事件
             * 如果重写，则会调用该方法，则不会调用默认的表单提交方法
             */
            _okFn : undefined,//重写表单提交按钮事件
            _okText : '提交',//确认按钮文本
            /**
             * 配置取消按钮事件，默认为无。
             */
            _cancleFn : undefined,//重写取消按钮事件
            _cancleText : '取消',//取消按钮文本
            /**
             * 配置一行有多少列
             */
            _columns : 1,//一行多少列，默认3
            /**
             * 表单配置 object[]
             * name : 字段名称
             * text : 要显示的字段标题
             * col : 占用几列
             * row : 占用几行
             * xtype : 指定类型，默认textfield
             * blankText : '为空时提醒',配置该属性则字段不允许为空。
             * 扩展：
             * combo : Array 具体API请查看ECombo
             * checkbox : Array 多选框组，具体API请查看ECheckbox
             * radio ： Array 单选框组，具体API请查看ERadio
             * imgfile : string 图片上传功能，只能上传图片
             * img : string 图片显示功能，只能显示图片
             * format : string 日期格式化，会自动选择时间选择器
             */
            _items : undefined,
            _labelWidth : 25,//默认的宽
            /**
             * 表单数据 Object
             */
            _data : undefined,//表单数据

            __childItems:[],
            /**
             * 组件绑定
             * 即绑定组件到该表单上，当表单在提交数据时，同时会禁用掉这些组件的可用性。
             */
            _bind : undefined//组件绑定
        };
        var _self = this;
        for(var k in init){
            _self[k] = init[k];
        }
        this.callParent(arguments);
    },

    initComponent : function(){
        var _self = this;
        if(!_self.id) _self.id = E._randomId('form_');
        _self.__childItems.push(_self.id);
        _self.layout = {
            type : 'table',
            columns : _self._columns || 1,
            tableAttrs : {
                style : {
                    width : '100%'
                }
            }
        };
        if(_self._items)
            _self.items = _self.__parseItems(_self._items);
        // _self.items.push({
        //     xtype : 'EImg'
        // });
        var buttons = _self._buttons || [];
        if(_self._okFn != false){
            buttons.push({
                _self  : _self,
	            _okFn : _self._okFn,
	            _valid : _self._valid,
	            _submit : _self._submit,
	            _bind : _self._bind,
	            text : _self._okText,
                hidden : _self._okFn == 'submit',
	            handler : function(btn){
	            	var form = btn._self;
	            	if(form._submitBefore) form._submitBefore(form);
                    if(form._submitConfirm){
                        var test = form._submitConfirm();
                        if (test==undefined){
                            return;
                        }else{
                            E.confirm(test,'确认',function(yes){
                                if (yes == true){
                                    form.__submit(form,btn);
                                }else{
                                    return;
                                }
                            });
                        }
                    }else{
                        form.__submit(form,btn);
                    }
	            }
	        });
        }
        if(_self._cancleFn != false){
            buttons.push({
                _self : _self,
                _cancleFn : _self._cancleFn,
                text : _self._cancleText,
                handler : function(btn){
                    if(btn._cancleFn){
                    	btn._cancleFn(btn);
                    }else{
                    	var form = btn._self;
                    	form.close();
                    }
                }
            });
        }
        if(_self.__fromShowId){
            _self.__fromShowButtons = buttons;
            _self.__afterrender = function(dom,_E){
                var id = dom.__fromShowId;
                var win = _E[id];
                if(win){
                	var cfg = ['->'];
                	if(dom._okFn != false){
                		cfg.push({
                            xtype : 'button',
                            text : '确认',
                            handler : function(){
                                var bar = this.up('toolbar');
                                bar._btn.handler(bar._btn);
                            }
                        });
                	}
                	if(dom._cancleFn != false){
                		cfg.push({
                            xtype:'button',
                            text:'取消',
                            handler : function(btn){
                                var bar = this.up('toolbar');
                                if(bar._win) bar._win.close();
                            }
                        });
                	}
                    win.addDocked({
                        _win : win,
                        _btn : _self.__fromShowButtons[0],
                        xtype : 'toolbar',
                        dock : 'bottom',
                        ui : 'footer',
                        items : cfg
                    });
                }
            };
        }else{
            _self.buttons = buttons;
        }
        _self.callParent();
    }
});
