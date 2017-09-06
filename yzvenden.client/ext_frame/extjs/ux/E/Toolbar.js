/**
 * Created by Administrator on 2016/11/28.
 */
Ext.define('Ext.ux.E.Toolbar',{
    extend : 'Ext.toolbar.Toolbar',
    xtype : 'EToolbar',

    requires : [
        'Ext.panel.Panel',
        'Ext.ux.E.Combo',//下拉框
        'Ext.ux.E.Checkbox',//多选框
        'Ext.ux.E.Radio'//单选框
    ],
    layout : 'fit',
    border : false,
    /**
     * 工具栏配置
     *  生效条件：
     *     配置该属性，且未配置源生的items属性
     */
    _items : undefined,
    _lineHeight : 40,//默认的工具栏高
    __childItems:[],
    __toolbarLength : 1,//工具栏行数
    __fields : [],//所有的字段
    __name2Index : {},//字段的name=index

    validate : function(key){
        var _self = this;
        var fields = _self.__fields;
        var validate = true;
        if(key){
            var field = fields[key];
            return field && field.validate ? field.validate() : false;
        }
        for(var k in fields){
            var field = fields[k];
            if(field && field.validate){
                if(!field.validate()){
                    validate = false;
                }
            }
        }
        return validate;
    },
    /**
     * 取得工具栏的值
     * @param key 只取得一个值
     */
    getValue : function(key){
        var _self = this;
        var fields = _self.__fields;
        var obj = {};
        for(var k in fields){
            var field = fields[k],name;
            if(field && (name = field.name)){
                var xtype = field.xtype,val;
                switch(xtype){
                    case 'numberfield':
                        val = Number(field.rawValue || 0);break;
                    case 'checkbox':
                    case 'radio':
                    case 'checkboxfield':
                    case 'radiofield':
                        val = field.getSubmitValue() || '';break;
                    default:
                        if(field.__combo || field.config.xtype == 'ECombo' || field.config.xtype == 'combo' || field.config.xtype == 'combobox'){
                        	if(field.getValue() == '0') val = 0;
                        	else val = field.getValue() || '';
                        }else if(field.__checkboxgroup){
                            val = field.getValue();
                            val = val[name] == '0' ? 0 : (val[name] || '');
                            if(!(val instanceof Array)){
                                val = [val];
                            }
                        }else if(field.__radiogroup){
                            val = field.getValue();
                            val = val[name] == '0' ? 0 : (val[name] || field._default);
                        }else{
                            val = field.rawValue == '0' ? 0 : (field.rawValue || '');
                        }
                        break;
                }
                obj[name] = val;
            }
        }
        return key ? obj[key] : obj;
    },
    /**
     * 取得工具栏上的元素item
     * @param index
     */
    getItem : function(index){
        var _self = this;
        var field;
        if(typeof index == 'number'){
            field = _self.__fields[index-1];
        }else{
            var i = _self.__name2Index[index];
            if(i) field = _self.__fields[Number(i)];
        }
        return field;
    },

    /**
     * @param cfgs Array[string/object]
     * 工具栏配置
     * object : {
     *   name : 字段名称
     *   text : 显示的文本
     *   xtype : 指定字段类型，默认 textfield
     *   handler : 指定单击事件，此时如果没有设置xtype,则默认xtype=button
     *   combo : 祥细配置，查看ECombo
     *   checkbox : 多选框，祥细配置，查看ECheckbox
     * }
     * string    : 分割符 -
     *              向右 ->
     *              换行 /
     *             其他文本
     * @returns {Array}
     * @private
     */
    __parseConfig : function(cfgs){
        var _self = this;
        if(!(cfgs instanceof Array))
            throw new Error("错误的配置，必须为数组");
        var res = [];
        var items = [];
        for(var i=0,len=cfgs.length;i<len;i++){
            var cfg = cfgs[i];
            if(typeof cfg == 'object'){
            	 // if(cfg.id){
              //    	_self.__childItems.push(cfg.id);
              //    }else{
              //        cfg.id = E._randomId('tb' + i + "_");
              //    }
                cfg.id = E._randomId('tb' + i + "_");
                cfg.border = cfg.border != false ? cfg.border : false;
                cfg.fieldLabel = cfg.text;
                cfg.labelWidth = E._labelWidth(cfg.fieldLabel);
                if(cfg.handler){
                    delete cfg.fieldLabel;
                    cfg.xtype = cfg.xtype || 'button';
                }
                if(cfg.dateFormat || cfg.format){
                    //日期选择框
                    cfg.xtype = 'datefield';
                    if(!cfg.format) cfg.format = cfg.dateFormat;
                }
                if(cfg.combo){
                    cfg.xtype = 'ECombo';
                    cfg._store = cfg.combo;
                }else if(cfg.checkbox){
                    cfg.xtype = 'ECheckbox';
                    cfg._group = cfg.checkbox;
                }else if(cfg.radio){
                    cfg.xtype = 'ERadio';
                    cfg._group = cfg.radio;
                }
                if(cfg.xtype){
                    switch(cfg.xtype){
                        case 'checkbox':
                        case 'checkboxfield':
                        case 'radio':
                        case 'radiofield':
                            cfg.inputValue = cfg.value;
                            delete cfg.value;
                            break;
                    }
                }else{
                    cfg.xtype = 'textfield';
                }
                if(!cfg.listeners){
                    cfg.listeners = {};
                }
                cfg.oldAfter = cfg.listeners.afterrender;
            }else{
                if('/' == cfg){
                    //换行符
                    res.push({xtype : 'toolbar',items : items,border:false,width: '100%'});
                    items = [];
                    ++_self.__toolbarLength;
                    continue;
                }
                if('-' == cfg){
                    cfg = {xtype : 'tbseparator'};
                }else if( '->' == cfg){
                    cfg = {xtype : 'tbfill'};
                }else{
                    cfg = {text: cfg, xtype: 'tbtext'};
                }
                // cfg.id = E._randomId();
            }
            cfg._index = i;
            cfg._self = _self;//绑定关系
            _self.__fields[i] = cfg.id;
            if(cfg.name){
                _self.__name2Index[cfg.name] = cfg.id;
            }
            items.push(cfg);
        }
        res.push({
            xtype : 'toolbar',
            border : false,
            items : items,
            width: '100%'
        });
        return [{
            xtype: 'panel',
            layout: 'fit',
            border : false,
            // height: _self.__toolbarLength * _self._lineHeight,
            items: [{
                xtype : 'panel',
                layout: 'vbox',
                items : res
            }]
        }];
    },

    constructor : function(){
        var _self = this;
        _self.layout = 'fit';
        _self.border = false;
        _self._items = undefined;
        _self._lineHeight = 40;//默认的工具栏高
        _self.__childItems=[];
        _self.__toolbarLength = 1;//工具栏行数
        _self.__fields = [];//所有的字段
        _self.__name2Index = {};//字段的name=index
        _self.callParent(arguments);
    },

    initComponent : function(){
        var _self = this;
        if(_self._items &&　!_self.items)
            _self.items = _self.__parseConfig(_self._items);

        if(!_self.listeners){
            _self.listeners ={};
        }
        _self.oldAfter = _self.listeners.afterrender;
        _self.listeners.afterrender = function(__self,opts){
            var fields = __self.__fields;
            for(var i in fields){
                var id = fields[i];
                var dom = Ext.getCmp(id);
                if(dom){
                    fields[i] = dom;//元素替换
                }
            }
            if(__self.oldAfter)
             __self.oldAfter(__self,opts);
        };

        _self.callParent();
    }
});
